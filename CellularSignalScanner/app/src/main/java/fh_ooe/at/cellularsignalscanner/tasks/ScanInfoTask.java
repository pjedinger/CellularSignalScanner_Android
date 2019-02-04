package fh_ooe.at.cellularsignalscanner.tasks;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.activities.ScanActivity;
import fh_ooe.at.cellularsignalscanner.data.ConnectionType;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.data.ScanDataPoint;
import fh_ooe.at.cellularsignalscanner.data.ScanInfo;
import fh_ooe.at.cellularsignalscanner.data.ScanServiceMetadata;
import fh_ooe.at.cellularsignalscanner.data.SignalQuality;
import fh_ooe.at.cellularsignalscanner.interfaces.RunnableHistory;

public class ScanInfoTask extends AsyncTask<TelephonyManager, Integer, ScanServiceMetadata>{

    public RunnableHistory delegate = null;
    private Location location;

    private ScanActivity context;

    private int best;
    private int worst;
    Location referenceLocation;

    //lat and lon difference for 25m
    double difLat = 0.00022483;
    double difLong = 0.00033742;

    public ScanInfoTask(ScanActivity context, Location location, Location referenceLocation) {
        this.context = context;
        this.location = location;
        best = -60;
        worst = -115;
        this.referenceLocation = referenceLocation;
    }

    @Override
    protected ScanServiceMetadata doInBackground(TelephonyManager... telephonyManagers) {
        TelephonyManager telephonyManager = telephonyManagers[0];
        @SuppressLint("MissingPermission") List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        int dbmLevel = -1;
        ConnectionType connection = ConnectionType.UNKNOWN;
        int timingAdvance = -1;


        CellInfo cellinfo = cellInfoList.get(0); //currently used signal saved at 0
        if (cellinfo instanceof CellInfoLte) {
            CellInfoLte cellinfolte = (CellInfoLte) cellInfoList.get(0);
            connection = ConnectionType.LTE;
            CellSignalStrengthLte cellSignalStrengthLte = cellinfolte.getCellSignalStrength();
            timingAdvance = cellSignalStrengthLte.getTimingAdvance();
            dbmLevel = cellSignalStrengthLte.getDbm();

        } else if (cellinfo instanceof CellInfoGsm) {
            CellInfoGsm cellinfogsm = (CellInfoGsm) cellInfoList.get(0);
            connection = ConnectionType.GSM;
            CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
            dbmLevel = cellSignalStrengthGsm.getDbm();
        } else if (cellinfo instanceof CellInfoCdma) {
            CellInfoCdma cellinfocdma = (CellInfoCdma) cellInfoList.get(0);
            connection = ConnectionType.CDMA;
            CellSignalStrengthCdma cellSignalStrengthCdma = cellinfocdma.getCellSignalStrength();
            dbmLevel = cellSignalStrengthCdma.getDbm();
        } else if (cellinfo instanceof CellInfoWcdma) {
            CellInfoGsm cellinfogsm = (CellInfoGsm) cellInfoList.get(0);
            connection = ConnectionType.WCDMA;
            CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
            dbmLevel = cellSignalStrengthGsm.getDbm();
        }
        String provider = "unknown";
        try {
            if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                provider = telephonyManager.getSimOperatorName();
            } else {
                provider = telephonyManager.getNetworkOperatorName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ScanInfo scanInfo = new ScanInfo(connection, dbmLevel, provider, location);

        if(referenceLocation == null){
            referenceLocation = location;
        }
        return new ScanServiceMetadata(scanInfo, referenceLocation, null);
    }

    @Override
    protected void onPostExecute(ScanServiceMetadata scanServiceMetadata) {
        super.onPostExecute(scanServiceMetadata);

        String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        TextView locationTextView = context.findViewById(R.id.scan_datelocation_textview);
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(scanServiceMetadata.getScanInfo().getLatitude(), scanServiceMetadata.getScanInfo().getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            locationTextView.setText(city);
        } catch (IOException e) {
            e.printStackTrace();
        }
        float dist = distFrom((float)scanServiceMetadata.getLocation().getLatitude(), (float)scanServiceMetadata.getLocation().getLongitude(), (float)location.getLatitude(), (float)location.getLongitude());
        Log.d("Measurements", "Dist: "+dist);

        //Log.d("Measurements", "Lat1: " + scanInfo.second.getLatitude() + " Long1: " + scanInfo.second.getLongitude() + " Lat2: " + location.getLatitude() + " Long2: " + location.getLongitude());
        float distLat = distFrom((float)scanServiceMetadata.getLocation().getLatitude(), (float)scanServiceMetadata.getLocation().getLongitude(), (float)location.getLatitude(), (float)scanServiceMetadata.getLocation().getLongitude());
        double distLong = distFrom((float)scanServiceMetadata.getLocation().getLatitude(), (float)scanServiceMetadata.getLocation().getLongitude(), (float)scanServiceMetadata.getLocation().getLatitude(), (float)location.getLongitude());
        Log.d("Measure", "DistLat: " + distLat +" DistLong: " +distLong);

        if(distLat > Math.ceil(context.heatMapRadius/2)){
            context.heatMapRadius = (int) Math.ceil(distLat*2);
            //update old datapoints with new radius
            List<ScanDataPoint> tempList = new ArrayList<>();
            Iterator<ScanDataPoint> i = context.dataPoints.iterator();
            while(i.hasNext()) {
                ScanDataPoint sdp = i.next();
                Pair<Double, Double> p = calcNewLatLong(sdp.getLatitude(), sdp.getLongitude());
                tempList.add(new ScanDataPoint(p.second.floatValue(), p.first.floatValue(), sdp.getVal(), sdp.getDbm(), sdp.getLatitude(), sdp.getLongitude()));
                Log.d("Measure", "Removed Node at X: " + sdp.getX()+" Y: "+sdp.getY()+" Val: " + sdp.getDbm()+". New Loc X: " + p.second + " Y: " +p.first);
                i.remove();
            }
            context.dataPoints.addAll(tempList);
        }
        if(distLong > Math.ceil(context.heatMapRadius/2)){
            context.heatMapRadius = (int) Math.ceil(distLong*2);
            List<ScanDataPoint> tempList = new ArrayList<>();
            Iterator<ScanDataPoint> i = context.dataPoints.iterator();
            while(i.hasNext()) {
                ScanDataPoint sdp = i.next();
                Pair<Double, Double> p = calcNewLatLong(sdp.getLatitude(), sdp.getLongitude());
                tempList.add(new ScanDataPoint(p.second.floatValue(), p.first.floatValue(), sdp.getVal(), sdp.getDbm(), sdp.getLatitude(), sdp.getLongitude()));
                Log.d("Measure", "Removed Node at X: " + sdp.getX()+" Y: "+sdp.getY()+" Val: " + sdp.getDbm()+". New Loc X: " + p.second + " Y: " +p.first);
                i.remove();
            }
            context.dataPoints.addAll(tempList);
        }
        //Log.d("Measurements", "Dist x: "+distx + " Dist y: " + disty);

        SeekBar seekBar = context.findViewById(R.id.scan_seekbar);
        TextView currentDBMTextView = context.findViewById(R.id.scan_currentdbm_textview);
        TextView connectionTextView = context.findViewById(R.id.scan_connection_textview);
        TextView qualityTextView = context.findViewById(R.id.scan_quality_textview);
        //heatMap = context.findViewById(R.id.signal_heatmap);

        TextView minDBMTextView = context.findViewById(R.id.scan_signalstrength_minimum_textview);
        TextView maxDBMTextView = context.findViewById(R.id.scan_signalstrength_maximum_textview);
        TextView avgDBMTextView = context.findViewById(R.id.scan_signalstrength_average_textview);

        Pair<Integer, Integer>p = calcBarSettings(scanServiceMetadata.getScanInfo().getDbm());
        seekBar.setProgress(p.first);
        seekBar.setMax(p.second);

        SignalQuality sq = SignalQuality.getSignalQuality(scanServiceMetadata.getScanInfo().getDbm());
        qualityTextView.setText(sq.name());
        currentDBMTextView.setText(scanServiceMetadata.getScanInfo().getDbm()+" dbm");
        connectionTextView.setText(scanServiceMetadata.getScanInfo().getConnectionType().name() + ", "+scanServiceMetadata.getScanInfo().getProvider());

        Pair<Double, Double> loc = calcNewLatLong(location.getLatitude(), location.getLongitude());
        ScanDataPoint dp = new ScanDataPoint(loc.second.floatValue(),loc.first.floatValue(),calcBarSettings(scanServiceMetadata.getScanInfo().getDbm()).first, scanServiceMetadata.getScanInfo().getDbm(), location.getLatitude(), location.getLongitude());

        if(!context.dataPoints.contains(dp)) {
            context.dataPoints.add(dp);
            if(!context.duplicateDataPoints.isEmpty()){
                int dataPointCount = context.duplicateDataPoints.size();
                float distanceX = context.lastAddedDataPoint.getX() - dp.getX();
                float distanceY = context.lastAddedDataPoint.getY() - dp.getY();
                float partDistanceX = distanceX / dataPointCount;
                float partDistanceY = distanceY / dataPointCount;
                double distanceLong = context.lastAddedDataPoint.getLongitude() - dp.getLongitude();
                double distanceLat = context.lastAddedDataPoint.getLatitude() - dp.getLatitude();
                double partDistanceLong = distanceLong / dataPointCount;
                double partDistanceLat = distanceLat / dataPointCount;
                int i = 1;
                for (ScanDataPoint dataPoint: context.duplicateDataPoints) {
                    if(i % 5 == 0) {
                        ScanDataPoint interpolatedPoint = new ScanDataPoint(context.lastAddedDataPoint.getX() - partDistanceX * i, context.lastAddedDataPoint.getY() - partDistanceY * i, calcBarSettings(dataPoint.getDbm()).first, dataPoint.getDbm(), context.lastAddedDataPoint.getLatitude() - partDistanceLat * i, context.lastAddedDataPoint.getLongitude() - partDistanceLong * i);
                        context.dataPoints.add(interpolatedPoint);
                    }
                    i += 1;

                }
                context.duplicateDataPoints.clear();
            }
            context.lastAddedDataPoint = dp;
        }else{
            //if the position of the point is already in the list, add it to the duplicates for later interpolation.
            context.duplicateDataPoints.add(dp);
            Log.d("Duplicate", "Added Duplicate.");
            //ScanDataPoint duplicate = context.dataPoints.remove(context.dataPoints.indexOf(dp));
            //context.dataPoints.add(dp);
        }
        minDBMTextView.setText(getMinDBM()+" dBm");
        maxDBMTextView.setText(getMaxDBM()+" dBm");
        avgDBMTextView.setText(getAvgDBM()+" dBm");

        TextView durationTextView = context.findViewById(R.id.scan_duration_textview);
        long duration = System.currentTimeMillis() - context.startTime;
        if(duration < 60000){
            durationTextView.setText(TimeUnit.MILLISECONDS.toSeconds(duration)+" sec");
        }else if(duration < 3600000){
            durationTextView.setText(TimeUnit.MILLISECONDS.toMinutes(duration)+":"+TimeUnit.MILLISECONDS.toSeconds(duration)%60+ " min");
        }else{
            durationTextView.setText(TimeUnit.MILLISECONDS.toHours(duration)+":"+TimeUnit.MILLISECONDS.toMinutes(duration)%60+":"+ TimeUnit.MILLISECONDS.toSeconds(duration)%3600+" hrs");
        }
        context.entryCount+=1;
        Log.d("DataPoints", ""+context.dataPoints.size() + " Entry: " + context.entryCount);

        //Creating History entry
        HistoryEntry historyEntry = new HistoryEntry();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        historyEntry.name="Unnamed Scan " + sp.getInt("scanCount", 1);;
        historyEntry.avg=getAvgDBM();
        historyEntry.max=getMaxDBM();
        historyEntry.min=getMinDBM();
        historyEntry.connection=scanServiceMetadata.getScanInfo().getConnectionType().name();
        historyEntry.provider=scanServiceMetadata.getScanInfo().getProvider();
        historyEntry.quality=sq.name();
        historyEntry.scanDuration=(int)duration;
        historyEntry.date=currentDateandTime;
        historyEntry.location=locationTextView.getText().toString();
        historyEntry.scanDataPoints = new ArrayList<>(context.dataPoints);
        delegate.processFinish(scanServiceMetadata);
    }


    private Pair<Integer, Integer> calcBarSettings(int dbm){
        int diff = 0;
        int progress = 0;

        best = Math.abs(best);
        worst = Math.abs(worst);
        dbm = Math.abs(dbm);

        if (dbm > worst) {
            worst=dbm;
            progress=0;
            diff=dbm;
        }else if(dbm < best){
            best = dbm;
            progress = dbm;
            diff = dbm;
        }else{
            diff = worst-best;
            progress = diff-(dbm-diff);
        }


        return new Pair<Integer, Integer>(progress, diff);
    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    int getMinDBM(){
        int min = context.min;
        for (ScanDataPoint dp : context.dataPoints){
            if(dp.getDbm() < min){
                min = dp.getDbm();
                context.min = min;
            }
        }
        return min;
    }

    int getMaxDBM(){
        int max = context.max;
        for (ScanDataPoint dp : context.dataPoints){
            if(dp.getDbm() > max){
                max = dp.getDbm();
                context.max = max;
            }
        }
        return max;
    }

    int getAvgDBM(){
        int total = 0;
        int count = 0;
        for (ScanDataPoint dp : context.dataPoints){
            count+=1;
            total+=dp.getDbm();
        }
        return total/count;
    }

    Pair<Double, Double> calcNewLatLong(double latitude, double longitude){
        double rad = ((double)context.heatMapRadius/2.0)/6371000.0;
        difLat = rad * (180 / Math.PI);
        difLong = rad * (180 / Math.PI) / Math.cos(referenceLocation.getLatitude()*Math.PI/180);
        double upperBoundLat = referenceLocation.getLatitude() + difLat;
        double lowerBoundLat = referenceLocation.getLatitude() - difLat;
        double upperBoundLong = referenceLocation.getLongitude() + difLong;
        double lowerBoundLong = referenceLocation.getLongitude() - difLong;
        double locLat = (1-0)/(upperBoundLat-lowerBoundLat)*(latitude - upperBoundLat)+1;
        double locLong = (1-0)/(upperBoundLong-lowerBoundLong)*(longitude - upperBoundLong)+1;
        return new Pair<>(locLat, locLong);
    }
}

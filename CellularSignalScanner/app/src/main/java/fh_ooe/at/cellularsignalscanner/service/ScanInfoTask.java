package fh_ooe.at.cellularsignalscanner.service;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.AsyncTask;
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

import java.util.List;

import ca.hss.heatmaplib.HeatMap;
import fh_ooe.at.cellularsignalscanner.data.ConnectionType;
import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.data.ScanDataPoint;
import fh_ooe.at.cellularsignalscanner.data.ScanInfo;
import fh_ooe.at.cellularsignalscanner.data.SignalQuality;
import fh_ooe.at.cellularsignalscanner.activities.MainActivity;

public class ScanInfoTask extends AsyncTask<TelephonyManager, Integer, Pair<ScanInfo, Location>>{

    private SeekBar signalSeekBar;
    private TextView dbmLevelTexView;
    private TextView connectionTextView;
    private TextView providerTextView;
    private HeatMap heatMap;

    private Location location;

    private MainActivity context;

    private int best;
    private int worst;
    Location referenceLocation;

    int heatMapRadius = 25;
    public ScanInfoTask(MainActivity context, Location location, Location referenceLocation) {
        this.context = context;
        this.location = location;
        best = -60;
        worst = -115;
        this.referenceLocation = referenceLocation;
    }

    @Override
    protected Pair<ScanInfo, Location> doInBackground(TelephonyManager... telephonyManagers) {
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
        return new Pair<>(scanInfo, referenceLocation);
    }

    @Override
    protected void onPostExecute(Pair<ScanInfo, Location> scanInfo) {
        super.onPostExecute(scanInfo);

        float dist = distFrom((float)scanInfo.second.getLatitude(), (float)scanInfo.second.getLongitude(), (float)location.getLatitude(), (float)location.getLongitude());
        Log.d("Measurements", "Dist: "+dist);

        //Log.d("Measurements", "Lat1: " + scanInfo.second.getLatitude() + " Long1: " + scanInfo.second.getLongitude() + " Lat2: " + location.getLatitude() + " Long2: " + location.getLongitude());
        double distx = scanInfo.second.getLongitude() - location.getLongitude();
        double disty = scanInfo.second.getLatitude() - location.getLatitude();
        //Log.d("Measurements", "Dist x: "+distx + " Dist y: " + disty);

        signalSeekBar = context.findViewById(R.id.signal_seekbar);
        dbmLevelTexView = context.findViewById(R.id.signalstrength_textview);
        connectionTextView = context.findViewById(R.id.connection_textview);
        providerTextView = context.findViewById(R.id.provider_textview);
        //heatMap = context.findViewById(R.id.signal_heatmap);

        Pair<Integer, Integer>p = calcBarSettings(scanInfo.first.getDbm());
        signalSeekBar.setProgress(p.first);
        signalSeekBar.setMax(p.second);

        SignalQuality sq = SignalQuality.getSignalQuality(scanInfo.first.getDbm());
        dbmLevelTexView.setText(scanInfo.first.getDbm() + " [" +sq.name()+ "]");
        connectionTextView.setText(scanInfo.first.getConnectionType().name());
        providerTextView.setText(scanInfo.first.getProvider());

        int width = context.heatMap.getWidth();
        int height = context.heatMap.getHeight();

        ScanDataPoint dp;

        //dp = new HeatMap.DataPoint((float)(0.5f+distx/heatMapRadius),(float)(0.5f+disty/heatMapRadius),calcBarSettings(scanInfo.first.dbm).first);
        if(scanInfo.second.getLongitude() < location.getLongitude()){
            if(scanInfo.second.getLatitude() < location.getLatitude()){
                dp = new ScanDataPoint(0.5f+dist/heatMapRadius,0.5f+dist/heatMapRadius,calcBarSettings(scanInfo.first.getDbm()).first);
            }else{
                dp = new ScanDataPoint(0.5f+dist/heatMapRadius,0.5f-dist/heatMapRadius,calcBarSettings(scanInfo.first.getDbm()).first);
            }
        }else{
            if(scanInfo.second.getLatitude() < location.getLatitude()){
                dp = new ScanDataPoint(0.5f-dist/heatMapRadius,0.5f+dist/heatMapRadius,calcBarSettings(scanInfo.first.getDbm()).first);
            }else{
                dp = new ScanDataPoint(0.5f-dist/heatMapRadius,0.5f-dist/heatMapRadius,calcBarSettings(scanInfo.first.getDbm()).first);
            }
        }
        //HeatMap.DataPoint dataPoint = new HeatMap.DataPoint(0.5f, 0.5f, calcBarSettings(scanInfo.first.dbm).first);

        if(!context.dataPoints.contains(dp)) {
            context.dataPoints.add(dp);
        }else{
            context.dataPoints.remove(context.dataPoints.indexOf(dp));
            context.dataPoints.add(dp);
        }
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
}

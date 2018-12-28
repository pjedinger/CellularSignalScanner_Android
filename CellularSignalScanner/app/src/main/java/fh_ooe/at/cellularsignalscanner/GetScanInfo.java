package fh_ooe.at.cellularsignalscanner;

import android.annotation.SuppressLint;
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
import android.util.Pair;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class GetScanInfo extends AsyncTask<TelephonyManager, Integer, ScanInfo> {

    SeekBar signalSeekBar;
    ProgressBar signalProgressBar;
    TextView dbmLevelTexView;
    TextView connectionTextView;
    TextView providerTextView;

    MainActivity context;

    private int best;
    private int worst;

    public GetScanInfo(MainActivity context) {
        this.context = context;
        best = -60;
        worst = -115;
    }

    @Override
    protected ScanInfo doInBackground(TelephonyManager... telephonyManagers) {
        TelephonyManager telephonyManager = telephonyManagers[0];
        @SuppressLint("MissingPermission") List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        int dbmLevel = -1;
        ConnectionType connection = ConnectionType.UNKNOWN;
        int timingAdvance = -1;


        CellInfo cellinfo = cellInfoList.get(0); //currently used signal saved at 0
        if (cellinfo instanceof CellInfoLte){
            CellInfoLte cellinfolte = (CellInfoLte) cellInfoList.get(0);
            connection = ConnectionType.LTE;
            CellSignalStrengthLte cellSignalStrengthLte = cellinfolte.getCellSignalStrength();
            timingAdvance = cellSignalStrengthLte.getTimingAdvance();
            dbmLevel = cellSignalStrengthLte.getDbm();

        }else if (cellinfo instanceof CellInfoGsm){
            CellInfoGsm cellinfogsm = (CellInfoGsm) cellInfoList.get(0);
            connection = ConnectionType.GSM;
            CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
            dbmLevel = cellSignalStrengthGsm.getDbm();
        }else if (cellinfo instanceof CellInfoCdma){
            CellInfoCdma cellinfocdma = (CellInfoCdma) cellInfoList.get(0);
            connection = ConnectionType.CDMA;
            CellSignalStrengthCdma cellSignalStrengthCdma = cellinfocdma.getCellSignalStrength();
            dbmLevel = cellSignalStrengthCdma.getDbm();
        }else if (cellinfo instanceof CellInfoWcdma){
            CellInfoGsm cellinfogsm = (CellInfoGsm) cellInfoList.get(0);
            connection = ConnectionType.WCDMA;
            CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
            dbmLevel = cellSignalStrengthGsm.getDbm();
        }
        String provider = "unknown";
        try{
            if(telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA){
                provider=telephonyManager.getSimOperatorName();
            }else{
                provider=telephonyManager.getNetworkOperatorName();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ScanInfo scanInfo = new ScanInfo(connection, dbmLevel, provider);
        return scanInfo;
    }

    @Override
    protected void onPostExecute(ScanInfo scanInfo) {
        super.onPostExecute(scanInfo);
        signalSeekBar = context.findViewById(R.id.signal_seekbar);
        signalProgressBar = context.findViewById(R.id.signal_progressbar);
        dbmLevelTexView = context.findViewById(R.id.signalstrength_textview);
        connectionTextView = context.findViewById(R.id.connection_textview);
        providerTextView = context.findViewById(R.id.provider_textview);

        Pair<Integer, Integer>p = calcBarSettings(scanInfo.dbm);
        signalSeekBar.setProgress(p.first);
        signalSeekBar.setMax(p.second);

        signalProgressBar.setProgress(p.first);
        signalProgressBar.setMax(p.second);

        dbmLevelTexView.setText(scanInfo.dbm +"");
        connectionTextView.setText(scanInfo.connectionType.name());
        providerTextView.setText(scanInfo.provider);
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
}
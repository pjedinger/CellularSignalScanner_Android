package fh_ooe.at.cellularsignalscanner;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;

public class ScanInfoService extends Service {
    Handler handler;
    Runnable test;
    TelephonyManager telephonyManager;
    MainActivity context;

    public ScanInfoService(final TelephonyManager telephonyManager, final MainActivity context) {
        this.telephonyManager = telephonyManager;
        this.context = context;

        handler = new Handler();
        test = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(test, 500);
                new GetScanInfo(context).execute(telephonyManager);
            }
        };

        handler.postDelayed(test, 0);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

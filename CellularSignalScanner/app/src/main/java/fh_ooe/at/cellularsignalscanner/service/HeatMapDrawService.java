package fh_ooe.at.cellularsignalscanner.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;

import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.activities.ScanActivity;
import fh_ooe.at.cellularsignalscanner.data.ScanDataPoint;
import fh_ooe.at.cellularsignalscanner.libary.HeatMap;

public class HeatMapDrawService extends Service{
    Handler handler;
    Runnable runnable;
    ScanActivity context;
    HeatMap heatMap;
    private final int REFRESH_RATE_MS = 2000;

    public HeatMapDrawService(final ScanActivity context) {
        this.context = context;
        this.heatMap = context.findViewById(R.id.scan_heatmap);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                heatMap.clearData();
               // long before = System.nanoTime();
                Collections.sort(context.dataPoints, new Comparator<ScanDataPoint>() {
                    @Override
                    public int compare(ScanDataPoint o1, ScanDataPoint o2) {
                        if(o1.getVal() < o2.getVal())
                            return -1;
                        else if(o1.getVal() == o2.getVal())
                             return 0;
                        else
                            return 1;
                    }
                });
                for(ScanDataPoint element : context.dataPoints) {
                    heatMap.addData(new HeatMap.DataPoint(element.getX(), element.getY(), element.getVal()));
                }
                //long after = System.nanoTime();
                //Log.d("HeatMapRuntime","Runtime Adding Data to HeatMap: " + (after-before));
               // before = System.nanoTime();
                heatMap.forceRefresh();
                //after = System.nanoTime();
                //Log.d("HeatMapRuntime","Runtime Force Refresh: " + (after-before));
                handler.postDelayed(runnable, REFRESH_RATE_MS);
            }
        };
        handler.postDelayed(runnable, REFRESH_RATE_MS);
        context.heatMapStarted = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

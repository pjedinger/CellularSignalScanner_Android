package fh_ooe.at.cellularsignalscanner;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import ca.hss.heatmaplib.HeatMap;

public class HeatMapDrawService extends Service{
    Handler handler;
    Runnable runnable;
    MainActivity context;
    HeatMap heatMap;
    private final int REFRESH_RATE_MS = 2000;

    public HeatMapDrawService(final MainActivity context) {
        this.context = context;
        this.heatMap = context.findViewById(R.id.signal_heatmap);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for(ScanDataPoint element : context.dataPoints) {
                    heatMap.addData(new HeatMap.DataPoint(element.x, element.y, element.val));
                }
                heatMap.forceRefresh();
                handler.postDelayed(runnable, REFRESH_RATE_MS);
            }
        };
        handler.postDelayed(runnable, REFRESH_RATE_MS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

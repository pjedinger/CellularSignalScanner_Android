package fh_ooe.at.cellularsignalscanner.tasks;

import android.os.AsyncTask;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import fh_ooe.at.cellularsignalscanner.data.ScanDataPoint;
import fh_ooe.at.cellularsignalscanner.libary.HeatMap;
import fh_ooe.at.cellularsignalscanner.activities.ScanResultActivity;
import fh_ooe.at.cellularsignalscanner.data.AppDatabase;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.data.ScanSingelton;
import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.libary.HeatMapMarkerCallback;

public class GetHistoryEntryTask extends AsyncTask<Integer, Void, HistoryEntry> {

    ScanResultActivity context;

    public GetHistoryEntryTask(ScanResultActivity context) {
        this.context = context;
    }

    @Override
    protected HistoryEntry doInBackground(Integer... integers) {
        AppDatabase database = ScanSingelton.getInstance().getDatabase();
        HistoryEntry historyEntry = database.historyEntryDao().loadById(integers[0]);
        return historyEntry;
    }

    @Override
    protected void onPostExecute(HistoryEntry historyEntry) {
        TextView minimumTextView = context.findViewById(R.id.scan_signalstrength_minimum_textview);
        TextView maximumTextView = context.findViewById(R.id.scan_signalstrength_maximum_textview);
        TextView averageTextView = context.findViewById(R.id.scan_signalstrength_average_textview);
        TextView qualityTextView = context.findViewById(R.id.scan_quality_textview);
        TextView connectionTextView = context.findViewById(R.id.scan_connection_textview);
        TextView scanDurationTextView = context.findViewById(R.id.scan_duration_textview);
        TextView dateLocationTextView = context.findViewById(R.id.scan_datelocation_textview);
        TextView scaleTextView = context.findViewById(R.id.scan_scale_textview);
        HeatMap scanResultHeatMap = context.findViewById(R.id.scanresult_heatmap);
        minimumTextView.setText(historyEntry.min+" dBm");
        maximumTextView.setText(historyEntry.max+" dBm");
        averageTextView.setText(historyEntry.avg+" dBm");
        qualityTextView.setText("Quality: "+historyEntry.quality);
        connectionTextView.setText(historyEntry.connection + ", "+historyEntry.provider);
        scaleTextView.setText(historyEntry.scale+ "m");

        if(historyEntry.scanDuration < 60000){
            scanDurationTextView.setText(TimeUnit.MILLISECONDS.toSeconds(historyEntry.scanDuration)+" sec");
        }else if(historyEntry.scanDuration < 3600000){
            scanDurationTextView.setText(TimeUnit.MILLISECONDS.toMinutes(historyEntry.scanDuration)+":"+TimeUnit.MILLISECONDS.toSeconds(historyEntry.scanDuration)%60+ " min");
        }else{
            scanDurationTextView.setText(TimeUnit.MILLISECONDS.toHours(historyEntry.scanDuration)+":"+TimeUnit.MILLISECONDS.toMinutes(historyEntry.scanDuration)%60+":"+ TimeUnit.MILLISECONDS.toSeconds(historyEntry.scanDuration)%3600+" hrs");
        }

        dateLocationTextView.setText(historyEntry.date + ", " + historyEntry.location);

        scanResultHeatMap.setMinimum(0.0);
        scanResultHeatMap.setMaximum(100.0);
        scanResultHeatMap.setRadius(1);
        scanResultHeatMap.setOpacity(0);
        scanResultHeatMap.setMarkerCallback(new HeatMapMarkerCallback.CircleHeatMapMarker(context));

        Collections.sort(historyEntry.scanDataPoints, new Comparator<ScanDataPoint>() {
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
        for(ScanDataPoint element : historyEntry.scanDataPoints) {
            scanResultHeatMap.addData(new HeatMap.DataPoint(element.getX(), element.getY(), element.getVal()));
        }
        scanResultHeatMap.forceRefresh();

        super.onPostExecute(historyEntry);
    }
}

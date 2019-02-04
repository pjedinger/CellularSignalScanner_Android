package fh_ooe.at.cellularsignalscanner.tasks;

import android.os.AsyncTask;
import android.widget.TextView;

import fh_ooe.at.cellularsignalscanner.libary.HeatMap;
import fh_ooe.at.cellularsignalscanner.activities.ScanResultActivity;
import fh_ooe.at.cellularsignalscanner.data.AppDatabase;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.data.ScanSingelton;
import fh_ooe.at.cellularsignalscanner.R;

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
        HeatMap scanResultHeatMap = context.findViewById(R.id.scanresult_heatmap);
        minimumTextView.setText(historyEntry.min+" dBm");
        maximumTextView.setText(historyEntry.max+" dBm");
        averageTextView.setText(historyEntry.avg+" dBm");
        qualityTextView.setText("Quality: "+historyEntry.quality);
        connectionTextView.setText(historyEntry.connection + ", "+historyEntry.provider);
        scanDurationTextView.setText(historyEntry.scanDuration + " sec");
        dateLocationTextView.setText(historyEntry.date + ", " + historyEntry.location);
        //TODO Set Heatmap in ScanResult
        //scanResultHeatMap.set



        super.onPostExecute(historyEntry);
    }
}

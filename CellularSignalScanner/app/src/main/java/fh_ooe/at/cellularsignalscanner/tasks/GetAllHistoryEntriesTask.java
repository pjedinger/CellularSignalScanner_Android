package fh_ooe.at.cellularsignalscanner.tasks;

import android.os.AsyncTask;
import android.widget.ListView;

import java.util.ArrayList;

import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.activities.HistoryActivity;
import fh_ooe.at.cellularsignalscanner.adapter.HistoryListAdapter;
import fh_ooe.at.cellularsignalscanner.data.AppDatabase;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.data.ScanSingelton;

public class GetAllHistoryEntriesTask extends AsyncTask<Void, Void, ArrayList<HistoryEntry>> {

    HistoryActivity context;

    public GetAllHistoryEntriesTask(HistoryActivity context) {
        this.context = context;
    }

    @Override
    protected ArrayList<HistoryEntry> doInBackground(Void... voids) {
        AppDatabase database = ScanSingelton.getInstance().getDatabase();
        ArrayList<HistoryEntry> historyEntries = new ArrayList<>(database.historyEntryDao().getAll());
        return historyEntries;
    }

    @Override
    protected void onPostExecute(ArrayList<HistoryEntry> historyEntries) {
        ListView historyListView = context.findViewById(R.id.history_listview);
        HistoryListAdapter historyListAdapter = new HistoryListAdapter(historyEntries, context);
        historyListView.setAdapter(historyListAdapter);
    }
}

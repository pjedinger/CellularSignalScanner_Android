package fh_ooe.at.cellularsignalscanner.tasks;

import android.os.AsyncTask;

import fh_ooe.at.cellularsignalscanner.data.AppDatabase;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.data.ScanSingelton;

public class AddHistoryEntryTask extends AsyncTask<HistoryEntry, Void, Void> {
    @Override
    protected Void doInBackground(HistoryEntry... historyEntries) {
        AppDatabase appDataBase = ScanSingelton.getInstance().getDatabase();
        appDataBase.historyEntryDao().insertAll(historyEntries);
        return null;
    }
}

package fh_ooe.at.cellularsignalscanner.tasks;

import android.os.AsyncTask;

import fh_ooe.at.cellularsignalscanner.data.AppDatabase;
import fh_ooe.at.cellularsignalscanner.data.ScanSingelton;

public class DeleteHistoryEntryTask extends AsyncTask<Integer, Void, Void>{
    @Override
    protected Void doInBackground(Integer... integers) {
        AppDatabase database = ScanSingelton.getInstance().getDatabase();
        int del = integers[0];
        database.historyEntryDao().deleteHistoryEntryById(del);
        return null;
    }
}

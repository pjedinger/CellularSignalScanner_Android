package fh_ooe.at.cellularsignalscanner.tasks;

import android.os.AsyncTask;

import fh_ooe.at.cellularsignalscanner.data.AppDatabase;
import fh_ooe.at.cellularsignalscanner.data.ScanSingelton;

public class UpdateHistoryEntryNameTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... strings) {
        AppDatabase database = ScanSingelton.getInstance().getDatabase();
        String name = strings[0];
        int id = Integer.parseInt(strings[1]);
        database.historyEntryDao().updateHistoryEntryName(name, id);
        return null;
    }
}

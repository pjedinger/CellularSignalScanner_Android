package fh_ooe.at.cellularsignalscanner.tasks;

import android.os.AsyncTask;

import fh_ooe.at.cellularsignalscanner.data.AppDatabase;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.data.ScanSingelton;
import fh_ooe.at.cellularsignalscanner.interfaces.AddHistoryEntryCallback;

public class AddHistoryEntryTask extends AsyncTask<HistoryEntry, Void, Integer>{

    public AddHistoryEntryCallback callback;

    @Override
    protected Integer doInBackground(HistoryEntry... historyEntries) {
        AppDatabase appDataBase = ScanSingelton.getInstance().getDatabase();
        long[] uids = appDataBase.historyEntryDao().insertAll(historyEntries);
        int uid = (int) uids[0];
        return uid;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(callback!=null){
            callback.onUidGenerated(integer);
        }
        super.onPostExecute(integer);
    }
}

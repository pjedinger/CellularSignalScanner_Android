package fh_ooe.at.cellularsignalscanner.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.tasks.GetAllHistoryEntriesTask;


public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.history));
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetAllHistoryEntriesTask getAllHistoryEntriesTask = new GetAllHistoryEntriesTask(this);
        getAllHistoryEntriesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void historyEntrySelected(HistoryEntry historyEntry) {
        //Toast.makeText(this, "Entry with Name: "+historyEntry.name+" clicked", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ScanResultActivity.class);
        i.putExtra("id", historyEntry.uid);
        i.putExtra("name", historyEntry.name);
        startActivity(i);
    }
}

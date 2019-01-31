package fh_ooe.at.cellularsignalscanner.activities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.adapter.HistoryListAdapter;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;


public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.history));

        ListView historyListView = findViewById(R.id.history_listview);
        ArrayList<HistoryEntry> historyEntries = new ArrayList<>();
        for (int i = 0; i<20; i++){
            historyEntries.add(new HistoryEntry("Scan"+i+1, i+1+".12.2018", "Linz", "25"+i));
        }

        HistoryListAdapter historyListAdapter = new HistoryListAdapter(historyEntries, this);
        historyListView.setAdapter(historyListAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void historyEntrySelected(HistoryEntry historyEntry) {
        Toast.makeText(this, "Entry with Name: "+historyEntry.getName()+" clicked", Toast.LENGTH_SHORT).show();
    }
}

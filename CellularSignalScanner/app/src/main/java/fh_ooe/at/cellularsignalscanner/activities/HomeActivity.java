package fh_ooe.at.cellularsignalscanner.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.data.AppDatabase;
import fh_ooe.at.cellularsignalscanner.data.ConnectionType;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.data.ScanInfo;
import fh_ooe.at.cellularsignalscanner.data.ScanSingelton;
import fh_ooe.at.cellularsignalscanner.tasks.AddHistoryEntryTask;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Button startScanButton = findViewById(R.id.home_startscan_button);
        Button historyButton = findViewById(R.id.home_history_button);

        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Data", "In startScanButton onClick");
                Intent i = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(i);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(i);
            }
        });

        ScanSingelton scanSingelton = ScanSingelton.getInstance();
        AppDatabase database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "appdatabase").fallbackToDestructiveMigration().build();
        scanSingelton.setDatabase(database);
        //adding database
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //only one element
        startInfoScreen();
        return super.onOptionsItemSelected(item);
    }

    private void startInfoScreen(){
        Intent i = new Intent(this, InfoActivity.class);
        startActivity(i);
    }
}

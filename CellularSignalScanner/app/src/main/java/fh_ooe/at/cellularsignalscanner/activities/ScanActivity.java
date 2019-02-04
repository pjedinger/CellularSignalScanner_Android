package fh_ooe.at.cellularsignalscanner.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.data.ScanDataPoint;
import fh_ooe.at.cellularsignalscanner.libary.HeatMap;
import fh_ooe.at.cellularsignalscanner.libary.HeatMapMarkerCallback;
import fh_ooe.at.cellularsignalscanner.service.HeatMapDrawService;
import fh_ooe.at.cellularsignalscanner.service.ScanInfoService;
import fh_ooe.at.cellularsignalscanner.tasks.AddHistoryEntryTask;

public class ScanActivity extends AppCompatActivity {


    public HeatMap heatMap;
    public List<ScanDataPoint> dataPoints;
    public List<ScanDataPoint> duplicateDataPoints;
    public ScanDataPoint lastAddedDataPoint;
    public int min, max = -200; //dBm metadata
    public long startTime;
    public int heatMapRadius = 10;
    public int entryCount = 0;
    public boolean scanStart = false, heatMapStarted = false;
    public HistoryEntry historyEntry;

    HeatMapDrawService heatMapDrawService;
    ScanInfoService scanInfoService;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        duplicateDataPoints = new ArrayList<>();
        dataPoints = new ArrayList<>();
        String[] perms = {"android.permission.FINE_LOCATION"};
        int permsRequestCode = 200;
        requestPermissions(perms, permsRequestCode);

        TextView durationTextView = findViewById(R.id.scan_duration_textview);
        durationTextView.setText(0+" sec");
        startTime = System.currentTimeMillis();

        heatMap = findViewById(R.id.scan_heatmap);
        heatMap.setMinimum(0.0);
        heatMap.setMaximum(100.0);
        heatMap.setRadius(1);
        heatMap.setOpacity(0);
        //draw a dark violet circle at the location of each data point
        heatMap.setMarkerCallback(new HeatMapMarkerCallback.CircleHeatMapMarker(this));

        Map<Float, Integer> colors = new ArrayMap<>();
        //build a color gradient in HSV from red at the center to green at the outside
//        for (int i = 0; i < 21; i++) {
//            float stop = ((float)i) / 20.0f;
//            //colors here are ids - deswegen 6 stellen --> example
//            //int primaryColor = getColor(R.color.colorPrimary); //definition im colors.xml
//            int color = doGradient(i * 5, 0, 100, 0xB22222, 0x008000);
//            colors.put(stop, color);
//        }
        colors.put(0.33f, Color.RED);
        colors.put(0.66f, Color.YELLOW);
        colors.put(1f, Color.GREEN);

       heatMap.setColorStops(colors);

        //do nothing when touched
        SeekBar seekBar = findViewById(R.id.scan_seekbar);
        seekBar.setEnabled(false);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Log.d("Data", "Starting the ScanInfo and heatMapDraw Service");
        if(scanStart == false) {
            scanInfoService = new ScanInfoService(telephonyManager, this);
        }
        if(heatMapStarted == false) {
            heatMapDrawService = new HeatMapDrawService(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 200:

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                break;
        }
    }
    private static int doGradient(double value, double min, double max, int min_color, int max_color) {
        if (value >= max) {
            return max_color;
        }
        if (value <= min) {
            return min_color;
        }
        float[] hsvmin = new float[3];
        float[] hsvmax = new float[3];
        float frac = (float)((value - min) / (max - min));
        Color.RGBToHSV(Color.red(min_color), Color.green(min_color), Color.blue(min_color), hsvmin);
        Color.RGBToHSV(Color.red(max_color), Color.green(max_color), Color.blue(max_color), hsvmax);
        float[] retval = new float[3];
        for (int i = 0; i < 3; i++) {
            retval[i] = interpolate(hsvmin[i], hsvmax[i], frac);
        }
        return Color.HSVToColor(retval);
    }
    private static float interpolate(float a, float b, float proportion) {
        return (a + ((b - a) * proportion));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.menu.scan_menu){
            //stop and go to result
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       // scanInfoService.stopService(new Intent(this, ScanInfoService.class));
        scanInfoService.stopScanInfoService();

        if(historyEntry != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            sp.edit().putInt("scanCount", sp.getInt("scanCount", 1) + 1).commit();

            AddHistoryEntryTask historyEntryTask = new AddHistoryEntryTask();
            historyEntryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, historyEntry);
        }
        heatMapDrawService.stopHeatMapDrawService();
        this.finish();
        //heatMapDrawService.stopService(new Intent(this, HeatMapDrawService.class));
    }
}

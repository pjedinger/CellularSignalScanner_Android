package fh_ooe.at.cellularsignalscanner.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.hss.heatmaplib.HeatMap;
import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.service.HeatMapDrawService;
import fh_ooe.at.cellularsignalscanner.data.ScanDataPoint;
import fh_ooe.at.cellularsignalscanner.service.ScanInfoService;

public class MainActivity extends AppCompatActivity {


    public HeatMap heatMap;
    public List<ScanDataPoint> dataPoints;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataPoints = new ArrayList<>();
        String[] perms = {"android.permission.FINE_LOCATION"};
        int permsRequestCode = 200;
        requestPermissions(perms, permsRequestCode);


        heatMap = findViewById(R.id.signal_heatmap);
        heatMap.setMinimum(0.0);
        heatMap.setMaximum(100.0);
        heatMap.setOpacity(0);
        heatMap.setRadius(700);

        Map<Float, Integer> colors = new ArrayMap<>();
        //build a color gradient in HSV from red at the center to green at the outside
        for (int i = 0; i < 21; i++) {
            float stop = ((float)i) / 20.0f;
            int color = doGradient(i * 5, 0, 100, 0xB22222, 0x008000);
            colors.put(stop, color);
        }
//        colors.put(0.33f, Color.RED);
//        colors.put(0.66f, Color.YELLOW);
//        colors.put(1f, Color.GREEN);

       heatMap.setColorStops(colors);

        //add random data to the map
//        Random rand = new Random();
//        for (int i = 0; i < 20; i++) {
//            HeatMap.DataPoint point = new HeatMap.DataPoint(rand.nextFloat(), rand.nextFloat(), rand.nextDouble() * 100.0);
//            heatMap.addData(point);
//        }
//        heatMap.setMarkerCallback(new HeatMapMarkerCallback.CircleHeatMapMarker(0xff9400D3));

//        heatMap.setMarkerCallback(new HeatMapMarkerCallback() {
//            @Override
//            public void drawMarker(Canvas canvas, float v, float v1) {
//                Paint paint = new Paint();
//                int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
//                paint.setColor(color);
//                canvas.drawCircle(5, 5, 360, paint);
//            }
//        });

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
        ScanInfoService scanInfoService = new ScanInfoService(telephonyManager, this);
        HeatMapDrawService heatMapDrawService = new HeatMapDrawService(this);
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
}

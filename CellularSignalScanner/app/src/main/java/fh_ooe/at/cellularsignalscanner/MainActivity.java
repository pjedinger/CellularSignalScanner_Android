package fh_ooe.at.cellularsignalscanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import ca.hss.heatmaplib.HeatMap;

public class MainActivity extends AppCompatActivity {


    HeatMap heatMap;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] perms = {"android.permission.FINE_LOCATION"};
        int permsRequestCode = 200;
        requestPermissions(perms, permsRequestCode);


        heatMap = findViewById(R.id.signal_heatmap);
        heatMap.setMinimum(0.0);
        heatMap.setMaximum(100.0);

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
}

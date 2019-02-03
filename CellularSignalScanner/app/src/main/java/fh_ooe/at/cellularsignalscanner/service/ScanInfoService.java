package fh_ooe.at.cellularsignalscanner.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;

import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;
import fh_ooe.at.cellularsignalscanner.data.ScanInfo;
import fh_ooe.at.cellularsignalscanner.activities.ScanActivity;
import fh_ooe.at.cellularsignalscanner.data.ScanServiceMetadata;
import fh_ooe.at.cellularsignalscanner.interfaces.AsyncResponse;
import fh_ooe.at.cellularsignalscanner.tasks.ScanInfoTask;

public class ScanInfoService extends Service implements LocationListener, AsyncResponse {
    Handler handler;
    Runnable runnable;
    TelephonyManager telephonyManager;
    ScanActivity context;

    HistoryEntry historyEntry;

    Location referenceLocation = null;
    private final int REFRESH_RATE_MS = 300;

    Boolean isGPSEnabled = false, isNetworkEnabled = false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 second

    Location location;

    public ScanInfoService(final TelephonyManager telephonyManager, final ScanActivity context) {
        this.telephonyManager = telephonyManager;
        this.context = context;

        //Create Location Manager and check which Providers are enabled.
        final LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        try {
            //Getting the location of the scan.
            // Getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
            } else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { }
                //  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                try {
                    ScanInfoTask scanInfoTask = new ScanInfoTask(context, location, referenceLocation);
                    //Pair<ScanInfo, Location> locPair = scanInfoTask.execute(telephonyManager).get();
                    ScanServiceMetadata scanServiceMetadata = scanInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, telephonyManager).get();

                    //here the delegate for history creation.
                    //scanInfoTask.delegate = this;

                    if(referenceLocation == null) {
                        referenceLocation = scanServiceMetadata.getLocation();
                    }
                   // historyEntry = scanServiceMetadata.getHistoryEntry();
                    handler.postDelayed(runnable, REFRESH_RATE_MS);
                    Thread.currentThread().setName("HelloGoodSir");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, REFRESH_RATE_MS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void processFinish(ScanServiceMetadata scanServiceMetadata) {
        //return of PostExecute in asynctask
    }
}

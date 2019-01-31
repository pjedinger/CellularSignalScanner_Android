package fh_ooe.at.cellularsignalscanner.data;

import android.location.Location;
import android.util.Log;

public class ScanInfo {
    ConnectionType connectionType;
    int dbm;
    String provider;
    Location location;


    public ScanInfo(ConnectionType connectionType, int dbm, String provider, Location location) {
        this.connectionType = connectionType;
        this.dbm = dbm;
        this.provider = provider;
        this.location = location;
        Log.d("ScanInfo: ", "Connection Type: " + getConnectionType().name() + " DBm: " + dbm + " Location: Long: " + getLongitude() + " Lat: " + getLatitude());
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public int getDbm() {
        return dbm;
    }

    public void setDbm(int dbm) {
        this.dbm = dbm;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public double getLatitude(){
        if(location != null){
            return location.getLatitude();
        }
        return Integer.MAX_VALUE;
    }
    public double getLongitude(){
        if(location != null){
            return location.getLongitude();
        }
        return Integer.MAX_VALUE;
    }
    public Location getLocation(){return location;}

}

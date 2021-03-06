package fh_ooe.at.cellularsignalscanner.data;

public class ScanDataPoint {
    float x, y;
    int val, dbm;
    double latitude, longitude;

    public ScanDataPoint(float x, float y, int val, int dbm, double latitude, double longitude){
        this.x = x;
        this.y = y;
        this.val = val;
        this.dbm = dbm;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ScanDataPoint){
            ScanDataPoint dp = (ScanDataPoint)obj;
            if(dp.getX() == this.x && dp.getY() == this.y){
                return true;
            }
        }
        return false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getVal() {
        return val;
    }

    public int getDbm() {
        return dbm;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

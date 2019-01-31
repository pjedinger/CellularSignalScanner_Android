package fh_ooe.at.cellularsignalscanner.data;

public class ScanDataPoint {
    float x, y;
    int val;

    public ScanDataPoint(float x, float y, int val){
        this.x = x;
        this.y = y;
        this.val = val;
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
}

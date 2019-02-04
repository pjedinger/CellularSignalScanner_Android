package fh_ooe.at.cellularsignalscanner.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

@Entity
public class HistoryEntry {
    @PrimaryKey (autoGenerate=true)
    public int uid;
    @ColumnInfo()
    public String name;
    @ColumnInfo
    public String date;
    @ColumnInfo
    public String location;
    @ColumnInfo
    public int scanDuration;
    @ColumnInfo
    public String connection;
    @ColumnInfo
    public String provider;
    @ColumnInfo
    public ArrayList<ScanDataPoint> scanDataPoints;
    @ColumnInfo
    public int min;
    @ColumnInfo
    public String quality;
    @ColumnInfo
    public int max;
    @ColumnInfo
    public int avg;
    @ColumnInfo
    public int scale;

    @Ignore
    public HistoryEntry(String name, String date, String location, int scanDuration, String connection, String provider, ArrayList<ScanDataPoint> scanDataPoints, int min, String quality, int max, int avg, int scale) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.scanDuration = scanDuration;
        this.connection = connection;
        this.provider = provider;
        this.scanDataPoints = scanDataPoints;
        this.min = min;
        this.quality = quality;
        this.max = max;
        this.avg = avg;
        this.scale = scale;
    }

    public HistoryEntry() {
    }
}

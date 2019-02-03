package fh_ooe.at.cellularsignalscanner.data;

import android.location.Location;

public class ScanServiceMetadata {
    ScanInfo scanInfo;
    Location location;
    HistoryEntry historyEntry;

    public ScanServiceMetadata(ScanInfo scanInfo, Location location, HistoryEntry historyEntry) {
        this.scanInfo = scanInfo;
        this.location = location;
        this.historyEntry = historyEntry;
    }

    public ScanInfo getScanInfo() {
        return scanInfo;
    }

    public void setScanInfo(ScanInfo scanInfo) {
        this.scanInfo = scanInfo;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public HistoryEntry getHistoryEntry() {
        return historyEntry;
    }

    public void setHistoryEntry(HistoryEntry historyEntry) {
        this.historyEntry = historyEntry;
    }
}

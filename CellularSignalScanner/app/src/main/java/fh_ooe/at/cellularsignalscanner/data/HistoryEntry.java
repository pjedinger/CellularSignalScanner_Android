package fh_ooe.at.cellularsignalscanner.data;

public class HistoryEntry {
    private String name;
    private String date;
    private String location;
    private String scanDuration;

    public HistoryEntry(String name, String datum, String location, String scanDuration) {
        this.name = name;
        this.date = datum;
        this.location = location;
        this.scanDuration = scanDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getScanDuration() {
        return scanDuration;
    }

    public void setScanDuration(String scanDuration) {
        this.scanDuration = scanDuration;
    }
}

package fh_ooe.at.cellularsignalscanner;

public class ScanInfo {
    ConnectionType connectionType;
    int dbm;
    String provider;

    public ScanInfo(ConnectionType connectionType, int dbm, String provider) {
        this.connectionType = connectionType;
        this.dbm = dbm;
        this.provider = provider;
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
}

package fh_ooe.at.cellularsignalscanner.data;

import java.util.ArrayList;

public class ScanSingelton {
    private static volatile ScanSingelton instance;

    private AppDatabase database;

    public static ScanSingelton getInstance() {
        if(instance == null){
            synchronized (ScanSingelton.class){
                if(instance == null){
                    instance = new ScanSingelton();
                }
            }
        }
        return instance;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}

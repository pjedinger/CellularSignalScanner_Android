package fh_ooe.at.cellularsignalscanner.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {HistoryEntry.class}, version = 3)
@TypeConverters({ScanInfoConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract HistoryEntryDao historyEntryDao();
}

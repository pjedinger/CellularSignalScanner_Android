package fh_ooe.at.cellularsignalscanner.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface HistoryEntryDao {
        @Query("SELECT * FROM historyentry")
        List<HistoryEntry> getAll();

        @Query("SELECT * FROM historyentry WHERE uid = :historyEntryId LIMIT 1")
        HistoryEntry loadById(int historyEntryId);

        @Query("UPDATE HistoryEntry SET name=:nameUpdate WHERE uid =:historyEntryId")
        void updateHistoryEntryName(String nameUpdate, int historyEntryId);

        @Query("DELETE FROM historyentry WHERE uid = :historyEntryId")
        void deleteHistoryEntryById(int historyEntryId);

        @Insert
        void insertAll(HistoryEntry... users);

        @Delete
        void delete(HistoryEntry user);
    }

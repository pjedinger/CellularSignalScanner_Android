package fh_ooe.at.cellularsignalscanner.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScanDataPointConverter {
    @TypeConverter
    public static ArrayList<ScanDataPoint> fromString(String value) {
        Type listType = new TypeToken<ArrayList<ScanDataPoint>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<ScanDataPoint> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
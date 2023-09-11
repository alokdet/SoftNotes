package com.example.softnotesbeta.Database;

import androidx.room.TypeConverter;

import com.example.softnotesbeta.Models.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StepsTypeConverter {

    @TypeConverter
    public static List<Step> fromString(String value) {
        Type listType = new TypeToken<List<Step>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Step> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

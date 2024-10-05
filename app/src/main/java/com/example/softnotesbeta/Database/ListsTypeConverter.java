package com.example.softnotesbeta.Database;

import androidx.room.TypeConverter;

import com.example.softnotesbeta.Models.ListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListsTypeConverter {

    @TypeConverter
    public static List<ListItem> fromString(String value) {
        Type listType = new TypeToken<List<ListItem>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<ListItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

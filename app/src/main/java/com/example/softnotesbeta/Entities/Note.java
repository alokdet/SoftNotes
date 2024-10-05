package com.example.softnotesbeta.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.softnotesbeta.Models.ListItem;
import com.example.softnotesbeta.Models.Step;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "parent")
    public String parent;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "list")
    public List<ListItem> list;

    @ColumnInfo(name = "indexList")
    public List<ListItem> indexList;

    @ColumnInfo(name = "keyList")
    public List<ListItem> keyList;

    @ColumnInfo(name = "valueList")
    public List<ListItem> valueList;

    @ColumnInfo(name = "timeList")
    public List<ListItem> timeList;

    public Note(String title, String text, String date, String type) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.type = type;
    }

    public List<ListItem> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<ListItem> timeList) {
        this.timeList = timeList;
    }

    public List<ListItem> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<ListItem> indexList) {
        this.indexList = indexList;
    }

    public List<ListItem> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<ListItem> keyList) {
        this.keyList = keyList;
    }

    public List<ListItem> getValueList() {
        return valueList;
    }

    public void setValueList(List<ListItem> valueList) {
        this.valueList = valueList;
    }

    public List<ListItem> getList() {
        return list;
    }

    public void setList(List<ListItem> list) {
        this.list = list;
    }

    public void addListItem(ListItem item) {
        this.list.add(item);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id && Objects.equals(title, note.title) && Objects.equals(text, note.text) && Objects.equals(date, note.date) && Objects.equals(parent, note.parent) && Objects.equals(type, note.type) && Objects.equals(list, note.list)  && Objects.equals(keyList, note.keyList) && Objects.equals(indexList, note.indexList) && Objects.equals(valueList, note.valueList) && Objects.equals(timeList, note.timeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, date, parent, type, list, keyList, indexList, valueList, timeList);
    }
}

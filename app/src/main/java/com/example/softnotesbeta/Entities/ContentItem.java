package com.example.softnotesbeta.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "content")
public class ContentItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "value")
    public String value;

    @ColumnInfo(name = "parent")
    public Long parent;

    public ContentItem(String type, String value, Long parent) {
        this.type = type;
        this.value = value;
        this.parent = parent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentItem contentItem = (ContentItem) o;
        return id == contentItem.id && Objects.equals(type, contentItem.type) && Objects.equals(value, contentItem.value) && Objects.equals(parent, contentItem.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, value, parent);
    }
}

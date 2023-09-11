package com.example.softnotesbeta.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "previews")
public class Preview {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "note_id")
    public long noteId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "text")
    public String preview;

    @ColumnInfo(name = "date")
    public String date;

    public boolean isSelected = false;

    public Preview(long noteId, String title, String preview, String date) {
        this.noteId = noteId;
        this.title = title;
        this.preview = preview;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
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
        Preview preview1 = (Preview) o;
        return id == preview1.id && Objects.equals(title, preview1.title) && Objects.equals(preview, preview1.preview) && Objects.equals(date, preview1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, preview, date);
    }
}

package com.example.softnotesbeta.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.softnotesbeta.Models.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "date_created")
    public String dateCreated;

    @ColumnInfo(name = "due_date")
    public String dueDate;

    @ColumnInfo(name = "steps")
    public List<Step> steps;

    private boolean isSelected = false;

    public Task() {

    }

    public Task(String title, String dateCreated, String dueDate) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.dueDate = dueDate;
    }

    public void addStep(Step step) {
        this.steps.add(step);
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task note = (Task) o;
        return id == note.id && Objects.equals(title, note.title) && Objects.equals(dateCreated, note.dateCreated) && Objects.equals(dueDate, note.dueDate) && Objects.equals(steps, note.steps) && Objects.equals(isSelected, note.isSelected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, dateCreated, dueDate, steps, isSelected);
    }
}

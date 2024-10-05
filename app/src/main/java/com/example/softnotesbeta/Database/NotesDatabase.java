package com.example.softnotesbeta.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.softnotesbeta.DAOs.ContentDao;
import com.example.softnotesbeta.DAOs.FolderDao;
import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.DAOs.ReminderDao;
import com.example.softnotesbeta.DAOs.StepDao;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Entities.ContentItem;
import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Entities.Reminder;
import com.example.softnotesbeta.Entities.Step;
import com.example.softnotesbeta.Entities.Task;

@Database(entities = {Note.class, Preview.class, Task.class, Reminder.class, Folder.class, Step.class, ContentItem.class}, version = 1)
@TypeConverters({StepsTypeConverter.class, ListsTypeConverter.class})
public abstract class NotesDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();
    public abstract PreviewDao previewDao();
    public abstract TaskDao taskDao();
    public abstract ReminderDao reminderDao();
    public abstract FolderDao folderDao();
    public abstract StepDao stepDao();
    public abstract ContentDao contentDao();
    public static NotesDatabase INSTANCE;

    public static NotesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, "notes").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}

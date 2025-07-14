package com.example.softnotesbeta;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Adapters.TableAdapter;
import com.example.softnotesbeta.Adapters.TimelineAdapter;
import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Models.ListItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimelineWorkspace extends AppCompatActivity implements TimelineItemListener, TimelineItemRemovedListener {

    private AppCompatEditText titleField;
    private AppCompatEditText textField;
    private AppCompatTextView dateView;
    private AppCompatTextView timeVIew;
    private RecyclerView timelineView;
    private ConstraintLayout layout;

    private TimelineAdapter adapter;
    private NotesDatabase database;
    private PreviewDao dao;
    private NoteDao noteDao;
    private long noteId;
    private long previewId;
    private boolean update;
    private int currentPosition;
    private List<ListItem> timeList;
    private List<ListItem> textList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {

            case Configuration.UI_MODE_NIGHT_YES:
                setTheme(R.style.DarkTheme_SoftNotesBeta);
                getWindow().setNavigationBarColor(Color.BLACK);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                setTheme(R.style.LightTheme_SoftNotesBeta);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setNavigationBarColor(Color.WHITE);
                break;
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timeline_workspace);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        Intent intent = getIntent();

        database = NotesDatabase.getInstance(getApplicationContext());
        dao = database.previewDao();
        noteDao = database.noteDao();

        titleField = (AppCompatEditText) findViewById(R.id.title_box2);
        textField = (AppCompatEditText) findViewById(R.id.text_field);
        dateView = (AppCompatTextView) findViewById(R.id.date_time_view);
        timeVIew = (AppCompatTextView) findViewById(R.id.action_time);
        timelineView = (RecyclerView) findViewById(R.id.timeline_contents);
        layout = (ConstraintLayout) findViewById(R.id.foreground_surface);

        ViewCompat.setTransitionName(layout, getIntent().getStringExtra("transitionName1"));
        noteId = intent.getLongExtra("noteId", 0);
        previewId = intent.getLongExtra("previewId", 0);


        if (noteId != 0) {
            Note note = noteDao.getNote(noteId);
            timeList = note.getTimeList();
            textList = note.getList();

            titleField.setText(intent.getStringExtra("title"));
        } else {
            timeList = new ArrayList<>();
            textList = new ArrayList<>();
        }

        adapter = new TimelineAdapter(textList, timeList, this::onClick, this::onItemRemoved);
        timelineView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        timelineView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(timelineView);

        dateView.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date()));
        timeVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                if (update) {
                    timeList.get(currentPosition).setStringText(time);
                    textList.get(currentPosition).setStringText(textField.getText().toString());
                    update = false;
                } else {
                    String text = textField.getText().toString();

                    timeList.add(new ListItem(time));
                    textList.add(new ListItem(text));
                }
                adapter.notifyDataSetChanged();
                textField.setText("");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (dao.isNoteExist(previewId) == 0) {
            Note note = new Note(titleField.getText().toString(), "", dateView.getText().toString(), "timeline");
            note.setList(textList);
            note.setTimeList(timeList);

            long savedNoteId = noteDao.insertNoteToDatabase(note);

            Preview preview = new Preview(savedNoteId, titleField.getText().toString(), "Timeline", dateView.getText().toString(), "timeline");
            dao.insertNoteToDatabase(preview);
        } else {
            Note note = noteDao.getNote(noteId);
            note.setList(textList);
            note.setTimeList(timeList);

            noteDao.updateNote(note);

            Preview preview = dao.getPreview(previewId);
            preview.setTitle(titleField.getText().toString());
            dao.updatePreview(preview);
        }
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            adapter.removeItem(position);
        }
    };

    @Override
    public void onClick(int position, String time, String text) {
        update = true;
        this.currentPosition = position;
        textField.setText(text);
        timeVIew.setText(time);
    }

    @Override
    public void onItemRemoved(int position, String time, String text) {

    }
}
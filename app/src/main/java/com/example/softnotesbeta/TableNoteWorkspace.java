package com.example.softnotesbeta;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Adapters.TableAdapter;
import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Models.ListItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TableNoteWorkspace extends AppCompatActivity implements TableItemListener, TableItemRemovedListener {

    private static final int COLUMN_KEY = 0;
    private static final int COLUMN_VALUE = 1;
    private static final int OPERATION_SUMMATION = 2;
    private static final int OPERATION_AVERAGE = 3;
    private static final int OPERATION_PERCENTAGE = 4;
    private RecyclerView tableView;
    private AppCompatEditText keyField;
    private AppCompatEditText valueField;
    private AppCompatEditText titleField;
    private AppCompatImageView actionInsertFields;
    private AppCompatImageView actionCloseFields;
    private AppCompatImageView actionShowHideIndexes;
    private AppCompatImageView operationSummation;
    private AppCompatImageView operationPercentage;
    private AppCompatImageView operationAverage;
    private BottomSheetBehavior opResultBottomSheet;
    private BottomSheetBehavior percentageInputBottomSheet;
    private ConstraintLayout percentageInputLayout;
    private ConstraintLayout opResultLayout;
    private AppCompatTextView dateTimeView;
    private ConstraintLayout layout;
    private ConstraintLayout bottomActions;
    private ConstraintLayout textFieldsContainer;
    private ConstraintLayout ops;
    private LinearLayout top_handle;
    private AppCompatTextView operationLabelTv;
    private AppCompatTextView selectKeyColumn;
    private AppCompatTextView selectValueColumn;
    private AppCompatTextView operationResultTv;
    private AppCompatImageView closeResultPanel;
    private AppCompatImageView proceedPercentageCalculation;
    private AppCompatEditText inputPercentageValue;

    private List<ListItem> indexList;
    private List<ListItem> keyList;
    private List<ListItem> valueList;
    private List<ListItem> removedIndexItems = new ArrayList<>();
    private List<ListItem> removedKeyItems = new ArrayList<>();
    private List<ListItem> removedValueItems = new ArrayList<>();

    private TableAdapter adapter;

    private Drawable keyBackgroundDrawable;
    private Drawable valueBackgroundDrawable;
    private NotesDatabase database;
    private PreviewDao dao;
    private NoteDao noteDao;
    private long noteId;
    private long previewId;
    private boolean compose = false;
    private boolean update = false;
    private Float percentageOutOf;
    private int updatePosition = -1;
    private int undoPosition = -1;
    private int operationColumn = COLUMN_VALUE;
    private int currentOperation;

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
        setContentView(R.layout.activity_table_note_workspace);
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

        tableView = (RecyclerView) findViewById(R.id.table_contents);
        keyField = (AppCompatEditText) findViewById(R.id.column_first_input);
        valueField = (AppCompatEditText) findViewById(R.id.column_two_input);
        actionInsertFields = (AppCompatImageView) findViewById(R.id.add_element);
        actionCloseFields = (AppCompatImageView) findViewById(R.id.close_fields_panel);
        //actionShowHideIndexes = (AppCompatImageView) findViewById(R.id.action_hide_index);
        operationSummation = (AppCompatImageView) findViewById(R.id.operation_summation);
        //operationPercentage = (AppCompatImageView) findViewById(R.id.operation_percentage);
        //actionRedo = (AppCompatImageView) findViewById(R.id.action_redo);
        operationAverage = (AppCompatImageView) findViewById(R.id.operation_average);
        titleField = (AppCompatEditText) findViewById(R.id.title_box);
        dateTimeView = (AppCompatTextView) findViewById(R.id.date_time_view);
        layout = (ConstraintLayout) findViewById(R.id.foreground_surface);
        bottomActions = (ConstraintLayout) findViewById(R.id.actions_two);
        textFieldsContainer = (ConstraintLayout) findViewById(R.id.text_fields_container);
        ops = (ConstraintLayout) findViewById(R.id.table_ops);
        top_handle = (LinearLayout) findViewById(R.id.top_jj);

        opResultLayout = findViewById(R.id.bottom_sheet_average);
        opResultBottomSheet = BottomSheetBehavior.from(opResultLayout);

//        percentageInputLayout = findViewById(R.id.bottom_sheet_percentage);
//        percentageInputBottomSheet = BottomSheetBehavior.from(percentageInputLayout);

        opResultBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        //percentageInputBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        operationLabelTv = opResultLayout.findViewById(R.id.top_text);
        selectKeyColumn = opResultLayout.findViewById(R.id.key_column);
        selectValueColumn = opResultLayout.findViewById(R.id.value_column);
        operationResultTv = opResultLayout.findViewById(R.id.result);
        closeResultPanel = opResultLayout.findViewById(R.id.close_result_panel);

//        proceedPercentageCalculation =  percentageInputLayout.findViewById(R.id.proceed);
//        inputPercentageValue = percentageInputLayout.findViewById(R.id.input_value);

        ViewCompat.setTransitionName(layout, getIntent().getStringExtra("transitionName1"));
        noteId = intent.getLongExtra("noteId", 0);
        previewId = intent.getLongExtra("previewId", 0);

        if (noteId != 0) {
            Note note = noteDao.getNote(noteId);
            indexList = note.getIndexList();
            keyList = note.getKeyList();
            valueList = note.getValueList();

            titleField.setText(intent.getStringExtra("title"));
        } else {
            indexList = new ArrayList<>();
            keyList = new ArrayList<>();
            valueList = new ArrayList<>();
        }

        adapter = new TableAdapter(indexList, keyList, valueList, this::onClick, this::onItemRemoved);
        tableView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tableView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(tableView);

        dateTimeView.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date()));
        actionInsertFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (compose) {
                    String key = keyField.getText().toString();
                    String value = valueField.getText().toString();

                    if (update) {
                        keyList.get(updatePosition).setStringText(key);
                        valueList.get(updatePosition).setStringText(value);
                        update = false;
                    } else {
                        keyList.add(new ListItem(key));
                        valueList.add(new ListItem(value));
                        indexList.add(new ListItem(String.valueOf(keyList.size())));
                    }

                    adapter.notifyDataSetChanged();
                    keyField.setText("");
                    valueField.setText("");
                } else {
                    animateOpenLayout();
                }
            }
        });

//        proceedPercentageCalculation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (TextUtils.isEmpty(inputPercentageValue.getText())) {
//                    Toast.makeText(TableNoteWorkspace.this, "Value cannot be null", Toast.LENGTH_SHORT).show();
//                } else {
//                    percentageOutOf = Float.parseFloat(inputPercentageValue.getText().toString());
//                    percentageInputBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
//
//                    currentOperation = OPERATION_PERCENTAGE;
//                    opResultBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    operationLabelTv.setText("Percentage of the Column");
//
//                    String result;
//
//                    if (operationColumn == COLUMN_KEY) {
//                        result = calculatePercentage(keyList);
//                    } else {
//                        result = calculatePercentage(valueList);
//                    }
//
//                    operationResultTv.setText(result);
//                }
//            }
//        });

        actionCloseFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateCLoseLayout();
            }
        });

        closeResultPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opResultBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        operationSummation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opResultBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                operationLabelTv.setText("Sum of the Column");
                currentOperation = OPERATION_SUMMATION;
                String result;

                if (operationColumn == COLUMN_KEY) {
                    result = calculateSum(keyList);
                } else {
                    result = calculateSum(valueList);
                }

                operationResultTv.setText(result);
            }
        });

//        operationPercentage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                percentageInputBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });

        operationAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOperation = OPERATION_AVERAGE;
                opResultBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                operationLabelTv.setText("Average of the Column");

                String result;

                if (operationColumn == COLUMN_KEY) {
                    result = calculateAverage(keyList);
                } else {
                    result = calculateAverage(valueList);
                }

                operationResultTv.setText(result);
            }
        });

        selectKeyColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBackgroundDrawable = DrawableCompat.wrap(selectKeyColumn.getBackground()).mutate();
                valueBackgroundDrawable = DrawableCompat.wrap(selectValueColumn.getBackground()).mutate();
                DrawableCompat.setTint(keyBackgroundDrawable, getResources().getColor(R.color.onBackground_light));
                DrawableCompat.setTint(valueBackgroundDrawable, getResources().getColor(R.color.android_green));
                if (operationColumn == COLUMN_VALUE) {
                    operationColumn = COLUMN_KEY;
                    switch (currentOperation) {
                        case OPERATION_SUMMATION:
                            String sumResult = calculateSum(keyList);
                            operationResultTv.setText(sumResult);
                            break;
                        case OPERATION_PERCENTAGE:
                            String percentageResult = calculatePercentage(keyList);
                            operationResultTv.setText(percentageResult);
                            break;
                        case OPERATION_AVERAGE:
                            String averageResult = calculateAverage(keyList);
                            operationResultTv.setText(averageResult);
                            break;
                    }
                }
            }
        });

        selectValueColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBackgroundDrawable = DrawableCompat.wrap(selectKeyColumn.getBackground()).mutate();
                valueBackgroundDrawable = DrawableCompat.wrap(selectValueColumn.getBackground()).mutate();
                DrawableCompat.setTint(valueBackgroundDrawable, getResources().getColor(R.color.onBackground_light));
                DrawableCompat.setTint(keyBackgroundDrawable, getResources().getColor(R.color.android_green));
                if (operationColumn == COLUMN_KEY) {
                    operationColumn = COLUMN_VALUE;
                    switch (currentOperation) {
                        case OPERATION_SUMMATION:
                            String sumResult = calculateSum(valueList);
                            operationResultTv.setText(sumResult);
                            break;
                        case OPERATION_PERCENTAGE:
                            String percentageResult = calculatePercentage(valueList);
                            operationResultTv.setText(percentageResult);
                            break;
                        case OPERATION_AVERAGE:
                            String averageResult = calculateAverage(valueList);
                            operationResultTv.setText(averageResult);
                            break;
                    }
                }
            }
        });

        opResultBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        opResultBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        //percentageInputBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    public float getNumerals(List<ListItem> list) {
        float count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().matches("\\d+(?:\\.\\d+)?")) {
                count++;
            }
        }
        return count;
    }

    private String calculateAverage(List<ListItem> list) {
        float sum = Float.parseFloat(calculateSum(list));
        Float result = sum / getNumerals(list);
        return String.valueOf(result);
    }

    private String calculatePercentage(List<ListItem> list) {
        Float result = (Float.parseFloat(calculateSum(list)) / percentageOutOf) * 100;
        return String.valueOf(result);
    }

    private String calculateSum(List<ListItem> list) {
        Float result = 0f;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().matches("\\d+(?:\\.\\d+)?")) {
                float value = Float.parseFloat(list.get(i).getText());
                result += value;
            }
        }
        return String.valueOf(result);
    }

    private void animateOpenLayout() {
        compose = true;

        textFieldsContainer.setVisibility(View.VISIBLE);
        textFieldsContainer.animate().alpha(1);
        actionCloseFields.setVisibility(View.VISIBLE);
        actionCloseFields.animate().alpha(1);
        ops.animate().alpha(0);
        ops.setVisibility(View.GONE);
        top_handle.setVisibility(View.VISIBLE);
        top_handle.animate().alpha(1);

        ValueAnimator heightAnimation = ValueAnimator.ofInt(bottomActions.getMeasuredHeight(), dpToPixels(200));
        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = bottomActions.getLayoutParams();
                layoutParams.height = val;
                bottomActions.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator btnWidthAnimation = ValueAnimator.ofInt(actionInsertFields.getMeasuredWidth(), dpToPixels(50));
        btnWidthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = actionInsertFields.getLayoutParams();
                layoutParams.width = val;
                actionInsertFields.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator btnHeightAnimation = ValueAnimator.ofInt(actionInsertFields.getMeasuredHeight(), dpToPixels(50));
        btnHeightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = actionInsertFields.getLayoutParams();
                layoutParams.height = val;
                actionInsertFields.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator marginAnimation = ValueAnimator.ofInt(0, dpToPixels(24));
        marginAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) actionInsertFields.getLayoutParams();
                layoutParams.rightMargin = val;
                layoutParams.bottomMargin = val;
                actionInsertFields.setLayoutParams(layoutParams);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.playTogether(heightAnimation, btnHeightAnimation, btnWidthAnimation, marginAnimation);
        animatorSet.start();

        actionInsertFields.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.xxxxxxxxxxxxxx));
        Drawable backgroundDrawable = DrawableCompat.wrap(actionInsertFields.getBackground()).mutate();
        DrawableCompat.setTint(backgroundDrawable, getResources().getColor(R.color.android_green));
    }

    private void animateCLoseLayout() {
        compose = false;

        textFieldsContainer.animate().alpha(0);
        textFieldsContainer.setVisibility(View.GONE);
        actionCloseFields.animate().alpha(0);
        actionCloseFields.setVisibility(View.GONE);
        ops.setVisibility(View.VISIBLE);
        ops.animate().alpha(1);
        top_handle.animate().alpha(0);
        top_handle.setVisibility(View.GONE);

        ValueAnimator heightAnimation = ValueAnimator.ofInt(bottomActions.getMeasuredHeight(), dpToPixels(80));
        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = bottomActions.getLayoutParams();
                layoutParams.height = val;
                bottomActions.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator btnWidthAnimation = ValueAnimator.ofInt(actionInsertFields.getMeasuredWidth(), dpToPixels(100));
        btnWidthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = actionInsertFields.getLayoutParams();
                layoutParams.width = val;
                actionInsertFields.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator btnHeightAnimation = ValueAnimator.ofInt(actionInsertFields.getMeasuredHeight(), dpToPixels(80));
        btnHeightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = actionInsertFields.getLayoutParams();
                layoutParams.height = val;
                actionInsertFields.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator marginAnimation = ValueAnimator.ofInt(dpToPixels(24), 0);
        marginAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) actionInsertFields.getLayoutParams();
                layoutParams.rightMargin = val;
                layoutParams.bottomMargin = val;
                actionInsertFields.setLayoutParams(layoutParams);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.playTogether(heightAnimation, btnHeightAnimation, btnWidthAnimation, marginAnimation);
        animatorSet.start();

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.foreground_color, typedValue, true);

        actionInsertFields.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.table_insert_widget_bg));
        Drawable backgroundDrawable = DrawableCompat.wrap(actionInsertFields.getBackground()).mutate();
        DrawableCompat.setTint(backgroundDrawable, typedValue.data);
    }

    private int dpToPixels(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (dao.isNoteExist(previewId) == 0) {
            Note note = new Note(titleField.getText().toString(), "", dateTimeView.getText().toString(), "table");
            note.setKeyList(keyList);
            note.setIndexList(indexList);
            note.setValueList(valueList);

            long savedNoteId = noteDao.insertNoteToDatabase(note);

            Preview preview = new Preview(savedNoteId, titleField.getText().toString(), "Table", dateTimeView.getText().toString(), "table");
            dao.insertNoteToDatabase(preview);
        } else {
            Note note = noteDao.getNote(noteId);
            note.setKeyList(keyList);
            note.setIndexList(indexList);
            note.setValueList(valueList);

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
    public void onClick(int position, String key, String value) {
        if (!compose) {
            animateOpenLayout();
        }
        keyField.setText(key);
        valueField.setText(value);
        update = true;
        updatePosition = position;
    }

    @Override
    public void onItemRemoved(int position, String key, String value) {
        undoPosition = position;
        removedIndexItems.add(new ListItem(String.valueOf(position)));
        removedKeyItems.add(new ListItem(key));
        removedValueItems.add(new ListItem(value));
    }
}
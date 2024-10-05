package com.example.softnotesbeta.WorkspaceControllers;

import android.animation.ValueAnimator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Adapters.ListAdapter;
import com.example.softnotesbeta.ListItemListener;
import com.example.softnotesbeta.ListItemRemovedListener;
import com.example.softnotesbeta.Models.ListItem;
import com.example.softnotesbeta.R;

import java.util.ArrayList;
import java.util.List;

public class ListController implements ListItemRemovedListener, ListItemListener {

    private ConstraintLayout layout;
    private View view;
    private NoteControllerHandler handler;
    private List<ListItem> itemsList = new ArrayList<>();
    private ListAdapter listAdapter;
    private AppCompatTextView dateTimeTv;
    private RecyclerView list;
    private AppCompatEditText listItemInput;
    private AppCompatImageView saveListItemBtn;
    private AppCompatImageView enableViewMode;
    private ConstraintLayout composeContainer;
    private boolean compose = false;
    private boolean update = false;
    private int currentPosition;

    public ListController(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.list_note_layout, layout, false);
        handler = NoteControllerHandler.getInstance();
        dateTimeTv = (AppCompatTextView) view.findViewById(R.id.date_time_tv);
        list = (RecyclerView) view.findViewById(R.id.list);
        listItemInput = (AppCompatEditText) view.findViewById(R.id.list_item_input);
        saveListItemBtn = (AppCompatImageView) view.findViewById(R.id.save_list_item);
        enableViewMode = (AppCompatImageView) view.findViewById(R.id.view_mode);
        composeContainer = (ConstraintLayout) view.findViewById(R.id.container_compose);
    }

    public void activate() {
        layout.removeAllViews();
        layout.addView(view);

        listAdapter = new ListAdapter(itemsList, layout.getContext(), this::onItemRemoved, this::onClick);
        list.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        list.setAdapter(listAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(list);


        saveListItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compose) {
                    if (update) {
                        itemsList.get(currentPosition).setText(listItemInput.getText().toString());
                        listAdapter.notifyDataSetChanged();
                        update = false;
                        listItemInput.setText("");
                    } else {
                        ListItem item = new ListItem(listItemInput.getText().toString());
                        itemsList.add(item);
                        listAdapter.notifyDataSetChanged();
                        listItemInput.setText("");
                        list.scrollToPosition(itemsList.size() - 1);
                        listItemInput.setText("");
                    }
                } else {
                    expand();
                }
            }
        });

        enableViewMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemInput.setText("");
                collapse();
            }
        });

        listItemInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (update) {
                        itemsList.get(currentPosition).setText(listItemInput.getText().toString());
                        listAdapter.notifyDataSetChanged();
                    } else {
                        ListItem item = new ListItem(listItemInput.getText().toString());
                        itemsList.add(item);
                        listAdapter.notifyDataSetChanged();
                        listItemInput.setText("");
                        list.scrollToPosition(itemsList.size() - 1);
                    }
                    listItemInput.setText("");
                }
                return true;
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            listAdapter.removeItem(position);
        }
    };

    public void displayList() {
        listAdapter = new ListAdapter(itemsList, layout.getContext(), this::onItemRemoved, this::onClick);
        list.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        list.setAdapter(listAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(list);
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public List<ListItem> getItemsList() {
        return this.itemsList;
    }

    public void setItemsList(List<ListItem> list) {
        this.itemsList = list;
    }

    public String getDate() {
        return dateTimeTv.getText().toString();
    }

    public void setDate(String date) {
        dateTimeTv.setText(date);
    }

    public void deActivate() {
        layout.removeView(view);
    }

    @Override
    public void onClick(int position, String item, boolean isChecked) {
        if (compose) {
            this.update = true;
            this.currentPosition = position;
            this.listItemInput.setText(item);
        }
        itemsList.get(position).setChecked(isChecked);
    }

    @Override
    public void onItemRemoved(int position, String item) {

    }

    private void expand() {

        DisplayMetrics displayMetrics = layout.getContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;

        ValueAnimator animator = ValueAnimator.ofInt(composeContainer.getMeasuredWidth(), (width-dpToPixels(48)));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) composeContainer.getLayoutParams();
                params.width = val;
                composeContainer.setLayoutParams(params);
            }
        });
        animator.setDuration(500);
        animator.start();

/*
        actionCreateTask.animate()
                .translationX((float) ((width / 2) - (dpToPixels(86))))
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(500);
 */


        enableViewMode.setVisibility(View.VISIBLE);
        enableViewMode.animate().alpha(1);
        listItemInput.setVisibility(View.VISIBLE);

        saveListItemBtn.setImageResource(R.drawable.icon_done);

        listItemInput.requestFocus();
        compose = true;
    }

    private void collapse() {
        enableViewMode.animate().alpha(0);
        enableViewMode.setVisibility(View.GONE);
        listItemInput.setVisibility(View.GONE);


        ValueAnimator animator = ValueAnimator.ofInt(composeContainer.getMeasuredWidth(), composeContainer.getMeasuredHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) composeContainer.getLayoutParams();
                params.width = val;
                composeContainer.setLayoutParams(params);
            }
        });
        animator.setDuration(500);
        animator.start();

/*
        actionCreateTask.animate()
                .translationX((float) ((width / 2) - (dpToPixels(86))))
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(500);
 */
        saveListItemBtn.setImageResource(R.drawable.icon_edit);
        compose = false;
    }

    private int dpToPixels(int dp) {
        float density = layout.getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}

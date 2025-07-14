package com.example.softnotesbeta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softnotesbeta.Adapters.NoteAdapter;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Models.DeleteNotesModel;
import com.example.softnotesbeta.Models.NotifySelectionModel;
import com.example.softnotesbeta.Models.SearchInvokedModel;
import com.example.softnotesbeta.Models.SearchNotesModel;
import com.example.softnotesbeta.Models.SelectBackModel;
import com.example.softnotesbeta.ViewModels.NotesVIewModel;
import com.example.softnotesbeta.ViewModels.SearchVIewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment implements NoteItemClickListener, OnSelectionStart, SearchNotesModel.TextChangeListener, DeleteNotesModel.OnDeleteRequest, MainActivity.ParentChangedListener, SearchInvokedModel.OnSearchRequest, SelectBackModel.NoteBackRequest {

    private static final int spanCount = 2;
    private static final int spacing = 38;

    private static final boolean includeEdge = true;
    public static final int REQUEST_CODE_UPDATE_NOTE = 1;
    private RecyclerView recyclerView;
    private CollapsingToolbarLayout toolbarLayout;
    private NotesVIewModel viewModel;
    private NoteAdapter adapter;
    StaggeredGridLayoutManager layoutManager;
    private List<Preview> selectedNotes;

    public NotesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setParentListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchNotesModel.getInstance().setListener(this::onFilterText);
        DeleteNotesModel.getInstance().setListener(this::onDeleteRequest);
        SearchInvokedModel.getInstance().setListener(this::onSearchRequest);
        SelectBackModel.getInstance().setNoteListener(this::onNoteBackRequest);
        recyclerView = (RecyclerView) view.findViewById(R.id.notes_list);
        toolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);

        viewModel = new ViewModelProvider(this).get(NotesVIewModel.class);
        adapter = new NoteAdapter(getContext(), this, this);

        selectedNotes = new ArrayList<>();

        viewModel.initAllPreviews("");
        viewModel.allPreviewList.observe(getViewLifecycleOwner(), previews -> {
            adapter.submitList(previews);
        });

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        viewModel.filterText.setValue("");
    }

    @Override
    public void onPreviewCLick(int position, Preview preview, View view1, View view2, boolean isSelected) {
        if (isSelected) {
            if (preview.isSelected()) {
                view2.setVisibility(View.GONE);

                view1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.note_preview_background));

                preview.setSelected(false);
                if (selectedNotes.isEmpty()) {
                    adapter.setSelected(false);
                    toolbarLayout.setTitle("Tasks");
                    NotifySelectionModel.getInstance().selectChanged(false);
                }

                selectedNotes.remove(preview);
                toolbarLayout.setTitle(selectedNotes.size() + " selected");

            } else {
                view2.setVisibility(View.VISIBLE);

                view1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.selected_preview_background));

                preview.setSelected(true);

                selectedNotes.add(preview);
                toolbarLayout.setTitle(selectedNotes.size() + " selected");
            }

        } else {
            if (preview.getType().equals("table")) {
                Intent intent = new Intent(getContext(), TableNoteWorkspace.class);
                intent.putExtra("transitionName1", ViewCompat.getTransitionName(view1));
                intent.putExtra("title", preview.getTitle());
                intent.putExtra("noteId", preview.getNoteId());
                intent.putExtra("previewId", preview.getId());
                intent.putExtra("mode", "updateAndView");
                intent.putExtra("noteType", preview.getType());

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view1, ViewCompat.getTransitionName(view1));
                startActivity(intent, activityOptions.toBundle());
            } else if (preview.getType().equals("timeline")) {
                Intent intent = new Intent(getContext(), TimelineWorkspace.class);
                intent.putExtra("transitionName1", ViewCompat.getTransitionName(view1));
                intent.putExtra("title", preview.getTitle());
                intent.putExtra("noteId", preview.getNoteId());
                intent.putExtra("previewId", preview.getId());
                intent.putExtra("mode", "updateAndView");
                intent.putExtra("noteType", preview.getType());

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view1, ViewCompat.getTransitionName(view1));
                startActivity(intent, activityOptions.toBundle());
            }else {
                Intent intent = new Intent(getContext(), Workspace.class);
                intent.putExtra("transitionName1", ViewCompat.getTransitionName(view1));
                intent.putExtra("title", preview.getTitle());
                intent.putExtra("noteId", preview.getNoteId());
                intent.putExtra("previewId", preview.getId());
                intent.putExtra("mode", "updateAndView");
                intent.putExtra("noteType", preview.getType());

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view1, ViewCompat.getTransitionName(view1));
                startActivity(intent, activityOptions.toBundle());
            }
        }
    }

    @Override
    public void performActions(boolean isSelected) {
        if (isSelected) {
            getActivity().findViewById(R.id.delete_action).setVisibility(View.VISIBLE);
            NotifySelectionModel.getInstance().selectChanged(true);
            //vibrate();
            toolbarLayout.setTitle(String.valueOf(selectedNotes.size()) + " selected");
        } else {
            getActivity().findViewById(R.id.delete_action).setVisibility(View.GONE);
            NotifySelectionModel.getInstance().selectChanged(false);
            toolbarLayout.setTitle("Notes");
        }
    }

    @Override
    public void onFilterText(String input) {
        viewModel.filterText.setValue(input);
    }

    @Override
    public void onDeleteRequest() {
        DeleteNotesModel.getInstance().setItemsToDelete(selectedNotes);
        toolbarLayout.setTitle("Notes");
        adapter.setSelected(false);
        selectedNotes.clear();
        getActivity().findViewById(R.id.delete_action).setVisibility(View.GONE);
    }

    @Override
    public void onParentChanged(String parent) {

    }

    @Override
    public void onSearchRequest() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), recyclerView, ViewCompat.getTransitionName(recyclerView));
        startActivity(intent, activityOptions.toBundle());
    }

    @Override
    public void onNoteBackRequest() {
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();

        toolbarLayout.setTitle("Notes");
        adapter.setSelected(false);
        selectedNotes.clear();

        getActivity().findViewById(R.id.delete_action).setVisibility(View.GONE);
    }
}
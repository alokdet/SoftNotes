package com.example.softnotesbeta;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softnotesbeta.Adapters.CreateStepsArch;
import com.example.softnotesbeta.ContentControllers.ContentControllerHandler;

public class StepContentWorkspace extends Fragment {

    private AppCompatEditText contentInputView;
    private ConstraintLayout layout;
    private CreateStepsArch arch;
    private ContentControllerHandler handler;

    public StepContentWorkspace() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition transition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.shared_transition);
        setSharedElementEnterTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_content_workspace, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arch = CreateStepsArch.getInstance();
        handler = ContentControllerHandler.getInstance();

        contentInputView = (AppCompatEditText) view.findViewById(R.id.content_input_view);
        layout = (ConstraintLayout) view.findViewById(R.id.content_actions_container);
        ViewCompat.setTransitionName(contentInputView, "edittext");

        handler.initialise(layout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        arch.setContentText(contentInputView.getText().toString());
        handler.saveScript();
    }
}
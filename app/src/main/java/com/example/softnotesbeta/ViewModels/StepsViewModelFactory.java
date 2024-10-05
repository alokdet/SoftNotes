package com.example.softnotesbeta.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class StepsViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private long id;

    public StepsViewModelFactory(Application application, long id) {
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StepsViewModel(application, id);
    }
}

package com.example.softnotesbeta.ViewModels;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class TableViewModel extends AndroidViewModel {

    private LiveData<List<String>> indexLiveData;
    private LiveData<List<String>> keyLiveData;
    private LiveData<List<String>> valueLiveData;

    public TableViewModel(@NonNull Application application) {
        super(application);

        indexLiveData = new MutableLiveData<>();
        keyLiveData = new MutableLiveData<>();
        valueLiveData = new MutableLiveData<>();
    }

    public void insertFields(String index, String key, String value) {
        indexLiveData.getValue().add(index);
        keyLiveData.getValue().add(key);
        valueLiveData.getValue().add(value);
    }
}

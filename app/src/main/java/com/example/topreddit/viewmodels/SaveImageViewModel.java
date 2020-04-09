package com.example.topreddit.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.topreddit.repository.Repository;

public class SaveImageViewModel extends AndroidViewModel {

    private Context context;
    private Repository repository;

    public SaveImageViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        this.context = application;
    }

    public void onSaveImageClick(String url) {
        repository.onSaveImageClick(url, context);
    }
}

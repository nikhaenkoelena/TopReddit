package com.example.topreddit.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.topreddit.domain.pojo.PostData;
import com.example.topreddit.repository.Repository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private Repository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void loadData() {
        repository.loadData();
    }

    public LiveData<List<PostData>> getAllPosts() {
        return repository.getAllPosts();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.dispose();
    }
}

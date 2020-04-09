package com.example.topreddit.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.topreddit.repository.Repository;

public class SaveImageViewModel extends AndroidViewModel {

    private Repository repository;
    private Context context;
    private MutableLiveData<Integer> notifications;

    private static final Integer RC_IMAGE_DOWNLOADED = 1;
    private static final Integer RC_CLEAR_LD = 0;

    public SaveImageViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        this.context = application;
        notifications = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getNotifications() {
        return notifications;
    }

    public void onSaveImageClick(String url, Activity activity) {
        repository.onSaveImageClick(url, activity, this);
    }

    public void passSavingResult() {
        notifications.postValue(RC_IMAGE_DOWNLOADED);
        notifications.postValue(RC_CLEAR_LD);
    }
}

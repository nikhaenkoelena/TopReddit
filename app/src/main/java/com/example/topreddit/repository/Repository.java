package com.example.topreddit.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.topreddit.datasources.api.ApiFactory;
import com.example.topreddit.datasources.api.ApiService;
import com.example.topreddit.datasources.database.PostDatabase;
import com.example.topreddit.domain.pojo.Child;
import com.example.topreddit.domain.pojo.Data;
import com.example.topreddit.domain.pojo.PostData;
import com.example.topreddit.domain.pojo.PostResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Repository {

    Context context;
    CompositeDisposable compositeDisposable;
    private PostDatabase database;
    private LiveData<List<PostData>> posts;

    public Repository(Context context) {
        this.context = context;
        compositeDisposable = new CompositeDisposable();
        database = PostDatabase.getInstance(context);
        posts = database.postDao().getAllPosts();
    }

    public void loadData() {
        final String afterDef = null;
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        Disposable disposable = apiService.getPosts(afterDef)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PostResult>() {
                    @Override
                    public void accept(PostResult postResult) throws Exception {
                        List<PostData> postDataFromJSON = new ArrayList<>();
                        Data data = postResult.getData();
                        List<Child> childList = data.getChildren();
                        for (Child child : childList) {
                            PostData postData = child.getPostData();
                            postData.setCreatedUtc(postData.getCreatedUtc() * 1000);
                            postData.setAfterDef(data.getAfter());
                            postDataFromJSON.add(postData);
                        }
                        deleteAllPosts();
                        insertAllPosts(postDataFromJSON);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void insertAllPosts(final List<PostData> posts) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (posts != null && posts.size() > 0) {
                    database.postDao().insertPosts(posts);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    public LiveData<List<PostData>> getAllPosts() {
        return posts;
    }

    public void deleteAllPosts() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                database.postDao().deleteAllPosts();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void dispose() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}

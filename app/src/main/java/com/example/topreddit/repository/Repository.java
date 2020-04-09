package com.example.topreddit.repository;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.topreddit.R;
import com.example.topreddit.datasources.api.ApiFactory;
import com.example.topreddit.datasources.api.ApiService;
import com.example.topreddit.datasources.database.PostDatabase;
import com.example.topreddit.domain.pojo.Child;
import com.example.topreddit.domain.pojo.Data;
import com.example.topreddit.domain.pojo.PostData;
import com.example.topreddit.domain.pojo.PostResult;
import com.example.topreddit.ui.MainActivity;
import com.example.topreddit.viewmodels.SaveImageViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Repository {

    private Context context;
    private CompositeDisposable compositeDisposable;
    private PostDatabase database;
    private LiveData<List<PostData>> posts;
    public MutableLiveData<Throwable> errors;

    private static final String NULL_CHECK = "null";
    private static final String TITLE = "VIEW_PICTURE";

    public Repository(Context context) {
        this.context = context;
        compositeDisposable = new CompositeDisposable();
        database = PostDatabase.getInstance(context);
        posts = database.postDao().getAllPosts();
        errors = new MutableLiveData<>();
    }

    public void loadData(String after) {
        final String afterDef = after;
        Log.i("CheckAfter", after.toString());
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
                        if (afterDef.equals(NULL_CHECK)) {
                            deleteAllPosts();
                        }
                        List<Child> childList = data.getChildren();
                        for (Child child : childList) {
                            PostData postData = child.getPostData();
                            postData.setCreatedUtc(postData.getCreatedUtc() * 1000);
                            postData.setAfterDef(data.getAfter());
                            postDataFromJSON.add(postData);
                        }
                        updateAfter(data.getAfter());
                        insertAllPosts(postDataFromJSON);
                        Log.i("CheckInsert", "one");


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errors.setValue(throwable);
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
                database.postDao().deleteAllPostTransaction();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void onSaveImageClick(String url, final Activity activity, final SaveImageViewModel viewModel) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Picasso.get().load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    savePicture(bitmap, viewModel);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }
    }


    private void savePicture(Bitmap bitmap, final SaveImageViewModel viewModel) {
        FileOutputStream outStream = null;
        File path = Environment.getExternalStorageDirectory();
        path.mkdirs();
        File imageFile = new File(path.getAbsolutePath(), System.currentTimeMillis() + ".png");
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream = new FileOutputStream(imageFile);
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            assert outStream != null;
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                viewModel.passSavingResult();
            }
        });
    }

    private void updateAfter(final String afterDef) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (afterDef != null && !afterDef.isEmpty()) {
                    database.postDao().updateAfter(afterDef);
                }
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

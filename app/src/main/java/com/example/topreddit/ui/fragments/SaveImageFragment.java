package com.example.topreddit.ui.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.os.LocaleList;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topreddit.R;
import com.example.topreddit.viewmodels.MainViewModel;
import com.example.topreddit.viewmodels.SaveImageViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SaveImageFragment extends Fragment {

    @BindView(R.id.imageViewBigImage)
    ImageView imageView;
    @BindView(R.id.textViewSaveToGallery)
    TextView textViewSaveToGallery;

    private Unbinder unbinder;
    private SaveImageViewModel viewModel;

    private static final String BUNDLE_KEY = "Url";
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_image, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SaveImageViewModel.class);
        if (getArguments() != null) {
            url = getArguments().getString(BUNDLE_KEY);
            Picasso.get().load(url).into(imageView);
            setOnSaveImageClickListener();
        } else {
            Picasso.get().load(R.drawable.redditicon).into(imageView);
            textViewSaveToGallery.setVisibility(View.GONE);
        }
    }

    private void setOnSaveImageClickListener() {
        textViewSaveToGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onSaveImageClick(url);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

package com.example.topreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topreddit.R;
import com.example.topreddit.viewmodels.SaveImageViewModel;
import com.squareup.picasso.Picasso;

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
    private static final Integer RC_IMAGE_DOWNLOADED = 1;

    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_image, container, false);
        unbinder = ButterKnife.bind(this, view);
        Objects.requireNonNull(getActivity()).setTitle(R.string.app_name);
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
        getNotifications();
    }

    private void getNotifications() {
        viewModel.getNotifications().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == RC_IMAGE_DOWNLOADED) {
                    Log.i("CheckInt", Integer.toString(integer));
                    Toast.makeText(getContext(), R.string.save_to_gallery, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOnSaveImageClickListener() {
        textViewSaveToGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onSaveImageClick(url, getActivity());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                viewModel.onSaveImageClick(url, getActivity());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

package com.example.topreddit.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.topreddit.R;
import com.example.topreddit.domain.pojo.PostData;
import com.example.topreddit.ui.adapters.PostAdapter;
import com.example.topreddit.viewmodels.MainViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends Fragment {

    @BindView(R.id.recyclerViewPosts)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Unbinder unbinder;
    private PostAdapter adapter;
    private NavController navController;
    private MainViewModel viewModel;
    private LiveData<List<PostData>> posts;

    private String after = "null";
    private static final int ELEMENT_INDEX_LIVEDATA = 1;
    private static final String JPEG_FORMAT = ".jpg";
    private static final String PNG_FORMAT = ".png";
    private static final String BUNDLE_KEY = "Url";

    private static boolean isLoading;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        adapter = new PostAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        isLoading = false;
        setOnImageClickListener();
        setOnPostClickListener();
        setOnReachEndListener();
        getPosts();
        viewModel.loadData(after);
    }

    private void getPosts() {
        posts = viewModel.getAllPosts();
        posts.observe(this, new Observer<List<PostData>>() {
            @Override
            public void onChanged(List<PostData> postData) {
                if (postData != null) {
                    adapter.setPostDatas(postData);
                    progressBar.setVisibility(View.GONE);
                    isLoading = false;
                    if (postData.size() > 0) {
                        after = postData.get(ELEMENT_INDEX_LIVEDATA).getAfterDef();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.error_showing_data, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setOnImageClickListener() {
        adapter.setOnImageClickListener(new PostAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(int position) {
                PostData postData = adapter.getPostDatas().get(position);
                String url = postData.getUrl();
                if (url.contains(JPEG_FORMAT) || url.contains(PNG_FORMAT)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(BUNDLE_KEY, postData.getUrl());
                    navController.navigate(R.id.action_mainFragment_to_saveImageFragment, bundle);
                } else {
                    Toast.makeText(getContext(), R.string.no_image_notification, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOnPostClickListener() {
        adapter.setOnPostClickListener(new PostAdapter.OnPostClickListener() {
            @Override
            public void onPostClick(int position) {
                PostData postData = adapter.getPostDatas().get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(postData.getUrl()));
                startActivity(intent);

            }
        });
    }

    private void setOnReachEndListener() {
        adapter.setOnReachEndListener(new PostAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                viewModel.loadData(after);
                progressBar.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

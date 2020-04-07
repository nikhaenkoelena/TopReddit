package com.example.topreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.topreddit.R;
import com.example.topreddit.domain.pojo.PostData;
import com.example.topreddit.ui.adapters.PostAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        adapter = new PostAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        List<PostData> test = new ArrayList<>();
        test.add(new PostData("gjndfjgv", "gjfdkjg", null, 5, 123, "jgkf", "jghg", 56, "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.astromeridian.ru%2Fsonnik%2F4132.html&psig=AOvVaw3FRmHdkgOsjRcXcvXMvG5B&ust=1586353942200000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCOianIy71ugCFQAAAAAdAAAAABAD", 5555L, "gjkdgnf"));
        test.add(new PostData("gjndfjgv", "gjfdkjg", null, 5, 123, "jgkf", "jghg", 56, "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.astromeridian.ru%2Fsonnik%2F4132.html&psig=AOvVaw3FRmHdkgOsjRcXcvXMvG5B&ust=1586353942200000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCOianIy71ugCFQAAAAAdAAAAABAD", 5555L, "gjkdgnf"));
        adapter.setPostDatas(test);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

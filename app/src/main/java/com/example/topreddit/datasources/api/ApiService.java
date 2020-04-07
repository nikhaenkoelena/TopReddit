package com.example.topreddit.datasources.api;

import com.example.topreddit.domain.pojo.PostResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/r/all/top.json")
    Observable<PostResult> getPosts(@Query("after") String after);
}

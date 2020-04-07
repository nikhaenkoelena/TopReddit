package com.example.topreddit.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private PostData postData;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public PostData getPostData() {
        return postData;
    }

    public void setPostData(PostData postData) {
        this.postData = postData;
    }
}

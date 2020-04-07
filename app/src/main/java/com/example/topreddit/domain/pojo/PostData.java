package com.example.topreddit.domain.pojo;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostData {

    @SerializedName("uniqId")
    @Expose
    private int uniqId;
    @SerializedName("selftext")
    @Expose
    private String selftext;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnailImage;
    @SerializedName("score")
    @Expose
    private int score;
    @SerializedName("created")
    @Expose
    private int created;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("num_comments")
    @Expose
    private int numComments;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("created_utc")
    @Expose
    private long createdUtc;
    @SerializedName("after")
    @Expose
    private String afterDef;

    @Ignore
    public PostData(String selftext, String title, String thumbnailImage, int score, int created, String id, String author, int numComments, String url, long createdUtc, String afterDef) {
        this.selftext = selftext;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
        this.score = score;
        this.created = created;
        this.id = id;
        this.author = author;
        this.numComments = numComments;
        this.url = url;
        this.createdUtc = createdUtc;
        this.afterDef = afterDef;
    }


    public PostData(int uniqId, String selftext, String title, String thumbnailImage, int score, int created, String id, String author, int numComments, String url, long createdUtc, String afterDef) {
        this.uniqId = uniqId;
        this.selftext = selftext;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
        this.score = score;
        this.created = created;
        this.id = id;
        this.author = author;
        this.numComments = numComments;
        this.url = url;
        this.createdUtc = createdUtc;
        this.afterDef = afterDef;
    }

    public int getUniqId() {
        return uniqId;
    }

    public void setUniqId(int uniqId) {
        this.uniqId = uniqId;
    }

    public String getSelftext() {
        return selftext;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreatedUtc() {
        return createdUtc;
    }

    public void setCreatedUtc(long createdUtc) {
        this.createdUtc = createdUtc;
    }

    public String getAfterDef() {
        return afterDef;
    }

    public void setAfterDef(String afterDef) {
        this.afterDef = afterDef;
    }
}

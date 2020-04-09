package com.example.topreddit.datasources.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.topreddit.domain.pojo.PostData;

import java.util.List;

@Dao
public abstract class PostDao {

    @Transaction
    public void deleteAllPostTransaction() {
        deleteAllPosts();
    }

    @Query("SELECT * FROM posts_table")
    public abstract LiveData<List<PostData>> getAllPosts();

    @Query("DELETE FROM posts_table")
    public abstract void deleteAllPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPosts(List<PostData> posts);

    @Query("UPDATE posts_table SET afterDef =:afterDefin;")
    public abstract void updateAfter(String afterDefin);

}

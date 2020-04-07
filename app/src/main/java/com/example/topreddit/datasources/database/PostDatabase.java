package com.example.topreddit.datasources.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.topreddit.domain.pojo.PostData;

@Database(entities = {PostData.class}, version = 2, exportSchema = false)
public abstract class PostDatabase extends RoomDatabase {

    private static PostDatabase database;
    private static final String DB_NAME = "posts.db";
    private static final Object LOCK = new Object();

    public static PostDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, PostDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            }
            return database;
        }
    }

    public abstract PostDao postDao();
}

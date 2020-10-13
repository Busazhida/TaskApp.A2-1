package com.example.taskapp;

import android.app.Application;

import androidx.room.Room;

import com.example.taskapp.room.AppDatabase;

public class App extends Application {

    private AppDatabase appDatabase;
    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "database").allowMainThreadQueries().build();
        new Prefs(this);
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static App getInstance() {
        return instance;
    }
}

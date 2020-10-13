package com.example.taskapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs  {

    private SharedPreferences preferences;
    public static Prefs instance;


    public Prefs(Context context) {
        instance = this;
        preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    public void saveShowState(){
        preferences.edit().putBoolean("ShowState", true).apply();
    }


    public boolean getShowState() {
        return preferences.getBoolean("ShowState", false);
    }
}

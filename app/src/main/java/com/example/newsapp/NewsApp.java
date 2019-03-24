package com.example.newsapp;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class NewsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}

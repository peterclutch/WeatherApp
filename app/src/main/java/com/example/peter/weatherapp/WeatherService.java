package com.example.peter.weatherapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Peter on 02-May-17.
 */

public class WeatherService extends Service {
    private static final String LOG = "WeatherService";
    public static final String EXTRA_TASK_RESULTS = "task_result";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    //this service is not for binding and returns null
    public IBinder onBind(Intent intent) {
        return null;
    }
}

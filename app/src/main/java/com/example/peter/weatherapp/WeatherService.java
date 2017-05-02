package com.example.peter.weatherapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Peter on 02-May-17.
 */

public class WeatherService extends Service {
    public static final String EXTRA_TASK_RESULTS = "task_result";
    public static final String EXTRA_TASK_TIME_MS = "task_time";

    private static final String LOG = "WeatherService";

    private boolean running = false;
    private long interval;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!running && intent != null) {
            running = true;
            interval = intent.getLongExtra(EXTRA_TASK_TIME_MS, 30000);
            Log.d(LOG, "onStartCommand: Service is running with wait: " + interval + "ms");

        } else {
            Log.d(LOG, "onStartCommand: Service is not running");
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    //this service is not for binding - returns null
    public IBinder onBind(Intent intent) {
        return null;
    }
}

package com.example.peter.weatherapp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Peter on 02-May-17.
 */

public class WeatherService extends Service {
    public static final String EXTRA_TASK_RESULTS = "task_result";
    public static final String EXTRA_TASK_TIME_MS = "task_time";
    public static final String BROADCAST_WEATHER_SERVICE_RESULT = "service_result";

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

            backgroundTask(interval);

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

    private void backgroundTask(final long interval){


        AsyncTask<Object, Object, String> task = new AsyncTask<Object, Object, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Object[] params) {
                try {
                    Log.d(LOG, "Task started");
                    Thread.sleep(interval);
                    Log.d(LOG, "Task completed");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG, "Failed to start task");
                }
                return "";
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                broadcastResult(result);

                //if Service is still running, keep doing this recursively
                if(running){
                    backgroundTask(interval);
                }
            }
        };

        task.execute();

    }

    private void broadcastResult(String result){
        Intent broadcastIntent = new Intent();
        //broadcastIntent.setAction(BROADCAST_WEATHER_SERVICE_RESULT);
        broadcastIntent.putExtra(EXTRA_TASK_RESULTS, result);
        Log.d(LOG, "Broadcasting:" + result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        running = false;
        Log.d(LOG,"Background service destroyed");
        super.onDestroy();
    }
}

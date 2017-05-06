package com.example.peter.weatherapp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.peter.weatherapp.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import android.os.Handler;

/**
 * Created by Peter on 02-May-17.
 */

public class WeatherService extends Service {
    public static final String EXTRA_TASK_RESULTS = "task_result";
    public static final String EXTRA_TASK_TIME_MS = "task_time";
    public static final String BROADCAST_WEATHER_SERVICE_RESULT = "service_result";

    String weatherURL = "http://api.openweathermap.org/data/2.5/weather?id=2624652&APPID=e69dc40b8fa2ada4db52635eb00bd736";
    RequestQueue requestQueue;

    private String description;
    private double temperature;


    private DatabaseHelper dbHelper;

    private static final String LOG = "WeatherService";

    private boolean running = false;
    private long interval;

    @Override
    public void onCreate() {
        super.onCreate();
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }
        Log.d(LOG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!running && intent != null) {
            running = true;
            interval = intent.getLongExtra(EXTRA_TASK_TIME_MS, 30000);
            Log.d(LOG, "onStartCommand: Service is running with wait: " + interval + "ms");

            initDatabase();
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
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG, "Failed to start task");
                }
                return "";
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                newSendRequest();
                Log.d(LOG, "temperature: " + temperature);
                Log.d(LOG, "Description: " + description);
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
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        running = false;
        Log.d(LOG,"Background service destroyed");
        super.onDestroy();
    }

    private boolean initDatabase() {
        if (dbHelper == null) {
            Log.d(LOG, "Database initializing");
            dbHelper = new DatabaseHelper(getApplicationContext());
        } else {
            return false;
        }
        return true;
    }

    public void newSendRequest(){
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, weatherURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray weatherData = response.getJSONArray("weather");
                            JSONObject main = response.getJSONObject("main");
                            JSONObject descriptionJSON = weatherData.getJSONObject(0);
                            description = descriptionJSON.getString("description");
                            double temperatureDouble =  main.getDouble("temp");

                            temperature = temperatureDouble - 273.15;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        requestQueue.add(jsObjRequest);
    }
}

package com.example.peter.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.peter.weatherapp.model.Weather;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";

    private long task_time = 4*1000; //4s

    private Intent weatherServiceIntent;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG, "OnCreate");

        initDatabase();

        //Creates weatherService and starts it
        weatherServiceIntent = new Intent(this, WeatherService.class);
        startWeatherService(task_time);
    }

    private void startWeatherService(long taskTime) {
        weatherServiceIntent.putExtra(WeatherService.EXTRA_TASK_TIME_MS, taskTime);
        startService(weatherServiceIntent);
    }

    private void stopWeatherService() {
        stopService(weatherServiceIntent);
    }

    private void handleWeatherResult() {
        Log.d(LOG, "Weather Service received");
    }

    //Data sent from WeatherService
    private BroadcastReceiver onWeatherServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG, "Received WeatherService data");
            String result = intent.getStringExtra(WeatherService.EXTRA_TASK_RESULTS);
            if(result==null) {
                //NULL HANDLER
            }
            handleWeatherResult();
        }
    };

    //Initializes database
    private boolean initDatabase() {
        if (dbHelper == null) {
            Log.d(LOG, "Database initializing");
            dbHelper = new DatabaseHelper(getApplicationContext());
        } else { return false; }

        Weather w = new Weather(23.0, "HEJ");

        w.setId(dbHelper.insertRow(w));

        Log.d(LOG, dbHelper.getAllWeather().toString());

        return true;
    }
}

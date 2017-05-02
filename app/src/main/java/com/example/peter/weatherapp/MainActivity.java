package com.example.peter.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG, "OnCreate");
        startWeatherService();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    private void startWeatherService() {
        Intent intent = new Intent(this, WeatherService.class);
        startService(intent);
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
            //HANDLE RESULTS
        }
    };
}

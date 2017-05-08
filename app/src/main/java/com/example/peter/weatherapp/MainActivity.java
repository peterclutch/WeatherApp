package com.example.peter.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peter.weatherapp.adaptors.WeatherAdaptor;
import com.example.peter.weatherapp.model.Weather;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";
    public static final String BROADCAST_WEATHER_SERVICE_RESULT = "service_result";

    private long task_time = 400*1000; //4s

    private Intent weatherServiceIntent;
    //private DatabaseHelper dbHelper;
    private ListView weatherListView;
    private List<Weather> weatherList;
    private WeatherAdaptor adaptor;
    private TextView temp, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG, "OnCreate started");

        //Define views from layout
        weatherListView = (ListView) findViewById(R.id.listView);
        temp = (TextView) findViewById(R.id.temp);
        desc = (TextView) findViewById(R.id.desc);

        //Creates weatherService and starts it - For requesting current weather and storing to DB
        weatherServiceIntent = new Intent(this, WeatherService.class);
        startWeatherService(task_time);

        //Creates an adaptor for the ListView to put 24 hour weather updates
        adaptor = new WeatherAdaptor(this, weatherList);
        weatherListView.setAdapter(adaptor);

        //Broadcast Receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(onWeatherServiceResult, new IntentFilter(BROADCAST_WEATHER_SERVICE_RESULT));
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
            Bundle result = intent.getBundleExtra(WeatherService.EXTRA_TASK_RESULTS);

            if(result!=null) {
                weatherList = (List<Weather>) result.getSerializable("weather_array");
                Log.d(LOG, weatherList.size() + "");

                Collections.reverse(weatherList);

                adaptor = new WeatherAdaptor(context, weatherList);
                weatherListView.setAdapter(adaptor);
                setCurrentWeather();
            }
            handleWeatherResult();
        }
    };

    //Sets the text fields of the current weather
    private void setCurrentWeather() {
        Weather current = weatherList.get(0);

        temp.setText(current.getTemperature() + "°C");
        desc.setText(current.getTime());
    }
}

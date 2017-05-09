package com.example.peter.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peter.weatherapp.adaptors.WeatherAdaptor;
import com.example.peter.weatherapp.model.Weather;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";
    public static final String BROADCAST_WEATHER_SERVICE_RESULT = "service_result";

    //Weather update time
    private long task_time = 10*60*1000; //10 minutes - We have chosen to do 10 minutes instead of 30 for ease of testing

    //Background service
    private Intent weatherServiceIntent;

    //List of 24hr weather sent from service
    private List<Weather> weatherList;

    //Layout variables
    private TextView temp, desc;
    private String temperature = "";
    private String description = "";
    private Button refresh;
    private ListView weatherListView;
    private WeatherAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG, "OnCreate started");

        //Define views from layout
        weatherListView = (ListView) findViewById(R.id.listView);
        temp = (TextView) findViewById(R.id.temp);
        desc = (TextView) findViewById(R.id.desc);
        refresh = (Button) findViewById(R.id.refresh);

        //Creates weatherService and starts it - For requesting current weather and storing to DB
        weatherServiceIntent = new Intent(this, WeatherService.class);
        startWeatherService(task_time);

        //Creates an adaptor for the ListView to put 24 hour weather updates
        adaptor = new WeatherAdaptor(this, weatherList);
        weatherListView.setAdapter(adaptor);

        //Broadcast Receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(onWeatherServiceResult, new IntentFilter(BROADCAST_WEATHER_SERVICE_RESULT));

        //On click listener for refresh button
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopWeatherService();
                weatherServiceIntent = new Intent(MainActivity.this, WeatherService.class);
                startWeatherService(task_time);
                Log.d(LOG, "Refresh pressed");
            }
        });
    }

    //Method that starts background intent
    private void startWeatherService(long taskTime) {
        weatherServiceIntent.putExtra(WeatherService.EXTRA_TASK_TIME_MS, taskTime);
        startService(weatherServiceIntent);
    }

    //Method that stops background intent
    private void stopWeatherService() {
        stopService(weatherServiceIntent);
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
        }
    };

    //Sets the text fields of the current weather
    private void setCurrentWeather() {
        Weather current = weatherList.get(0);

        temperature = current.getTemperature() + "°C";
        description = current.getIcon();

        temp.setText(temperature);
        desc.setText(description);
    }

    //Saves data to onCreate if the process is killed and restarted.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (weatherList != null) {
            savedInstanceState.putSerializable("weather_array", (Serializable) weatherList);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    //Restores data from savedInstanceState
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Set data
        weatherList = (List<Weather>) savedInstanceState.getSerializable("weather_array");

        //Put data into text fields
        adaptor = new WeatherAdaptor(this, weatherList);
        weatherListView.setAdapter(adaptor);
        setCurrentWeather();


    }
}

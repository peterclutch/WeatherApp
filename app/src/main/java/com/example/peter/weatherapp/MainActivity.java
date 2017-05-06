package com.example.peter.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peter.weatherapp.adaptors.WeatherAdaptor;
import com.example.peter.weatherapp.model.Weather;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";

    private long task_time = 4*1000; //4s

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

        //Runs database and gathers information for UI
        //initDatabase();

        //Creates an adaptor for the ListView to put 24 hour weather updates
        adaptor = new WeatherAdaptor(this, weatherList);
        weatherListView.setAdapter(adaptor);
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
    /*private boolean initDatabase() {
        if (dbHelper == null) {
            Log.d(LOG, "Database initializing");
            dbHelper = new DatabaseHelper(getApplicationContext());
        } else { return false; }

        //Get the weather from last 24 hours
        weatherList = dbHelper.getDailyWeather();

        //Weather w = new Weather(23.0, "HEJ");
        //w.setId(dbHelper.insertRow(w));
        //Weather vever = dbHelper.getWeather(5);
        //Log.d(LOG, wehe.size() + "");

        return true;
    }*/
}

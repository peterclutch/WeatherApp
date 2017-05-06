package com.example.peter.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.peter.weatherapp.model.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Peter on 06-May-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    /* This class is inspired by Kaspers DataStorageDemo databaseHelper.java file */

    private static final String LOG = "DatabaseHelper";

    //Database Version
    private static final int DATABASE_VERSION = 4;

    //Database Name
    private static final String DATABASE_NAME = "database";

    //Table Names
    private static final String TABLE_WEATHER = "weather";

    //Column names
    private static final String KEY_ID = "id";
    private static final String KEY_TEMP = "temperature";
    private static final String KEY_ICON = "icon";
    private static final String KEY_TIME = "time";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_WEATHER
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TEMP + " INTEGER,"
            + KEY_ICON + " TEXT," + KEY_TIME + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create required tables
        db.execSQL(CREATE_TABLE);

        Log.d(LOG, "Created DB");
    }

    //When database is changed - Would be ideal to add a way to handle the data and not just drop everything
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //Drop older table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);

        //Create a new table
        onCreate(db);

        Log.d(LOG, "Upgraded DB");
    }

    public long insertRow(Weather weather) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Create DateTime string formater
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        //Create all values from model
        ContentValues values = new ContentValues();
        values.put(KEY_TEMP, weather.getTemperature());
        values.put(KEY_ICON, weather.getIcon());
        values.put(KEY_TIME, dateFormat.format(new Date()));

        //Insert row
        long weather_id = db.insert(TABLE_WEATHER, null, values);

        Log.d(LOG, "inserted row - id: " + weather_id);
        return weather_id;
    }

    public Weather getWeather(long weather_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        //Create cursor to run query
        String selectQuery = "SELECT * FROM " + TABLE_WEATHER + " WHERE "
                + KEY_ID + " = " + weather_id;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        //Extract data from cursor
        Weather weather = new Weather();
        weather.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        weather.setTemperature(c.getInt(c.getColumnIndex(KEY_TEMP)));
        weather.setIcon(c.getString(c.getColumnIndex(KEY_ICON)));
        weather.setTime(c.getString(c.getColumnIndex(KEY_TIME)));

        return weather;
    }

    //Method for getting all the weather data in the database
    public List<Weather> getAllWeather() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Weather> allWeather = new ArrayList<Weather>();

        //Create cursor to run query
        String selectQuery = "SELECT * FROM " + TABLE_WEATHER;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                //Extract data from cursor
                Weather weather = new Weather();
                weather.setId(c.getInt(c.getColumnIndexOrThrow(KEY_ID)));
                weather.setTemperature(c.getInt(c.getColumnIndexOrThrow(KEY_TEMP)));
                weather.setIcon(c.getString(c.getColumnIndexOrThrow(KEY_ICON)));
                weather.setTime(c.getString(c.getColumnIndexOrThrow(KEY_TIME)));

                //add to list
                allWeather.add(weather);
            } while (c.moveToNext());
        }

        return allWeather;
    }

    //Method for getting weather data from the last 24 hr
    public List<Weather> getDailyWeather() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Weather> dailyWeather = new ArrayList<Weather>();

        //Create cursor to run query
        String selectQuery = "SELECT * FROM " + TABLE_WEATHER + " WHERE "
                + KEY_TIME + " > datetime('now', '-1 days')";
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                //Extract data from cursor
                Weather weather = new Weather();
                weather.setId(c.getInt(c.getColumnIndexOrThrow(KEY_ID)));
                weather.setTemperature(c.getInt(c.getColumnIndexOrThrow(KEY_TEMP)));
                weather.setIcon(c.getString(c.getColumnIndexOrThrow(KEY_ICON)));
                weather.setTime(c.getString(c.getColumnIndexOrThrow(KEY_TIME)));

                //add to list
                dailyWeather.add(weather);
            } while (c.moveToNext());
        }

        return dailyWeather;
    }

}

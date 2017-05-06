package com.example.peter.weatherapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.peter.weatherapp.MainActivity;
import com.example.peter.weatherapp.R;
import com.example.peter.weatherapp.model.Weather;

import java.util.List;

/**
 * Created by Peter on 06-May-17.
 * This class is inspired by Kasper's DataStorageDemo databaseHelper.java file
 */

public class WeatherAdaptor extends BaseAdapter {

    private List<Weather> weatherList;
    private Context context;

    private Weather w;

    public WeatherAdaptor(Context context, List<Weather> weatherList) {
        this.weatherList = weatherList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (weatherList == null) {
            return 0;
        }
        return weatherList.size();
    }

    @Override
    public Weather getItem(int i) {
        if(weatherList!=null && weatherList.size() > i){
            return weatherList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        if(weatherList!=null && weatherList.size() > i){
            return weatherList.get(i).getId();
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.weather_list_item, null);
        }

        if(weatherList != null && weatherList.size() > i) {
            w = weatherList.get(i);

            TextView desc = (TextView) view.findViewById(R.id.olddesc);
            desc.setText(w.getIcon());
            TextView temp = (TextView) view.findViewById(R.id.oldtemp);
            temp.setText(w.getTemperature() + "");
            TextView time = (TextView) view.findViewById(R.id.oldtime);
            time.setText(w.getTime());
            return view;
        }
        return null;
    }
}

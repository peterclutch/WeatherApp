package com.example.peter.weatherapp.model;

import java.util.Date;

/**
 * Created by Peter on 04-May-17.
 */

public class Weather {
    private int id;
    private Double temperature;
    //private String icon;
    private Date time;

    public Weather(Double temperature, String icon) {
        this.temperature = temperature;
        //this.icon = icon;
        time = new Date();
    }

    public int getid() {
        return id;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }
}

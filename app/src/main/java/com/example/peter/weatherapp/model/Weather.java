package com.example.peter.weatherapp.model;

import java.util.Date;

/**
 * Created by Peter on 04-May-17.
 */

public class Weather {
    private long id;
    private Double temperature;
    private String icon;
    private String time;

    public Weather() {

    }

    public Weather(Double temperature, String icon) {
        this.temperature = temperature;
        this.icon = icon;
        this.time = "";
    }

    public void setId(long id) { this.id = id; }

    public long getId() {
        return id;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setIcon(String icon) { this.icon = icon; }

    public String getIcon() { return icon; }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}

package com.project.mobile.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Use to
 * Created by DzungVu on 8/17/2017.
 */

public class Configuration {

    public static String apiRequest(String lat, String lng) {
        String API_KEY = "4f9d2641c76c10869bbf5dc2d044c6a7";
        String API_LINK = "http://api.openweathermap.org/data/2.5/weather";
        return API_LINK + String.format("?lat=%s&lon=%s&appid=%s", lat, lng, API_KEY);
    }

    public static String unixTimeStampToDateTime(double unixTimeStamp) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long) unixTimeStamp * 1000);
        return dateFormat.format(date);
    }

    public static String getImage(String icon) {
        return String.format("http://openweathermap.org/img/w/%s.png", icon);

    }

    public static String getDateNow() {
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static float toCelsius(double tempF) {
        return (float) (tempF - 273.15);
    }
}

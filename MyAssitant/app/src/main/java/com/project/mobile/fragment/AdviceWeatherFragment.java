package com.project.mobile.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.mobile.configuration.Configuration;
import com.project.mobile.helper.ConnectHelper;
import com.project.mobile.model.OpenWeatherMap;
import com.project.mobile.myassitant.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

/**
 * Use to
 * Created by DzungVu on 8/16/2017.
 */

public class AdviceWeatherFragment extends Fragment implements LocationListener {
    public static AdviceWeatherFragment newInstance() {
        return new AdviceWeatherFragment();
    }

    private TextView tvTimeUpdate;
    private TextView tvTempNow;
    private TextView tvHumidity;
    private TextView tvWind;
    private TextView tvPressure;
    private TextView tvTodayDescription;
    private TextView tvTomorrowDescription;
    private TextView tvNextTomorrowDescription;
    private TextView tvWeatherNextTomorrow;
    private TextView tvSummaryToday;
    private TextView tvSummaryTomorrow;
    private TextView tvSummaryNextTomorrow;
    private TextView tvCountry;
    private TextView tvDescription;

    private ImageView imgNow;
    private ImageView imgWeatherToday;
    private ImageView imgWeatherTomorrow;
    private ImageView imgWeatherNextTomorrow;

    private LinearLayout llToday;
    private LinearLayout llTomorrow;
    private LinearLayout llNextTomorrow;
    private LinearLayout llForecast5Day;

    private double lat;
    private double lng;
    private OpenWeatherMap openWeatherMap;
    private final int MY_PERMISSION = 0;
    private LocationManager locationManager;
    private String provider;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);
            return;
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            Log.e("TAG", "No location");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice_weather, container, false);
        tvTimeUpdate = view.findViewById(R.id.tv_advice_weather_time_update);
        tvTempNow = view.findViewById(R.id.tv_advice_weather_temperature);
        tvHumidity = view.findViewById(R.id.tv_advice_weather_humidity);
        tvWind = view.findViewById(R.id.tv_advice_weather_wind);
        tvPressure = view.findViewById(R.id.tv_advice_weather_pressure);
        tvTodayDescription = view.findViewById(R.id.tv_advice_weather_today_description);
        tvTomorrowDescription = view.findViewById(R.id.tv_advice_weather_tomorrow_description);
        tvNextTomorrowDescription = view.findViewById(R.id.tv_advice_weather_nexttomorrow_description);
        tvWeatherNextTomorrow = view.findViewById(R.id.tv_advice_weather_nexttomorrow);
        tvSummaryToday = view.findViewById(R.id.tv_advice_weather_today_summary);
        tvSummaryTomorrow = view.findViewById(R.id.tv_advice_weather_tomorrow_summary);
        tvSummaryNextTomorrow = view.findViewById(R.id.tv_advice_weather_nexttomorrow_summary);
        tvCountry = view.findViewById(R.id.tv_advice_weather_country);
        tvDescription = view.findViewById(R.id.tv_advice_weather_description);

        imgNow = view.findViewById(R.id.img_advice_weather_represent);
        imgWeatherToday = view.findViewById(R.id.img_advice_weather_today);
        imgWeatherTomorrow = view.findViewById(R.id.img_advice_weather_tomorrow);
        imgWeatherNextTomorrow = view.findViewById(R.id.img_advice_weather_nexttomorrow);

        llToday = view.findViewById(R.id.ll_advice_weather_today);
        llTomorrow = view.findViewById(R.id.ll_advice_weather_tomorrow);
        llNextTomorrow = view.findViewById(R.id.ll_advice_weather_nexttomorrow);
        llForecast5Day = view.findViewById(R.id.ll_advice_weather_next_5_day);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!ConnectHelper.isNetWorkAvalable(getActivity())){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Fail to connect to internet");
            builder.setTitle("Please check your internet connection and try again");
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{

                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
        Location location = locationManager.getLastKnownLocation(provider);
        lat = location.getLatitude();
        lng = location.getLongitude();
        GetWeatherTask task = new GetWeatherTask();
        task.execute(Configuration.apiRequest(String.valueOf(lat), String.valueOf(lng)));
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Getting data", Toast.LENGTH_LONG).show();
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("")){
                Toast.makeText(getActivity(), "Fail to get data", Toast.LENGTH_LONG).show();
                return;
            }
            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>(){}.getType();
            openWeatherMap = gson.fromJson(s, mType);
            Picasso.with(getActivity())
                    .load(Configuration.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(imgNow);
            tvTimeUpdate.setText(Configuration.unixTimeStampToDateTime((double) openWeatherMap.getDt()));
            tvTempNow.setText(String.format("%.1f °C", Configuration.toCelsius(openWeatherMap.getMain().getTemp())));
            tvHumidity.setText(String.format("Humidity: %d%%", openWeatherMap.getMain().getHumidity()));
            tvWind.setText(String.format("Speed: %.1f Km/h"
                    , openWeatherMap.getWind().getSpeed()));
            tvPressure.setText(String.format("Pressure: %.1f", openWeatherMap.getMain().getPressure()));
            tvCountry.setText(String.format("%s, %s", openWeatherMap.getSys().getCountry(), openWeatherMap.getName()));
            tvDescription.setText(String.format("%s", openWeatherMap.getWeather().get(0).getDescription()));
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            ConnectHelper helper = new ConnectHelper();
            return helper.getHttpData(strings[0]);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}

package com.example.android.rl_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {

    String CITY = "jaipur";
    String API = "8118ed6ee68db2debfaaa5a44c832918";
    TextView alocation,atemp ,atemp_max, atemp_min, ahumidity, apressure, awind_speed, asky_condition, asunset_time,
            asunrise_time, aday_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alocation=findViewById(R.id.location);
         atemp = findViewById(R.id.temp);
         atemp_max = findViewById(R.id.temp_max);
         atemp_min = findViewById(R.id.temp_min);
         ahumidity = findViewById(R.id.humidity);
         apressure = findViewById(R.id.pressure);
         awind_speed = findViewById(R.id.wind_speed);
         asky_condition = findViewById(R.id.sky_condition);
         asunset_time = findViewById(R.id.sunset_time);
         asunrise_time = findViewById(R.id.sunrise_time);
         aday_time = findViewById(R.id.day_time);




        new weatherTask().execute();




    }

         class weatherTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
           findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.main_container).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
            }



        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONObject weather_obj= obj.getJSONArray("weather").getJSONObject(0);
                JSONObject main_obj = obj.getJSONObject("main");
                JSONObject wind= obj.getJSONObject("wind");
                JSONObject sys_obj= obj.getJSONObject("sys");
                String city_name=obj.getString("name");
                String temp= main_obj.getString("temp") + " °C";
                String max_temp= "max_temp " +main_obj.getString("temp_max") + "°C";
                String min_temp= "min_temp " +main_obj.getString("temp_min") + "°C";
                String humidity= main_obj.getString("humidity");
                String pressure= main_obj.getString("pressure");
                Long sunrise_time= sys_obj.getLong("sunrise");
                Long sunset_time= sys_obj.getLong("sunset");
                Long day_time= obj.getLong("dt");
                String country_name=sys_obj.getString("country");
                String wind_speed=wind.getString("speed");
                String sky_cond= weather_obj.getString("description");

                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(day_time * 1000));
                String sunrise_at= new SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(new Date(sunrise_time*1000));
                String sunset_at= new SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(new Date(sunset_time*1000));

                String location=city_name+ "," +country_name;

                atemp.setText(temp);
                atemp_max.setText(max_temp);
                atemp_min.setText(min_temp);
                ahumidity.setText(humidity);
                apressure.setText(pressure);
                asunrise_time.setText(sunrise_at);
                asunset_time.setText(sunset_at);
                alocation.setText(location);
                awind_speed.setText(wind_speed);
                asky_condition.setText(sky_cond);
                aday_time.setText(updatedAtText);
               findViewById(R.id.loader).setVisibility(View.GONE);
               findViewById(R.id.main_container).setVisibility(View.VISIBLE);



            } catch (JSONException e) {
                e.printStackTrace();

                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);

            }
        }


    }


}
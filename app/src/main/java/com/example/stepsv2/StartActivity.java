package com.example.stepsv2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Locale;

public class StartActivity extends AppCompatActivity implements LocationListener, GpsStatus.Listener{
    private SharedPreferences sharedPreferences;
    private LocationManager mLocationManager;
    private static Data data;
    //private FloatingActionButton fab;
    //private FloatingActionButton refresh;
    private Button start;
    private Button stop;
    private TextView satellite;
    private TextView status;
    private TextView accuracy;
    private TextView currentSpeed;
    private TextView maxSpeed;
    private TextView averageSpeed;
    private TextView distance;
    private Chronometer time;
    private Data.onGpsServiceUpdate onGpsServiceUpdate;

    private boolean firstfix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        data = new Data(onGpsServiceUpdate);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //setTitle("");
        start = findViewById(R.id.Start);

        stop = findViewById(R.id.Stop);

        onGpsServiceUpdate = new Data.onGpsServiceUpdate() {
            @Override
            public void update() {
                double maxSpeedTemp = data.getMaxSpeed();
                double distanceTemp = data.getDistance();
                double averageTemp;
                if (sharedPreferences.getBoolean("auto_average", false)) {
                    averageTemp = data.getAverageSpeedMotion();
                } else {
                    averageTemp = data.getAverageSpeed();
                }

                String speedUnits;
                String distanceUnits;

                speedUnits = "км/ч";
                if (distanceTemp <= 1000.0) {
                    distanceUnits = "м";
                } else {
                    distanceTemp /= 1000.0;
                    distanceUnits = "км";
                }

                SpannableString s = new SpannableString(String.format("%.0f", maxSpeedTemp) + speedUnits);
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 4, s.length(), 0);
                maxSpeed.setText(s);

                s = new SpannableString(String.format("%.0f", averageTemp) + speedUnits);
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 4, s.length(), 0);
                averageSpeed.setText(s);

                s = new SpannableString(String.format("%.3f", distanceTemp) + distanceUnits);
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 2, s.length(), 0);
                distance.setText(s);
            }
        };

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        satellite = (TextView) findViewById(R.id.satellite);
        status = (TextView) findViewById(R.id.status);
        accuracy = (TextView) findViewById(R.id.accuracy);
        maxSpeed = (TextView) findViewById(R.id.maxSpeed);
        averageSpeed = (TextView) findViewById(R.id.averageSpeed);
        distance = (TextView) findViewById(R.id.distance);
        time = (Chronometer) findViewById(R.id.time);
        currentSpeed = (TextView) findViewById(R.id.currentSpeed);

        time.setText("00:00:00");
        time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            boolean isPair = true;

            @Override
            public void onChronometerTick(Chronometer chrono) {
                long time;
                if (data.isRunning()) {
                    time = SystemClock.elapsedRealtime() - chrono.getBase();
                    data.setTime(time);
                } else {
                    time = data.getTime();
                }

                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                chrono.setText(hh + ":" + mm + ":" + ss);

                if (data.isRunning()) {
                    chrono.setText(hh + ":" + mm + ":" + ss);
                } else {
                    if (isPair) {
                        isPair = false;
                        chrono.setText(hh + ":" + mm + ":" + ss);
                    } else {
                        isPair = true;
                        chrono.setText("");
                    }
                }

            }
        });
    }

    public void onStartClick(View v) {
        if (!data.isRunning()) {
            data.setRunning(true);
            time.setBase(SystemClock.elapsedRealtime() - data.getTime());
            time.start();
            data.setFirstTime(true);
            startService(new Intent(getBaseContext(), MyService.class));
        } else {
            data.setRunning(false);
            status.setText("");
            stopService(new Intent(getBaseContext(), MyService.class));
        }
    }

    public void onStopClick(View v) {
        resetData();
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstfix = true;
        if (!data.isRunning()) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("data", "");
            data = gson.fromJson(json, Data.class);
        }
        if (data == null) {
            data = new Data(onGpsServiceUpdate);
        } else {
            data.setOnGpsServiceUpdate(onGpsServiceUpdate);
        }
        
        if (mLocationManager.getAllProviders().indexOf(LocationManager.GPS_PROVIDER) >= 0) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, (LocationListener) this);
        } else {
            Log.w("MainActivity", "No GPS location provider found. GPS data display will not be available.");
        }

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, R.string.please_enable_gps, Toast.LENGTH_LONG).show();
        }
        mLocationManager.addGpsStatusListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
        mLocationManager.removeGpsStatusListener(this);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefsEditor.putString("data", json);
        prefsEditor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.hasAccuracy()) {
            SpannableString s = new SpannableString(String.format("%.0f", location.getAccuracy()) + "м");
            s.setSpan(new RelativeSizeSpan(0.75f), s.length() - 1, s.length(), 0);
            accuracy.setText(s);

            if (firstfix) {
                status.setText("");
                firstfix = false;
            }
        } else {
            firstfix = true;
        }

        if (location.hasSpeed()) {
            String speed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6) + "км/ч";

            SpannableString s = new SpannableString(speed);
            s.setSpan(new RelativeSizeSpan(0.25f), s.length() - 4, s.length(), 0);
            currentSpeed.setText(s);
        }

    }

    public void onGpsStatusChanged(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                int satsInView = 0;
                int satsUsed = 0;
                Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
                for (GpsSatellite sat : sats) {
                    satsInView++;
                    if (sat.usedInFix()) {
                        satsUsed++;
                    }
                }
                satellite.setText(String.valueOf(satsUsed) + "/" + String.valueOf(satsInView));
                if (satsUsed == 0) {
                    data.setRunning(false);
                    status.setText("");
                    stopService(new Intent(getBaseContext(), MyService.class));
                    accuracy.setText("");
                    status.setVisibility(View.VISIBLE);
                    firstfix = true;
                }
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(this,R.string.please_enable_gps,Toast.LENGTH_LONG).show();
                }
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                break;
        }
    }

   /* public void showGpsDisabledDialog(){
        Dialog dialog = new Dialog(this, getResources().getString(R.string.gps_disabled), getResources().getString(R.string.please_enable_gps));

        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        dialog.show();
    }*/

    public void resetData(){
        time.stop();
        maxSpeed.setText("");
        averageSpeed.setText("");
        distance.setText("");
        time.setText("00:00:00");
        data = new Data(onGpsServiceUpdate);
    }

    public static Data getData() {
        return data;
    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
/*
    public static class ChangeProgressEvent {

        public int progressmessage;

        public ChangeProgressEvent(int progressmessage) {
            this.progressmessage = progressmessage;
        }
    }*/
}


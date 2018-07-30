package com.example.stepsv2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import es.dmoral.toasty.Toasty;

public class StartActivity extends AppCompatActivity {
    TextView path;
    TextView speed;
    TextView accuracy;
    float meters=0;
    float check;
    Location s;
    boolean first=true;
    boolean second=false;
    double middle_x1;
    double middle_y1;
    double middle_x2;
    double middle_y2;

    TextView textView ;
    Button start, pause, stop;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds ;
    Fragment fragment;




    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        path = findViewById(R.id.path);
        speed = findViewById(R.id.speed);
        accuracy = findViewById(R.id.accuracy);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//    //Stopwatch////////////////////////////////////////////////////////////////////////////////
        textView = findViewById(R.id.textView);
        start = findViewById(R.id.Start);
        pause = findViewById(R.id.Pause);
        stop = findViewById(R.id.Stop);
        stop.setEnabled(false);
        pause.setEnabled(false);
        handler = new Handler() ;



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

                stop.setEnabled(false);
                pause.setEnabled(true);
                start.setEnabled(false);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);

                stop.setEnabled(true);
                start.setEnabled(false);

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;
                textView.setText("00:00:00");
                pause.setEnabled(false);
                stop.setEnabled(false);
                start.setEnabled(true);
                //TODO: окошко "красавчик" с кнопкой "домой"
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setTitle("Выход")
                        .setMessage("Красавчик, ты пробежал : "+Math.round(meters))
                        .setCancelable(false)
                        .setNegativeButton("Домой",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        EventBus.getDefault().post(new ChangeProgressEvent(Math.round(meters)));
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }
    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            textView.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }
    };
    //Stopwatch//////////////////////////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, locationListener);
            }
            else{
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    Toast.makeText(this,"Откройте доступ к местоположению",Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            speed.setText(String.valueOf(location.getSpeed()) + " м/с");
            path.setText(String.valueOf(Math.round(distance(location))) + " м");
            accuracy.setText(String.valueOf(location.getAccuracy()));
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    public float distance(Location location) {
        if(first)
        {
            s = location;
            first=false;
            second=true;
        }
        if(second)
        {
            middle_x1=(s.getLatitude()+location.getLatitude())/2;
            middle_y1=(s.getLongitude()+location.getLongitude())/2;
            meters+=distanceBetweenTwoPoint(s.getLatitude(),s.getLongitude(),middle_x1,middle_y1);
            second=false;
        }
        else {
            middle_x2=(s.getLatitude()+location.getLatitude())/2;
            middle_y2=(s.getLongitude()+location.getLongitude())/2;
            meters+=distanceBetweenTwoPoint(middle_x1,middle_y1,middle_x2,middle_y2);
            middle_x1=middle_x2;
            middle_y1=middle_y2;
        }
        s = location;
        return meters;
    }
    double distanceBetweenTwoPoint(double srcLat, double srcLng, double desLat, double desLng) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(desLat - srcLat);
        double dLng = Math.toRadians(desLng - srcLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(srcLat))
                * Math.cos(Math.toRadians(desLat)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        double meterConversion = 1609;

        return (dist * meterConversion);
    }

    public static class ChangeProgressEvent {

        public int progressmessage;

        public ChangeProgressEvent(int progressmessage) {
            this.progressmessage = progressmessage;
        }
    }
}


package com.example.stepsv2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity{
    TextView path;
    TextView speed;
    TextView accuracy;
    float meters=0;
    Location s;
    boolean first=true;
    boolean second=false;
    boolean active=false;
    double middle_x1;
    double middle_y1;
    double middle_x2;
    double middle_y2;
    public final static String PATH = "path";
    public final static String SPEED = "speed";
    public final static String BROADCAST_ACTION = "com.example.stepsv2";
    BroadcastReceiver br;

    TextView textView ;
    Button start, pause, stop;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds ;


    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        path = findViewById(R.id.path);
        speed = findViewById(R.id.speed);
        accuracy = findViewById(R.id.accuracy);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intFilt);
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
                start.setEnabled(false);
                pause.setEnabled(true);
                active=true;
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);

                stop.setEnabled(true);
                start.setEnabled(true);
                pause.setEnabled(false);
                active=false;
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
                textView.setText("0:00:00");
                pause.setEnabled(false);
                stop.setEnabled(false);
                start.setEnabled(true);
                active=false;
                meters=0;
                path.setText("0 м");
                speed.setText("0 м/c");
                //TODO: окошко "красавчик" с кнопкой "домой"
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
    protected void onDestroy() {
        super.onDestroy();
        // дерегистрируем (выключаем) BroadcastReceiver
        unregisterReceiver(br);
    }
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
    }
}


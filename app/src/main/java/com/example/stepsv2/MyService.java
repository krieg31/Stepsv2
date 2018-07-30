package com.example.stepsv2;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Intent;

public class MyService extends Service {

    BroadcastReceiver broadcaster;
    Intent intent;
    static final public String BROADCAST_ACTION = "com.example.stepsv2";

    float meters=0;
    Location s;
    boolean first=true;
    boolean second=false;
    boolean active=false;
    double middle_x1;
    double middle_y1;
    double middle_x2;
    double middle_y2;
    private LocationManager locationManager;

    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Toast.makeText(this, "Служба создана",
                Toast.LENGTH_SHORT).show();
        intent = new Intent(BROADCAST_ACTION);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Старт службы",
                Toast.LENGTH_SHORT).show();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, locationListener);
        return super.onStartCommand(intent, flags, startId);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            intent.putExtra("path", meters);
            intent.putExtra("speed", location.getSpeed());
            sendBroadcast(intent);
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
        if ((location.getAccuracy() < 15) && (active)) {
            if (first) {
                s = location;
                first = false;
                second = true;
            }
            if (second) {
                middle_x1 = (s.getLatitude() + location.getLatitude()) / 2;
                middle_y1 = (s.getLongitude() + location.getLongitude()) / 2;
                meters += distanceBetweenTwoPoint(s.getLatitude(), s.getLongitude(), middle_x1, middle_y1);
                second = false;
            }
            else {
                middle_x2 = (s.getLatitude() + location.getLatitude()) / 2;
                middle_y2 = (s.getLongitude() + location.getLongitude()) / 2;
                meters += distanceBetweenTwoPoint(middle_x1, middle_y1, middle_x2, middle_y2);
                middle_x1 = middle_x2;
                middle_y1 = middle_y2;
                s = location;
            }
        }
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
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
        Toast.makeText(this, "Служба уничтожена",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}

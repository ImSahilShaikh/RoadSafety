package com.example.deva.block;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    private LocationManager locManager;
    private LocationListener locListener = new myLocationListener();
    static final Double EARTH_RADIUS = 6371.00;

    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    private Handler handler = new Handler();
    Thread t;

    @Override
    public IBinder onBind(Intent intent) {return null;}
    @Override
    public void onCreate() {}
    @Override
    public void onDestroy() {}
    @Override
    public void onStart(Intent intent, int startid) {}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT).show();

        final Runnable r = new Runnable()
        {   public void run()
        {
            Log.v("Debug", "Hello");
            location();
            handler.postDelayed(this, 3000);
        }
        };
        handler.postDelayed(r, 3000);
        return START_STICKY;
    }

    public void location(){
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex){}
        try{
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex){}
        Log.v("Debug", "in on create.. 2");
        if (gps_enabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locListener);
            Log.v("Debug", "Enabled..");
        }
        if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locListener);
            Log.v("Debug", "Disabled..");
        }
        Log.v("Debug", "in on create..3");
    }

    private class myLocationListener implements LocationListener
    {
        double lat_old=0.0;
        double lon_old=0.0;
        double lat_new;
        double lon_new;
        double time=10;
        double speed=0.0;
        @Override
        public void onLocationChanged(Location location) {
            Log.v("Debug", "in onLocation changed..");
            if(location!=null){
                locManager.removeUpdates(locListener);
                //String Speed = "Device Speed: " +location.getSpeed();
                lat_new=location.getLongitude();
                lon_new =location.getLatitude();
                String longitude = "Longitude: " +location.getLongitude();
                String latitude = "Latitude: " +location.getLatitude();
                double distance =CalculationByDistance(lat_new, lon_new, lat_old, lon_old);
                speed = distance/time;
                Toast.makeText(getApplicationContext(), longitude+"\n"+latitude+"\nDistance is: "
                        +distance+"\nSpeed is: "+speed , Toast.LENGTH_SHORT).show();

                ((MyApp) getApplication()).setSpeed(speed);

                lat_old=lat_new;
                lon_old=lon_new;
            }
        }
        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    public double CalculationByDistance(double lat1, double lon1, double lat2, double lon2) {
        double Radius = EARTH_RADIUS;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }
}

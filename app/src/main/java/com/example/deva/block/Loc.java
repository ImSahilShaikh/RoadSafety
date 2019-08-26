package com.example.deva.block;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class Loc extends AppCompatActivity {

    WebView webview;
    WebSettings websettings;
    Button gps;
    Button sms;
    LocationManager locationManager;
    LocationListener listener;
    Double lattitude;
    Double longitude;
    String smsBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc2);

        webview = (WebView) findViewById(R.id.webview);
        gps = (Button) findViewById(R.id.btnEnableGPS);
        sms = (Button) findViewById(R.id.btnSms);
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
        });

            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String string ="http://maps.google.com/?q="+lattitude+","+longitude;

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, string);
                        intent.setType("text/plain");
                        startActivity(Intent.createChooser(intent, "Share Location Via "));  } catch (Exception e) {


                    }

                }
            });
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lattitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestAllpermissions();
                }
                return;
            }



            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);




            websettings = webview.getSettings();
            websettings.setJavaScriptEnabled(true);
            websettings.setDisplayZoomControls(true);
            websettings.setBuiltInZoomControls(true);
            websettings.setGeolocationEnabled(true);
            webview.setWebChromeClient(new GeoWebChromeClient());
            webview.loadUrl("https://www.google.com/maps");

        }

        @Override
        protected void onStart() {
            super.onStart();
            requestAllpermissions();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            switch(requestCode)
            {
                case 10:
                    if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    return;
            }

        }

        public class GeoWebChromeClient extends WebChromeClient
        {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin,true,false);
            }
        }
    private void requestAllpermissions()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
        {
            new AlertDialog.Builder(this).setTitle("Permission Needed").setMessage("We Need your Location Access To get GPS Co-ordinates.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(Loc.this,new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest        .permission.INTERNET
                    },10);

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET
            },10);

        }
    }

}


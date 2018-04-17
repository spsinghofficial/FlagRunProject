package com.robpercival.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;

    LocationListener locationListener;
    int previousDistance = 5000;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startListening();

        }

    }

    public void startListening() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        }

    }

    public void updateLocationInfo(Location location) {

        Log.i("LocationInfo", location.toString());
        TextView latTextView = (TextView) findViewById(R.id.latTextView);
        TextView lonTextView = (TextView) findViewById(R.id.lonTextView);
        TextView altTextView = (TextView) findViewById(R.id.altTextView);
        TextView accTextView = (TextView) findViewById(R.id.accTextView);
        latTextView.setText("Latitude: " + location.getLatitude());
        lonTextView.setText("Longitude: " + location.getLongitude());
        altTextView.setText("Altitude: " + location.getAltitude());
        accTextView.setText("Accuracy: " + location.getAccuracy());
        TextView distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        TextView updates = (TextView) findViewById(R.id.updates);



       // final Double[] previousDistance = {5000.0};

        Location startPoint=new Location("my location");
        startPoint.setLatitude(location.getLatitude());
        startPoint.setLongitude(location.getLongitude());

        Location endPoint=new Location("flag location ");
        endPoint.setLatitude(43.775438);
        endPoint.setLongitude(-79.334250);

       int newDistance= (int) startPoint.distanceTo(endPoint);

        Log.d("current latitude", String.valueOf(location.getLatitude()));
        Log.d("current longitude", String.valueOf(location.getLongitude()));
        Log.d("new distance", String.valueOf(newDistance));

        // LOGIC TO CHECK IF USER IS MOVING TOWARD THE FLAG OR NOT

        if (newDistance > previousDistance) {
            Log.d("status", "moving away from the flag");
            updates.setText("Moving Away from the flag ");
            previousDistance = newDistance;

        }
        else {

            Log.d("status", "moving towards the flag");
            updates.setText("Moving Towards  the flag ");
            previousDistance = newDistance;
        }


        distanceTextView.setText("Distance: " + newDistance + "meter");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

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
        };

        if (Build.VERSION.SDK_INT < 23) {

            startListening();

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {

                    updateLocationInfo(location);

                }

            }

        }



    }
}

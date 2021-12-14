package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView latTextView;
    TextView longTextView;
    TextView accuracyTextView;
    TextView addressTextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults != null && grantResults.length >0)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTextView =findViewById(R.id.latTextView);
        longTextView =findViewById(R.id.longTextView);
        accuracyTextView =findViewById(R.id.accuracyTextView);
        addressTextView =findViewById(R.id.addressTextView);

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAdresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if(listAdresses != null && listAdresses.size() > 0)
                    {
                        String address = "";
                        if(listAdresses.get(0).getAddressLine(1) != null )
                            address+= listAdresses.get(0).getAddressLine(1)+"\n ";
                        if(listAdresses.get(0).getThoroughfare() != null )
                            address+= listAdresses.get(0).getThoroughfare()+"\n ";
                        if(listAdresses.get(0).getLocality() != null)
                            address+= listAdresses.get(0).getLocality()+"\n ";
                        if(listAdresses.get(0).getAdminArea() != null)
                            address+= listAdresses.get(0).getAdminArea()+"\n ";
                        if(listAdresses.get(0).getPostalCode() != null  )
                            address+= "Postal code: "+ listAdresses.get(0).getPostalCode();

                        latTextView.setText("Latitude: "+location.getLatitude());
                        longTextView.setText("longitude: "+location.getLongitude());
                        accuracyTextView.setText("Latitude: "+location.getAccuracy());
                        addressTextView.setText("address: \n"+address);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        // if permission not granted yet
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
    }
}
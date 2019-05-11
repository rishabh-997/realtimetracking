package com.example.realtimetracking;

import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference databaseReference;

    Double latitude, longitude;
    String email;

    Double latitude_sample, longitude_sample;
    String email_sample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**
         * get the passes intents here;
         */
        latitude = getIntent().getDoubleExtra("Latitude", 0);
        longitude = getIntent().getDoubleExtra("Longitude", 0);
        email = getIntent().getStringExtra("Email");

        latitude_sample=26.8621305;
        longitude_sample=80.6591949;
        email_sample="chaze@gmail.com";

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**
         * firstly created loaction dataset for my Location
         */
        LatLng mine = new LatLng(latitude, longitude);
        Location myLocation = new Location("");
        myLocation.setLatitude(latitude);
        myLocation.setLongitude(longitude);

        /**
         * Secondly, Created a marker for my location will be zoomed in as soon as I open the map
         */
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(mine)
                    .title(email)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
        }

        /**
         *  Adding Location dataset for friend :
         */
        LatLng friend=new LatLng(latitude_sample,longitude_sample);
        Location friendLocation=new Location("");
        friendLocation.setLatitude(latitude_sample);
        friendLocation.setLongitude(longitude_sample);

        /**
         * Calculating distance between the two pointers
         */
        float distance=(friendLocation.distanceTo(myLocation))/1000;


        /**
         * Created aa marker for my Friend will be zoomed in
         */
        mMap.addMarker(new MarkerOptions()
                .position(friend)
                .title(email_sample)
                .snippet(""+distance+" kms")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


    }
}
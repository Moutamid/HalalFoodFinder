package com.moutamid.halalfoodfinder.Activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.moutamid.halalfoodfinder.Model.LocationModel;
import com.moutamid.halalfoodfinder.R;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // below are the latitude and longitude
    // of 4 different locations.
    double lat, lng;
    // creating array list for adding all our locations.
    private ArrayList<LatLng> locationArrayList;
    String name;
    ArrayList<LocationModel> locationModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationModels = Stash.getArrayList("Locations", LocationModel.class);

        // in below line we are initializing our array list.
        locationArrayList = new ArrayList<>();

        // on below line we are adding our

        // locations in our array list.
        lat = getIntent().getDoubleExtra("lat", 0.0);
        lng = getIntent().getDoubleExtra("lng", 0.0);
        name = getIntent().getStringExtra("name");
        if (lat != 0.0) {

            LatLng sydney = new LatLng(lat, lng);
            locationArrayList.add(sydney);
        } else {
            for (int i = 0; i < locationModels.size(); i++) {
                LatLng sydney = new LatLng(locationModels.get(i).getLat(), locationModels.get(i).getLng());
                if (locationModels.get(i).getLat() > -90 && locationModels.get(i).getLat() < 90 && locationModels.get(i).getLng() > -180 && locationModels.get(i).getLng() < 180) {
                    locationArrayList.add(sydney);

                }
            }
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < locationArrayList.size(); i++) {
            if (lat != 0.0) {
                mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(name));
            } else {
                mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(locationModels.get(i).getName()));
            }
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList.get(i), 16.0f));
//            float zoomLevel = 16.0f; //This goes up to 21
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        }
    }
}

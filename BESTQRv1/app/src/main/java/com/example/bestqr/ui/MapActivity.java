package com.example.bestqr.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.bestqr.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap qrMap;

    Location location1 ;
    Location location2;

    LatLng edmonton;
    LatLng wested;

    ArrayList<LatLng> locations = new ArrayList<LatLng>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Toast.makeText(MapActivity.this, "Here you go", Toast.LENGTH_SHORT).show();


        location1.setLatitude(53.631611);
        location1.setLongitude( -113.323975);

        location2.setLatitude(53.522778);
        location2.setLongitude(-113.623055);

        edmonton = new LatLng(location1.getLatitude(),location2.getLongitude());
        wested = new LatLng(location2.getLatitude(),location2.getLongitude());

        locations.add(edmonton);
        locations.add(wested);



    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        qrMap = googleMap;
        qrMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        qrMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng+" : "+latLng.longitude);
                qrMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                qrMap.addMarker(markerOptions);
            }
        });


//        for (int i = 0;i < locations.size();i++){
//            qrMap.addMarker(new MarkerOptions().position(locations.get(i)).title("Marker"));
//            qrMap.animateCamera(CameraUpdateFactory.newLatLngZoom(edmonton,10));
//            qrMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(i)));
//        }

    }

    //TODO: Auto-zoom to user location, add clickable pins representing codes
}

package com.example.bestqr.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationListenerCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bestqr.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class MapsFragment extends Fragment {

    GoogleMap qrMap;
    FusedLocationProviderClient client;
    Context mapContext;
    SupportMapFragment supportMapFragment;
    List<DocumentSnapshot> nearbyCodeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Initialize view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        //Initialize map fragment
        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        // Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(mapContext);

        // Check permission
        if (ActivityCompat.checkSelfPermission(mapContext,
                Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED){
            // When permission is granted
            // Call method
            getCurrentLocation();

        } else {
            // When permission denied
            // Request permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        return view;
    }

    private void getCurrentLocation() {
        // Initialize task location
        @SuppressLint("MissingPermission") Task<Location> task = client.getCurrentLocation(102, null);
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // When successful
                if (location != null) {
                    // Sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            // Initialize lat lng
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            // Create marker options
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("Current location");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                            // Add marker on map
                            googleMap.addMarker(options);

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // When permission is granted
                // Call method
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mapContext = context;
    }
}
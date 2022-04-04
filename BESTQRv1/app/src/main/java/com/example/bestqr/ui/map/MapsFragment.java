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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestqr.Database.Database;
import com.example.bestqr.QRCodeList;
import com.example.bestqr.R;
import com.example.bestqr.models.QRCODE;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.maps.android.SphericalUtil;

import java.util.List;

public class MapsFragment extends Fragment {

    GoogleMap qrMap;
    FusedLocationProviderClient client;
    Context mapContext;
    SupportMapFragment supportMapFragment;
    LatLng userLatLng;
    QRCodeList allQRCodes = Database.getAllCodes();
    Button geoSearchButton;
    EditText searchLat;
    EditText searchLng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Initialize view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        searchLat = (EditText) view.findViewById(R.id.editLatitude);
        searchLng = (EditText) view.findViewById(R.id.editLongitude);
        geoSearchButton = (Button) view.findViewById(R.id.geoSearchButton);

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

        geoSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    double lat = Double.parseDouble(searchLat.getText().toString());
                    double lng = Double.parseDouble(searchLng.getText().toString());

                    LatLng searchLatLng = new LatLng(lat, lng);

                    // Create marker options
                    MarkerOptions options = new MarkerOptions().position(searchLatLng)
                            .title("Searched location");

                    qrMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchLatLng, 10));

                    // Add marker on map
                    qrMap.addMarker(options);

                    plotNearbyCodes(searchLatLng, 5000);


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        });

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

                            qrMap = googleMap;

                            // Initialize lat lng
                            userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                            // Create marker options
                            MarkerOptions options = new MarkerOptions().position(userLatLng)
                                    .title("Current location");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 10));

                            // Add marker on map
                            googleMap.addMarker(options);


                            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                @Override
                                public void onMapLoaded() {

                                    plotNearbyCodes(userLatLng, 5000);

                                }
                            });

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

    public void plotNearbyCodes(LatLng center, double maxDistance) {

        for (QRCODE qrcode: allQRCodes) {

            if (qrcode.getCodeLocation() != null) {

                LatLng qrLatLng = new LatLng(qrcode.getCodeLocation().getLatitude(), qrcode.getCodeLocation().getLongitude());

                if (SphericalUtil.computeDistanceBetween(center, qrLatLng) < maxDistance) {

                    qrMap.addMarker(new MarkerOptions()
                            .position(new LatLng(qrcode.getCodeLocation().getLatitude(), qrcode.getCodeLocation().getLongitude()))
                            .title("Score: " + qrcode.getScore()));
                }

            }
        }
    }
}
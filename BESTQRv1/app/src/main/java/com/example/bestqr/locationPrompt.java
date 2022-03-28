package com.example.bestqr;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.io.Serializable;


public class locationPrompt extends DialogFragment {

    public static final int CAMERA_REQUEST_CODE = 1890;
    private CheckBox storeImage;
    private CheckBox storeLocation;
    private ImageView qrImage;
    private TextView scores;
    private Profile profile;
    private QRCODE qrcode;
    private OnFragmentInteractionListener listener;
    private static final int PICK_IMAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    public static locationPrompt newInstance(Profile profile,QRCODE qrcode) {
        Bundle args = new Bundle();
        args.putSerializable("profile", profile);
        args.putSerializable("qr", qrcode);

        locationPrompt fragment = new locationPrompt();
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void onOkPressed(Profile profile,QRCODE qrcode);
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_location, null);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        storeImage = view.findViewById(R.id.storeImage);
        storeLocation = view.findViewById(R.id.storeLocation);
        qrImage = view.findViewById(R.id.ObjectImage);
        scores = view.findViewById(R.id.scoreText);
        Bundle args = getArguments();
        if (args != null) {
            profile = (Profile) args.getSerializable("profile");
            qrcode = (QRCODE) args.getSerializable("qr");
            qrImage.setImageBitmap(qrcode.getCode());
            scores.setText("Score: "+ Integer.toString(qrcode.getScore()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Select Settings")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (storeImage.isChecked()){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        }
                        if (storeLocation.isChecked()){
                            fetchLocation();
                            Toast.makeText(getActivity(), "Latitude: " + qrcode.getCodeLocation().getLatitude() +"Longitude: " + qrcode.getCodeLocation().getLongitude() , Toast.LENGTH_SHORT).show();
                        }
                            profile.addNewQRCode(qrcode);

                        UserViewModel model = new ViewModelProvider(getActivity()).get(UserViewModel.class);
                        model.setUserProfile(profile);

                    }
                }).create();

    }

    private void fetchLocation() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(getContext())
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            LocationManager loc = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria crta = new Criteria();
            crta.setAccuracy(Criteria.ACCURACY_FINE);
            crta.setAltitudeRequired(true);
            crta.setBearingRequired(true);
            crta.setCostAllowed(true);
            crta.setPowerRequirement(Criteria.POWER_LOW);
            String provider = loc.getBestProvider(crta, true);
            Location location = loc.getLastKnownLocation(provider);
            qrcode.setCodeLocation(location);
        }
        UserViewModel userModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userModel.setUserProfile(profile);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //abc
            } else {

            }
        }
    }

}


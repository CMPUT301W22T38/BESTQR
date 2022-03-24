package com.example.bestqr;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.common.StringUtils;
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
    private EditText comments;
    private QRCODE qrcode;
    private OnFragmentInteractionListener listener;
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
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        storeImage = view.findViewById(R.id.storeImage);
        storeLocation = view.findViewById(R.id.storeLocation);
        comments = view.findViewById(R.id.comments);
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
                        }

                        if (TextUtils.isEmpty(comments.getText().toString()) == false){
                            qrcode.addComments(comments.getText().toString());
                        }
                        profile.addNewQRCode(qrcode);
                    }
                }).create();

    }

}


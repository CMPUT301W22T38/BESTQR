package com.example.bestqr;

import static android.content.ContentValues.TAG;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                            Toast.makeText(getActivity(), "Location is Checked", Toast.LENGTH_SHORT).show();
                        }

                        profile.addNewQRCode(qrcode);
                    }
                }).create();

    }


}


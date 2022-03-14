package com.example.bestqr;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bestqr.ui.user.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.databinding.ActivityMainBinding;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CameraActivity extends AppCompatActivity {

    // Define the pic id
    private static final int PIC_ID = 123;
    private static final int PICK_IMAGE = 1;

    // Define the button and imageview type variable
    Button scanButton;

    // Button to scan qr Code from gallery
    ImageButton openGallery;

    // Top Level Navigation Destinations
    // (Fragments that are navigable to from the bottom bar)
    private static Set<Integer> topLevelDestinations = new HashSet<>(Arrays.asList(
            R.id.navigation_notifications, R.id.navigation_home, R.id.navigation_leaderboard, R.id.navigation_user));

    // UserViewModel
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.bestqr.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // This should be in th login activity
        // get unique device id
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        // test identification of user ideally info will be taken in the signup activity and stored in firebase
        QR_CODE userIdentification = new QR_CODE(androidId);
        Profile userProfile = new Profile("UserName",userIdentification,1231231231,"emailaddress");
        //ToDo Store profiles in firebase

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setUserProfile(userProfile);

        //This is open camera
        scanButton= findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //initialize intent integrator
                IntentIntegrator intentIntegrator = new IntentIntegrator(CameraActivity.this);
                //locked orientation
                intentIntegrator.setOrientationLocked(true);
                //Set capture activity
                intentIntegrator.setCaptureActivity(Capture.class);
                //Initiate scan
                intentIntegrator.initiateScan();
            }
        });

        // Choose image from gallery
        openGallery = (ImageButton) this.findViewById(R.id.openGallery);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent();
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Image"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );

        MessageDigest digest;
        String qrHash;

        // If user takes photo with camera
        if (requestCode == PIC_ID) {
            //Check condition
            if (intentResult.getContents() != null) {
                //When result content is not null
                //Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        CameraActivity.this
                );
                //Set title
                builder.setTitle("Score = ");
                //Set score(but currently is set message)
                builder.setMessage(intentResult.getContents());
                //Set positive button
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                //Show alert dialog
                builder.show();
            } else {
                //When result content is null
                //Display toast
                Toast.makeText(getApplicationContext()
                        , "sorry, nothing is scanned", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        // If user chooses an image from the gallery
        if (requestCode == PICK_IMAGE) {

            // Convert QR from gallery to contents
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                try {
                    Bitmap bMap = selectedImage;
                    String contents = null;
                    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    MultiFormatReader reader = new MultiFormatReader();
                    Result result = reader.decode(bitmap);
                    contents = result.getText();

                    // Create new QR object using contents as argument
                    QR_CODE newQR = new QR_CODE(contents);

                    // Display toast showing QR hash
                    Toast.makeText(getApplicationContext(),newQR.getScore(),Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    e.printStackTrace();
                }


            } catch (FileNotFoundException e) {

                e.printStackTrace();

                Toast.makeText(CameraActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static Set<Integer> getTopLevelDestinations(){
        return topLevelDestinations;
    }
}
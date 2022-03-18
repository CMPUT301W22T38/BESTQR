package com.example.bestqr;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bestqr.ui.qr.QrViewModel;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CameraActivity extends AppCompatActivity {

    // Define the pic id and pic_image id
    private static final int PIC_ID = 123;
    private static final int PICK_IMAGE = 1;
    private QRCODE qr;
    private String contents;
    private int score = 0;

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
    private QrViewModel QrViewModel;
    private Database db;
    private Profile userProfile;

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



        String androidid1 = "androidid1";
        String androidid2 = "androidid2";
        db = new Database();
        Profile p1 = db.get_user(androidid1);
        Profile p2 = db.get_user(androidid2);

        QRCODE qr1 = new QRCODE("1");
        QRCODE qr2 = new QRCODE("2");
        QRCODE qr3 = new QRCODE("3");
        QRCODE qr4 = new QRCODE("1");
        QRCODE qr5 = new QRCODE("5");
        QRCODE qr6 = new QRCODE("6");

        db.add_qrcode(androidid1,qr1);
        db.add_qrcode(androidid1,qr2);
        db.add_qrcode(androidid1,qr3);
        db.add_qrcode(androidid2,qr4);
        db.add_qrcode(androidid2,qr5);
        db.add_qrcode(androidid2,qr6);



//        Storage storage = new Storage();
//
//        QRCODE q1 = new QRCODE("random data5513123532");
//        System.out.println(q1.getCode());
//        System.out.println(q1.getCode().toString());
//        System.out.println(q1.getCode().getRowBytes());





//        bitmap
//        QRCODE q1 = new QRCODE("random54321");





        // This should be in th login activity
        // get unique device id
//        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        // test identification of user ideally info will be taken in the signup activity and stored in firebase
//        QRCODE userIdentification = new QRCODE(androidId);
//        //ToDo Store profiles in firebase
//        // TEMP: Test user profile, to showcase functionality of fragments showing user info.
//        userProfile = new Profile("Test User",userIdentification,"1231231231","email@address.com");
//        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//        userViewModel.setUserProfile(userProfile);
//
//        QrViewModel = new ViewModelProvider(this).get(QrViewModel.class);
//        QrViewModel.setUserProfile(userProfile);
//
//        //This is open camera
//
//        ////////////////////////////
//        db = new Database();
//
//        Profile p = db.getProfile(androidId);
//
//        QRCODE q1 = new QRCODE("random1");
//        QRCODE q2 = new QRCODE("random2");
//
//        db.writeQRCode(q1, androidId);
//        db.writeQRCode(q2, androidId);
//        // https://console.firebase.google.com/project/bestqrdb/database/bestqrdb-default-rtdb/data
//        // https://console.firebase.google.com/project/bestqrdb/storage/bestqrdb.appspot.com/files
//        ///////////////////////////////

        // jay

//        System.out.println(userProfile.)
//
//        Location location = new Location(LocationManager.GPS_PROVIDER);
//        location.setAltitude(1.421);
//        location.setLongitude(5.325);
//        qr.setCodeLocation(location);
//        db.add_qrcode(androidid, qr);
//
//        db.get_qrcode(androidid, "3ad621f46a4bcc34d12490adc689d51ef0dbc12c913538427a667e1c52b97459");




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
//        Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );

        // If user chooses an image from the gallery
        if (requestCode == PICK_IMAGE) {

            // Convert QR from gallery to contents
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                try {
                    Bitmap bMap = selectedImage;
                    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    MultiFormatReader reader = new MultiFormatReader();
                    Result result = reader.decode(bitmap);
                    contents = result.getText();


                    // Create new QR object using contents as argument
                    qr = new QRCODE(contents);
                    score = qr.getScore();

//                    db.writeImage(newQR, profile.getandroidId());
//                    db.QRCodeReceivedFromCameraActivity(newQR, profile.getandroidId());

                    // Display toast showing QR hash
                    //When result content is not null
                    //Initialize alert dialog

                } catch (Exception e){
                    e.printStackTrace();
                }


            } catch (FileNotFoundException e) {

                e.printStackTrace();

                Toast.makeText(CameraActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        }

        else {
            //Check condition
            if (intentResult.getContents() != null) {
                //When result content is not null
                // Create new QR object
                contents = intentResult.getContents();
                qr = new QRCODE(contents);
                score = qr.getScore();

            } else {
                //When result content is null
                //Display toast
                Toast.makeText(getApplicationContext()
                        , "sorry, nothing is scanned", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        // Build dialog using score of QR
        AlertDialog.Builder builder = new AlertDialog.Builder(
                CameraActivity.this
        );



        userProfile.addNewQRCode(qr);
        //Set title
        builder.setTitle("Score = ");
        //Set score(but currently is set message)
        builder.setMessage(score + " ");
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

        //
    }

    /**
     * This method converts the a bytes representation to a hexadecimal string
     * @param hash The bytes that are to be converted
     * @return the String hexadecimal representation of the bytes provided
     */
    protected static String bytesToHex(byte[] hash) {
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

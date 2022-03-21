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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bestqr.ui.qr.QrViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CameraActivity extends AppCompatActivity implements locationPrompt.OnFragmentInteractionListener {

    // Define the pic id and pic_image id
    private static final int PIC_ID = 123;
    private static final int PICK_IMAGE = 1;
    private QRCODE qr;
    private String contents;
    private Profile p1;
    private int score = 0;

    private static final String TAG = "CameraActivity";

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
        QRCODE userIdentification = new QRCODE(androidId);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        String androidid1 = "52e697d704";
        String androidid2 = "753098d871";
        db = new Database();
//        p1 = db.get_user(androidid1);
        p1 = new Profile(androidId);
//        Profile p2 = this.db.get_user(androidid2);

//        QRCODE qr1 = new QRCODE("qr1");
//        QRCODE qr2 = new QRCODE("qr2");
//        QRCODE qr3 = new QRCODE("qr3");
//        QRCODE qr5 = new QRCODE("qr5");
//        QRCODE qr6 = new QRCODE("qr6");
//
//        db.add_qrcode(androidid1,qr1);
//        db.add_qrcode(androidid1,qr2);
//        db.add_qrcode(androidid1,qr3);
//
//        db.add_qrcode(androidid2,qr3);
//        db.add_qrcode(androidid2,qr5);
//        db.add_qrcode(androidid2,qr6);

        userViewModel.setUserProfile(p1);



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
    }

    public void scanButton(View v){
        //initialize intent integrator
        IntentIntegrator intentIntegrator = new IntentIntegrator(CameraActivity.this);
        //locked orientation
        intentIntegrator.setOrientationLocked(true);
        //Set capture activity
        intentIntegrator.setCaptureActivity(Capture.class);
        //Initiate scan
        intentIntegrator.initiateScan();
    }


    public void openGallery(View v){
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Image"), PICK_IMAGE);
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
                    locationPrompt.newInstance(p1,qr).show(getSupportFragmentManager(),"NEW QRCODE");

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

        else if (requestCode == 49374) {
            //Check condition
            if (intentResult.getContents() != null) {
                //When result content is not null
                // Create new QR object
                contents = intentResult.getContents();
                qr = new QRCODE(contents);
                score = qr.getScore();
                locationPrompt.newInstance(p1,qr).show(getSupportFragmentManager(),"NEW QRCODE");

            } else {
                //When result content is null
                //Display toast
                Toast.makeText(getApplicationContext()
                        , "sorry, nothing is scanned", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        else{
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            qr.setObjectImage(captureImage);
        }
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

    @Override
    public void onOkPressed(Profile profile,QRCODE qrcode) {
//        db.add_qrcode();
    }

}

package com.example.bestqr;

import android.annotation.SuppressLint;

import android.app.Activity;
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

import com.example.bestqr.Database.Database;
import com.example.bestqr.Database.EntryExist;
import com.example.bestqr.models.Comment;
import com.example.bestqr.models.Location;
import com.example.bestqr.models.Profile;
import com.example.bestqr.models.QRCODE;
import com.example.bestqr.ui.leaderboard.LeaderboardViewModel;
import com.example.bestqr.ui.qr.QrViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.databinding.ActivityMainBinding;

import com.google.firebase.FirebaseApp;
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
    private static final int SCAN_IMAGE = 49374;
    private static final int PICK_IMAGE = 1;
    private QRCODE qr;
    private String contents;
    private boolean toEnter;
    private boolean seeProfile;
    private Profile profile;
    private Owner owner;
    private int score;
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

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
    private LeaderboardViewModel leaderboardViewModel;
    private QrViewModel QrViewModel;
    private Database db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(this);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        com.example.bestqr.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // This should be in th login activity
        // get unique device id
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // test identification of user ideally info will be taken in the signup activity and stored in firebase
        QRCODE userIdentification = new QRCODE(androidId);
        owner = new Owner();
        Database.registerOwner(owner);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        this.db = new Database();


        profile = Database.getUser(androidId);
        userViewModel.setUserProfile(profile);

    }


    public void scanButton(View v) {
        //initialize intent integrator
        IntentIntegrator intentIntegrator = new IntentIntegrator(CameraActivity.this);
        //locked orientation
        intentIntegrator.setOrientationLocked(true);
        //Set capture activity
        intentIntegrator.setCaptureActivity(Capture.class);
        //Initiate scan
        intentIntegrator.initiateScan();
    }


    public void EnterProfileButton(View v) {
        //initialize intent integrator
        IntentIntegrator intentIntegrator = new IntentIntegrator(CameraActivity.this);
        //locked orientation
        intentIntegrator.setOrientationLocked(true);
        //Set capture activity
        intentIntegrator.setCaptureActivity(Capture.class);
        //Initiate scan
        intentIntegrator.initiateScan();
        toEnter = true;
    }


    public void SeeProfileButton(View v) {
        //initialize intent integrator
        IntentIntegrator intentIntegrator = new IntentIntegrator(CameraActivity.this);
        //locked orientation
        intentIntegrator.setOrientationLocked(true);
        //Set capture activity
        intentIntegrator.setCaptureActivity(Capture.class);
        //Initiate scan
        intentIntegrator.initiateScan();
        seeProfile = true;
    }

    public void openGallery(View v) {
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
                requestCode, resultCode, data
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
                    int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    MultiFormatReader reader = new MultiFormatReader();
                    Result result = reader.decode(bitmap);
                    contents = result.getText();

                    // Create new QR object using contents as argument
                    qr = new QRCODE(contents);
                    userViewModel.setSelectedQrcode(qr);
                    locationPrompt.newInstance().show(getSupportFragmentManager(), "NEW QRCODE");

                    // Display toast showing QR hash
                    //When result content is not null
                    //Initialize alert dialog

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (FileNotFoundException e) {

                e.printStackTrace();

                Toast.makeText(CameraActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                // If user does not select image
            } catch (NullPointerException e) {
                e.printStackTrace();

                Toast.makeText(CameraActivity.this, "Image not selected or something went wrong", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == SCAN_IMAGE) {
            //Check condition
            if (intentResult.getContents() != null) {
                //When result content is not null
                // Create new QR object
                contents = intentResult.getContents();

                // handle switching profiles
                if (toEnter == true) {
                    try {
                        if (EntryExist.isRegistered(contents)) {
                            profile = Database.getUser(contents);
                            userViewModel.setUserProfile(profile);
                            Toast.makeText(this, "Welcome to the Profile of " + profile.getUserName(), Toast.LENGTH_SHORT).show();
                            userViewModel.setOwner(null);
                            toEnter = false;
                        } else if (Database.isOwner(contents)) {
                            owner.setAndroidId(contents);
                            userViewModel.setOwner(Database.getOwner(owner));
                            userViewModel.setUserProfile(owner);
                        } else {
                            
                            Toast.makeText(this, "Unsuccessful. Invalid Code", Toast.LENGTH_SHORT).show();
                            toEnter = false;
                            userViewModel.setOwner(null);

                    }
                    } catch (Exception exception) {
                        toEnter = false;
                    }

                // handle viewing profile
                } else if (seeProfile == true) {
                    try {
                        //go to guest-profile fragment
                        if (Database.userNameExist(contents)) {
                            String guestId = Database.getAndroidIdByName(contents);
                            Profile guestProfile = Database.getUser(guestId);
                            userViewModel.setGuestProfile(guestProfile);
                            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
                            navController.navigate(R.id.action_navigation_home_to_navigation_guest_user);
                            seeProfile = false;

                        } else {
                            Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "Unsuccessful. Invalid Code", Toast.LENGTH_SHORT).show();
                            seeProfile = false;
                        }
                    } catch (Exception exception) {
                        seeProfile = false;
                    }


                }else {
                    if (intentResult.getContents() != null) {
                        qr = new QRCODE(contents);
                        userViewModel.setSelectedQrcode(qr);
                        locationPrompt.newInstance().show(getSupportFragmentManager(), "NEW QRCODE");

                    } else {
                        //When result content is null
                        //Display toast
                        Toast.makeText(getApplicationContext()
                                , "sorry, nothing is scanned", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

            }
        } else {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");

            qr.setObjectImage(Bitmap.createScaledBitmap(captureImage, 300, 300, false));
        }
    }

    /**
     * This method converts the a bytes representation to a hexadecimal string
     *
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

    public static Set<Integer> getTopLevelDestinations() {
        return topLevelDestinations;
    }

    @Override
    public void onOkPressed() {
        userViewModel.setSelectedQrcode(null);
    }
}








package com.example.bestqr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.databinding.ActivityMainBinding;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CameraActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    // Define the pic id
    private static final int pic_id = 123;

    // Define the button and imageview type variable
    Button scanButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Manually set the action bar to the toolbar specified in the layout
        // So we can use a custom toolbar view instead of the default ActionBar
        setSupportActionBar(binding.toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_leaderboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // This should be in th login activity
        // get unique device id
        String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        // test identification of user ideally info will be taken in the signup activity and stored in firebase
        QR_CODE userIdentification = new QR_CODE(androidId);
        Profile userProfile = new Profile("UserName",userIdentification,1231231231,"emailaddress");
        //ToDo Store profiles in firebase

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );
        //Check condition
        if (intentResult.getContents()!= null){
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
        }else{
            //When result content is null
            //Display toast
            Toast.makeText(getApplicationContext()
                    ,"sorry, nothing is scanned", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
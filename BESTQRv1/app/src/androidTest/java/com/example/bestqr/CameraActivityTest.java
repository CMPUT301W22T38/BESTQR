package com.example.bestqr;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.database.FirebaseDatabase;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CameraActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<CameraActivity> rule=
            new ActivityTestRule<>(CameraActivity.class, true, true);

    @Before
    public void setUp()throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }
    @Test
    public void start()throws Exception{
        Activity activity = rule.getActivity();
    }
    /**
     * this tests the ScanButton
     */
    @Test
    public void CameraTest(){
        solo.assertCurrentActivity("Wrong activity", CameraActivity.class);
        solo.clickOnButton("Scan");

    }
    @Test
    public void CameraEnterProfile(){
        solo.assertCurrentActivity("Wrong activity", CameraActivity.class);
        solo.clickOnButton("ENTER PROFILE");

    }
    @Test
    public void CameraSeeProfile(){
        solo.assertCurrentActivity("Wrong activity", CameraActivity.class);
        solo.clickOnButton("SEE PROFILE");
    }

    @Test
    public void LeaderBordTest(){
        solo.assertCurrentActivity("Wrong activity", CameraActivity.class);
        solo.clickOnActionBarItem(R.id.navigation_leaderboard);
        //solo.waitForFragmentByTag("Leaderboard", 2000);
    }


    @After
    public void tearDown()throws Exception{
        solo.finishOpenedActivities();
    }
}
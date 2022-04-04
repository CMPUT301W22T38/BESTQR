package com.example.bestqr;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.chrono.IsoChronology;

public class MapTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<CameraActivity> rule=
            new ActivityTestRule<>(CameraActivity.class, true, true);

    @Before
    public void setUp()throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }
    @Test
    public void start()throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkMap(){
        solo.assertCurrentActivity("WrongActivity", CameraActivity.class);
        solo.clickOnImage(0);
        solo.sleep(2000);
        // clear lat and lon
        solo.clearEditText((EditText) solo.getView(R.id.editLatitude));
        solo.clearEditText((EditText) solo.getView(R.id.editLongitude));
        // enter lat and lon
        solo.enterText((EditText) solo.getView(R.id.editLatitude), "53.522929");
        solo.enterText((EditText) solo.getView(R.id.editLongitude), "-113.526256");
        // search the position (University of Alberta)
        solo.clickOnButton("Search");
        solo.sleep(5000);
        // clear lat and lon
        solo.clearEditText((EditText) solo.getView(R.id.editLatitude));
        solo.clearEditText((EditText) solo.getView(R.id.editLongitude));
        solo.sleep(2000);
        // enter lat and lon
        solo.enterText((EditText) solo.getView(R.id.editLatitude), "35.6815");
        solo.enterText((EditText) solo.getView(R.id.editLongitude), "139.76696");
        // search the position (Tokyo station)
        solo.clickOnButton("Search");
        solo.sleep(5000);



    }
}

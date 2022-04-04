package com.example.bestqr;

import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bestqr.ui.leaderboard.LeaderboardFragment;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LeaderboardTest {
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
    public void LeaderboardTest(){
        solo.assertCurrentActivity("Wrong activity", CameraActivity.class);
        solo.clickOnActionBarItem(R.id.navigation_leaderboard);
        CameraActivity cameraActivity = (CameraActivity) solo.getCurrentActivity();
        solo.waitForFragmentByTag("Leaderboard", 2000);
    }
}

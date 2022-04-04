package com.example.bestqr;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.security.auth.callback.PasswordCallback;

public class UserboardTest {
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
    /**
     * This will tests the personal information changes.
     */
    public void checkUserInfo(){
        solo.assertCurrentActivity("WrongActivity", CameraActivity.class);
        solo.clickOnActionBarItem(R.id.navigation_user);
        CameraActivity cameraActivity = (CameraActivity) solo.getCurrentActivity();
        solo.waitForFragmentByTag("User", 2000);
        solo.clickOnImage(3);// click user Tab
        solo.clickOnImageButton(3); // click userinfo

        //delete default user info
        solo.clearEditText((EditText) solo.getView(R.id.user_info_username));
        solo.clearEditText((EditText) solo.getView(R.id.user_info_email));
        solo.clearEditText((EditText) solo.getView(R.id.user_info_phone_number));
        solo.sleep(2000);

        //enter user info
        solo.enterText((EditText) solo.getView(R.id.user_info_username), "Name");
        solo.enterText((EditText) solo.getView(R.id.user_info_email), "email");
        solo.enterText((EditText) solo.getView(R.id.user_info_phone_number), "123456789");
        solo.sleep(2000);

        assertTrue(solo.waitForText("Name", 1, 2000));
        assertTrue(solo.waitForText("email", 1, 2000));
        assertTrue(solo.waitForText("123456789", 1, 2000));

        // Check that the name on the leaderboard has changed.
        solo.clickOnImage(2); // go to leaderboard
        assertTrue(solo.searchText("Name"));
    }

}

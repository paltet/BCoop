package com.bcoop.bcoop.configurarPerfil;




import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.bcoop.bcoop.MainActivity;
import com.bcoop.bcoop.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class canviarUsername {

    private String user;
    private String mail;
    private String password;

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initializeData(){
        Intents.init();
        user = "newUserName";
        mail = "delete@mail.com";
        password = "12345678";

    }

    @Test
    public void executeTest() throws InterruptedException {
        

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(R.string.user_settings)).perform(click());
        onView(withId(R.id.usernameButton)).perform(click());
        onView(withId(R.id.newUsernameForm)).perform(typeText(user), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.confirmButton)).perform(click());
        onView(withText(user)).check(matches(isDisplayed()));

    }


}

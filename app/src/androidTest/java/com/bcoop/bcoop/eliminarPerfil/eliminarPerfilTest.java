package com.bcoop.bcoop.eliminarPerfil;




import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;


import com.bcoop.bcoop.MainActivity;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ToastMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class eliminarPerfilTest {

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
        user = "delete";
        mail = "delete@mail.com";
        password = "12345678";

    }

    @Test
    public void executeTest() throws InterruptedException {


        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.register_username)).perform(typeText(user), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register_email)).perform(typeText(mail), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register_password)).perform(typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register_confirm_password)).perform(typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        onView(withText("Register successful!")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        onView(withId(R.id.acceptButton)).perform(click());
        onView(withId(R.id.remindMeLaterButton)).perform(click());
        onView(withId(R.id.remindMeLaterButton)).perform(click());
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(R.string.user_settings)).perform(click());
        onView(withId(R.id.deleteUserButton)).perform(click());
        onView(withId(R.id.newUsernameForm)).perform(typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.confirmButton)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.mail)).perform(typeText(mail), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        onView(withText("Error ! There is no user record corresponding to this identifier. The user may have been deleted.")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

    }
}

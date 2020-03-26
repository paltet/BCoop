package com.bcoop.bcoop.loginTest;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.bcoop.bcoop.MainActivity;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ToastMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class loginCorrect {

    private String mail;
    private String password;
    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initializeData(){
        Intents.init();
        mail = "xx@gmail.com";
        password = "12345678";
    }

    @Test
    public void executeTest() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.mail)).perform(typeText(mail), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        onView(withText("Login successful!")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        //intended(hasComponent(HomeActivity.class.getName()));
    }

}

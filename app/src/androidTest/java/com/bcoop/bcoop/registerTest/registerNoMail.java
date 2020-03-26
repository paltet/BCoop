package com.bcoop.bcoop.registerTest;

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



import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class registerNoMail {

    private String user;
    private String password;



    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initializeData(){
        Intents.init();
        user = "bbbb";
        password = "123456";


    }

    @Test
    public void executeTest() throws InterruptedException {


        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.register_username)).perform(typeText(user), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register_password)).perform(typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register_confirm_password)).perform(typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.register_email)).check(matches(hasErrorText("Please enter a valid email")));
    }
}

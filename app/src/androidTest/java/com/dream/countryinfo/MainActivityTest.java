package com.dream.countryinfo;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.dream.countryinfo.feature.country.activity.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by lixingming on 12/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public MainActivityTest() {
        super();
    }

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void testSearchCountry() {
        Matcher<View> matcher = withId(R.id.searchView);
        ViewInteraction viewInteraction = onView(matcher);
        viewInteraction.perform(ViewActions.typeText("chi"));

//        onData(allOf(is(instanceOf(String.class)), is("China"))).perform(click());
//        onData(hasToString(containsString("China"))).perform(click());
    }
}

package com.dream.countryinfo;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.dream.countryinfo.feature.country.activity.MainActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by lixingming on 12/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private IdlingResource resource;

    public MainActivityTest() {
        super();
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSearchCountry() {
        Matcher<View> matcher = withId(R.id.searchView);
        ViewInteraction viewInteraction = onView(matcher);
        viewInteraction.perform(ViewActions.click());

        viewInteraction.perform(ViewActions.typeText("chi"));

        onView(allOf(withText("China"))).perform(ViewActions.click());
    }
}

package com.dream.countryinfo;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.dream.countryinfo.feature.country.activity.MainActivity;
import com.dream.countryinfo.network.CountryApiHelper;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
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

    @Before
    public void before() {
        IdlingResource resource = OkHttp3IdlingResource.create("OkHttp", CountryApiHelper.getSingleton().getOkHttpClient());
        IdlingRegistry.getInstance().register(resource);
    }

    @Test
    public void testSearchCountry() {
        Matcher<View> matcher = withId(R.id.searchView);
        ViewInteraction viewInteraction = onView(matcher);
        viewInteraction.perform(ViewActions.typeText("chi"));

        onData(allOf(is(instanceOf(TextView.class)), is(withText("China")))).perform(click());
//        onData(hasToString(containsString("China"))).perform(click());
    }
}

package com.dream.countryinfo;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dream.countryinfo.feature.country.activity.CountryDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by lixingming on 12/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class CountryDetailActivityTest {

    @Rule
    public ActivityTestRule<CountryDetailActivity> mActivityRule = new ActivityTestRule<>(CountryDetailActivity.class,
            true, false);

//    @Before
//    public void before() {
//
//    }

    @Test
    public void testCountryDetail() {
        Intent intent = new Intent();
        intent.putExtra(CountryDetailActivity.KEY_COUNTRY_NAME, "China");
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.tv_name)).check(matches(withText("Name: China")));
    }

}

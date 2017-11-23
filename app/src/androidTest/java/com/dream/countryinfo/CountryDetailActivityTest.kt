package com.dream.countryinfo

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import com.dream.countryinfo.feature.country.activity.CountryDetailActivity

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText

/**
 * Created by lixingming on 12/11/2017.
 */

@RunWith(AndroidJUnit4::class)
class CountryDetailActivityTest {

    @Rule
    var mActivityRule = ActivityTestRule(CountryDetailActivity::class.java,
            true, false)

    //    @Before
    //    public void before() {
    //
    //    }

    @Test
    fun testCountryDetail() {
        val intent = Intent()
        intent.putExtra(CountryDetailActivity.KEY_COUNTRY_NAME, "China")
        mActivityRule.launchActivity(intent)
        onView(withId(R.id.tv_name)).check(matches(withText("Name: China")))
    }

}

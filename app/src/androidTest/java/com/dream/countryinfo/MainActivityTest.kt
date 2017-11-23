package com.dream.countryinfo

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dream.countryinfo.feature.country.activity.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by lixingming on 12/11/2017.
 */

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val resource: IdlingResource? = null

    @Rule
    var mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testSearchCountry() {
        val matcher = withId(R.id.searchView)
        val viewInteraction = onView(matcher)
        viewInteraction.perform(ViewActions.click())

        viewInteraction.perform(ViewActions.typeText("chi"))

        onView(allOf(withText("China"))).perform(ViewActions.click())
    }
}

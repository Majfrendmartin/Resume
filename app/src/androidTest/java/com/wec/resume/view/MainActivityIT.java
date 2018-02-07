package com.wec.resume.view;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wec.resume.R;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Pawel Raciborski on 02.02.2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityIT {

    private static final String GITHUB_URL = "https://github.com/Majfrendmartin";
    private static final String LINKED_IN_URL = "https://www.linkedin.com/in/pawelraciborski";

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void cantNavigateToLinkedInPageBeforeSelectingSocialButton() throws Exception {
        testNavigationToSocialPageOnButtonClicked(R.id.fab_linkedin, LINKED_IN_URL, false);
    }

    @Test
    public void navigateToLinkedInPage() throws Exception {
        testNavigationToSocialPageOnButtonClicked(R.id.fab_linkedin, LINKED_IN_URL, true);
    }

    @Test
    public void cantNavigateToGithubPageBeforeSelectingSocialButton() throws Exception {
        testNavigationToSocialPageOnButtonClicked(R.id.fab_github, GITHUB_URL, false);
    }

    @Test
    public void navigateToGithubPage() throws Exception {
        testNavigationToSocialPageOnButtonClicked(R.id.fab_github, GITHUB_URL, true);
    }

    private void testNavigationToSocialPageOnButtonClicked(@IdRes int buttonId, String url, boolean shouldNavigationAppear) {
        Intents.init();

        final Matcher<Intent> expectedIntent = allOf(IntentMatchers.hasAction(Intent.ACTION_VIEW), hasData(url));
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));

        if (shouldNavigationAppear) {
            onView(withId(R.id.fab)).perform(click());
        }

        onView(withId(buttonId)).perform(click());

        intended(expectedIntent, times(shouldNavigationAppear ? 1 : 0));
        Intents.release();
    }

    @Test
    public void splashScreenDisplayed() throws Exception {
        onView(withId(R.id.iv_splash_screen)).check(matches(isDisplayed()));
        Thread.sleep(2000);
        onView(withId(R.id.iv_splash_screen)).check(matches(not(isDisplayed())));
    }

    @Test
    public void expandedToolbarVisible() throws Exception {
        onView(withId(R.id.app_bar_layout)).check(matches(isCompletelyDisplayed()));
    }
}
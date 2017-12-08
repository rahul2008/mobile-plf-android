/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utilstest;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.philips.platform.uid.actions.ActionSetText;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.test.BuildConfig;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.utils.UIDResources;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static com.philips.platform.uid.matcher.ViewPropertiesMatchers.isVisible;
import static com.philips.platform.uid.utils.UIDTestUtils.waitFor;

@RunWith(AndroidJUnit4.class)
public class LanguagePackTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources testResources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        IdlingResource idlingResource = activity.getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
        testResources = activity.getResources();
    }

    @Test
    public void verifyUIDResource() {
        if (BuildConfig.DEBUG && !(testResources instanceof UIDResources)) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifySetEmptyPath() {
        if (BuildConfig.DEBUG && !("Taalpakket label".equals(testResources.getText(com.philips.platform.uid.test.R.string.language_pack_label)))) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyGetText() {
        String path = "";
        UIDLocaleHelper uidLocaleHelper = UIDLocaleHelper.getInstance();
        uidLocaleHelper.setFilePath(path);
        if (BuildConfig.DEBUG && !(uidLocaleHelper.isLookUp() == false && uidLocaleHelper.lookUpString("abc") == null)) {
            throw new AssertionError();
        }
    }


    @Test
    public void verifySetTitle() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UIDHelper.setTitle(activity, "ABCD");

            }
        });
        Espresso.onView(ViewMatchers.withId(com.philips.platform.uid.R.id.uid_toolbar_title)).check(matches(TextViewPropertiesMatchers.hasSameText("ABCD")));
    }

    @Test
    public void verifySetTitleWithRes() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UIDHelper.setTitle(activity, com.philips.platform.uid.test.R.string.search_menu_title);

            }
        });
        Espresso.onView(ViewMatchers.withId(com.philips.platform.uid.R.id.uid_toolbar_title)).check(matches(TextViewPropertiesMatchers.hasSameText("Search")));
    }

    @After
    public void tearDown() {
        UIDLocaleHelper.getInstance().setFilePath(activity.getCatalogAppJSONAssetPath());
    }

}

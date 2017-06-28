/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utilstest;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.test.BuildConfig;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.utils.UIDResources;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_for_utils);
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

    @After
    public void tearDown() {
        UIDLocaleHelper.getInstance().setFilePath(activity.getCatalogAppJSONAssetPath());
    }

}

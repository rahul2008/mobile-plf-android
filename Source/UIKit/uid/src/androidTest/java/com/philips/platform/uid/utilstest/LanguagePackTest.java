package com.philips.platform.uid.utilstest;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.test.BuildConfig;
import com.philips.platform.uid.utils.UIDResources;
import com.philips.platform.uid.view.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LanguagePackTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources testResources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_language_pack);
        testResources = activity.getResources();
    }

    @Test
    public void verifyUIDResource() {
        if (BuildConfig.DEBUG && !(testResources instanceof UIDResources)) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyGetText() {
        if (BuildConfig.DEBUG && !("Taalpakket label".equals(testResources.getText(com.philips.platform.uid.test.R.string.language_pack_label)))) {
            throw new AssertionError();
        }
    }
}

package com.philips.platform.uid.components.socialicons;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;

import org.junit.Rule;
import org.junit.Test;

import static com.philips.platform.uid.activity.BaseTestActivity.CONTENT_COLOR_KEY;

public class SocialIcons {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    Resources resources;
    private static final int ULTRA_LIGHT = 0;
    private static final int VERY_LIGHT = 1;
    private static final int LIGHT = 2;
    private static final int BRIGHT = 3;
    private static final int VERY_DARK = 4;

    /***********************
     * Layout scenarios
     ***************************/

    @Test
    public void verifySocialIconHeight() {

    }

    @Test
    public void verifySocialIconWidth() {

    }

    @Test
    public void verifySocialIconCornerRadius() {

    }

    /***********************
     * Theming scenarios
     ***************************/

    @Test
    public void verifySocialMediaPrimaryButtonFillULTone() {

    }

    @Test
    public void verifySocialMediaPrimaryButtonFillLightTone() {

    }

    @Test
    public void verifySocialMediaPrimaryButtonFillVDTone() {

    }

    @Test
    public void verifySocialMediaPrimaryButtonIconFill() {

    }

    @Test
    public void verifySocialMediaPrimaryButtonPressedFillColorULTone() {

    }

    @Test
    public void verifySocialMediaPrimaryButtonPressedFillColorLightTone() {

    }

    @Test
    public void verifySocialMediaPrimaryButtonPressedFillColorVDTone() {

    }

    @Test
    public void verifySocialMediaWhiteButtonFillLightBrightVDTone() {

    }

    @Test
    public void verifySocialMediaWhiteButtonIconFillLightTone() {

    }

    @Test
    public void verifySocialMediaWhiteButtonIconFillBrightTone() {

    }

    @Test
    public void verifySocialMediaWhiteButtonIconFillVDTone() {

    }

    @Test
    public void verifySocialMediaWhiteButtonPressedFillLightBrightVDTone() {

    }

    @NonNull
    private Intent getIntent(final int contentColorIndex) {
        final Bundle bundleExtra = new Bundle();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        bundleExtra.putInt(CONTENT_COLOR_KEY, contentColorIndex);
        intent.putExtras(bundleExtra);
        return intent;
    }
}
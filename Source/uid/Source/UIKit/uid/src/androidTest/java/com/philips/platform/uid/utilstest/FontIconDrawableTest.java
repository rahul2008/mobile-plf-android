/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utilstest;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.test.rule.ActivityTestRule;
import android.widget.ImageView;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.utils.UIDTestUtils.waitFor;

public class FontIconDrawableTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources testResources;
    private BaseTestActivity activity;
    String string = "ABC";
    ImageView imageView;
    FontIconDrawable fontIconDrawable;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_for_utils);
        testResources = activity.getResources();
        waitFor(testResources, 500);
        imageView = (ImageView) activity.findViewById(com.philips.platform.uid.test.R.id.image_view);
    }

    public FontIconDrawable getActionBarSizeFontIcon(){
        fontIconDrawable = new FontIconDrawable(activity, string, TypefaceUtils.load(activity.getAssets(), TestConstants.FONT_PATH_CS_MEDIUM));
        fontIconDrawable = fontIconDrawable.actionBarSize().colorRes(com.philips.platform.uid.test.R.color.uidColorBlack).alpha(100);
        return fontIconDrawable;
    }

    public FontIconDrawable getNormalFontIcon(){
        fontIconDrawable = new FontIconDrawable(activity, string, TypefaceUtils.load(activity.getAssets(), TestConstants.FONT_PATH_CS_MEDIUM));
        fontIconDrawable.setAlpha(100);
        fontIconDrawable.color(Color.BLACK);
        fontIconDrawable.sizeRes(com.philips.platform.uid.test.R.dimen.iconbutton_height);
        return fontIconDrawable;
    }

    @Test
    public void verifyActionBarFontIconDrawbleSize(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageDrawable(getActionBarSizeFontIcon());
            }
        });
        int expected = activity.getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.icon_height);
        onView(withId(com.philips.platform.uid.test.R.id.image_view)).check(matches(ViewPropertiesMatchers.isSameViewHeight(expected)));
    }

    @Test
    public void verifyNormalFontIconDrawbleSize(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageDrawable(getNormalFontIcon());
            }
        });
        int expected = activity.getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconbutton_height);
        onView(withId(com.philips.platform.uid.test.R.id.image_view)).check(matches(ViewPropertiesMatchers.isSameViewHeight(expected)));
    }

    @Test
    public void verifyFontIconMutate(){
        final FontIconDrawable drawable = (FontIconDrawable) getNormalFontIcon().colorStateList(ColorStateList.valueOf(Color.BLACK)).mutate();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageDrawable(drawable);
            }
        });
        int expected = activity.getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconbutton_height);
        onView(withId(com.philips.platform.uid.test.R.id.image_view)).check(matches(ViewPropertiesMatchers.isSameViewHeight(expected)));
    }

    @Test
    public void verifyFontIconSaveRestoreState(){
        Parcelable parcelable = getNormalFontIcon().onSaveInstanceState();
        final FontIconDrawable drawable = new FontIconDrawable(activity, string, TypefaceUtils.load(activity.getAssets(), TestConstants.FONT_PATH_CS_MEDIUM));
        drawable.onRestoreInstanceState(parcelable);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageDrawable(drawable);
            }
        });
        int expected = activity.getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconbutton_height);
        onView(withId(com.philips.platform.uid.test.R.id.image_view)).check(matches(ViewPropertiesMatchers.isSameViewHeight(expected)));
    }


}

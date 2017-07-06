package com.philips.platform.uid.utilstest;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.VectorDrawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.test.BuildConfig;
import com.philips.platform.uid.text.utils.UIDSpans;
import com.philips.platform.uid.text.utils.UIDStringUtils;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.philips.platform.uid.utils.UIDTestUtils.waitFor;

public class UIDUtilsTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources testResources;
    private BaseTestActivity activity;
    CharSequence str = "Hello World";
    CharSequence subStr = "Hello";
    SpannableString spannableString = new SpannableString("Hello World");
    SpannableString spannableSubString = new SpannableString("Hello");
    Toolbar toolbar;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_for_utils);
        testResources = activity.getResources();
    }

//    Text utils test cases

    @Test
    public void verifyBoldSubstring(){
        if (BuildConfig.DEBUG && !(UIDSpans.boldSubString(true,activity, str, subStr) instanceof SpannableString)) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyDoesNotBoldEmptyString(){
        boolean expected = false;
        if (BuildConfig.DEBUG && !((UIDSpans.boldSubString(true,activity, "", "") instanceof SpannableString) == expected)) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyDoesNotBoldOtherString(){
        boolean expected = false;
        if (BuildConfig.DEBUG && !((UIDSpans.boldSubString(true,activity, str, "xyz") instanceof SpannableString) == expected)) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifySubstringLessThanString(){
        boolean expected = false;
        if (BuildConfig.DEBUG && !(UIDStringUtils.regionMatches(true, subStr, 0, spannableString, 0, 2) == expected)) {
            throw new AssertionError();
        }
    }


    @Test
    public void verifyInvalidStartPoint(){
        boolean expected = false;
        if (BuildConfig.DEBUG && !(UIDStringUtils.regionMatches(true, spannableString, -1, subStr, -1, subStr.length()) == expected)) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyInvalidLength(){
        boolean expected = false;
        if (BuildConfig.DEBUG && !(UIDStringUtils.regionMatches(true, spannableString, 0, subStr, 0, 15) == expected)) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyHasSubString(){
        if (BuildConfig.DEBUG && !(UIDStringUtils.regionMatches(true, spannableString, 0, spannableSubString, 0, spannableSubString.length()))) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyCaseSensitiveSubString(){
        SpannableString spannable = new SpannableString("hello");
        if (BuildConfig.DEBUG && !(UIDStringUtils.regionMatches(true, spannableString, 0, spannable, 0, spannable.length()))) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifySubStringIgnoreCase(){
        SpannableString spannable = new SpannableString("xyz");
        boolean expected = false;
        if (BuildConfig.DEBUG && !(UIDStringUtils.regionMatches(false, spannableString, 0, spannable, 0, spannable.length()) == expected)) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyHideNavigationIcon(){
        waitFor(testResources, 500);
        toolbar = (Toolbar) activity.findViewById(com.philips.platform.uid.test.R.id.uid_toolbar);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbar.setNavigationIcon(com.philips.platform.uid.test.R.drawable.uid_ic_cross_icon);
                UIDNavigationIconToggler toggler = new UIDNavigationIconToggler(activity);
                toggler.hideNavigationIcon();
            }
        });
        if (BuildConfig.DEBUG && !(activity.getToolbar().getNavigationIcon() == null)) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyRestoreNavigationIcon(){
        waitFor(testResources, 500);
        toolbar = (Toolbar) activity.findViewById(com.philips.platform.uid.test.R.id.uid_toolbar);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbar.setNavigationIcon(com.philips.platform.uid.test.R.drawable.uid_ic_cross_icon);
                UIDNavigationIconToggler toggler = new UIDNavigationIconToggler(activity);
                toggler.hideNavigationIcon();
                toggler.restoreNavigationIcon();
            }
        });
        waitFor(testResources, 500);
        Object icon = activity.getToolbar().getNavigationIcon();
        if (BuildConfig.DEBUG && !(icon instanceof VectorDrawableCompat || icon instanceof VectorDrawable)) {
            throw new AssertionError();
        }
    }

}

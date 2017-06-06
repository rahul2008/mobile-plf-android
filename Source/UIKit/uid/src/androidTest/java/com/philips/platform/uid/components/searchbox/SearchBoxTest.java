/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.searchbox;


import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.NavigationColor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SearchBoxTest extends BaseTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources resources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(NavigationColor.BRIGHT.ordinal(), ColorRange.GROUP_BLUE.ordinal());
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_searchbox);
        resources = activity.getResources();
    }

    public ViewInteraction getBackIcon(){
        return onView(withId(com.philips.platform.uid.test.R.id.uid_search_back_button));
    }

    @Test
    public void verifyBackIconHeight(){
        int backIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getBackIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(backIconSize)));
    }

    @Test
    public void verifyBackIconWidth(){
        int backIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getBackIcon().check(matches(ViewPropertiesMatchers.isSameViewWidth(backIconSize)));
    }

    @Test
    public void verifyBackIconStartMargin(){
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_back_icon_margin);
        getBackIcon().check(matches(ViewPropertiesMatchers.isSameStartMargin(backIconStartMargin)));
    }

    @Test
    public void verifyBackIconEndMargin(){
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_padding_end);
        getBackIcon().check(matches(ViewPropertiesMatchers.isSameEndMargin(backIconStartMargin)));
    }


    public ViewInteraction getClearIcon(){
        return onView(withId(com.philips.platform.uid.test.R.id.uid_search_clear_layout));
    }

    @Test
    public void verifyClearIconHolderHeight(){
        int clearIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getClearIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(clearIconSize)));
    }


    @Test
    public void verifyClearIconHolderWidth(){
        int clearIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getClearIcon().check(matches(ViewPropertiesMatchers.isSameViewWidth(clearIconSize)));
    }

    @Test
    public void verifyClearIconStartMargin(){
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_padding);
        getClearIcon().check(matches(ViewPropertiesMatchers.isSameStartMargin(backIconStartMargin)));
    }

    @Test
    public void verifyClearIconEndMargin(){
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_padding);
        getClearIcon().check(matches(ViewPropertiesMatchers.isSameEndMargin(backIconStartMargin)));
    }

    public ViewInteraction getAutoCompleteTextView(){
        return onView(withId(com.philips.platform.uid.test.R.id.uid_search_src_text));
    }

    @Test
    public void verifyHintTextSize(){
        
    }

}

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
import com.philips.platform.uid.matcher.SearchBoxMatcher;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.TestConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
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

    public ViewInteraction getSearchBox(){
        return onView(withId(com.philips.platform.uid.test.R.id.test_search_box));
    }

    @Test
    public void verifySearchBoxHeight(){
        int searchBoxheight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_height);
        getSearchBox().check(matches(ViewPropertiesMatchers.isSameViewHeight(searchBoxheight)));
    }

    @Test
    public void verifyBackIconHeight(){
        int backIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getSearchBox().check(matches(SearchBoxMatcher.isSameBackIconHeight(backIconSize)));
    }

    @Test
    public void verifyBackIconWidth(){
        int backIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getSearchBox().check(matches(SearchBoxMatcher.isSameBackIconWidth(backIconSize)));
    }

    @Test
    public void verifyBackIconStartMargin(){
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_back_icon_margin);
        getSearchBox().check(matches(SearchBoxMatcher.isSameBackIconStartMargin(backIconStartMargin)));
    }

    @Test
    public void verifyBackIconEndMargin(){
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_padding_end);
        getSearchBox().check(matches(SearchBoxMatcher.isSameBackIconEndMargin(backIconStartMargin)));
    }

    @Test
    public void verifyClearIconHolderHeight(){
        int clearIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getSearchBox().check(matches(SearchBoxMatcher.isSameClearIconHolderHeight(clearIconSize)));
    }


    @Test
    public void verifyClearIconHolderWidth(){
        int clearIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getSearchBox().check(matches(SearchBoxMatcher.isSameClearIconHolderWidth(clearIconSize)));
    }

    @Test
    public void verifyClearIconStartMargin(){
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_padding);
        getSearchBox().check(matches(SearchBoxMatcher.isSameClearIconStartMargin(backIconStartMargin)));
    }

    @Test
    public void verifyClearIconEndMargin(){
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_padding);
        getSearchBox().check(matches(SearchBoxMatcher.isSameClearIconEndMargin(backIconStartMargin)));
    }

    @Test
    public void verifyAutoCompleteTextFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_font_size);
        getSearchBox().check(matches(SearchBoxMatcher.isSameTextSize((int) expectedFontSize)));
    }


    @Test
    public void verifyAutoCompleteTextTypeface() {
        getSearchBox().check(matches(SearchBoxMatcher.isSameTypeFace(activity, TestConstants.FONT_PATH_CS_MEDIUM)));
    }

    @Test
    public void verifyAutoCompleteTextColor() {

    }

    @Test
    public void verifyAutoCompleteHintTextColor() {

    }

    @Test
    public void verifyBackIconColor(){

    }

    @Test
    public void verifySearchBoxFillColor(){

    }


}

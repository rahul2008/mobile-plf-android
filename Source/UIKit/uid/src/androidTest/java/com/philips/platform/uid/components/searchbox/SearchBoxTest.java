/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.searchbox;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.philips.platform.uid.R;
import com.philips.platform.uid.actions.ActionSetText;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.components.separator.ComponentListFragment;
import com.philips.platform.uid.matcher.SearchBoxMatcher;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.SearchBox;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.matcher.ViewPropertiesMatchers.isVisible;

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
        IdlingResource idlingResource = activity.getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
        resources = activity.getResources();

    }

    public ViewInteraction getSearchBox() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_search_box)).perform(click());
    }

    @Test
    public void verifySearchBoxHeight() {
        int searchBoxheight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_height);
        getSearchBox().check(matches(ViewPropertiesMatchers.isSameViewHeight(searchBoxheight)));
    }

    @Test
    public void verifyBackIconHeight() {
        int backIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getSearchBox().check(matches(SearchBoxMatcher.isSameBackIconHeight(backIconSize)));
    }

    @Test
    public void verifyBackIconWidth() {
        int backIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getSearchBox().check(matches(SearchBoxMatcher.isSameBackIconWidth(backIconSize)));
    }

    @Test
    public void verifyBackIconStartMargin() {
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_back_icon_margin);
        getSearchBox().check(matches(SearchBoxMatcher.isSameBackIconStartMargin(backIconStartMargin)));
    }

    @Test
    public void verifyInputBoxStartMargin() {
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_padding_end);
        getSearchBox().check(matches(SearchBoxMatcher.isSameBackIconEndMargin(backIconStartMargin)));
    }

    @Test
    public void verifyClearIconHolderHeight() {
        int clearIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getSearchBox().check(matches(SearchBoxMatcher.isSameClearIconHolderHeight(clearIconSize)));
    }


    @Test
    public void verifyClearIconHolderWidth() {
        int clearIconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_dimen);
        getSearchBox().check(matches(SearchBoxMatcher.isSameClearIconHolderWidth(clearIconSize)));
    }

    @Test
    public void verifyClearIconStartMargin() {
        int backIconStartMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.searchbox_button_padding);
        getSearchBox().check(matches(SearchBoxMatcher.isSameClearIconStartMargin(backIconStartMargin)));
    }

    @Test
    public void verifyClearIconEndMargin() {
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
        int expectedTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidSearchBoxAndroidDefaultNormalInputTextColor);
        getSearchBox().check(matches(SearchBoxMatcher.isSameTextColor(expectedTextColor)));
    }

    @Test
    public void verifyAutoCompleteHintTextColor() {
        int expectedHintTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidSearchBoxAndroidDefaultNormalHintTextColor);
        getSearchBox().check(matches(SearchBoxMatcher.isSameHintTextColor(expectedHintTextColor)));
    }

    @Test
    public void verifySearchBoxFillColor() {
        int expectedFillColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidSearchBoxAndroidDefaultFocusInputBackgroundColor);
        getSearchBox().check(matches(SearchBoxMatcher.isSameFillColor(expectedFillColor)));
    }

    @Test
    public void verifySearchBoxBackButtonClose() {
        getSearchBox();
        onView(withId(com.philips.platform.uid.R.id.uid_search_back_button)).perform(click());
        onView(withId(R.id.uid_search_box_layout)).check(matches(isVisible(View.GONE)));
    }

    @Test
    public void verifySearchBoxIconHolderTrigger() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SearchBox searchBox = (SearchBox) activity.findViewById(com.philips.platform.uid.test.R.id.test_search_box);
                searchBox.setSearchIconified(true);
                searchBox.setDecoySearchViewHint(com.philips.platform.uid.test.R.string.search_menu_title);
                searchBox.setSearchBoxHint(com.philips.platform.uid.test.R.string.search_menu_title);

            }
        });
        onView(withId(com.philips.platform.uid.R.id.uid_search_icon_holder)).perform(click());
        onView(withId(R.id.uid_search_box_layout)).check(matches(isVisible(View.VISIBLE)));
    }

    @Test
    public void verifySearchBoxClearText() {
        getSearchBox();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SearchBox searchBox = (SearchBox) activity.findViewById(com.philips.platform.uid.test.R.id.test_search_box);
                searchBox.setSearchIconified(true);
                searchBox.setDecoySearchViewHint("mytest");
                searchBox.setSearchBoxHint("mytest");
                searchBox.setQuery("mytest");
            }
        });
        onView(withId(com.philips.platform.uid.R.id.uid_search_close_btn)).perform(click());
        onView(withId(R.id.uid_search_src_text)).check(matches(TextViewPropertiesMatchers.hasNoText()));
    }
}

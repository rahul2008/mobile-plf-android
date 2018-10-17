package com.philips.platform.uid.components.expander;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.view.widget.Expander;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class ExpanderTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private Expander expander;
    private BaseTestActivity activity;


    @Before
    public void setUp() {
        activity = activityTestRule.getActivity();
        activity.switchTo(R.layout.uid_expander);
        resources = getInstrumentation().getContext().getResources();


    }

    // start of Expander Title Panel test cases
    @Test
    public void verifyExpanderTitlePanelStartMargin() {
        int expectedStartMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_view_title_margin_start_end);
        getExpanderTitlePanel().check(matches(ViewPropertiesMatchers.isSameStartMargin(expectedStartMargin)));
    }

    @Test
    public void verifyExpanderTitlePanelEndMargin() {
        int expectedEndMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_view_title_margin_start_end);
        getExpanderTitlePanel().check(matches(ViewPropertiesMatchers.isSameEndMargin(expectedEndMargin)));
    }

    @Test
    public void verifyExpanderTitlePanelTopMargin() {
        int expectedTopMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_view_title_margin_top_bottom);
        getExpanderTitlePanel().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    @Test
    public void verifyExpanderTitlePanelBottomMargin() {
        int expectedBottomMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_view_title_margin_top_bottom);
        getExpanderTitlePanel().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expectedBottomMargin)));
    }



    // start of Expander content view test cases
  /*  @Test
    public void verifyExpanderContentViewWidth() {
        int expectedStartMargin = resources.getDimensionPixelSize(ViewGroup.LayoutParams.MATCH_PARENT);
        getExpanderContentView().check(matches(ViewPropertiesMatchers.isSameStartMargin(expectedStartMargin)));
    }*/

    @Test
    public void verifyExpanderContentViewStartMargin() {
        int expectedStartMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_view_content_margin_start_end);
        getExpanderContentView().check(matches(ViewPropertiesMatchers.isSameStartMargin(expectedStartMargin)));
    }

    @Test
    public void verifyExpanderContentViewEndMargin() {
        int expectedEndMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_view_content_margin_start_end);
        getExpanderContentView().check(matches(ViewPropertiesMatchers.isSameEndMargin(expectedEndMargin)));
    }

    @Test
    public void verifyExpanderContentViewTopMargin() {
        int expectedTopMargin = 0; // top margin of content is
        getExpanderContentView().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    @Test
    public void verifyExpanderTitleContentViewBottomMargin() {
        int expectedBottomMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_view_content_margin_bottom);
        getExpanderContentView().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expectedBottomMargin)));
    }



    @Test
    public void verifyExpanderTitlePanelBottomDividerViewHeight(){
        int expectedHeight = resources.getDimensionPixelSize(R.dimen.uid_divider_Height);
        getExpanderTitlePanelBottomDivider().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }


    @Test
    public void verifyExpanderContentViewBottomDividerViewHeight(){
        int expectedHeight = resources.getDimensionPixelSize(R.dimen.uid_divider_Height);
        getExpanderContentViewBottomDivider().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }


    // returns Relative layout which is ViewGroup for Title panel view
    private ViewInteraction getExpanderTitlePanel() {
        return onView(withId(com.philips.platform.uid.R.id.uid_expander_view_title));
    }

    // returns Relative layout which is ViewGroup for Content view
    private ViewInteraction getExpanderContentView() {
        return onView(withId(com.philips.platform.uid.R.id.uid_expander_view_content));
    }

    // returns divider view below Title panel
    private ViewInteraction getExpanderTitlePanelBottomDivider() {
        return onView(withId(com.philips.platform.uid.R.id.uid_expander_title_bottom_divider));
    }

    // returns divider view below content view
    private ViewInteraction getExpanderContentViewBottomDivider() {
        return onView(withId(com.philips.platform.uid.R.id.uid_expander_title_bottom_divider));
    }


}

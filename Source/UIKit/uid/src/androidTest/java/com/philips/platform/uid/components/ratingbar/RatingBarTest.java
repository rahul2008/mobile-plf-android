package com.philips.platform.uid.components.ratingbar;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.test.BuildConfig;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.RatingBar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class RatingBarTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private RatingBar ratingBar;
    private Activity activityContext;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_ratingbar);
        resources = getInstrumentation().getContext().getResources();
        ratingBar = new RatingBar(activity);
        activityContext = activity;
    }

    private ViewInteraction getInputRating() {
        return onView(withId(com.philips.platform.uid.test.R.id.rating_input));
    }

    private ViewInteraction getDisplayRating() {
        return onView(withId(com.philips.platform.uid.test.R.id.rating_display_default));
    }

    @Test
    public void verifyDisplayRatingBarPadding(){
        int expectedPadding = (int)ratingBar.getPaint().measureText("4.5") + resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.rating_bar_display_padding);
        getDisplayRating().check(matches(ViewPropertiesMatchers.isSameStartPadding(expectedPadding)));
    }

    @Test
    public void verifyInputRatingBarHeight() {
        int expectedHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.ratingbar_input_height);
        getInputRating().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyDisplayRatingBarHeight() {
        int expectedHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.ratingbar_display_height);
        getDisplayRating().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyDisplayRatingBarLabelSize(){
        float expectedTextSize = getInstrumentation().getContext().getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.rating_bar_text_size);
        UIDTestUtils.waitFor(resources,750);
        RatingBar displayRating = (RatingBar) activityContext.findViewById(com.philips.platform.uid.test.R.id.rating_display_default);
        if (BuildConfig.DEBUG && !(expectedTextSize==displayRating.getPaint().getTextSize())) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyDisplayRatingLabelColor(){
        int expectedColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidRatingBarDefaultNormalOnTextColor);
        UIDTestUtils.waitFor(resources,750);
        RatingBar displayRating = (RatingBar) activityContext.findViewById(com.philips.platform.uid.test.R.id.rating_display_default);
        if (BuildConfig.DEBUG && !(expectedColor==displayRating.getPaint().getColor())) {
            throw new AssertionError();
        }
    }

    @Test
    public void verifyDisplayRatingLabelFontType(){
        Typeface expectedTypeface = TypefaceUtils.load(activityContext.getAssets(), "fonts/centralesansmedium.ttf");
        UIDTestUtils.waitFor(resources,750);
        RatingBar displayRating = (RatingBar) activityContext.findViewById(com.philips.platform.uid.test.R.id.rating_display_default);
        if (BuildConfig.DEBUG && !(expectedTypeface==displayRating.getPaint().getTypeface())) {
            throw new AssertionError();
        }
    }
}

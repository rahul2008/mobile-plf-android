package com.tooltip;


import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tooltip.widget.DrawableMatcher;
import com.tooltip.widget.TextViewPropertiesMatchers;
import com.tooltip.widget.ViewPropertiesMatchers;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by 310230979 on 3/13/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    Context context;

    @Before
    public void setUp() {
        context = getInstrumentation().getTargetContext();
        getButtonEvent().perform(click());
    }

    //Checking Text string
    @Test
    public void textString() {
        getText().check(matches(withText("Something went wrong with different text :)")));
    }

    //Checking Text Font Size
    @Test
    public void textFontSize() {
        float expectedFontSize = context.getResources().getDimensionPixelSize(R.dimen.fontsize);
        getText().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    // check Text Color
    @Test
    public void textFontColor() {
        int color = ContextCompat.getColor(context, R.color.colorWhite);
        getText().check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }


    // Checking Background drawable
    @Test
    public void backgroundDrawable() {
        getLayout().check(matches(withDrawable(R.drawable.tooltip_test)));
    }

    // Checking error icon
    @Test
    public void checkErrorIcon() {
        getImage().check(matches(withDrawable(R.drawable.error)));
    }

    // Layout Top Margin
    @Test
    public void LayoutTopMargin() {
        int expectedEndMargin = context.getResources().getDimensionPixelSize(R.dimen.fab_margin);
        getLayout().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedEndMargin)));
    }

    @Test
    public void errorIconLeftPadding() {
        int expectedLeftPadding =  context.getResources().getDimensionPixelSize(R.dimen.paddingleft);
        getImage().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }


    private ViewInteraction getButtonEvent() {
        return onView(withId(R.id.anchor_bottom_view));
    }

    private ViewInteraction getText() {
        return onView(withId(R.id.tooltip_Bottom_text));
    }

    private ViewInteraction getImage() {
        return onView(withId(R.id.iv_icon));
    }

    private ViewInteraction getLayout() {
        return onView(withId(R.id.linearLayout));
    }

}
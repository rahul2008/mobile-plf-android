package com.tooltip;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by 310230979 on 3/13/2017.
 */
public class MainActivityToolTipTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Before

    private ViewInteraction gettopButton() {
        return onView(withId(com.tooltip.R.id.anchor_view2));
    }

    private ViewInteraction getBottomButton() {
        return onView(withId(com.tooltip.R.id.anchor_view));
    }

    private ViewInteraction getTexttop() {
        return onView(withId(com.tooltip.R.id.tooltip_top_text));
    }

    private ViewInteraction getTextbottom() {
        return onView(withId(com.tooltip.R.id.tooltip_Bottom_text));
    }

    @Test
    public void clicktopevent() {
        gettopButton().perform(click());
        getTexttop().check(matches(withText("Tooltip using PopupWindow :)")));
    }

    @Test
    public void clickbottomevent() {
        getBottomButton().perform(click());
        getTextbottom().check(matches(withText("Something went wrong with different text :)")));
    }
    /*@Test
    public void verifyAlertContentBottomMargin() {

        int expectedBottomMargin = testResources.getDimensionPixelSize(R.dimen.fab_margin);
        getBottomButton().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedBottomMargin)));
    }
    public static Matcher<View> isSameTopMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                setValues(lp.topMargin, expectedValue);
                return areEqual();
            }
        };
    }*/

}
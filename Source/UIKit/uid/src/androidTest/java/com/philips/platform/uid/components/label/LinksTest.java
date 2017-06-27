/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.label;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.text.Spannable;
import android.text.style.MetricAffectingSpan;
import android.text.style.URLSpan;

import com.philips.platform.uid.R;
import com.philips.platform.uid.actions.ActionDown;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.text.utils.UIDClickableSpanWrapper;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LinksTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private BaseTestActivity activity;

    @Before
    public void setUp() {
        activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_links);
    }

    @Test
    public void verifyNumOfUIDWrapperSpansAreTwo() {
        getLinksLabelInteraction().check(ViewAssertions.matches(isEnabled()));
        Spannable text = (Spannable) initLabelWithInterceptor().getText();
        UIDClickableSpanWrapper[] uidSpans = text.getSpans(0, text.length(), UIDClickableSpanWrapper.class);
        assertEquals(2, uidSpans.length);
    }

    @Test
    public void veriyTotalNumberOfURLAreTwo() {
        getLinksLabelInteraction().check(ViewAssertions.matches(isEnabled()));
        URLSpan[] urls = initLabelWithInterceptor().getUrls();
        assertEquals(2, urls.length);
    }

    //Verifies that we are not updating font size or typeface
    @Test
    public void verifyNumOfUIDWrapperSpansIsNotOfTypeMetricAffectingSpan() {
        getLinksLabelInteraction().check(ViewAssertions.matches(isEnabled()));
        assertFalse(getClickableSpan(0).getClass().isAssignableFrom(MetricAffectingSpan.class));
    }

    @Test
    public void verifySpanIsVisitedIfClicked() {
        getLinksLabelInteraction().check(ViewAssertions.matches(isEnabled()));
        initLabelWithInterceptor();
        getLinksLabelInteraction().perform(ViewActions.click());
        assertTrue(getClickableSpan(0).isVisited());
    }

    @Test
    public void verifySpanIsPressedOnTouchDown() {
        getLinksLabelInteraction().perform(new ActionDown());
        assertTrue(getClickableSpan(0).isPressed());
    }

    @Test
    public void verifySpanDefaultColorIsSameAsSpec() {
        getLinksLabelInteraction().perform(new ActionDown());
        int defColor = getClickableSpan(0).getColors().getColorForState(new int[]{android.R.attr.state_enabled}, android.R.color.white);
        int hyperLinkThemedNormalColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidHyperlinkDefaultNormalTextColor);
        assertEquals(defColor, hyperLinkThemedNormalColor);
    }

    @Test
    public void verifySpanPressedColorIsSameAsSpec() {
        getLinksLabelInteraction().perform(new ActionDown());
        int pressedColor = getClickableSpan(0).getColors().getColorForState(new int[]{android.R.attr.state_pressed}, android.R.color.white);
        int hyperLinkThemedPressedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidHyperlinkDefaultPressedTextColor);
        assertEquals(pressedColor, hyperLinkThemedPressedColor);
    }

    @Test
    public void verifySpanVisitedColorIsSameAsSpec() {
        initLabelWithInterceptor();
        getLinksLabelInteraction().perform(ViewActions.click());
        int visitedColor = getClickableSpan(0).getColors().getColorForState(new int[]{R.attr.uid_state_visited}, android.R.color.white);
        int hyperLinkThemedVisitedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidHyperlinkDefaultVisitedTextColor);
        assertEquals(visitedColor, hyperLinkThemedVisitedColor);
    }

    @Test
    public void verifySpanCustomDefaultColorIsSame() {
        initLabelWithInterceptor();
        getLinksLabelInteraction().perform(ViewActions.click());
        getClickableSpan(0).setColors(getColorList());
        int visitedColor = getClickableSpan(0).getColors().getColorForState(new int[]{android.R.attr.state_enabled}, android.R.color.white);
        assertEquals(Color.RED, visitedColor);
    }

    @Test
    public void verifySpanCustomNormalColorIsSame() {
        initLabelWithInterceptor();
        getClickableSpan(0).setColors(getColorList());
        int visitedColor = getClickableSpan(0).getColors().getColorForState(new int[]{android.R.attr.state_pressed}, android.R.color.white);
        assertEquals(Color.GREEN, visitedColor);
    }

    @Test
    public void verifySpanCustomVisitedColorIsSame() {
        initLabelWithInterceptor();
        getClickableSpan(0).setColors(getColorList());
        int visitedColor = getClickableSpan(0).getColors().getColorForState(new int[]{R.attr.uid_state_visited}, android.R.color.white);
        assertEquals(Color.BLUE, visitedColor);
    }

    private UIDClickableSpanWrapper getClickableSpan(int index) {
        Spannable text = (Spannable) initLabelWithInterceptor().getText();
        UIDClickableSpanWrapper[] uidSpans = text.getSpans(0, text.length(), UIDClickableSpanWrapper.class);
        return uidSpans[index];
    }

    private Label initLabelWithInterceptor() {
        getLinksLabelInteraction().check(ViewAssertions.matches(isEnabled()));
        final Label label = (Label) activity.findViewById(com.philips.platform.uid.test.R.id.links);
        label.setSpanClickInterceptor(clickInterceptor);
        return label;
    }

    private ViewInteraction getLinksLabelInteraction() {
        return onView(withId(com.philips.platform.uid.test.R.id.links));
    }

    UIDClickableSpanWrapper.ClickInterceptor clickInterceptor = new UIDClickableSpanWrapper.ClickInterceptor() {
        @Override
        public boolean interceptClick(CharSequence tag) {
            assertEquals("http://www.philips.com", String.valueOf(tag));
            return true;
        }
    };

    private ColorStateList getColorList() {
        int[][] states = new int[][]{{android.R.attr.state_enabled}, {android.R.attr.state_pressed}, {R.attr.uid_state_visited}};
        int[] colors = new int[]{Color.RED, Color.GREEN, Color.BLUE};
        return new ColorStateList(states, colors);
    }
}
package com.philips.platform.uid.components.expander;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.Expander;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIDExpanderListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ExpanderTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private Expander expander;
    private BaseTestActivity activity;


    @Before
    public void setUp() {

        final BaseTestActivity baseTestActivity = activityTestRule.getActivity();
        baseTestActivity.switchTo(com.philips.platform.uid.test.R.layout.layout_expander);
        resources = getInstrumentation().getContext().getResources();
        Espresso.registerIdlingResources(baseTestActivity.getIdlingResource());
        expander = (Expander) baseTestActivity.findViewById(com.philips.platform.uid.test.R.id.layout_expander_id_test);
        // expander = new Expander(baseTestActivity);
        activity = baseTestActivity;


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

    @Test
    public void verifyExpanderTitlePanelClickable() {
        getExpanderTitlePanel().check(matches(ViewPropertiesMatchers.isClickable(true)));
    }

    @Test
    public void verifyExpanderTitlePanelMinHeight() {
        int expectedMinHeight = resources.getDimensionPixelSize(R.dimen.uid_expander_view_panel_min_height);
        getExpanderTitlePanel().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expectedMinHeight)));
    }


    // start of Expander content view test cases

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
    public void verifyExpanderContentViewBottomMargin() {
        int expectedBottomMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_view_content_margin_bottom);
        getExpanderContentView().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expectedBottomMargin)));
    }

    @Test
    public void verifyExpanderContentViewAutoHeight() {
        //  getExpanderContentView().check(matches(ViewPropertiesMatchers.isSameViewHeight(ViewGroup.LayoutParams.WRAP_CONTENT)));
    }


    // separator test case
    @Test
    public void verifyExpanderTitlePanelBottomDividerViewHeight() {
        int expectedHeight = resources.getDimensionPixelSize(R.dimen.uid_divider_Height);
        getExpanderTitlePanelBottomDivider().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }


    @Test
    public void verifyExpanderContentViewBottomDividerViewHeight() {
        int expectedHeight = resources.getDimensionPixelSize(R.dimen.uid_divider_Height);
        getExpanderContentViewBottomDivider().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }


    @Test
    public void verifyExpanderBackgroundColor(){// there is No background color as it it Quite Expander
        assertEquals(0,expander.getSolidColor());
    }

    @Test
    public void verifySeparatorVisibilityGone() {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expander.setSeparatorVisible(false);
            }
        });
        getExpanderTitlePanelBottomDivider().check(matches(ViewPropertiesMatchers.isVisible(View.GONE)));
        getExpanderContentViewBottomDivider().check(matches(ViewPropertiesMatchers.isVisible(View.GONE)));


    }

    @Test
    public void verifySeparatorVisibilityVisible() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expander.setSeparatorVisible(true);
            }
        });
        expander.setSeparatorVisible(true);
        getExpanderTitlePanelBottomDivider().check(matches(ViewPropertiesMatchers.isVisible(View.VISIBLE)));
        getExpanderContentViewBottomDivider().check(matches(ViewPropertiesMatchers.isVisible(View.VISIBLE)));

    }

    @Test
    public void verifyExpanderExpand() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Label label = new Label(getContext());
                RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                label.setLayoutParams(paramsLabel);
                label.setText("customise Expander Content ");
                expander.setExpanderContentView(label);
                expander.expand(true);
            }
        });
        getExpanderContentView().check(matches(ViewPropertiesMatchers.isVisible(View.VISIBLE)));
        assertEquals(true, expander.isExpanded());
    }

    @Test
    public void verifyExpanderCollapse() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Label label = new Label(getContext());
                RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                label.setLayoutParams(paramsLabel);
                label.setText("customise Expander Content ");
                expander.setExpanderContentView(label);
                expander.expand(false);
            }
        });
        getExpanderContentView().check(matches(ViewPropertiesMatchers.isVisible(View.GONE)));
        assertEquals(false, expander.isExpanded());
    }

/*    @Test
    public void verifyExpanderCustomHeader() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Label label = new Label(getContext());
                RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                label.setLayoutParams(paramsLabel);
                label.setText("customise Expander title");
                expander.setExpanderCustomPanelView(label);

            }
        });
        LayoutInflater layoutInflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }*/

    @Test
    public void verifyExpanderListenerCallBackForExpand() {
        UIDExpanderListener uidExpanderListener = new UIDExpanderListener() {
            @Override
            public void expanderPanelExpanded() {
                expander.setExpanderTitle("Expander expanded"); // call back will reset title text
            }

            @Override
            public void expanderPanelCollapsed() {

            }
        };
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Label label = new Label(getContext());
                RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                label.setLayoutParams(paramsLabel);
                label.setText("Expander content");
                expander.setExpanderContentView(label);
                expander.setExpanderTitle("Expander NOT expanded");
                expander.setExpanderListener(uidExpanderListener);
                expander.expand(true);// expand

            }
        });
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.hasSameText("Expander expanded")));
    }

    @Test
    public void verifyExpanderListenerCallBackForCollapse() {
        UIDExpanderListener uidExpanderListener = new UIDExpanderListener() {
            @Override
            public void expanderPanelExpanded() {
            }

            @Override
            public void expanderPanelCollapsed() {
                expander.setExpanderTitle("Expander collapsed"); // call back will reset title text
            }
        };
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Label label = new Label(getContext());
                RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                label.setLayoutParams(paramsLabel);
                label.setText("Expander content");
                expander.setExpanderContentView(label);
                expander.setExpanderTitle("Expander NOT collapsed");
                expander.setExpanderListener(uidExpanderListener);
                expander.expand(false); //collapse

            }
        });
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.hasSameText("Expander collapsed")));
    }

    // Icon tests
    @Test
    public void verifyIconEndMargin() {
        int expectedEndMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_title_icon_margin_end);
        getExpanderTitlePanelIcon().check(matches(TextViewPropertiesMatchers.isSameRightMargin((expectedEndMargin))));
    }

    @Test
    public void verifyIconFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_expander_title_icon_font_size);
        getExpanderTitlePanelIcon().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyIconColor() {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentItemSecondaryNormalIconColor);
        getExpanderTitlePanelIcon().check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Test
    public void verifyIconDefaultVisibiulity() {
        getExpanderTitlePanelIcon().check(matches(ViewPropertiesMatchers.isVisible(View.GONE)));
    }


    @Test
    public void verifyIconVisibilityWhenSet() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expander.setExpanderPanelIcon(resources.getString(R.string.dls_exclamationmark_24));
            }
        });
        getExpanderTitlePanelIcon().check(matches(ViewPropertiesMatchers.isVisible(View.VISIBLE)));
    }


    // Chevron tests

    @Test
    public void verifyChevronFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_expander_title_chevron_font_size);
        getExpanderTitlePanelChevron().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyChevronColor() {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentItemSecondaryNormalIconColor);
        getExpanderTitlePanelChevron().check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Test
    public void verifyChevronFontTextWhenExpanded() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Label label = new Label(getContext());
                RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                label.setLayoutParams(paramsLabel);
                label.setText("customise Expander title");
                expander.setExpanderContentView(label);
                expander.expand(true);
            }
        });
        getExpanderTitlePanelChevron().check(matches(TextViewPropertiesMatchers.hasSameText(resources.getString(R.string.dls_navigationup))));
    }

    @Test
    public void verifyChevronFontTextWhenCollapsed() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Label label = new Label(getContext());
                RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                label.setLayoutParams(paramsLabel);
                label.setText("customise Expander title");
                expander.setExpanderContentView(label);
                expander.expand(false);

            }
        });
        getExpanderTitlePanelChevron().check(matches(TextViewPropertiesMatchers.hasSameText(resources.getString(R.string.dls_navigationdown))));
    }


    // Title tests

    @Test
    public void verifyTitleTextEndMargin() {
        int expectedEndMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_title_text_margin_end);
        getExpanderTitlePanelText().check(matches(ViewPropertiesMatchers.isSameEndMargin(expectedEndMargin)));
    }

    @Test
    public void verifyTitleTextMinHeight() {
        int expectedMinHeight = resources.getDimensionPixelSize(R.dimen.uid_expander_view_title_min_height);
        getExpanderTitlePanelText().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expectedMinHeight)));
    }


    @Test
    public void verifyTitleTextColor() {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentItemPrimaryNormalTextColor);
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }


    @Test
    public void verifyTitleTextTypeface() {
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_MEDIUM)));
    }

    @Test
    public void verifyTitleTextFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_expander_title_text_size);
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyTitleTextMaximumLines() {
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.isSameMaxline(3)));
    }

    @Test
    public void verifyTitleTextDefault() { // default text should be null
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.hasSameText("")));
    }

    @Test
    public void verifyTitleTextGravity() { // default text should be null
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.isCenterVerticallyAligned()));
    }


    //////////start of view getters/////////

    // returns icon label from default title panel
    private ViewInteraction getExpanderTitlePanelIcon() {
        return onView(withId(R.id.uid_expander_title_icon));
    }

    // returns chevron label from default title panel
    private ViewInteraction getExpanderTitlePanelChevron() {
        return onView(withId(R.id.uid_expander_title_chevron));
    }

    // returns title label from default title panel
    private ViewInteraction getExpanderTitlePanelText() {
        return onView(withId(R.id.uid_expander_title_text));
    }


    // returns Relative layout which is ViewGroup for Title panel view
    private ViewInteraction getExpanderTitlePanel() {
        return onView(withId(R.id.uid_expander_view_title));
    }

    // returns Relative layout which is ViewGroup for Content view
    private ViewInteraction getExpanderContentView() {
        return onView(withId(R.id.uid_expander_view_content));
    }

    // returns divider view below Title panel
    private ViewInteraction getExpanderTitlePanelBottomDivider() {
        return onView(withId(R.id.uid_expander_title_bottom_divider));
    }

    // returns divider view below content view
    private ViewInteraction getExpanderContentViewBottomDivider() {
        return onView(withId(R.id.uid_expander_title_bottom_divider));
    }

    private ViewInteraction getExpanderContainer() {
        return onView(withId(R.id.uid_expander));
    }
    //////////end of view getters/////////

}

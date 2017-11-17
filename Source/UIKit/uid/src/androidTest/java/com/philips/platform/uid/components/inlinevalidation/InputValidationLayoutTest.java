package com.philips.platform.uid.components.inlinevalidation;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.InputValidationMatcher;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.InputValidationLayout;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.matcher.ViewPropertiesMatchers.isVisible;

public class InputValidationLayoutTest extends BaseTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources resources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(NavigationColor.BRIGHT.ordinal(), ContentColor.ULTRA_LIGHT.ordinal(),ColorRange.AQUA.ordinal());
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_validation_text);
        resources = activity.getResources();
    }

    @Test
    public void verifyErrorTextColor() {
        int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidTypographyValidationIconColor);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputValidationLayout layout = (InputValidationLayout) activity.findViewById(com.philips.platform.uid.test.R.id.validation_layout);
                layout.setErrorDrawable(R.drawable.uid_texteditbox_clear_icon);
                layout.setErrorMessage("Hello World");
                layout.showError();
            }
        });
        getValidationLayout().check(matches(InputValidationMatcher.isSameErrorColor(color)));
    }

    @Test
    public void verifyIconMargin() {
        int expectedEndMargin = resources.getDimensionPixelSize(R.dimen.uid_inline_validation_icon_margin_end);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputValidationLayout layout = (InputValidationLayout) activity.findViewById(com.philips.platform.uid.test.R.id.validation_layout);
                layout.setErrorDrawable(resources.getDrawable(R.drawable.uid_texteditbox_clear_icon));
                layout.setErrorMessage(com.philips.platform.uid.test.R.string.textbox_dummy_string);
                layout.showError();
            }
        });
        getValidationLayout().check(matches(InputValidationMatcher.isSameIconMargin(expectedEndMargin)));
    }

    @Test
    public void verifyErrorLayoutTopMargin() {
        int expectedEndMargin = resources.getDimensionPixelSize(R.dimen.uid_inline_validation_message_margin_top);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputValidationLayout layout = (InputValidationLayout) activity.findViewById(com.philips.platform.uid.test.R.id.validation_layout);
                layout.setErrorDrawable(R.drawable.uid_texteditbox_clear_icon);
                layout.setErrorMessage(com.philips.platform.uid.test.R.string.textbox_dummy_string);
                layout.showError();
            }
        });
        getErrorLayout().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedEndMargin)));
    }

    @Test
    public void verifyErrorTextFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(R.dimen.uid_inline_label_text_size);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputValidationLayout layout = (InputValidationLayout) activity.findViewById(com.philips.platform.uid.test.R.id.validation_layout);
                layout.setErrorDrawable(R.drawable.uid_texteditbox_clear_icon);
                layout.setErrorMessage(com.philips.platform.uid.test.R.string.textbox_dummy_string);
                layout.showError();
            }
        });
        getValidationLayout().check(matches(InputValidationMatcher.isSameErrorFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyHideErrorLayout() {
        float expectedFontSize = resources.getDimensionPixelSize(R.dimen.uid_inline_label_text_size);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputValidationLayout layout = (InputValidationLayout) activity.findViewById(com.philips.platform.uid.test.R.id.validation_layout);
                layout.setErrorDrawable(R.drawable.uid_texteditbox_clear_icon);
                layout.setErrorMessage(com.philips.platform.uid.test.R.string.textbox_dummy_string);
                layout.showError();
                if(layout.isShowingError()){
                    layout.hideError();
                }
            }
        });
        getErrorLayout().check(matches(isVisible(View.GONE)));
    }

    private ViewInteraction getValidationLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.validation_layout));
    }

    private ViewInteraction getErrorLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_inline_validation_message_layout));
    }
}

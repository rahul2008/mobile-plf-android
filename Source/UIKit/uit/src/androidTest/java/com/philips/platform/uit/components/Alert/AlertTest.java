/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.components.Alert;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uit.activity.BaseTestActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class AlertTest {

    private Resources testResources;
    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        mActivityTestRule.getActivity().switchTo(com.philips.platform.uit.test.R.layout.layout_buttons);
        testResources = getInstrumentation().getContext().getResources();
        instrumentationContext = getInstrumentation().getContext();
    }

    /*****************************************
     * Alert Layout Scenarios
     *********************************************/

    @Test
    public void verifyAlertMinWidth() {

    }

    @Test
    public void verifyAlertMaxWidth() {

    }

    @Test
    public void verifyAlertCornerRadius() {

    }

    /***** Alert title layout scenarios ******/

    @Test
    public void verifyAlertTitleFontSize() {

    }

    @Test
    public void verifyAlertTitleBottomPadding() {

    }

    @Test
    public void verifyAlertTitleIconSize() {

    }

    @Test
    public void verifyAlertTitleIconRightPadding() {

    }


    /***** Alert content layout scenarios ******/

    @Test
    public void verifyAlertContentFontSize() {

    }

    @Test
    public void verifyAlertContentTextLeading() {

    }

    @Test
    public void verifyAlertContentLeftPadding() {

    }

    @Test
    public void verifyAlertContentRightPadding() {

    }

    @Test
    public void verifyAlertContentTopPadding() {

    }

    @Test
    public void verifyAlertContentBottomPadding() {

    }

    /***** Alert action button layout scenarios ******/

    @Test
    public void verifyPaddingBetweenActionButtons() {

    }

    @Test
    public void verifyRightPaddingOfActionButtonView() {

    }

    @Test
    public void verifyActionAreaHeight() {

    }

    @Test
    public void verifyFunctionalityOfConfirmativeButton() {

    }

    @Test
    public void verifyFunctionalityOfDismissiveButton() {

    }

    /*******************************************************
     * Theming Scenarios for Alert
     ******************************************************/

    @Test
    public void verifyFillColorofAlert() {

    }

    @Test
    public void verifyTextColorofAlertTitle() {

    }

    @Test
    public void verifyTextColorofAlertContent() {

    }

    @Test
    public void verifyIconColorofAlertTitle() {

    }

    @Test
    public void verifyIconColorofAlertContent() {

    }

    @Test
    public void verifyColorofDimLayer() {

    }

//    private ViewInteraction getPrimaryButton() {
//        return onView(withId(com.philips.platform.uit.test.R.id.primary_button));
//    }


}


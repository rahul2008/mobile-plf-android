/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.actions;

import android.content.Context;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.MotionEvents;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

public class ActionSetText implements ViewAction {
    CharSequence titleText;

    public ActionSetText(CharSequence title) {
        titleText = title;
    }

    public ActionSetText(Context context, int resID) {
        titleText = context.getResources().getText(resID);
    }
    @Override
    public Matcher<View> getConstraints() {
        return isDisplayed();
    }

    @Override
    public String getDescription() {
        return "Type the text on the view";
    }

    @Override
    public void perform(UiController uiController, View view) {
        if(view instanceof TextView) {
            ((TextView) view).setText(titleText);
        }
    }
}

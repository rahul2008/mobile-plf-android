package com.philips.platform.uid.actions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.MotionEvents;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

public class ActionDown implements ViewAction {
    @Override
    public Matcher<View> getConstraints() {
        return isDisplayed();
    }

    @Override
    public String getDescription() {
        return "Sends action down on the view";
    }

    @Override
    public void perform(UiController uiController, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        float[] coordinates = new float[]{location[0], location[1]};
        float[] precision = new float[]{1f, 1f};

        MotionEvents.sendDown(uiController, coordinates, precision);
    }
}

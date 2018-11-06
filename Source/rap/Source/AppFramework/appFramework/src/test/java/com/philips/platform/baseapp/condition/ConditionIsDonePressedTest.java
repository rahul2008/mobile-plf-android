/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.baseapp.condition;

import android.content.Context;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class ConditionIsDonePressedTest extends TestCase {
    private ConditionIsDonePressed conditionIsDonePressed;

    private Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = Robolectric.buildActivity(TestActivity.class).create().start().get().getApplicationContext();
        conditionIsDonePressed = new ConditionIsDonePressed();
    }

    @Test
    public void testConditionIsDonePressed() {
        boolean isDonePressed = conditionIsDonePressed.isSatisfied(context);
        assertFalse(isDonePressed);
    }
}

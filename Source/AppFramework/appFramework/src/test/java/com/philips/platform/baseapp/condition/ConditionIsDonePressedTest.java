/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.condition;

import android.content.Context;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class ConditionIsDonePressedTest extends TestCase {
    private SharedPreferenceUtility sharedPreferenceUtility;
    private ConditionIsDonePressed conditionIsDonePressed;

    private Context context;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        context = Robolectric.buildActivity(TestActivity.class).create().start().get().getApplicationContext();
        sharedPreferenceUtility = new SharedPreferenceUtility(context);
        conditionIsDonePressed = new ConditionIsDonePressed();
        //appFrameworkApplication = mock(AppFrameworkApplication.class);

    }

    @Test
    public void testConditionIsDonePressed(){
        boolean isDonePressed = conditionIsDonePressed.isSatisfied(context);
        assertEquals(false,isDonePressed);
    }
}

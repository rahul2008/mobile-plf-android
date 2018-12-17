/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.condition;

import com.philips.platform.appframework.flowmanager.AppConditions;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConditionAppLaunchTest extends TestCase {

    private ConditionAppLaunch conditionAppLaunch;
    private AppFrameworkApplication appFrameworkApplication;
    private BaseFlowManager targetFlowManager;
    private ConditionIsLoggedIn conditionIsLoggedIn;
    private ConditionIsDonePressed conditionIsDonePressed;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        appFrameworkApplication = mock(AppFrameworkApplication.class);
        targetFlowManager = mock(BaseFlowManager.class);
        conditionIsLoggedIn = mock(ConditionIsLoggedIn.class);
        conditionIsDonePressed = mock(ConditionIsDonePressed.class);
    }

    @Test
    public void testConditionAppLaunch(){
        when(appFrameworkApplication.getTargetFlowManager()).thenReturn(targetFlowManager);
        when(targetFlowManager.getCondition(AppConditions.IS_LOGGED_IN)).thenReturn(conditionIsLoggedIn);
        when(targetFlowManager.getCondition(AppConditions.IS_DONE_PRESSED)).thenReturn(conditionIsDonePressed);
        when(targetFlowManager.getCondition(AppConditions.IS_LOGGED_IN).isSatisfied(appFrameworkApplication)).thenReturn(false);
        when(targetFlowManager.getCondition(AppConditions.IS_DONE_PRESSED).isSatisfied(appFrameworkApplication)).thenReturn(true);
        conditionAppLaunch = new ConditionAppLaunch();
        assertEquals(true,conditionAppLaunch.isSatisfied(appFrameworkApplication));

    }
}

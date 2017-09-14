/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.condition;

import com.philips.platform.baseapp.base.AppFrameworkApplication;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConditionIsLoggedInTest extends TestCase {

    private AppFrameworkApplication appFrameworkApplication;
    private ConditionIsLoggedIn conditionIsLoggedIn;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        appFrameworkApplication = mock(AppFrameworkApplication.class);
        conditionIsLoggedIn = mock(ConditionIsLoggedIn.class);
    }

    @Test
    public void testConditionIsLoggedIn(){
        when(conditionIsLoggedIn.isUserSignIn(appFrameworkApplication)).thenReturn(false);
        boolean isLoggedIn = conditionIsLoggedIn.isSatisfied(appFrameworkApplication);
        assertEquals(false,isLoggedIn);
    }
}

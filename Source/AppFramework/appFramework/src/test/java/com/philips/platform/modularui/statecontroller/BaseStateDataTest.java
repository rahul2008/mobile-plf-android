/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import junit.framework.TestCase;

import org.junit.Before;


public class BaseStateDataTest extends TestCase {

    private UIStateData uiStateData;

    @Before
    public void setUp() throws Exception {
        uiStateData = new UIStateData();
    }

    public void testGetFragmentLaunchState() throws Exception {
        uiStateData.setFragmentLaunchType(0);
        assertEquals(uiStateData.getFragmentLaunchState(), 0);
    }
}
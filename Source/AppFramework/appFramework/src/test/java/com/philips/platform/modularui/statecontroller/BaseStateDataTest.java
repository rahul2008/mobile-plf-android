package com.philips.platform.modularui.statecontroller;

import junit.framework.TestCase;

import org.junit.Before;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
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
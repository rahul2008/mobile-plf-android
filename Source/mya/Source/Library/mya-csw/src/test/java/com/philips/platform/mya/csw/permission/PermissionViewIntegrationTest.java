package com.philips.platform.mya.csw.permission;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class PermissionViewIntegrationTest {

    private PermissionView fragment;

    @Before
    public void setup() {
        fragment = new PermissionView();
        SupportFragmentTestUtil.startFragment(fragment);
    }

    @Test
    public void testX() {
        assertNotNull(fragment);
    }
}

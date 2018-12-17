/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.themesettings;

import com.philips.platform.uid.thememanager.AccentRange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class AccentOptionsTest {

    private AccentOptions accentOptions;

    @Before
    public void setUp() {
        accentOptions = new AccentOptions();
    }

    @Test
    public void testGetGroupBlueAccentList() {
        ArrayList<AccentRange> accentRanges = accentOptions.getGroupBlueAccentList();
        assertEquals(5, accentRanges.size());
        assertFalse(accentRanges.contains(AccentRange.GROUP_BLUE));
    }

    @Test
    public void testGetAquaAccentList() {
        ArrayList<AccentRange> accentRanges = accentOptions.getAquaAccentList();
        assertEquals(4, accentRanges.size());
        assertFalse(accentRanges.contains(AccentRange.AQUA));
    }

    @Test
    public void testGetGreenAccentList() {
        ArrayList<AccentRange> accentRanges = accentOptions.getGreenAccentList();
        assertEquals(3, accentRanges.size());
        assertFalse(accentRanges.contains(AccentRange.GREEN));
    }

    @Test
    public void testGetOrangeAccentList() {
        ArrayList<AccentRange> accentRanges = accentOptions.getOrangeAccentList();
        assertEquals(3, accentRanges.size());
        assertFalse(accentRanges.contains(AccentRange.ORANGE));
    }

    @Test
    public void testGetPinkAccentList() {
        ArrayList<AccentRange> accentRanges = accentOptions.getPinkAccentList();
        assertEquals(2, accentRanges.size());
        assertFalse(accentRanges.contains(AccentRange.PINK));
    }

    @Test
    public void testGetPurpleAccentList() {
        ArrayList<AccentRange> accentRanges = accentOptions.getPurpleAccentList();
        assertEquals(5, accentRanges.size());
        assertFalse(accentRanges.contains(AccentRange.PURPLE));
    }

    @Test
    public void testGetGrayAccentList() {
        ArrayList<AccentRange> accentRanges = accentOptions.getGrayAccentList();
        assertEquals(7, accentRanges.size());
        assertFalse(accentRanges.contains(AccentRange.GRAY));
    }

    @After
    public void tearDown() {
        accentOptions = null;
    }

}
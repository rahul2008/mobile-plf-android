/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.utils;

import android.graphics.Bitmap;

import com.philips.cdp.registration.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class BitMapDecoderTest {

    @Test
    public void testDecodeSampledBitmapFromResourceBelow100() {
        Bitmap bitmap = BitMapDecoder.decodeSampledBitmapFromResource(RuntimeEnvironment.application.getResources(), R.drawable.jr_icon_amazon, 100, 100);
        assertNotNull(bitmap);
        assertEquals(bitmap.getHeight(),100);
        assertEquals(bitmap.getWidth(),100);
    }

    @Test
    public void testDecodeSampledBitmapFromResourceAbove100() {
        Bitmap bitmap = BitMapDecoder.decodeSampledBitmapFromResource(RuntimeEnvironment.application.getResources(), R.drawable.reg_over_age_tab, 100, 100);
        assertNotNull(bitmap);
        assertEquals(bitmap.getHeight(),100);
        assertEquals(bitmap.getWidth(),100);
    }
}
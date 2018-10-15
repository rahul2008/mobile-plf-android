package com.philips.cdp.registration.ui.utils;

import android.graphics.Bitmap;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)

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
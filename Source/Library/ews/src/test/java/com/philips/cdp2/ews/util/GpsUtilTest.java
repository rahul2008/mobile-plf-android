/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.util;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.util.ReflectionHelpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class GpsUtilTest {

    @Before
    public void itShouldReturnTrueAlwaysIfOSVersionIsBelowAndroidM() throws Exception {
        stubAndroidSdkVersion(Build.VERSION_CODES.LOLLIPOP);

        assertFalse(GpsUtil.isGPSRequiredForWifiScan());
    }

    @Test
    public void testGpsUtilConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<GpsUtil> constructor = GpsUtil.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void itShouldReturnTrueIfVersionOSIsFromAndroidM() throws Exception {
        stubAndroidSdkVersion(Build.VERSION_CODES.M);

        assertTrue(GpsUtil.isGPSRequiredForWifiScan());
    }

    @Test
    public void itShouldCheckIfGPSIsEnabledWhenAsked() throws Exception {
        final Context contextMock = mock(Context.class);
        final LocationManager locationMangerMock = mock(LocationManager.class);

        when(contextMock.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationMangerMock);
        when(locationMangerMock.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);

        assertTrue(GpsUtil.isGPSEnabled(contextMock));
    }

    private void stubAndroidSdkVersion(final int sdkInt) {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", sdkInt);
    }

}
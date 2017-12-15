/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.util;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Build.VERSION.class})
public class GpsUtilTest {

    @Before
    public void itShouldReturnTrueAlwaysIfOSVersionIsBelowAndroidM() throws Exception {
        Whitebox.setInternalState(Build.VERSION.class, "SDK_INT", 16);
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
        Whitebox.setInternalState(Build.VERSION.class, "SDK_INT", 23);
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

}
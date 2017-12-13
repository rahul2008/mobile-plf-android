package com.philips.platform.ews.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.philips.platform.ews.configuration.BaseContentConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BundleUtilsTest {

    @Mock BaseContentConfiguration mockBaseContentConfiguration;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldExtractStringFromArgumentWhenItIsPresent() throws Exception {
        String key = "key";
        String value = "value";
        Bundle mockBundle = mock(Bundle.class);
        when(mockBundle.getString(key)).thenReturn(value);
        when(mockBundle.containsKey(key)).thenReturn(true);

        assertEquals(value, BundleUtils.extractStringFromBundleOrThrow(mockBundle, key));
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldThrowAnExceptionWhenItIsNotPresent() throws Exception {
        Bundle mockBundle = mock(Bundle.class);

        BundleUtils.extractStringFromBundleOrThrow(mockBundle, "key");
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldThrowAnExceptionWhenBundleIsNull() throws Exception {
        BundleUtils.extractStringFromBundleOrThrow(null, "key");
    }

    @Test
    public void itShouldExtractParcelableFromBundleWhenPresent() throws Exception{
        Bundle mockBundle = mock(Bundle.class);
        when(mockBundle.containsKey("key")).thenReturn(true);
        when(mockBundle.getParcelable("key")).thenReturn(mockBaseContentConfiguration);
        BundleUtils.extractParcelableFromIntentOrNull(mockBundle, "key");
    }

    @Test
    public void itShouldVerifyItIsAbstractClass() throws Exception{
        assertTrue(Modifier.isFinal(BundleUtils.class.getModifiers()));
        Constructor<BundleUtils> constructor = BundleUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @NonNull
    private Intent getMockIntentWithBundle() {
        Intent mockIntent = mock(Intent.class);
        Bundle mockBundle = mock(Bundle.class);
        when(mockIntent.getExtras()).thenReturn(mockBundle);
        return mockIntent;
    }
}
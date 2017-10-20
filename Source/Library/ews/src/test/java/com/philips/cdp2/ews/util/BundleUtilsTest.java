package com.philips.cdp2.ews.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
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
        Intent mockIntent = getMockIntentWithBundle();
        Bundle mockBundle = mockIntent.getExtras();
        when(mockBundle.containsKey("key")).thenReturn(true);
        when(mockBundle.getParcelable("key")).thenReturn(mockBaseContentConfiguration);
        BundleUtils.extractParcelableFromIntentOrNull(mockIntent, "key");
    }

    @NonNull
    private Intent getMockIntentWithBundle() {
        Intent mockIntent = mock(Intent.class);
        Bundle mockBundle = mock(Bundle.class);
        when(mockIntent.getExtras()).thenReturn(mockBundle);
        return mockIntent;
    }
}
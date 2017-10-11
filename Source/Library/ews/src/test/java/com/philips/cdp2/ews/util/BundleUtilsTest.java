package com.philips.cdp2.ews.util;

import android.os.Bundle;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BundleUtilsTest {

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
}
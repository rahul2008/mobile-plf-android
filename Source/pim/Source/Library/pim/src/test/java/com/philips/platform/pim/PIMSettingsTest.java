package com.philips.platform.pim;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class PIMSettingsTest extends TestCase {
    private PIMSettings pimSettings;
    @Mock
    private Context mockContext;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        pimSettings = new PIMSettings(mockContext);
    }

    @Test
    public void testToCheckNull(){
        assertNotNull(pimSettings);
    }
}
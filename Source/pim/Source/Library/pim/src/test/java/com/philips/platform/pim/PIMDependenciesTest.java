package com.philips.platform.pim;

import com.philips.platform.appinfra.AppInfraInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class PIMDependenciesTest extends TestCase {
    private PIMDependencies pimDependencies;
    @Mock
    private AppInfraInterface mockAppInfraInterface;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        pimDependencies = new PIMDependencies(mockAppInfraInterface);
    }


    @Test
    public void testToCheckNull() {
        assertNotNull(pimDependencies);
    }
}
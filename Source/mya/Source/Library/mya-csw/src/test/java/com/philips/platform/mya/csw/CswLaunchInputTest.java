package com.philips.platform.mya.csw;

import android.content.Context;
import android.test.mock.MockContext;

import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CswLaunchInputTest {

    @Mock
    private MockContext contextMock;

    private CswLaunchInput subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject = new CswLaunchInput(contextMock, new ArrayList<ConsentDefinition>());
    }

    @Test
    public void givenLaunchInputSetup_whenGetContext_thenShouldAlwaysReturnObject() {
        Context result = subject.getContext();
        assertNotNull(result);
    }

    @Test
    public void givenLaunchInputSetupWithNull_whenGetContext_thenShouldReturnNull() {
        subject = new CswLaunchInput(null, new ArrayList<ConsentDefinition>());

        Context result = subject.getContext();

        assertNull(result);
    }

    @Test
    public void givenLaunchInputSetup_whenAddToBackStack_withTrue_thenShouldBeTrue() {
        subject.addToBackStack(true);

        assertTrue(subject.isAddtoBackStack());
    }

    @Test
    public void givenLaunchInputSetup_whenAddToBackStack_withFalse_thenShouldBeTrue() {
        subject.addToBackStack(false);

        assertFalse(subject.isAddtoBackStack());
    }
}
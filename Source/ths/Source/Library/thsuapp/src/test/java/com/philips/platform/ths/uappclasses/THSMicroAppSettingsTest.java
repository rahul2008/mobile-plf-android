/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.uappclasses;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSMicroAppSettingsTest {
    THSMicroAppSettings mThsMicroAppSettings;

    @Mock
    Context contextMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsMicroAppSettings = new THSMicroAppSettings(contextMock);
    }

    @Test
    public void testObject(){
        assertNotNull(mThsMicroAppSettings);
    }

}
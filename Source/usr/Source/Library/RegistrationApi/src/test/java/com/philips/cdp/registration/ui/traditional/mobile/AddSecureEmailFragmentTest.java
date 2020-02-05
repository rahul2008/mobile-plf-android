/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 11/21/17.
 */
@RunWith(RobolectricTestRunner.class)
public class AddSecureEmailFragmentTest {

    private AddSecureEmailFragment addSecureEmailFragment;

    @Mock
    private RegistrationComponent mockRegistrationComponent;
    @Mock
    private AppTaggingInterface mockAppTaggingInterface;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(mockRegistrationComponent.getAppTaggingInterface()).thenReturn(mockAppTaggingInterface);
        when(mockAppTaggingInterface.createInstanceForComponent(anyString(), anyString())).thenReturn(mockAppTaggingInterface);

        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        AppTagging.init();

        addSecureEmailFragment= new AddSecureEmailFragment();
    }

    @Test
    public void should_startFragment() throws Exception {
//        SupportFragmentController.of(addSecureEmailFragment).create().start().resume();
    }
}
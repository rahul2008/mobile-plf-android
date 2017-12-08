package com.philips.cdp.registration.update;

import com.janrain.android.capture.Capture;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by philips on 12/3/17.
 */
@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class UpdateJanRainUserProfileTest {
    private UpdateJanRainUserProfile updateJanRainUserProfile;

    @Mock
    private Capture.CaptureApiRequestCallback captureApiRequestCallbackMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        updateJanRainUserProfile=new UpdateJanRainUserProfile();

    }

    @Test
    public void updateUserEmail() throws Exception {

        updateJanRainUserProfile.updateUserEmail("philips@gmail.com");
    }

    @Test(expected = NullPointerException.class)
    public void shouldCall_updateUserEmail() throws Exception {
        updateJanRainUserProfile.updateUserEmail("philips@gmail.com",captureApiRequestCallbackMock);
    }
}
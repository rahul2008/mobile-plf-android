package com.philips.cdp.registration.update;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by philips on 12/3/17.
 */
@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class UpdateUserTest {

    UpdateUser updateUser;

    @Mock
    private UpdateUser.UpdateUserListener updateUserListenerMock;
    @Mock
    private JSONObject userDataMock;

    @Mock
    private JSONObject updateUserObjectMock;

    @Mock
    private CaptureApiError captureAPIError;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        updateUser = new UpdateUser();
    }


    @Test(expected = ClassCastException.class)
    public void update() throws Exception {
        updateUser.update(updateUserObjectMock,userDataMock,updateUserListenerMock);
    }

    @Test(expected = NullPointerException.class)
    public void onSuccess() throws Exception {
        updateUser.onSuccess();
    }



    @Test(expected = NullPointerException.class)
    public void onFailure() throws Exception {
        updateUser.onFailure(captureAPIError);
    }

}
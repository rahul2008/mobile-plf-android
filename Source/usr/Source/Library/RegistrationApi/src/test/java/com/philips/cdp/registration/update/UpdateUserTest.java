package com.philips.cdp.registration.update;

import com.janrain.android.capture.CaptureApiError;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by philips on 12/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
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
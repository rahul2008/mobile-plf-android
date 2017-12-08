package com.philips.cdp.registration.ui.social;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationSettingsURL;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/22/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MergeAccountPresenterTest {

    @Mock
    User userMock;

    @Mock
    private RegistrationComponent mockRegistrationComponent;


    private MergeAccountPresenter presenter;

    MergeAccountContract mergeAccountContract;

    @Mock
    MergeAccountContract mergeAccountContractMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        URInterface.setComponent(mockRegistrationComponent);

        mergeAccountContract=new MergeAccountContract() {
            @Override
            public void connectionStatus(boolean isOnline) {

            }

            @Override
            public void mergeStatus(boolean isOnline) {

            }

            @Override
            public void mergeSuccess() {

            }

            @Override
            public void mergeFailure(String reason) {

            }

            @Override
            public void mergePasswordFailure() {

            }
        };
        presenter = new MergeAccountPresenter(mergeAccountContractMock,userMock);
    }

    @Test
    public void shouldSetStatus_OnNetWorkStateReceived() throws Exception {
        presenter.onNetWorkStateReceived(true);
        Mockito.verify(mergeAccountContractMock).connectionStatus(true);
        Mockito.verify(mergeAccountContractMock).mergeStatus(true);
    }

    @Test
    public void shouldReturnEmailIfValid_OnCallGetLoginWithDetails() throws Exception {
        Mockito.when(userMock.getEmail()).thenReturn("philips@gmail.com");
        Assert.assertSame("philips@gmail.com",presenter.getLoginWithDetails());
    }

    @Test
    public void shouldReturnMobileIfInvalidEmail_OnCallGetLoginWithDetails() throws Exception {
        Mockito.when(userMock.getEmail()).thenReturn("philips");
        Mockito.when(userMock.getMobile()).thenReturn("8904291902");
        Assert.assertSame("8904291902",presenter.getLoginWithDetails());
    }
}
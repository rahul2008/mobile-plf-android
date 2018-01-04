package com.philips.cdp.registration.ui.social;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by philips on 11/22/17.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MergeSocialToSocialAccountPresenterTest {

    @Mock
    User userMock;

    @Mock
    private RegistrationComponent mockRegistrationComponent;



    MergeSocialToSocialAccountPresenter mergeSocialToSocialAccountPresenter;

    @Mock
    MergeSocialToSocialAccountContract mergeSocialToSocialAccountContract;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);

        mergeSocialToSocialAccountPresenter = new MergeSocialToSocialAccountPresenter(mergeSocialToSocialAccountContract,userMock);

    }

    @Test
    public void onNetWorkStateReceived() throws Exception {
        mergeSocialToSocialAccountPresenter.onNetWorkStateReceived(true);
        verify(mergeSocialToSocialAccountContract).connectionStatus(true);
        verify(mergeSocialToSocialAccountContract).mergeStatus(true);
    }

    @Test
    public void onLoginSuccess() throws Exception {
        mergeSocialToSocialAccountPresenter.onLoginSuccess();
        verify(mergeSocialToSocialAccountContract).mergeSuccess();
    }

    @Mock
    UserRegistrationFailureInfo userRegistrationFailureInfoMock;

    @Test
    public void onLoginFailedWithError() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorDescription()).thenReturn("unknown");
        mergeSocialToSocialAccountPresenter.onLoginFailedWithError(userRegistrationFailureInfoMock);
        verify(mergeSocialToSocialAccountContract).mergeFailure("unknown");
    }


    @Test
    public void onLoginFailedWithTwoStepError() throws Exception {

        JSONObject jsonObject=new JSONObject();
        mergeSocialToSocialAccountPresenter.onLoginFailedWithTwoStepError(jsonObject,"token");
        verify(mergeSocialToSocialAccountContract).mergeFailureIgnored();
    }

    @Test
    public void onLoginFailedWithMergeFlowError() throws Exception {
        mergeSocialToSocialAccountPresenter.onLoginFailedWithMergeFlowError("token","existingprovider","identityProvider",
                "nameLocalized","existingNameLocalized","email");
        verify( mergeSocialToSocialAccountContract).mergeFailureIgnored();
    }

    @Test
    public void onContinueSocialProviderLoginSuccess() throws Exception {
        mergeSocialToSocialAccountPresenter.onContinueSocialProviderLoginSuccess();
        verify(mergeSocialToSocialAccountContract).mergeSuccess();
    }

    @Test
    public void onContinueSocialProviderLoginFailure() throws Exception {
        mergeSocialToSocialAccountPresenter.onContinueSocialProviderLoginFailure(userRegistrationFailureInfoMock);
        verify(mergeSocialToSocialAccountContract).mergeFailureIgnored();
    }

    @Test
    public void logout() throws Exception {
        mergeSocialToSocialAccountPresenter.logout();
        verify(userMock).logout(null);
    }

    @Test
    public void loginUserUsingSocialProvider() throws Exception {
        mergeSocialToSocialAccountPresenter.loginUserUsingSocialProvider("provider","token");
    }


    @Test
    public void shouldReturnEmailIfValid_OnCallGetLoginWithDetails() throws Exception {
        Mockito.when(userMock.getEmail()).thenReturn("philips@gmail.com");
        Assert.assertSame("philips@gmail.com",mergeSocialToSocialAccountPresenter.getLoginWithDetails());
    }

    @Test
    public void shouldReturnMobileIfInvalidEmail_OnCallGetLoginWithDetails() throws Exception {
        Mockito.when(userMock.getEmail()).thenReturn("philips");
        Mockito.when(userMock.getMobile()).thenReturn("8904291902");
        Assert.assertSame("8904291902",mergeSocialToSocialAccountPresenter.getLoginWithDetails());
    }

}
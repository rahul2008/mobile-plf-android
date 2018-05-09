package com.philips.cdp.registration.dao;

import android.content.Context;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.Gender;
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDetailsListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 25)
public class UserDataProviderTest {

    @Mock
    Context contextMock;

    @Mock
    User userMock;

    @Mock
    RegistrationComponent registrationComponent;

    private UserDataProvider userDataProvider;

    @Mock
    UserDetailsListener userDetailsListenerMock;

    @Mock
    RefreshListener refreshListenerMock;

    @Mock
    LogoutListener logoutListenerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(registrationComponent);
        getUserDetailsObject();
    }

    private void getUserDetailsObject() {
        userDataProvider = new UserDataProvider(contextMock) {
            @Override
            public String getHSDPUUID() {
                return "hsdpUuid";
            }

            @Override
            public String getJanrainUUID() {
                return "uuid";
            }

            @Override
            public String getHSDPAccessToken() {
                return "hsdpAccessToken";
            }

            @Override
            public String getJanrainAccessToken() {
                return "accessToken";
            }

            @Override
            public String getGivenName() {
                return "Name";
            }

            @Override
            public String getFamilyName() {
                return "family";
            }

            @Override
            public Gender getGender() {
                return Gender.MALE;
            }

            @Override
            public String getEmail() {
                return "Email";
            }

            @Override
            public String getMobile() {
                return "9999988888";
            }

            @Override
            public Date getDateOfBirth() {
                return new Date();
            }

            @Override
            public boolean isUserLoggedIn(Context context) {
                return true;
            }
        };
    }

    @Test
    public void testUUIDNotNull(){
        assertNotNull(userDataProvider.getJanrainUUID());
    }

    @Test
    public void testHSDPAccessTokenNotNull(){
        assertNotNull(userDataProvider.getHSDPAccessToken());
    }

    @Test
    public void testHSDPUUIDNotNull(){
        assertNotNull(userDataProvider.getHSDPUUID());
    }

    @Test
    public void testUserSignedIn(){
        assertEquals(true, userDataProvider.isUserLoggedIn(contextMock));
    }

    @Test
    public void testGetDetails() throws Exception {
        ArrayList<String> testArray = new ArrayList<>(Arrays.asList(
                UserDetailConstants.GIVEN_NAME,
                UserDetailConstants.GENDER,
                UserDetailConstants.EMAIL,
                UserDetailConstants.MOBILE_NUMBER,
                UserDetailConstants.BIRTHDAY,
                UserDetailConstants.RECEIVE_MARKETING_EMAIL));
        HashMap<String,Object> detailsMap = userDataProvider.getUserDetails(testArray);
        assertEquals("Name",detailsMap.get(UserDetailConstants.GIVEN_NAME));
        assertNull(detailsMap.get(UserDetailConstants.FAMILY_NAME));
    }

    @Test
    public void testGetDetailsRepeatKeys() throws Exception {
        ArrayList<String> testArray = new ArrayList<>(Arrays.asList(
                UserDetailConstants.GIVEN_NAME,
                UserDetailConstants.GIVEN_NAME
        ));
        HashMap<String,Object> detailsMap = userDataProvider.getUserDetails(testArray);
        assertEquals("Name",detailsMap.get(UserDetailConstants.GIVEN_NAME));
        assertNull(detailsMap.get(UserDetailConstants.FAMILY_NAME));
    }

    @Test
    public void testGetDetailsForEmpty() throws Exception {
        ArrayList<String> testArray = new ArrayList<>();
        assertNotNull(userDataProvider.getUserDetails(testArray));
        HashMap<String,Object> detailsMap = userDataProvider.getUserDetails(testArray);
        assertEquals("Name",detailsMap.get(UserDetailConstants.GIVEN_NAME));
        assertEquals("family",detailsMap.get(UserDetailConstants.FAMILY_NAME));
        assertEquals(Gender.MALE,detailsMap.get(UserDetailConstants.GENDER));
        assertEquals("Email",detailsMap.get(UserDetailConstants.EMAIL));
        assertEquals("9999988888",detailsMap.get(UserDetailConstants.MOBILE_NUMBER));
        assertNotNull(detailsMap.get(UserDetailConstants.BIRTHDAY));
        assertEquals(false,detailsMap.get(UserDetailConstants.RECEIVE_MARKETING_EMAIL));
    }

    @Test(expected = Exception.class)
    public void testGetDetailsForException() throws Exception{
        userDataProvider.getUserDetails(new ArrayList<>(Arrays.asList("Invalid","key")));
    }

    @Test
    public void testOnLogoutSuccess(){
        userDataProvider.getLogoutHandler(logoutListenerMock).onLogoutSuccess();
        Mockito.verify(logoutListenerMock).onLogoutSuccess();
    }

    @Test
    public void testOnLogoutFailure(){
        userDataProvider.getLogoutHandler(logoutListenerMock).onLogoutFailure(404,"No internet");
        Mockito.verify(logoutListenerMock).onLogoutFailure(404,"No internet");
    }

    @Test
    public void testOnRefreshSuccess(){
        userDataProvider.getRefreshHandler(refreshListenerMock).onRefreshLoginSessionSuccess();
        Mockito.verify(refreshListenerMock).onRefreshSessionSuccess();
    }

    @Test
    public void testOnRefreshfailure(){
        userDataProvider.getRefreshHandler(refreshListenerMock).onRefreshLoginSessionFailedWithError(404);
        Mockito.verify(refreshListenerMock).onRefreshSessionFailure(404);
    }

    @Test
    public void testOnUpdateMarketingEmail(){
        userDataProvider.getUpdateReceiveMarketingEmailHandler(userDetailsListenerMock).onUpdateSuccess();
        Mockito.verify(userDetailsListenerMock).onUpdateSuccess();
    }

    @Test
    public void testOnUpdateMarketingEmailFailure(){
        userDataProvider.getUpdateReceiveMarketingEmailHandler(userDetailsListenerMock).onUpdateFailedWithError(404);
        Mockito.verify(userDetailsListenerMock).onUpdateFailure(404);
    }

}

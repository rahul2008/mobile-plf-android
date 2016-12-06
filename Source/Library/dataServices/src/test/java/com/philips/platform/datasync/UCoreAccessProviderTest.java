package com.philips.platform.datasync;

import android.content.SharedPreferences;

import com.philips.platform.core.datatypes.UserProfile;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sangamesh on 06/12/16.
 */
public class UCoreAccessProviderTest {


    public static final String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    public static final String TEST_USER_ID = "TEST_USER_ID";
    public static final String TEST_BABY_ID = "TEST_BABY_ID";

    @Mock
    private UserRegistrationFacade userRegistrationFacadeMock;

    @Mock
    private UserProfile userProfileMock;

    @Mock
    private SharedPreferences sharedPreferencesMock;

    @Mock
    private SharedPreferences.Editor editorMock;

    private UCoreAccessProvider uCoreAccessProvider;

    @Before
    public void setUp() {
        initMocks(this);

        uCoreAccessProvider = new UCoreAccessProvider(userRegistrationFacadeMock);
    }

    @Test
    public void ShouldReturnValueFromFacade_WhenIsLoggedInIsCalled() {
        when(userRegistrationFacadeMock.isUserLoggedIn()).thenReturn(true);

        boolean loggedIn = uCoreAccessProvider.isLoggedIn();

        verify(userRegistrationFacadeMock).isUserLoggedIn();
        assertThat(loggedIn).isTrue();
    }

    @Test
    public void ShouldReturnValueFromFacade_WhenGetAccessTokenIsCalled() {
        when(userRegistrationFacadeMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);

        String accessToken = uCoreAccessProvider.getAccessToken();

        assertThat(accessToken).isEqualTo(TEST_ACCESS_TOKEN);
    }

    @Test
    public void ShouldReturnValueFromUserProfile_WhenGetUserIdIsCalled() {
        when(userRegistrationFacadeMock.getUserProfile()).thenReturn(userProfileMock);
        when(userProfileMock.getGUid()).thenReturn(TEST_USER_ID);

        String userId = uCoreAccessProvider.getUserId();

        assertThat(userId).isEqualTo(TEST_USER_ID);
    }



}
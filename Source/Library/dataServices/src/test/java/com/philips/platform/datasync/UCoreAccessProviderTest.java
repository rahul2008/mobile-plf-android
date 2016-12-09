package com.philips.platform.datasync;

import android.content.SharedPreferences;

import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.datatypes.BaseAppData;
import com.philips.platform.core.datatypes.UserProfile;
import com.philips.platform.datasync.userprofile.ErrorHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 06/12/16.
 */
public class UCoreAccessProviderTest {


    public static final String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    public static final String TEST_USER_ID = "TEST_USER_ID";
    public static final String TEST_BABY_ID = "TEST_BABY_ID";
    public static final String MOMENT_LAST_SYNC_URL_KEY = "MOMENT_LAST_SYNC_URL_KEY";
    public static final String INSIGHT_LAST_SYNC_URL_KEY = "INSIGHT_LAST_SYNC_URL_KEY";
    public static final String INSIGHT_FOR_USER_LAST_SYNC_URL_KEY = "INSIGHT_FOR_USER_LAST_SYNC_URL_KEY";

    @Mock
    private ErrorHandler userRegistrationFacadeMock;

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

    @Test
    public void ShouldReturnValueFromFacade_WhenInjectSaredPrefsIsCalled() {
//        when(sharedPreferencesMock.g()).thenReturn(TEST_ACCESS_TOKEN);

        uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);

        //assertThat(accessToken).isInstanceOf(SharedPreferences.class);
    }

    @Test
    public void ShouldReturnValueFromUserProfile_WhengetSubjectIdIsCalled() {
        when(userRegistrationFacadeMock.getUserProfile()).thenReturn(userProfileMock);
        when(userProfileMock.getGUid()).thenReturn(TEST_BABY_ID);

        String userId = uCoreAccessProvider.getSubjectId();

        assertThat(userId).isEqualTo(TEST_BABY_ID);
    }


//    @Test
//    public void ShouldReturnValueFromPreferences_WhenGetBabyIdIsCalled() {
//        uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);
//        when(sharedPreferencesMock.getString(eq(UCoreAccessProvider.ACTIVE_BABY_ID_KEY), anyString())).thenReturn(TEST_BABY_ID);
//
//        String babyId = uCoreAccessProvider.getSubjectId();
//
//        assertThat(babyId).isEqualTo(TEST_BABY_ID);
//    }

    @Test
    public void ShouldReturnValue_WhenGetMomentLastSyncTimestampIsCalled() {
        uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);
        when(sharedPreferencesMock.getString(eq(UCoreAccessProvider.MOMENT_LAST_SYNC_URL_KEY), anyString())).thenReturn(MOMENT_LAST_SYNC_URL_KEY);

        String babyId = uCoreAccessProvider.getMomentLastSyncTimestamp();

        assertThat(babyId).isEqualTo(MOMENT_LAST_SYNC_URL_KEY);
    }

    @Test
    public void ShouldReturnValue_WhenGetInsightLastSyncTimestampIsCalled() {
        uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);
        when(sharedPreferencesMock.getString(eq(UCoreAccessProvider.INSIGHT_LAST_SYNC_URL_KEY), anyString())).thenReturn(INSIGHT_LAST_SYNC_URL_KEY);

        String babyId = uCoreAccessProvider.getInsightLastSyncTimestamp();

        assertThat(babyId).isEqualTo(INSIGHT_LAST_SYNC_URL_KEY);
    }

    @Test
    public void ShouldReturnValue_WhenGetInsightLastSyncTimestampForUserIsCalled() {
        uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);
        when(sharedPreferencesMock.getString(eq(UCoreAccessProvider.INSIGHT_FOR_USER_LAST_SYNC_URL_KEY), anyString())).thenReturn(INSIGHT_FOR_USER_LAST_SYNC_URL_KEY);

        String babyId = uCoreAccessProvider.getInsightLastSyncTimestampForUser();

        assertThat(babyId).isEqualTo(INSIGHT_FOR_USER_LAST_SYNC_URL_KEY);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldSaveBabyId_WhenActiveBabyIdEventReceived() {
        uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        when(editorMock.putString(anyString(), anyString())).thenReturn(editorMock);

        BaseAppData activeBabyIdEvent = new BaseAppData() {
            @Override
            public int getId() {
                return 0;
            }
        };

        uCoreAccessProvider.saveLastSyncTimeStamp(MOMENT_LAST_SYNC_URL_KEY,MOMENT_LAST_SYNC_URL_KEY);

        verify(editorMock).putString(MOMENT_LAST_SYNC_URL_KEY, MOMENT_LAST_SYNC_URL_KEY);
        verify(editorMock).commit();
    }

}
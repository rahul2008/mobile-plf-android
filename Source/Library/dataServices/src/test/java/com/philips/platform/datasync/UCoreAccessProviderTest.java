package com.philips.platform.datasync;

import android.content.SharedPreferences;

import com.philips.platform.core.datatypes.BaseAppData;
import com.philips.platform.core.datatypes.UserProfile;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UCoreAccessProviderTest {


    public static final String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    public static final String TEST_USER_ID = "TEST_USER_ID";
    public static final String TEST_BABY_ID = "TEST_BABY_ID";
    public static final String MOMENT_LAST_SYNC_URL_KEY = "MOMENT_LAST_SYNC_URL_KEY";
    public static final String INSIGHT_LAST_SYNC_URL_KEY = "INSIGHT_LAST_SYNC_URL_KEY";
    public static final String INSIGHT_FOR_USER_LAST_SYNC_URL_KEY = "INSIGHT_FOR_USER_LAST_SYNC_URL_KEY";

    @Mock
    private UserRegistrationInterface userRegistrationFacadeMock;

    @Mock
    private UserProfile userProfileMock;

    @Mock
    private SharedPreferences sharedPreferencesMock;

    @Mock
    private SharedPreferences.Editor editorMock;

    private UCoreAccessProvider uCoreAccessProvider;
    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() {
        initMocks(this);

        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        uCoreAccessProvider = new UCoreAccessProvider(userRegistrationFacadeMock);
        uCoreAccessProvider.sharedPreferences = sharedPreferencesMock;
    }

    @Test
    public void ShouldReturnValueFromFacade_WhenIsLoggedInIsCalled() {
        when(userRegistrationFacadeMock.isUserLoggedIn()).thenReturn(true);

        boolean loggedIn = uCoreAccessProvider.isLoggedIn();

        verify(userRegistrationFacadeMock).isUserLoggedIn();
        assertThat(loggedIn).isTrue();
    }

    @Test
    public void ShouldReturnValueFromFacade_WhenIsLoggedInIsCalled_User_null() {
        uCoreAccessProvider.userRegistrationInterface = null;

        boolean loggedIn = uCoreAccessProvider.isLoggedIn();

        //verify(userRegistrationFacadeMock).isUserLoggedIn();
        assertThat(loggedIn).isFalse();
    }

    @Test
    public void ShouldReturnValueFromFacade_WhengetAccessTokenIsCalled_User_null() {
        uCoreAccessProvider.userRegistrationInterface = null;

        String token = uCoreAccessProvider.getAccessToken();

        //verify(userRegistrationFacadeMock).isUserLoggedIn();
        assertThat(token).isNull();
    }

    @Test
    public void ShouldReturnValueFromFacade_WhengetUserIdIsCalled_User_null() {
        uCoreAccessProvider.userRegistrationInterface = null;

        String id = uCoreAccessProvider.getUserId();

        //verify(userRegistrationFacadeMock).isUserLoggedIn();
        assertThat(id).isNull();
    }

    @Test
    public void ShouldReturnValueFromFacade_WhengetSubjectIdIsCalled_User_null() {
        uCoreAccessProvider.userRegistrationInterface = null;

        String id = uCoreAccessProvider.getSubjectId();

        //verify(userRegistrationFacadeMock).isUserLoggedIn();
        assertThat(id).isNull();
    }

    @Test
    public void ShouldReturnValueFromFacade_WhenGetAccessTokenIsCalled() {
        when(userRegistrationFacadeMock.getHSDPAccessToken()).thenReturn(TEST_ACCESS_TOKEN);

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
    public void ShouldReturnValueFromUserProfile_WhengetSubjectIdIsCalled() {
        when(userRegistrationFacadeMock.getUserProfile()).thenReturn(userProfileMock);
        when(userProfileMock.getGUid()).thenReturn(TEST_BABY_ID);

        String userId = uCoreAccessProvider.getSubjectId();

        assertThat(userId).isEqualTo(TEST_BABY_ID);
    }

    @Test
    public void ShouldReturnValue_WhenGetMomentLastSyncTimestampIsCalled() {
       // uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);
        when(sharedPreferencesMock.getString(eq(UCoreAccessProvider.MOMENT_LAST_SYNC_URL_KEY), anyString())).thenReturn(MOMENT_LAST_SYNC_URL_KEY);

        String babyId = uCoreAccessProvider.getMomentLastSyncTimestamp();

        assertThat(babyId).isEqualTo(MOMENT_LAST_SYNC_URL_KEY);
    }

    @Test
    public void ShouldReturnValue_WhenGetInsightLastSyncTimestampIsCalled() {
      //  uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);
        when(sharedPreferencesMock.getString(eq(UCoreAccessProvider.INSIGHT_LAST_SYNC_URL_KEY), anyString())).thenReturn(INSIGHT_LAST_SYNC_URL_KEY);

        String babyId = uCoreAccessProvider.getInsightLastSyncTimestamp();

        assertThat(babyId).isEqualTo(INSIGHT_LAST_SYNC_URL_KEY);
    }

    @Test
    public void ShouldReturnValue_WhenGetInsightLastSyncTimestampForUserIsCalled() {
     //   uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);
        when(sharedPreferencesMock.getString(eq(UCoreAccessProvider.INSIGHT_LAST_SYNC_URL_KEY), anyString())).thenReturn(INSIGHT_FOR_USER_LAST_SYNC_URL_KEY);

        String babyId = uCoreAccessProvider.getInsightLastSyncTimestamp();

        assertThat(babyId).isEqualTo(INSIGHT_FOR_USER_LAST_SYNC_URL_KEY);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldSaveBabyId_WhenActiveBabyIdEventReceived() {
      //  uCoreAccessProvider.injectSaredPrefs(sharedPreferencesMock);
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
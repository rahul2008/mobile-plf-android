package com.philips.platform.datasync.subjectProfile;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetSubjectProfileListResponseEvent;
import com.philips.platform.core.events.GetSubjectProfileResponseEvent;
import com.philips.platform.core.events.SubjectProfileErrorResponseEvent;
import com.philips.platform.core.events.SubjectProfileResponseEvent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedString;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SubjectProfileControllerTest {
    private String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    private String TEST_USER_ID = "TEST_USER_ID";

    private SubjectProfileController mSubjectProfileController;

    @Mock
    private Eventing mEventing;
    @Mock
    private RetrofitError mRetrofitError;
    @Mock
    private UCoreAccessProvider mUCoreAccessProvider;
    @Mock
    private UCoreAdapter mUCoreAdapter;
    @Mock
    private SubjectProfileClient mSubjectProfileClient;
    @Mock
    private GsonConverter mGsonConverter;
    @Mock
    private AppComponent mAppComponent;

    private Response mResponse;

    @Captor
    private ArgumentCaptor<BackendResponse> mBackendResponseArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(mAppComponent);
        mSubjectProfileController = new SubjectProfileController(mUCoreAdapter, mGsonConverter);
        mSubjectProfileController.mUCoreAccessProvider = mUCoreAccessProvider;
        mSubjectProfileController.mEventing = mEventing;
    }

    @Test
    public void createSubjectProfileResponseOKTest() throws Exception {
        mResponse = new Response("", 200, "OK", new ArrayList<Header>(), null);
        createSubjectProfile();
    }

    @Test
    public void createSubjectProfileResponseNoContentTest() throws Exception {
        mResponse = new Response("", 201, "OK", new ArrayList<Header>(), null);
        createSubjectProfile();
    }

    @Test
    public void createSubjectProfileResponseCreatedTest() throws Exception {
        mResponse = new Response("", 204, "OK", new ArrayList<Header>(), null);
        createSubjectProfile();
    }

    private void createSubjectProfile() {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mSubjectProfileClient);


        UCoreCreateSubjectProfileRequest uCoreCreateSubjectProfileRequest = new UCoreCreateSubjectProfileRequest();
        uCoreCreateSubjectProfileRequest.setFirstName("firstName");
        uCoreCreateSubjectProfileRequest.setDateOfBirth("dateOfBirth");
        uCoreCreateSubjectProfileRequest.setGender("gender");
        uCoreCreateSubjectProfileRequest.setWeight(10.0);
        uCoreCreateSubjectProfileRequest.setCreationDate("creationDate");

        assertTrue(uCoreCreateSubjectProfileRequest.getFirstName() != null);
        assertTrue(uCoreCreateSubjectProfileRequest.getDateOfBirth() != null);
        assertTrue(uCoreCreateSubjectProfileRequest.getGender() != null);
        assertTrue(uCoreCreateSubjectProfileRequest.getWeight() == 10.0);
        assertTrue(uCoreCreateSubjectProfileRequest.getCreationDate() != null);

        UCoreCreateSubjectProfileResponse uCoreCreateSubjectProfileResponse = new UCoreCreateSubjectProfileResponse();
        uCoreCreateSubjectProfileResponse.setSubjectID("guid");
        assertTrue(uCoreCreateSubjectProfileResponse.getSubjectID() != null);

        when(mSubjectProfileClient.createSubjectProfile(eq(TEST_USER_ID), any(UCoreCreateSubjectProfileRequest.class))).thenReturn(mResponse);
        mSubjectProfileController.createSubjectProfile(uCoreCreateSubjectProfileRequest);
        assertEquals(mResponse.getReason(), "OK");
    }

    @Test
    public void getSubjectProfileTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mSubjectProfileClient);
        mSubjectProfileController.getSubjectProfile("subjectID");
        verify(mEventing).post(isA(GetSubjectProfileResponseEvent.class));
    }

    @Test
    public void getSubjectProfileListTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mSubjectProfileClient);

        List<UCoreSubjectProfile> uCoreSubjectProfiles = new ArrayList<>();
        UCoreSubjectProfileList uCoreSubjectProfileList = new UCoreSubjectProfileList();
        UCoreSubjectProfile uCoreSubjectProfile = new UCoreSubjectProfile();
        uCoreSubjectProfile.setGuid("guid");
        uCoreSubjectProfile.setFirstName("firstName");
        uCoreSubjectProfile.setDateOfBirth("dateOfBirth");
        uCoreSubjectProfile.setGender("gender");
        uCoreSubjectProfile.setWeight(10.0);
        uCoreSubjectProfile.setVersion(1);
        uCoreSubjectProfile.setInactive(true);
        uCoreSubjectProfile.setLastModified("lastModified");
        uCoreSubjectProfile.setCreationDate("creationDate");

        assertTrue(uCoreSubjectProfile.getGuid() != null);
        assertTrue(uCoreSubjectProfile.getFirstName() != null);
        assertTrue(uCoreSubjectProfile.getDateOfBirth() != null);
        assertTrue(uCoreSubjectProfile.getGender() != null);
        assertTrue(uCoreSubjectProfile.getWeight() == 10.0);
        assertTrue(uCoreSubjectProfile.getCreationDate() != null);
        assertTrue(uCoreSubjectProfile.getVersion() == 1);
        assertTrue(uCoreSubjectProfile.getLastModified() != null);
        assertTrue(uCoreSubjectProfile.isInactive());

        uCoreSubjectProfiles.add(uCoreSubjectProfile);
        uCoreSubjectProfileList.setSubjectProfiles(uCoreSubjectProfiles);
        assertTrue(uCoreSubjectProfileList.getSubjectProfiles() != null);

        mSubjectProfileController.getSubjectProfileList();
        verify(mEventing).post(isA(GetSubjectProfileListResponseEvent.class));
    }

    @Test
    public void deleteSubjectProfileTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mSubjectProfileClient);

        mSubjectProfileController.deleteSubjectProfile("subjectID");
        verify(mEventing).post(isA(SubjectProfileResponseEvent.class));
    }

    @Test
    public void retrofitErrorWhileCreatingSubjectProfileTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mSubjectProfileClient);

        UCoreCreateSubjectProfileRequest uCoreCreateSubjectProfileRequest = new UCoreCreateSubjectProfileRequest();
        uCoreCreateSubjectProfileRequest.setFirstName("firstName");
        uCoreCreateSubjectProfileRequest.setDateOfBirth("dateOfBirth");
        uCoreCreateSubjectProfileRequest.setGender("gender");
        uCoreCreateSubjectProfileRequest.setWeight(10.0);
        uCoreCreateSubjectProfileRequest.setCreationDate("creationDate");

        final RetrofitError retrofitError = mock(RetrofitError.class);
        mResponse = new Response("", 403, "Test error", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitError.getResponse()).thenReturn(mResponse);
        when(mSubjectProfileClient.createSubjectProfile(TEST_USER_ID, uCoreCreateSubjectProfileRequest)).thenThrow(retrofitError);
        mSubjectProfileController.createSubjectProfile(uCoreCreateSubjectProfileRequest);
        verify(mEventing).post(isA(SubjectProfileErrorResponseEvent.class));
    }

    @Test
    public void retrofitErrorWhileFetchingProfileTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mSubjectProfileClient);

        final RetrofitError retrofitError = mock(RetrofitError.class);
        mResponse = new Response("", 403, "Test error", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitError.getResponse()).thenReturn(mResponse);
        when(mSubjectProfileClient.getSubjectProfile(TEST_USER_ID, "subjectID")).thenThrow(retrofitError);
        mSubjectProfileController.getSubjectProfile("subjectID");
        verify(mEventing).post(isA(SubjectProfileErrorResponseEvent.class));
    }

    @Test
    public void retrofitErrorWhileFetchingProfileListTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mSubjectProfileClient);

        final RetrofitError retrofitError = mock(RetrofitError.class);
        mResponse = new Response("", 403, "Test error", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitError.getResponse()).thenReturn(mResponse);
        when(mSubjectProfileClient.getSubjectProfiles(TEST_USER_ID)).thenThrow(retrofitError);
        mSubjectProfileController.getSubjectProfileList();
        verify(mEventing).post(isA(SubjectProfileErrorResponseEvent.class));
    }

    @Test
    public void retrofitErrorWhileDeletingProfileTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mSubjectProfileClient);

        final RetrofitError retrofitError = mock(RetrofitError.class);
        mResponse = new Response("", 403, "Test error", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitError.getResponse()).thenReturn(mResponse);
        when(mSubjectProfileClient.deleteSubjectProfile(TEST_USER_ID, "subjectID")).thenThrow(retrofitError);
        mSubjectProfileController.deleteSubjectProfile("subjectID");
        verify(mEventing).post(isA(SubjectProfileErrorResponseEvent.class));
    }

    @Test
    public void userInvalidWhenCreatingSubjectProfileTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(false);
        assertFalse(mSubjectProfileController.createSubjectProfile(null));
    }

    @Test
    public void userInvalidWhenFetchingProfileListTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(false);
        assertFalse(mSubjectProfileController.getSubjectProfileList());
    }

    @Test
    public void userInvalidWhenFetchingProfileTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(false);
        assertFalse(mSubjectProfileController.getSubjectProfile("subjectID"));
    }

    @Test
    public void userInvalidWhenDeletingProfileTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(false);
        assertFalse(mSubjectProfileController.deleteSubjectProfile("subjectID"));
    }

    @Test
    public void nullAccessProviderTest() throws Exception {
        mSubjectProfileController.mUCoreAccessProvider = null;
        assertThat(mSubjectProfileController.isUserInvalid()).isFalse();
    }
}
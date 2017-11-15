package com.philips.platform.datasync.subjectProfile;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.CreateSubjectProfileRequestEvent;
import com.philips.platform.core.events.DeleteSubjectProfileRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileListRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileListResponseEvent;
import com.philips.platform.core.events.GetSubjectProfileRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileResponseEvent;
import com.philips.platform.core.events.SubjectProfileErrorResponseEvent;
import com.philips.platform.core.events.SubjectProfileResponseEvent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import retrofit.converter.GsonConverter;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SubjectProfileMonitorTest {

    private SubjectProfileMonitor mSubjectProfileMonitor;
    @Mock
    private SubjectProfileController mSubjectProfileController;
    @Mock
    private GsonConverter mGsonConverter;
    @Mock
    private UCoreAdapter mUCoreAdapter;
    @Mock
    private CreateSubjectProfileRequestEvent mCreateSubjectProfileRequestEvent;
    @Mock
    private GetSubjectProfileListRequestEvent mGetSubjectProfileListRequestEvent;
    @Mock
    private GetSubjectProfileRequestEvent mGetSubjectProfileRequestEvent;
    @Mock
    private DeleteSubjectProfileRequestEvent mDeleteSubjectProfileRequestEvent;
    @Mock
    private SubjectProfileResponseEvent mSubjectProfileResponseEvent;
    @Mock
    private SubjectProfileErrorResponseEvent mSubjectProfileErrorResponseEvent;
    @Mock
    private GetSubjectProfileListResponseEvent mGetSubjectProfileListResponseEvent;
    @Mock
    private GetSubjectProfileResponseEvent mGetSubjectProfileResponseEvent;
    @Mock
    private AppComponent mAppComponent;
    @Mock
    private Eventing mEventing;
    @Mock
    private UCoreAccessProvider mUCoreAccessProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(mAppComponent);
        mSubjectProfileMonitor = new SubjectProfileMonitor(mSubjectProfileController);
    }

    @Test
    public void createSubjectProfileRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mSubjectProfileMonitor.onEventAsync(mCreateSubjectProfileRequestEvent);
        verify(mEventing, never()).post(isA(CreateSubjectProfileRequestEvent.class));
    }

    @Test
    public void getSubjectProfileListRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mSubjectProfileMonitor.onEventAsync(mGetSubjectProfileListRequestEvent);
        verify(mEventing, never()).post(isA(GetSubjectProfileListRequestEvent.class));
    }

    @Test
    public void getSubjectProfileRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mSubjectProfileMonitor.onEventAsync(mGetSubjectProfileRequestEvent);
        verify(mEventing, never()).post(isA(GetSubjectProfileRequestEvent.class));
    }

    @Test
    public void deleteSubjectProfileRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mSubjectProfileMonitor.onEventAsync(mDeleteSubjectProfileRequestEvent);
        verify(mEventing, never()).post(isA(DeleteSubjectProfileRequestEvent.class));
    }

    @Test
    public void subjectProfileResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        CreateSubjectProfileRequestEvent createSubjectProfileRequestEvent =
                new CreateSubjectProfileRequestEvent(null, new SubjectProfileListener() {
                    @Override
                    public void onResponse(boolean status) {

                    }

                    @Override
                    public void onError(DataServicesError dataServicesError) {

                    }

                    @Override
                    public void onGetSubjectProfiles(List<UCoreSubjectProfile> uCoreSubjectProfileList) {

                    }
                });
        mSubjectProfileMonitor.onEventAsync(createSubjectProfileRequestEvent);
        mSubjectProfileMonitor.onEventAsync(mSubjectProfileResponseEvent);
        verify(mEventing, never()).post(isA(SubjectProfileResponseEvent.class));
    }

    @Test
    public void subjectProfileErrorResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        CreateSubjectProfileRequestEvent createSubjectProfileRequestEvent =
                new CreateSubjectProfileRequestEvent(null, new SubjectProfileListener() {
                    @Override
                    public void onResponse(boolean status) {

                    }

                    @Override
                    public void onError(DataServicesError dataServicesError) {

                    }

                    @Override
                    public void onGetSubjectProfiles(List<UCoreSubjectProfile> uCoreSubjectProfileList) {

                    }
                });
        mSubjectProfileMonitor.onEventAsync(createSubjectProfileRequestEvent);
        mSubjectProfileMonitor.onEventAsync(mSubjectProfileErrorResponseEvent);
        verify(mEventing, never()).post(isA(SubjectProfileErrorResponseEvent.class));
    }

    @Test(expected = NullPointerException.class)
    public void getSubjectProfileListResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        GetSubjectProfileListRequestEvent getSubjectProfileListRequestEvent =
                new GetSubjectProfileListRequestEvent(new SubjectProfileListener() {
                    @Override
                    public void onResponse(boolean status) {

                    }

                    @Override
                    public void onError(DataServicesError dataServicesError) {

                    }

                    @Override
                    public void onGetSubjectProfiles(List<UCoreSubjectProfile> uCoreSubjectProfileList) {

                    }
                });
        mSubjectProfileMonitor.onEventAsync(getSubjectProfileListRequestEvent);
        mSubjectProfileMonitor.onEventAsync(mGetSubjectProfileListResponseEvent);
        verify(mEventing, never()).post(isA(GetSubjectProfileListResponseEvent.class));
    }

    @Test
    public void getSubjectProfileResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        GetSubjectProfileRequestEvent getSubjectProfileRequestEvent =
                new GetSubjectProfileRequestEvent("subjectID", new SubjectProfileListener() {
                    @Override
                    public void onResponse(boolean status) {

                    }

                    @Override
                    public void onError(DataServicesError dataServicesError) {

                    }

                    @Override
                    public void onGetSubjectProfiles(List<UCoreSubjectProfile> uCoreSubjectProfileList) {

                    }
                });
        mSubjectProfileMonitor.onEventAsync(getSubjectProfileRequestEvent);
        mSubjectProfileMonitor.onEventAsync(mGetSubjectProfileResponseEvent);
        verify(mEventing, never()).post(isA(GetSubjectProfileResponseEvent.class));
    }
}
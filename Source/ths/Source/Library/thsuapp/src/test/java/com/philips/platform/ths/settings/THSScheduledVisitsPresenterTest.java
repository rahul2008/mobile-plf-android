/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.ths.settings;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.visit.Appointment;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class THSScheduledVisitsPresenterTest {

    @Mock
    private THSScheduledVisitsFragment thsScheduledVisitsFragmentMock;

    @Mock
    private AWSDK awsdkMock;

    @Mock
    private Context contextMock;

    private THSScheduledVisitsPresenter mTHSScheduledVisitsPresenter;

    @Mock
    private Appointment appointmentMock;

    @Mock
    private Consumer consumerMock;

    @Mock
    private THSConsumerWrapper thsConsumerWrapper;

    @Mock
    private ConsumerManager consumerManagerMock;

    @Mock
    private SDKLocalDate sdkLocalDateMock;

    @Mock
    private SDKError sdkErrorMock;

    @Mock
    private THSSDKError thssdkErrorMock;

    @Mock
    private Throwable throwableMock;

    @Mock
    private AppInfraInterface appInfraInterface;

    @Mock
    private AppTaggingInterface appTaggingInterface;

    @Mock
    private LoggingInterface loggingInterface;

    @Mock
    private ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    private User userMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);

        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);

        when(userMock.getHsdpUUID()).thenReturn("123");
        when(userMock.getHsdpAccessToken()).thenReturn("123");

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        THSManager.getInstance().setPTHConsumer(thsConsumerWrapper);
        when(thsConsumerWrapper.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsScheduledVisitsFragmentMock.getContext()).thenReturn(contextMock);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(thsScheduledVisitsFragmentMock.isFragmentAttached()).thenReturn(true);
        when(thsScheduledVisitsFragmentMock.isFragmentAttached()).thenReturn(true);

        mTHSScheduledVisitsPresenter = new THSScheduledVisitsPresenter(thsScheduledVisitsFragmentMock);
        when(thsScheduledVisitsFragmentMock.isFragmentAttached()).thenReturn(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void onEventConfirm() throws Exception {
        mTHSScheduledVisitsPresenter.onEvent(R.id.ths_confirmation_dialog_primary_button);
        verify(thsScheduledVisitsFragmentMock).stopRefreshing();
        verify(consumerManagerMock).cancelAppointment(any(Consumer.class), (Appointment)isNull(), (SDKCallback<Void, SDKError>) any());
    }

    @Test
    public void onEventDontConfirm() throws Exception {
        mTHSScheduledVisitsPresenter.onEvent(R.id.ths_confirmation_dialog_secondary_button);
        verifyNoMoreInteractions(awsdkMock);
    }

    @Test
    public void testSetAppointment() {
        mTHSScheduledVisitsPresenter.setAppointment(appointmentMock);
        assert mTHSScheduledVisitsPresenter.mAppointment != null;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void cancelAppointment() throws Exception {
        mTHSScheduledVisitsPresenter.cancelAppointment(appointmentMock);
        verify(consumerManagerMock).cancelAppointment(any(Consumer.class), any(Appointment.class), (SDKCallback<Void, SDKError>) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getAppointmentsSince() throws Exception {
        mTHSScheduledVisitsPresenter.getAppointmentsSince(sdkLocalDateMock);
        verify(consumerManagerMock).getAppointments(any(Consumer.class), any(SDKLocalDate.class), (SDKCallback<List<Appointment>, SDKError>) any());
    }

    @Test
    public void onResponse() throws Exception {
        List<Appointment> list = new ArrayList<>();
        list.add(appointmentMock);
        mTHSScheduledVisitsPresenter.onResponse(list, sdkErrorMock);
        verify(thsScheduledVisitsFragmentMock).updateList(ArgumentMatchers.<Appointment>anyList());
    }

    @Test
    public void onFailure() throws Exception {
        when(thsScheduledVisitsFragmentMock.isFragmentAttached()).thenReturn(false);
        mTHSScheduledVisitsPresenter.onFailure(throwableMock);
    }

    @Test
    public void onInitializationResponse() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(null);
        when(thsScheduledVisitsFragmentMock.isFragmentAttached()).thenReturn(true);
        mTHSScheduledVisitsPresenter.onInitializationResponse(null, thssdkErrorMock);
        verify(thsScheduledVisitsFragmentMock).onRefresh();
    }

    @Test
    public void onInitializationFailure() throws Exception {
        mTHSScheduledVisitsPresenter.onInitializationFailure(throwableMock);
        verify(thsScheduledVisitsFragmentMock).stopRefreshing();
    }

}

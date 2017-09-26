/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
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
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSScheduledVisitsPresenterTest {

    @Mock
    THSScheduledVisitsFragment thsScheduledVisitsFragmentMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    Context contextMock;

    THSScheduledVisitsPresenter mTHSScheduledVisitsPresenter;

    @Mock
    Appointment appointmentMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapper;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    SDKLocalDate sdkLocalDateMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    THSSDKError thssdkErrorMock;

    @Mock
    Throwable throwableMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        THSManager.getInstance().setPTHConsumer(thsConsumerWrapper);
        when(thsConsumerWrapper.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsScheduledVisitsFragmentMock.getContext()).thenReturn(contextMock);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);

        mTHSScheduledVisitsPresenter = new THSScheduledVisitsPresenter(thsScheduledVisitsFragmentMock);
    }

    @Test
    public void onEvent() throws Exception {
        mTHSScheduledVisitsPresenter.onEvent(0);
    }

    @Test
    public void cancelAppointment() throws Exception {
        mTHSScheduledVisitsPresenter.cancelAppointment(appointmentMock);
        verify(consumerManagerMock).cancelAppointment(any(Consumer.class),any(Appointment.class),any(SDKCallback.class));
    }

    @Test
    public void getAppointmentsSince() throws Exception {
        mTHSScheduledVisitsPresenter.getAppointmentsSince(sdkLocalDateMock);
        verify(consumerManagerMock).getAppointments(any(Consumer.class),any(SDKLocalDate.class),any(SDKCallback.class));
    }

    @Test
    public void onResponse() throws Exception {
        List list = new ArrayList();
        list.add(appointmentMock);
        mTHSScheduledVisitsPresenter.onResponse(list,sdkErrorMock);
        verify(thsScheduledVisitsFragmentMock).updateList(anyList());
    }

    @Test
    public void onFailure() throws Exception {
        mTHSScheduledVisitsPresenter.onFailure(throwableMock);
        verify(thsScheduledVisitsFragmentMock).hideProgressBar();
    }

    @Test
    public void onInitializationResponse() throws Exception {
        mTHSScheduledVisitsPresenter.onInitializationResponse(null,thssdkErrorMock);
      //  verify(appInfraInterface).getTagging().trackActionWithInfo(anyString(),anyMap());
    }

    @Test
    public void onInitializationFailure() throws Exception {
        mTHSScheduledVisitsPresenter.onInitializationFailure(throwableMock);
        verify(thsScheduledVisitsFragmentMock).hideProgressBar();
    }

}
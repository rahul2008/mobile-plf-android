/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.RemindOptions;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsDisplayHelper;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSAvailableProviderDetailPresenterTest {

    THSAvailableProviderDetailPresenter mThsAvailableProviderDetailPresenter;

    @Mock
    THSAvailableProviderDetailFragment thsAvailableProviderDetailFragmentMock;

    @Mock
    THSProviderDetailsDisplayHelper thsProviderDetailsDisplayHelperMock;

    @Mock
    Map map;

    @Mock
    ConsumerManager consumerManagerMock;


    @Captor
    private ArgumentCaptor<DatePickerDialog.OnDateSetListener> dateCallback;

    @Mock
    Context contextMock;

    @Mock
    ProviderInfo providerInfo;

    @Mock
    THSProviderInfo thsProviderInfoMock;

    @Mock
    PracticeProvidersManager practiceProvidersManagerMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    THSSDKError thssdkError;

    @Mock
    Provider providerMock;

    @Mock
    Throwable throwableMock;

    @Mock
    Date dateMock;

    @Mock
    EstimatedVisitCost estimatedVisitCostMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;


    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);
        when(thsProviderInfoMock.getProviderInfo()).thenReturn(providerInfo);
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiceProvidersManagerMock);
        when(thsAvailableProviderDetailFragmentMock.getContext()).thenReturn(contextMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

       // when(thsAvailableProviderDetailFragmentMock.getString(R.string.something_went_wrong)).thenReturn("123");
        mThsAvailableProviderDetailPresenter = new THSAvailableProviderDetailPresenter(thsAvailableProviderDetailFragmentMock,thsProviderDetailsDisplayHelperMock);
    }

    @Test
    public void onEvent_set_reminder_layout() throws Exception {
        mThsAvailableProviderDetailPresenter.onEvent(R.id.set_reminder_layout);
        verify(thsProviderDetailsDisplayHelperMock).launchSetRemainderDialogFragment(any(THSBaseFragment.class));
    }

    @Test
    public void onEvent_calendar_container() throws Exception {
        when(thsAvailableProviderDetailFragmentMock.getContext()).thenReturn(contextMock);
        mThsAvailableProviderDetailPresenter.onEvent(R.id.calendar_container);
    }

    @Test
    public void updateContinueButtonStateEnabledTrue() throws Exception {
        mThsAvailableProviderDetailPresenter.updateContinueButtonState(true);
        verify(thsProviderDetailsDisplayHelperMock).updateContinueButtonState(true);
    }

    @Test
    public void updateContinueButtonStateEnabledFalse() throws Exception {
        mThsAvailableProviderDetailPresenter.updateContinueButtonState(false);
        verify(thsProviderDetailsDisplayHelperMock).updateContinueButtonState(false);
    }

    @Test
    public void fetchProviderDetails() throws Exception {
        mThsAvailableProviderDetailPresenter.fetchProviderDetails(contextMock,thsProviderInfoMock);
        verify(practiceProvidersManagerMock).getProvider(any(ProviderInfo.class),any(Consumer.class),any(SDKCallback.class));
    }

    @Test
    public void onProviderDetailsReceived() throws Exception {
        when(thsAvailableProviderDetailFragmentMock.isFragmentAttached()).thenReturn(true);
        sdkErrorMock = null;
        mThsAvailableProviderDetailPresenter.onProviderDetailsReceived(providerMock,sdkErrorMock);
        verify(practiceProvidersManagerMock).getEstimatedVisitCost(any(Consumer.class),any(Provider.class),any(SDKCallback.class));
    }

    @Test
    public void onProviderDetailsFetchError() throws Exception {
        when(thsAvailableProviderDetailFragmentMock.isFragmentAttached()).thenReturn(true);
        mThsAvailableProviderDetailPresenter.onProviderDetailsFetchError(throwableMock);
        verify(thsAvailableProviderDetailFragmentMock).showToast(anyInt());
    }

    @Test
    public void onResponseAvailableProviderList() throws Exception {
        List list = new ArrayList();
        list.add(dateMock);
        when(thssdkError.getSdkError()).thenReturn(sdkErrorMock);
        when(thsAvailableProviderDetailFragmentMock.isFragmentAttached()).thenReturn(true);
        mThsAvailableProviderDetailPresenter.onResponse((List<Date>)list,thssdkError);
        verify(thsAvailableProviderDetailFragmentMock).hideProgressBar();
    }

    @Test
    public void onResponse() throws Exception {
        List list = new ArrayList();
        list.add(dateMock);
        sdkErrorMock = null;
        mThsAvailableProviderDetailPresenter.onResponse(list,sdkErrorMock);
        verify(thsProviderDetailsDisplayHelperMock).launchConfirmAppointmentFragment(0);
    }

    @Test
    public void onFailure() throws Exception {
        when(thsAvailableProviderDetailFragmentMock.isFragmentAttached()).thenReturn(true);
        mThsAvailableProviderDetailPresenter.onFailure(throwableMock);
        verify(thsAvailableProviderDetailFragmentMock).hideProgressBar();
    }

    @Test
    public void onEstimatedCostFetchSuccess() throws Exception {
        mThsAvailableProviderDetailPresenter.onEstimatedCostFetchSuccess(estimatedVisitCostMock,sdkErrorMock);
        verify(thsProviderDetailsDisplayHelperMock).updateEstimateCost(any(EstimatedVisitCost.class));
    }

    @Test
    public void onError() throws Exception {
        mThsAvailableProviderDetailPresenter.onError(throwableMock);
    }

    @Test
    public void scheduleAppointment() throws Exception {
        when(thsAvailableProviderDetailFragmentMock.getTHSProviderInfo()).thenReturn(thsProviderInfoMock);
        when(thsProviderInfoMock.getProviderInfo()).thenReturn(providerInfo);
        mThsAvailableProviderDetailPresenter.dateList = new ArrayList<>();
        mThsAvailableProviderDetailPresenter.dateList.add(dateMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsAvailableProviderDetailFragmentMock.getReminderOptions()).thenReturn(RemindOptions.EIGHT_HOURS);
        mThsAvailableProviderDetailPresenter.scheduleAppointment(0);
    }

    @Test
    public void scheduleAppointmentOnError() throws Exception {
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).scheduleAppointment(any(Consumer.class),any(ProviderInfo.class),any(Date.class),anyString(),any(RemindOptions.class),any(RemindOptions.class),any(SDKValidatedCallback.class));
        when(thsAvailableProviderDetailFragmentMock.getTHSProviderInfo()).thenReturn(thsProviderInfoMock);
        when(thsProviderInfoMock.getProviderInfo()).thenReturn(providerInfo);
        mThsAvailableProviderDetailPresenter.dateList = new ArrayList<>();
        mThsAvailableProviderDetailPresenter.dateList.add(dateMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsAvailableProviderDetailFragmentMock.getReminderOptions()).thenReturn(RemindOptions.EIGHT_HOURS);
        mThsAvailableProviderDetailPresenter.scheduleAppointment(0);
        verify(consumerManagerMock).scheduleAppointment(any(Consumer.class),any(ProviderInfo.class),any(Date.class),anyString(),any(RemindOptions.class),any(RemindOptions.class),any(SDKValidatedCallback.class));
    }

    @Test
    public void onValidationFailure() throws Exception {
        mThsAvailableProviderDetailPresenter.onValidationFailure(map);
    }

}
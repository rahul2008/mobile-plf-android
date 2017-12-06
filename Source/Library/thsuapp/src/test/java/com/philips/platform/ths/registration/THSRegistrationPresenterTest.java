/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.enrollment.ConsumerEnrollment;
import com.americanwell.sdk.entity.enrollment.DependentEnrollment;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.ProgressBarButton;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Map;

import static com.americanwell.sdk.entity.SDKErrorReason.AUTH_SCHEDULED_DOWNTIME;
import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSRegistrationPresenterTest {

    THSRegistrationPresenter mTHSRegistrationPresenter;

    @Mock
    THSRegistrationFragment thsRegistrationFragmentMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    Consumer consumerMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    THSSDKError thssdkErrorMock;

    @Mock
    Map mapMock;

    @Mock
    ProgressBarButton buttonMock;

    @Mock
    Throwable throwableMock;

    @Mock
    Date dateMock;

    @Mock
    State stateMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    DatePickerDialog.OnDateSetListener onDateSetListenerMock;

    @Mock
    ConsumerEnrollment consumerEnrollmentMock;

    @Mock
    DependentEnrollment dependentEnrollmentMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    THSSDKErrorFactory thssdkErrorFactory;

    @Captor
    private ArgumentCaptor<DatePickerDialog.OnDateSetListener> requestCaptor;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    Context contextMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

        when(thsConsumerMock.getFirstName()).thenReturn("fn");
        when(thsConsumerMock.getHsdpUUID()).thenReturn("123");
        when(thsConsumerMock.getEmail()).thenReturn("222");

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(thsRegistrationFragmentMock.isFragmentAttached()).thenReturn(true);

        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        mTHSRegistrationPresenter = new THSRegistrationPresenter(thsRegistrationFragmentMock);
        when(consumerManagerMock.getNewConsumerEnrollment()).thenReturn(consumerEnrollmentMock);
        when(consumerManagerMock.getNewDependentEnrollment(consumerMock)).thenReturn(dependentEnrollmentMock);
        thsRegistrationFragmentMock.mContinueButton = buttonMock;
    }

    @Mock
    THSBaseFragment thsBaseFragment;
    @Test
    public void onResponseError() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(thsRegistrationFragmentMock.isFragmentAttached()).thenReturn(true);
        when(sdkErrorMock.getSDKErrorReason()).thenReturn(AUTH_SCHEDULED_DOWNTIME);

        /*mTHSRegistrationPresenter.onResponse(thsConsumerWrapperMock,sdkErrorMock);
        verify(thsRegistrationFragmentMock).showError(THSSDKErrorFactory.getErrorType(" ",sdkErrorMock));*/
    }

    @Test
    public void onResponseSuccessDefault() throws Exception {
        when(thsRegistrationFragmentMock.isFragmentAttached()).thenReturn(true);
        sdkErrorMock = null;
        mTHSRegistrationPresenter.onResponse(thsConsumerWrapperMock,sdkErrorMock);
        verify(thsRegistrationFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test
    public void onResponseSuccessTHS_PRACTICES() throws Exception {
        when(thsRegistrationFragmentMock.isFragmentAttached()).thenReturn(true);
        thsRegistrationFragmentMock.mLaunchInput = THSConstants.THS_PRACTICES;
        sdkErrorMock = null;
        mTHSRegistrationPresenter.onResponse(thsConsumerWrapperMock,sdkErrorMock);
        verify(thsRegistrationFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test
    public void onResponseSuccessNo_input() throws Exception {
        when(thsRegistrationFragmentMock.isFragmentAttached()).thenReturn(true);
        thsRegistrationFragmentMock.mLaunchInput = -1;
        sdkErrorMock = null;
        mTHSRegistrationPresenter.onResponse(thsConsumerWrapperMock,sdkErrorMock);
        verify(thsRegistrationFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test
    public void onResponseSuccessTHS_SCHEDULED_VISITS() throws Exception {
        when(thsRegistrationFragmentMock.isFragmentAttached()).thenReturn(true);
        thsRegistrationFragmentMock.mLaunchInput = THSConstants.THS_SCHEDULED_VISITS;
        sdkErrorMock = null;
        mTHSRegistrationPresenter.onResponse(thsConsumerWrapperMock,sdkErrorMock);
        verify(thsRegistrationFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test
    public void onResponseSuccessTHS_VISITS_HISTORY() throws Exception {
        when(thsRegistrationFragmentMock.isFragmentAttached()).thenReturn(true);
        sdkErrorMock = null;
        thsRegistrationFragmentMock.mLaunchInput = THSConstants.THS_VISITS_HISTORY;
        mTHSRegistrationPresenter.onResponse(thsConsumerWrapperMock,sdkErrorMock);
        verify(thsRegistrationFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test(expected = IllegalStateException.class)
    public void onFailure() throws Exception {
        mTHSRegistrationPresenter.onFailure(throwableMock);
        verify(thsRegistrationFragmentMock).showError(anyString());
    }


    @Test(expected = IllegalStateException.class)
    public void onValidationFailure() throws Exception {
        mTHSRegistrationPresenter.onValidationFailure(mapMock);
        verify(thsRegistrationFragmentMock).showError(anyString());
        verify(thsRegistrationFragmentMock).hideProgressBar();
    }

    @Test
    public void enrollUser() throws Exception {
        mTHSRegistrationPresenter.enrollUser(dateMock,"spoo","hallur",Gender.MALE,stateMock);
        verify(consumerManagerMock).enrollConsumer(any(ConsumerEnrollment.class),any(SDKValidatedCallback.class));
    }

    @Test
    public void enrollDependent() throws Exception {
        mTHSRegistrationPresenter.enrollDependent(dateMock,"spoo","hall",Gender.FEMALE,stateMock);
        verify(consumerManagerMock).enrollDependent(any(DependentEnrollment.class),any(SDKValidatedCallback.class));
    }

    @Test
    public void onEvent_ths_edit_dob(){
        mTHSRegistrationPresenter.onEvent(R.id.ths_edit_dob);
    }

    @Test(expected = IllegalStateException.class)
    public void validateNameValid(){
        mTHSRegistrationPresenter.validateName("Spoorti", false);
    }

    @Test(expected = IllegalStateException.class)
    public void validateNameAlphanumericName(){
        mTHSRegistrationPresenter.validateName("Spoorti12223785*&%$", false);
    }

    @Test(expected = IllegalStateException.class)
    public void validateNameTwoChars(){
        mTHSRegistrationPresenter.validateName("S", false);
    }

    @Test
    public void validateDOB(){
        THSManager.getInstance().getThsConsumer(contextMock).setDependent(true);
        final boolean isDateValid = mTHSRegistrationPresenter.validateDOB(dateMock);
        assert isDateValid == true;
    }

    @Test
    public void validateDOBNotDependent(){
        THSManager.getInstance().getThsConsumer(contextMock).setDependent(true);
        final boolean isDateValid = mTHSRegistrationPresenter.validateDOB(dateMock);
        assert isDateValid == true;
    }

    @Test
    public void validateDOBNull(){
        THSManager.getInstance().getThsConsumer(contextMock).setDependent(true);
        final boolean isDateValid = mTHSRegistrationPresenter.validateDOB(null);
        assert isDateValid == false;
    }

    @Test
    public void validateLocationNull(){
        final boolean isLocationNull = mTHSRegistrationPresenter.validateLocation(null);
        assert isLocationNull == false;
    }

    @Test
    public void validateLocationNotNull(){
        final boolean isLocationNull = mTHSRegistrationPresenter.validateLocation("MA");
        assert isLocationNull == false;
    }
}
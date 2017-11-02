/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import android.os.Bundle;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.enrollment.ConsumerEnrollment;
import com.americanwell.sdk.entity.enrollment.DependentEnrollment;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.ProgressBarButton;

import org.junit.Before;
import org.junit.Test;
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
    ConsumerEnrollment consumerEnrollmentMock;

    @Mock
    DependentEnrollment dependentEnrollmentMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    THSSDKErrorFactory thssdkErrorFactory;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

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
        THSManager.getInstance().setAppInfra(appInfraInterface);

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

    @Test
    public void onFailure() throws Exception {
        mTHSRegistrationPresenter.onFailure(throwableMock);
    }


    @Test
    public void onValidationFailure() throws Exception {
        mTHSRegistrationPresenter.onValidationFailure(mapMock);
    }

    @Test
    public void enrollUser() throws Exception {
        mTHSRegistrationPresenter.enrollUser(dateMock,"spoo","hallur",Gender.MALE,stateMock);
    }

    @Test
    public void enrollDependent() throws Exception {
        mTHSRegistrationPresenter.enrollDependent(dateMock,"spoo","hall",Gender.FEMALE,stateMock);
    }

}
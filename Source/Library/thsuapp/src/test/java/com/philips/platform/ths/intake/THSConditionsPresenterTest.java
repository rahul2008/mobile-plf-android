package com.philips.platform.ths.intake;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class THSConditionsPresenterTest {

    THSMedicalConditionsPresenter thsMedicalConditionsPresenter;

    @Mock
    THSMedicalConditionsFragment pTHBaseViewMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    FragmentActivity fragmentActivityMock;



    @Mock
    Activity mActivity;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumerWrapper pthConsumerMock;

    @Mock
    THSConditionsList thsConditions;

    @Mock
    THSSDKError pthsdkError;

    @Mock
    Condition condition1Mock;

    @Mock
    Condition condition2Mock;

    @Mock
    Throwable throwableMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSConsumer thsConsumerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        thsMedicalConditionsPresenter = new THSMedicalConditionsPresenter(pTHBaseViewMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(pTHBaseViewMock.isFragmentAttached()).thenReturn(true);

        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
    }

    @Test
    public void onEventContinueBtn() throws Exception {
        thsMedicalConditionsPresenter.onEvent(R.id.continue_btn);
        verify(consumerManagerMock).updateConditions(any(Consumer.class),anyList(),any(SDKCallback.class));
    }

    @Test
    public void onEventSkipBtn() throws Exception {
        thsMedicalConditionsPresenter.onEvent(R.id.conditions_skip);
        verify(pTHBaseViewMock, atLeast(1)).addFragment(any(THSFollowUpFragment.class),anyString(),any(Bundle.class), anyBoolean());
    }

    @Test
    public void getConditions() throws Exception {

    }

    @Test
    public void onResponse() throws Exception {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition1Mock);
        conditions.add(condition2Mock);
        when(thsConditions.getConditions()).thenReturn(conditions);
        thsMedicalConditionsPresenter.onResponse(thsConditions, pthsdkError);
        verify(pTHBaseViewMock).setConditions(anyList());
    }

    @Test
    public void onFailure() throws Exception {
        when(pTHBaseViewMock.isFragmentAttached()).thenReturn(true);
        thsMedicalConditionsPresenter.onFailure(throwableMock);
        verify(pTHBaseViewMock).showToast(anyInt());
    }

    @Test
    public void onUpdateConditonResponse() throws Exception {
        thsMedicalConditionsPresenter.onUpdateConditonResponse(null,pthsdkError);
    }

    @Test
    public void onUpdateConditionFailure() throws Exception {
        thsMedicalConditionsPresenter.onUpdateConditionFailure(throwableMock);
    }

    @Test
    public void updateConditionsFailsWithException(){
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).updateConditions(any(Consumer.class),anyList(),any(SDKCallback.class));
        thsMedicalConditionsPresenter.onEvent(R.id.continue_btn);
        verify(consumerManagerMock).updateConditions(any(Consumer.class),anyList(),any(SDKCallback.class));
    }

}
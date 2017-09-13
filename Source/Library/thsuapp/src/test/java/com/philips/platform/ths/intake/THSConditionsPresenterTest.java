package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumer;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSConditionsPresenterTest {

    THSMedicalConditionsPresenter thsMedicalConditionsPresenter;

    @Mock
    THSMedicalConditionsFragment pTHBaseViewMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    FragmentActivity activityMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumer pthConsumerMock;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsMedicalConditionsPresenter = new THSMedicalConditionsPresenter(pTHBaseViewMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(activityMock);
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
        verify(pTHBaseViewMock).addFragment(any(THSFollowUpFragment.class),anyString(),any(Bundle.class));
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
        thsMedicalConditionsPresenter.onFailure(throwableMock);
        verify(pTHBaseViewMock).showToast(anyString());
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
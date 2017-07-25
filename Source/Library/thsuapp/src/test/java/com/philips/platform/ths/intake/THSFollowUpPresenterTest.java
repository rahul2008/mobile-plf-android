package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerInfo;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.ths.R;
import com.philips.platform.ths.practice.THSPracticePresenter;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 7/16/17.
 */

public class THSFollowUpPresenterTest {

    Method mMethod;
    THSFollowUpPresenter tHSFollowUpPresenter;

    @Mock
    THSFollowUpFragment  tHSFollowUpFragmentMock;

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
    THSSDKPasswordError tHSSDKPasswordError;

    @Mock
    Throwable throwableMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tHSFollowUpPresenter = new THSFollowUpPresenter(tHSFollowUpFragmentMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumerMock);
        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(tHSFollowUpFragmentMock.getFragmentActivity()).thenReturn(activityMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
    }

    @Test
    public void onEventContinueBtn() throws Exception {
        tHSFollowUpPresenter.onEvent(R.id.pth_intake_follow_up_continue_button);
        verify(consumerManagerMock).updateConditions(any(Consumer.class),anyList(),any(SDKCallback.class));
        //verify(tHSFollowUpFragmentMock).addFragment(any(THSFollowUpFragment.class),anyString(),any(Bundle.class));
        // TODO here next fragment name to be assigned ie Pharmacy
    }

    @Test
    public void onUpdateConsumerResponse() throws Exception {


        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);

        tHSFollowUpPresenter.onUpdateConsumerResponse(pthConsumerMock,tHSSDKPasswordError);
    }

    @Test
    public void onUpdateConsumerValidationFailure() {
        tHSFollowUpPresenter.onUpdateConsumerValidationFailure(any(Map.class));
    }

    @Test
    public void onUpdateConsumerFailure() throws Exception{
        tHSFollowUpPresenter.onUpdateConsumerFailure(throwableMock);
    }

    @Test
    public void updateConsumer()  {
        try {
            mMethod = THSPracticePresenter.class.getDeclaredMethod("updateConsumer", String.class);
            mMethod.setAccessible(true);
            mMethod.invoke(tHSFollowUpPresenter,"12345678"); // phone number
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException  e) {
            e.printStackTrace();
        }
    }
}

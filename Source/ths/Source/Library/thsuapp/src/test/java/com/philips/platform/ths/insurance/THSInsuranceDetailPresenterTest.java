package com.philips.platform.ths.insurance;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.americanwell.sdk.entity.insurance.Relationship;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.entity.insurance.SubscriptionUpdateRequest;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */


public class THSInsuranceDetailPresenterTest {

    THSInsuranceDetailFragment thsInsuranceDetailFragment;

    THSInsuranceDetailPresenter thsInsuranceDetailPresenter;

    Method mMethod;

    @Mock
    Map<String, ValidationReason> validationReasonMapMock;


    @Mock
    AWSDK awsdkMock;

    @Mock
    Consumer consumerMoxk;

    @Mock
    THSConsumerWrapper thsConsumerMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSSDKError thssdkError;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    Map mapMock;

    @Mock
    THSSubscriptionUpdateRequest THSSubscriptionUpdateRequestMock;

    @Mock
    SubscriptionUpdateRequest SubscriptionUpdateRequestMock;

    @Mock
    Subscription SubscriptionMock;


    Throwable throwableMock;

    @Mock
    Context ContextMock;

    @Mock
    Relationship relationshipMock;

    @Mock
    HealthPlan healthPlanMock;

    @Mock
    THSSubscription thsSubscriptionMock;


    Void aVoid;

    @Mock
    FragmentManager fragmentManagerMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMoxk);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(consumerManagerMock.getNewSubscriptionUpdateRequest(consumerMoxk, false)).thenReturn(SubscriptionUpdateRequestMock);
        when(SubscriptionUpdateRequestMock.getSubscription()).thenReturn(SubscriptionMock);
        thsInsuranceDetailFragment = new THSInsuranceDetailFragmentMock();
        thsInsuranceDetailFragment.setActionBarListener(actionBarListenerMock);

        thsInsuranceDetailPresenter = new THSInsuranceDetailPresenter(thsInsuranceDetailFragment);
        //when(thsInsuranceDetailFragment.getFragmentManager()).thenReturn(fragmentManagerMock);


    }

    @Test
    public void fetchHealthPlanList() throws Exception {
        assertNotNull(thsInsuranceDetailPresenter.fetchHealthPlanList());

    }

    @Test
    public void fetchSubscriberRelationList() throws Exception {
        assertNotNull(thsInsuranceDetailPresenter.fetchSubscriberRelationList());
    }

    @Test
    public void fetchExistingSubscription() throws Exception {
        try {
            mMethod = THSInsuranceDetailPresenter.class.getDeclaredMethod("fetchExistingSubscription");
            mMethod.setAccessible(true);
            mMethod.invoke(thsInsuranceDetailPresenter);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void updateTHSInsuranceSubscription() {
        try {
            thsInsuranceDetailPresenter.updateTHSInsuranceSubscription(healthPlanMock, "subscriberId", "suffix", relationshipMock, "firstName", "lastName", "1991-05-12");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void onEvent() throws Exception {
        thsInsuranceDetailPresenter.onEvent(R.id.ths_insurance_detail_continue_button);

    }

    @Test
    public void onGetInsuranceResponse() throws Exception {
        thsInsuranceDetailPresenter.onGetInsuranceResponse(thsSubscriptionMock,thssdkError);



        //THSInsuranceDetailFragment fragment = THSInsuranceDetailFragment.class.newInstance();

        verifyNoMoreInteractions(awsdkMock);
    }

    @Test
    public void onGetInsuranceFailure() throws Exception {
        Throwable t = new Throwable();
        thsInsuranceDetailPresenter.onGetInsuranceFailure(t);
    }

  /*  @Test
    public void onValidationFailure() throws Exception {
        thsInsuranceDetailPresenter.onValidationFailure(validationReasonMapMock);
    }*/

    @Test
    public void onResponse() throws Exception {
        thsInsuranceDetailPresenter.onResponse(aVoid,sdkErrorMock);
    }

   /* @Test
    public void onFailure() throws Exception {
        thsInsuranceDetailPresenter.onFailure(throwableMock);
    }*/

}
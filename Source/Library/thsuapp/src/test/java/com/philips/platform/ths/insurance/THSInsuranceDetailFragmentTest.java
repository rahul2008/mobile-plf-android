package com.philips.platform.ths.insurance;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.americanwell.sdk.entity.insurance.Relationship;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

//@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSInsuranceDetailFragmentTest {
    THSInsuranceDetailFragment thsInsuranceDetailFragment;


    @Mock
    AWSDK awsdkMock;

    @Mock
    Consumer consumerMoxk;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSInsuranceDetailPresenter THSInsuranceDetailPresenterMock;

    @Mock
    THSSubscription thsSubscriptionMock;

    @Mock
    Subscription SubscriptionMock;

    @Mock
    HealthPlan healthPlanMock;

    @Mock
    Relationship relationshipMock;


    @Mock
    SDKLocalDate sdkLocalDate;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMoxk);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        thsInsuranceDetailFragment = new THSInsuranceDetailFragment();
        thsInsuranceDetailFragment.setActionBarListener(actionBarListenerMock);


    }

    @Test
    public void onCreateView() throws Exception {

    }

    @Test
    public void onActivityCreated() throws Exception {

    }

    @Test
    public void showProgressbar() throws Exception {

    }

    @Test
    public void onResume() throws Exception {

    }

    @Test
    public void onClick() throws Exception {

    }

    @Test
    public void updateInsuranceUI() throws Exception {
       /* SupportFragmentTestUtil.startFragment(thsInsuranceDetailFragment);
        thsInsuranceDetailFragment.mPresenter = THSInsuranceDetailPresenterMock;


        when(thsSubscriptionMock.getSubscription()).thenReturn(SubscriptionMock);
        when(SubscriptionMock.getHealthPlan()).thenReturn(healthPlanMock);
        when(healthPlanMock.getName()).thenReturn("Health Plan Name");
        when(healthPlanMock.isUsesSuffix()).thenReturn(true);
        when(SubscriptionMock.getSubscriberId()).thenReturn("subscriber ID");
        when(SubscriptionMock.getSubscriberSuffix()).thenReturn("suffix");
        when(SubscriptionMock.getRelationship()).thenReturn(relationshipMock);
        when(relationshipMock.isPrimarySubscriber()).thenReturn(false);
        when(SubscriptionMock.getPrimarySubscriberFirstName()).thenReturn("first name");
        when(SubscriptionMock.getPrimarySubscriberLastName()).thenReturn("last name");
        when(SubscriptionMock.getPrimarySubscriberDateOfBirth()).thenReturn(SDKLocalDate.valueOf("1992-05-12"));

        thsInsuranceDetailFragment.updateInsuranceUI(thsSubscriptionMock);*/
    }

    @Test
    public void showDatePicker() throws Exception {

    }

}
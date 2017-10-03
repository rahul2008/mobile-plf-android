package com.philips.platform.ths.insurance;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
public class THSInsuranceConfirmationFragmentTest {

    THSInsuranceConfirmationFragment tHSInsuranceConfirmationFragment;


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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMoxk);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        tHSInsuranceConfirmationFragment = new THSInsuranceConfirmationFragmentMock();
        tHSInsuranceConfirmationFragment.setActionBarListener(actionBarListenerMock);

    }

}
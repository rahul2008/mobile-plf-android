package com.philips.cdp.registration.update;

import android.content.Context;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.ConsumerInterest;
import com.philips.cdp.registration.handlers.UpdateConsumerInterestHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

/**
 * Created by philips on 11/30/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConsumerInterestUpdateTest {


    @Mock
    private RegistrationComponent mockRegistrationComponent;
    @Mock
    private LoggingInterface mockLoggingInterface;

    ConsumerInterestUpdate consumerInterestUpdate;
    @Mock
    private Context contextMock;

    @Mock
    private UpdateConsumerInterestHandler updateConsumerInterestHandlerMock;

    @Mock
    private ArrayList<ConsumerInterest> consumerInterests;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        RLog.setMockLogger(mockLoggingInterface);

        consumerInterestUpdate=new ConsumerInterestUpdate();
    }

    @Mock
    ConsumerInterest consumerInterestMock;

    @Test(expected = NullPointerException.class)
    public void updateConsumerInterest() throws Exception {
        consumerInterests.add(consumerInterestMock);
        consumerInterestUpdate.updateConsumerInterest(contextMock,updateConsumerInterestHandlerMock,consumerInterests);
    }

}
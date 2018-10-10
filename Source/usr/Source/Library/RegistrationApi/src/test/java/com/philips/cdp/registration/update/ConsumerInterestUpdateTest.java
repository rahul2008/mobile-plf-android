package com.philips.cdp.registration.update;

import android.content.Context;

import com.janrain.android.capture.CaptureApiError;
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
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class ConsumerInterestUpdateTest {

    @Mock
    private ConsumerInterest consumerInterestMock;

    @Mock
    private RegistrationComponent mockRegistrationComponent;
    @Mock
    private LoggingInterface mockLoggingInterface;

    private ConsumerInterestUpdate consumerInterestUpdate;
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

        consumerInterestUpdate = new ConsumerInterestUpdate();
    }


    @Test(expected = NoClassDefFoundError.class)
    public void updateConsumerInterest() throws Exception {
        consumerInterests.add(consumerInterestMock);
        consumerInterestUpdate.updateConsumerInterest(contextMock, updateConsumerInterestHandlerMock, consumerInterests);
    }

    @Test
    public void testConvertConsumerArrayToJOSNString() {
        ArrayList<ConsumerInterest> consumerInterestList = new ArrayList<ConsumerInterest>();
        ConsumerInterest consumerInterest = new ConsumerInterest();
        consumerInterest.setCampaignName("campaignName");
        consumerInterest.setSubjectArea("subjectArea");
        consumerInterest.setTopicCommunicationKey("topicCommunicationKey");
        consumerInterest.setTopicValue("topicValue");
        consumerInterestList.add(consumerInterest);

        Method method = null;
        try {
            method = ConsumerInterestUpdate.class.getDeclaredMethod("convertConsumerArrayToJOSNString", ArrayList.class);
            method.setAccessible(true);
            method.invoke(consumerInterestUpdate, consumerInterestList);

            consumerInterestList = null;
            method = ConsumerInterestUpdate.class.getDeclaredMethod("convertConsumerArrayToJOSNString", ArrayList.class);
            method.setAccessible(true);
            method.invoke(consumerInterestUpdate, consumerInterestList);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //startUpdateTask(UpdateConsumerInterestHandler updateConsumerInterestHandler, String attributes)
    @Test
    public void testStartUpdateTask() {
        Method method = null;
        UpdateConsumerInterestHandler updateConsumerInterestHandler = new UpdateConsumerInterestHandler() {
            @Override
            public void onUpdateConsumerInterestSuccess() {

            }

            @Override
            public void onUpdateConsumerInterestFailedWithError(CaptureApiError error) {

            }
        };
        String attributes = "sample";
        try {
            method = ConsumerInterestUpdate.class.getDeclaredMethod("startUpdateTask", new Class[]{UpdateConsumerInterestHandler.class, String.class});
            method.setAccessible(true);
            method.invoke(consumerInterestUpdate, new Object[]{updateConsumerInterestHandler, attributes});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
package com.philips.cdp.registration.update;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.dao.ConsumerInterest;
import com.philips.cdp.registration.handlers.UpdateConsumerInterestHandler;

import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 9/16/2016.
 */
public class ConsumerInterestUpdateTest extends InstrumentationTestCase{

    ConsumerInterestUpdate mConsumerInterestUpdate;
    Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        mConsumerInterestUpdate = new ConsumerInterestUpdate();
        assertNotNull(mConsumerInterestUpdate);
    }

 public void testConvertConsumerArrayToJOSNString(){
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
         method.invoke(mConsumerInterestUpdate,consumerInterestList);

         consumerInterestList = null;
         method = ConsumerInterestUpdate.class.getDeclaredMethod("convertConsumerArrayToJOSNString", ArrayList.class);
         method.setAccessible(true);
         method.invoke(mConsumerInterestUpdate,consumerInterestList);
     } catch (NoSuchMethodException e) {
         e.printStackTrace();
     } catch (InvocationTargetException e) {
         e.printStackTrace();
     } catch (IllegalAccessException e) {
         e.printStackTrace();
     }
 }
    //startUpdateTask(UpdateConsumerInterestHandler updateConsumerInterestHandler, String attributes)

    public void testStartUpdateTask(){
        Method method = null;
        UpdateConsumerInterestHandler updateConsumerInterestHandler = new UpdateConsumerInterestHandler() {
            @Override
            public void onUpdateConsumerInterestSuccess() {

            }

            @Override
            public void onUpdateConsumerInterestFailedWithError(CaptureApiError error) {

            }
        };
        String attributes= "sample";
        try {
            method = ConsumerInterestUpdate.class.getDeclaredMethod("startUpdateTask", new Class[]{UpdateConsumerInterestHandler.class,String.class});
            method.setAccessible(true);
            method.invoke(mConsumerInterestUpdate,new Object[]{updateConsumerInterestHandler, attributes});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
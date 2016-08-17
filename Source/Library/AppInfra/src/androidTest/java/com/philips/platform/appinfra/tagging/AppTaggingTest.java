package com.philips.platform.appinfra.tagging;

import android.app.Application;
import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Created by 310238655 on 4/29/2016.
 */
public class AppTaggingTest extends MockitoTestCase {

    private Context context;
    private AppInfra mAppInfra;

    AppTaggingInterface mAIAppTaggingInterface;
    AppTaggingInterface mockAppTaggingInterface;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();

        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        mAIAppTaggingInterface = mAppInfra.getTagging().createInstanceForComponent("Component name","Component ID");
        mockAppTaggingInterface = mock(AppTaggingInterface.class);
    }


    public void testSetPreviousPage(){
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockAppTaggingInterface).setPreviousPage("SomePreviousPage");
        mAIAppTaggingInterface.setPreviousPage("SomePreviousPage");
    }

    public void testPrivacyConsent(){
        mAIAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTIN, mAIAppTaggingInterface.getPrivacyConsent());
        mAIAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTOUT);
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTOUT, mAIAppTaggingInterface.getPrivacyConsent());
        mAIAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.UNKNOWN);
        //assertEquals(AppTaggingInterface.PrivacyStatus.UNKNOWN, mAIAppTaggingInterface.getPrivacyConsent());
    }

    public void testTrackPageWithInfo() {

       mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", "key1", "value1");
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockAppTaggingInterface).trackPageWithInfo("AppTaggingDemoPage", "key1", "value1");

        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", keyValuePair);
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockAppTaggingInterface).trackPageWithInfo("AppTaggingDemoPage", keyValuePair);
        }
    }

    public void testTrackActionWithInfo() {

        mAIAppTaggingInterface.trackActionWithInfo("AppTaggingDemoPage", "key1", "value1");
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockAppTaggingInterface).trackActionWithInfo("AppTaggingDemoPage", "key1", "value1");

        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAIAppTaggingInterface.trackActionWithInfo("AppTaggingDemoPage", keyValuePair);
            doAnswer(new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(mockAppTaggingInterface).trackActionWithInfo("AppTaggingDemoPage", keyValuePair);
        }
    }

    public void testLifecycle(){
        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler(mAIAppTaggingInterface);
        Application mockApplication = mock(Application.class);

       /* mockApplication.registerActivityLifecycleCallbacks(handler);
        mockApplication.registerComponentCallbacks(handler);*/
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockApplication).registerActivityLifecycleCallbacks(handler);

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(mockApplication).registerComponentCallbacks(handler);
    }

}

package com.philips.platform.appinfra.tagging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import java.util.HashMap;

/**
 * Created by 310238655 on 4/29/2016.
 */
public class AppTaggingTest extends MockitoTestCase {

    private Context context;
    private AppInfra mAppInfra;

    AppTaggingInterface mAIAppTaggingInterface;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();

        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        mAIAppTaggingInterface = mAppInfra.getTagging().createInstanceForComponent("Component name","Component ID");
        mAIAppTaggingInterface.setPreviousPage("SomePreviousPage");

    }

    public void testPrivacyConsent(){
        mAIAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTIN, mAIAppTaggingInterface.getPrivacyConsent());
        mAIAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTOUT);
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTOUT, mAIAppTaggingInterface.getPrivacyConsent());
        mAIAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.UNKNOWN);
//        assertEquals(AppTaggingInterface.PrivacyStatus.UNKNOWN, mAIAppTaggingInterface.getPrivacyConsent());
    }

    public void testTrackPageWithInfo() {

        mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", "key1", "value1");
        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", keyValuePair);
        }
    }

    public void testTrackActionWithInfo() {

        mAIAppTaggingInterface.trackActionWithInfo("AppTaggingDemoPage", "key1", "value1");
        HashMap<String, String> keyValuePair;
        String[] keyArray = {"key1", "key2", "key3"};
        String[] valueArray = {"value1", "value2", "value3"};
        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
            keyValuePair = new HashMap<String, String>();
            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
            }
            mAIAppTaggingInterface.trackActionWithInfo("AppTaggingDemoPage", keyValuePair);
        }
    }

}

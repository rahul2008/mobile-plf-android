package com.philips.platform.appinfra.tagging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.json.JSONObject;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * AppTagging Test class.
 */
public class AppTaggingTest extends AppInfraInstrumentation {

    AppTaggingInterface mAIAppTaggingInterface;
    AppTaggingInterface mockAppTaggingInterface;
    AppConfigurationManager mConfigInterface;
    AppTagging mAppTagging,appTagging;
    private Context context;
    private AppInfra mAppInfra;
    private AppConfigurationInterface.AppConfigurationError configError;
    AppTaggingHandler mAppTaggingHandler;
    AppTaggingHandler mAppTaggingHandlerMock;
    private AppInfra appInfraMock;
    LoggingInterface loggingInterfaceMock;
    AppIdentityInterface appIdentityInterfaceMock;
    SecureStorageInterface secureStorageInterfaceMock;
    InternationalizationInterface internationalizationInterfaceMock;

    private BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equalsIgnoreCase(AppTagging.ACTION_TAGGING_DATA)) {
                    Map textExtra = (Map) intent.getSerializableExtra(AppTagging.EXTRA_TAGGING_DATA);
                    assertNotNull(textExtra);
                }
            }

        }
    };

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);

        mAppInfra = new AppInfra.Builder().build(context);

        testConfig("Staging");
        testAdobeJsonConfig(true);

        mConfigInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                }
                return result;
            }

        };
        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
        configError = new AppConfigurationInterface
                .AppConfigurationError();


        Object dynAppState = mAppInfra.getConfigInterface().getPropertyForKey("appidentity.appState", "appinfra", configError);
        assertNotNull(dynAppState.toString());

        mAIAppTaggingInterface = mAppInfra.getTagging().createInstanceForComponent("Component name", "Component ID");
        assertNotNull(mAIAppTaggingInterface);

        mAppTaggingHandler=new AppTaggingHandler(mAppInfra);

        mockAppTaggingInterface = mock(AppTaggingInterface.class);

        mAppTaggingHandlerMock = mock(AppTaggingHandler.class);
        appInfraMock = mock(AppInfra.class);
        appTagging=new AppTagging(appInfraMock) {
            @Override
            AppTaggingHandler getAppTaggingHandler() {
                return mAppTaggingHandlerMock;
            }
        };

        loggingInterfaceMock = mock(LoggingInterface.class);
        appIdentityInterfaceMock=mock(AppIdentityInterface.class);
        secureStorageInterfaceMock=mock(SecureStorageInterface.class);
        internationalizationInterfaceMock=mock(InternationalizationInterface.class);
        mAppInfra.getConfigInterface().setPropertyForKey("appidentity.appState", "appinfra",
                "PRODUCTION", configError);
    }




    public void testConfig(final String value) {
        mConfigInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = "{\n" +
                            "  \"UR\": {\n" +
                            "\n" +
                            "    \"Development\": \"ad7nn99y2mv5berw5jxewzagazafbyhu\",\n" +
                            "    \"Testing\": \"xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7\",\n" +
                            "    \"Evaluation\": \"4r36zdbeycca933nufcknn2hnpsz6gxu\",\n" +
                            "    \"Staging\": \"f2stykcygm7enbwfw2u9fbg6h6syb8yd\",\n" +
                            "    \"Production\": \"mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3\"\n" +
                            "\n" +
                            "  },\n" +
                            "  \"AI\": {\n" +
                            "    \"MicrositeID\": 77001,\n" +
                            "    \"RegistrationEnvironment\": \"Staging\",\n" +
                            "    \"NL\": [\"googleplus\", \"facebook\"  ],\n" +
                            "    \"US\": [\"facebook\",\"googleplus\" ],\n" +
                            "    \"EE\": [123,234 ]\n" +
                            "  }, \n" +
                            " \"appinfra\": { \n" +
                            "   \"appidentity.micrositeId\" : \"77000\",\n" +
                            "    \"tagging.sensitiveData\": [\"bundleId\", \"language\"  ],\n" +
                            "  \"appidentity.sector\"  : \"B2C\",\n" +
                            " \"appidentity.appState\"  : \"" + value + "\",\n" +
                            "\"appidentity.serviceDiscoveryEnvironment\"  : \"Staging\",\n" +
                            "\"restclient.cacheSizeInKB\"  : 1024 \n" +
                            "} \n" + "}";
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                }
                return result;
            }

        };
        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
    }

    public void testAdobeJsonConfig(final boolean value) {
        mAppTagging = new AppTagging(mAppInfra);
        mAppTagging.mComponentID = "mComponentID";
        mAppTagging.mComponentVersion = "mComponentVersion";
        mAppInfra = new AppInfra.Builder().setTagging(mAppTagging).build(context);
        mAppInfra.setConfigInterface(mConfigInterface);
    }
    public void testPrivacyConsentOPTIN() {
        appTagging.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTIN, appTagging.getPrivacyConsent());
    }

    public void testPrivacyConsentOPTOUT() {
        appTagging.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTOUT);
        assertEquals(AppTaggingInterface.PrivacyStatus.OPTOUT, appTagging.getPrivacyConsent());
    }


    public void testEmumValues_facebook() {
        assertEquals("facebook", AppTaggingInterface.SocialMedium.Facebook.toString());
    }
    public void testEmumValues_twitter() {
        assertEquals("twitter", AppTaggingInterface.SocialMedium.Twitter.toString());
    }

    public void testEmumValues_mail() {
        assertEquals("mail", AppTaggingInterface.SocialMedium.Mail.toString());
    }



    public void testPrivacyConsentForSensitiveData() {
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        when(appInfraMock.getSecureStorage()).thenReturn(secureStorageInterfaceMock);
        AppTagging appTaggingPage=new AppTagging(appInfraMock);
        appTaggingPage.setPrivacyConsentForSensitiveData(false);
        assertFalse(appTaggingPage.getPrivacyConsentForSensitiveData());
    }

}

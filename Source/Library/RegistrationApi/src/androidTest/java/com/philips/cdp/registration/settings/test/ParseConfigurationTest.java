package com.philips.cdp.registration.settings.test;

import android.app.Activity;
import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.configuration.ConfigurationParser;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.tagging.Tagging;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by 310202337 on 10/13/2015.
 */
public class ParseConfigurationTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    private RegistrationHelper mRegistrationHelper;

    public ParseConfigurationTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        Locale locale = new Locale("en","US");
        Tagging.enableAppTagging(true);
        Tagging.setTrackingIdentifier("integratingApplicationAppsId");
        Tagging.setLaunchingPageName("demoapp:home");
        mRegistrationHelper = RegistrationHelper.getInstance();
       // mRegistrationHelper.initializeUserRegistration(getInstrumentation().getTargetContext(), locale);
        RegistrationConfiguration.getInstance().setCoppaFlow(true);
        //mEmailValidator = mock(EmailValidator.class);
    }




   /* public void testParseConfiguration(){
        AssetManager assetManager = getInstrumentation().getTargetContext().getAssets();

        try {
            JSONObject con = Mockito.mock(JSONObject.class);
            JSONObject conf = new JSONObject(convertStreamToString(assetManager.open(RegConstants.CONFIGURATION_JSON_PATH)));
           // JSONObject cond = Mockito.spy(conf);
           // Mockito.when(n).thenReturn(con);
          //  Mockito.when(new JSONObject(convertStreamToString(assetManager.open(RegConstants.CONFIGURATION_JSON_PATH)))).thenReturn(con);
          //  JSONObject configurationJson = new JSONObject(
                  //  convertStreamToString(assetManager.open(RegConstants.CONFIGURATION_JSON_PATH)));
            ConfigurationParser configurationParser = Mockito.mock(ConfigurationParser.class);
            NetworkUtility mockNetworkUtility = Mockito.mock(NetworkUtility.class);
            //Mockito.when(mockNetworkUtility.isNetworkAvailable( getInstrumentation().getTargetContext())).thenReturn(true);
            // ConfigurationParser configurationParser = new ConfigurationParser();
            Locale locale = new Locale("en","US");
            Tagging.enableAppTagging(true);
            Tagging.setTrackingIdentifier("integratingApplicationAppsId");
            Tagging.setLaunchingPageName("demoapp:home");
            assertNotNull(RegistrationHelper.getInstance());
          //  RegistrationHelper.getInstance().initializeUserRegistration(getInstrumentation().getTargetContext(), locale);
            configurationParser.parse(conf);
            RegistrationHelper rh = Mockito.mock(RegistrationHelper.class);
         //   Mockito.verify(configurationParser,Mockito.atLeast(1)).parse(conf);



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private  String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void testCoppaFlow(){

        Boolean coppa = mRegistrationHelper.isCoppaFlow();


        assertTrue(coppa);
    }

    public void testCountryCode(){
        String code = mRegistrationHelper.getCountryCode();
        assertNotNull(code);
    }

    public void testLocale(){
        Locale locale = mRegistrationHelper.getLocale();
        assertNotNull(locale);
    }

    public void testUserRegistrationListener(){

        mRegistrationHelper.registerUserRegistrationListener(new UserRegistrationListener() {
            @Override
            public void onUserRegistrationComplete(Activity activity) {

            }

            @Override
            public void onPrivacyPolicyClick(Activity activity) {

            }

            @Override
            public void onTermsAndConditionClick(Activity activity) {

            }

            @Override
            public void onUserLogoutSuccess() {

            }

            @Override
            public void onUserLogoutFailure() {

            }

            @Override
            public void onUserLogoutSuccessWithInvalidAccessToken() {

            }
        });

        UserRegistrationHelper url = mRegistrationHelper.getUserRegistrationListener();
        assertNotNull(url);

    }
*/
}

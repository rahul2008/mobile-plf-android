package com.philips.cdp.registration.Configuration;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.configuration.RegistrationStaticConfiguration;
import com.philips.cdp.registration.configuration.SigninProviders;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.cdp.registration.ui.utils.RegUtility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vinayak on 28/01/16.
 */
public class RegistrationSignInProvidersConfigurationTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    //Constructor for instrimental test case
    public RegistrationSignInProvidersConfigurationTest() {
        super(RegistrationActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String CONFIGURATION_JSON_PATH = "registration/configuration/configuration_for_test_cases.json";
        RegistrationStaticConfiguration.getInstance().parseConfigurationJson(getInstrumentation().getTargetContext(), CONFIGURATION_JSON_PATH);
    }

    public void testPILConfigurationFlieldsWithStatic() {

       SigninProviders signinProviders  = RegistrationConfiguration.getInstance().getSignInProviders();

        if(signinProviders.getProviders().size()!=3){
            assertTrue(false);
        }

        if(signinProviders.getProvidersForCountry("NL")==null && signinProviders.getProvidersForCountry("NL").size()!=3){
            assertTrue(false);
        }

        if(signinProviders.getProvidersForCountry("US")==null && signinProviders.getProvidersForCountry("US").size()!=3){
            assertTrue(false);
        }

        if(signinProviders.getProvidersForCountry("IN")==null && signinProviders.getProvidersForCountry("IN").size()!=3){
            assertTrue(false);
        }


        assertTrue(true);
    }

    public void testPILConfigurationFlieldsWithDynamicReplace() {
        ArrayList<String> providers = new ArrayList<>();
        providers.add("abc");
        providers.add("xyz");

        HashMap<String, ArrayList<String>> pro = new HashMap<>();
        pro.put("NL",providers);

        RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(pro);

        SigninProviders signinProviders  = RegistrationConfiguration.getInstance().getSignInProviders();
        if(signinProviders.getProviders().size()!=3){
            assertTrue(false);
        }

        if(signinProviders.getProvidersForCountry("NL")==null && signinProviders.getProvidersForCountry("NL").size()!=2){
            assertTrue(false);
        }

        RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
    }

    public void testPILConfigurationFlieldsWithDynamicAdd() {
        ArrayList<String> providers = new ArrayList<>();
        providers.add("abc");
        providers.add("xyz");

        HashMap<String, ArrayList<String>> pro = new HashMap<>();
        pro.put("BB",providers);

        RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(pro);

        SigninProviders signinProviders  = RegistrationConfiguration.getInstance().getSignInProviders();
        if(signinProviders.getProviders().size()!=4){
            assertTrue(false);
        }

        RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
    }


    public void testPILConfigurationFlieldsOnlyDynamic() {

        RegistrationStaticConfiguration.getInstance().getSignInProviders().setProviders(null);
        ArrayList<String> providers = new ArrayList<>();
        providers.add("abc");
        providers.add("xyz");

        HashMap<String, ArrayList<String>> pro = new HashMap<>();
        pro.put("BB", providers);

        RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(pro);

        SigninProviders signinProviders  = RegistrationConfiguration.getInstance().getSignInProviders();
        if(signinProviders.getProviders().size()!=1){
            assertTrue(false);
        }

        RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
    }

    public void testTwitterNotProviderException() {
        try{
            RegistrationStaticConfiguration.getInstance().getSignInProviders().setProviders(null);
            ArrayList<String> providers = new ArrayList<>();
            providers.add("twitter");
            providers.add("xyz");

            HashMap<String, ArrayList<String>> pro = new HashMap<>();
            pro.put("BB", providers);

            RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(pro);
            RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
        }catch (RuntimeException e){
            assertTrue(true);
        }
        assertFalse(false);
    }

    public void testCheckIsValidSignInProviders() {
        try{
            RegistrationStaticConfiguration.getInstance().getSignInProviders().setProviders(null);
            ArrayList<String> providers = new ArrayList<>();
            providers.add("twitter");
            providers.add("xyz");

            HashMap<String, ArrayList<String>> pro = new HashMap<>();
            pro.put("BB", providers);

            RegistrationDynamicConfiguration.getInstance().getSignInProviders().setProviders(pro);
            RegUtility.checkIsValidSignInProviders(pro);
            RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
        }catch (RuntimeException e){
            assertTrue(true);
        }
        assertFalse(false);
    }
}

package com.philips.cdp.registration.coppa.base;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.coppa.utils.CoppaSettings;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class CoppaExtensionTest extends InstrumentationTestCase {

    CoppaExtension mCoppaConsentUpdater;
    Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        mCoppaConsentUpdater = new CoppaExtension(mContext);
        assertNotNull(mCoppaConsentUpdater);

    }

    @Test
    public void testUpdateCoppaConsentStatus() {

        assertNotNull(mContext);
        assertNull(mCoppaConsentUpdater.getCoppaEmailConsentStatus());

        try {
            mCoppaConsentUpdater.buildConfiguration();
        } catch (Exception e) {

        }
    }
    @Test
    public void testResetConfiguration(){
        mCoppaConsentUpdater.resetConfiguration();
        CoppaConfiguration.clearConfiguration();
        assertNotNull(mCoppaConsentUpdater);
    }

    @Test
    public void testGetCoppaStatusForConsent(){
        com.philips.cdp.registration.coppa.base.Consent consent = new com.philips.cdp.registration.coppa.base.Consent();
        consent.setMicroSiteID("77000");
        consent.setCampaignId("COPPA");
        consent.setCommunicationSentAt("1928-10-30");
        consent.setConfirmationStoredAt("1928-10-30");
        consent.setConfirmationCommunicationSentAt("1928-10-30");
        consent.setLocale("en_US");
        consent.setId("22222222");
        consent.setStoredAt("22222222");
        consent.setConfirmationCommunicationToSendAt("22222222");
        synchronized(this){//synchronized block

            try{
                RegistrationHelper.getInstance().
                        setAppInfraInstance(new AppInfra.Builder().build(mContext));
            }catch(Exception e){System.out.println(e);}
        }
        RLog.initForTesting(mContext);

            Method method = null;
            try {
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);

                consent.setConfirmationGiven(Boolean.toString(true));
                consent.setGiven(Boolean.toString(false));
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);


                consent.setConfirmationGiven(Boolean.toString(true));
                consent.setGiven(Boolean.toString(true));
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);

                consent.setConfirmationGiven(Boolean.toString(false));
                consent.setGiven(Boolean.toString(true));
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);

                consent.setConfirmationGiven(Boolean.toString(false));
                consent.setGiven(Boolean.toString(false));
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);

                consent.setConfirmationGiven(null);
                consent.setGiven(Boolean.toString(true));
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);

                consent.setConfirmationGiven("NULL");
                consent.setGiven(Boolean.toString(true));
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);

                consent.setConfirmationGiven("NULL");
                consent.setGiven(Boolean.toString(true));
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);

                consent.setLocale("en-US");
                consent.setConfirmationGiven(null);
                consent.setGiven(Boolean.toString(true));
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);

                consent.setLocale("en_US");
                consent.setConfirmationGiven(null);
                consent.setGiven(Boolean.toString(true));
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);

                consent.setGiven(null);
                method = CoppaExtension.class.getDeclaredMethod("getCoppaStatusForConsent",com.philips.cdp.registration.coppa.base.Consent.class);
                method.setAccessible(true);
                method.invoke(mCoppaConsentUpdater,consent);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


    }
}
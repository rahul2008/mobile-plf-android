package com.philips.cdp.registration.coppa.base;

import android.content.Context;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;


public class CoppaExtensionTest extends RegistrationApiInstrumentationBase {

    CoppaExtension mCoppaConsentUpdater;
    Context mContext;

    @Override
    public void setUp() throws Exception {

        super.setUp();

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
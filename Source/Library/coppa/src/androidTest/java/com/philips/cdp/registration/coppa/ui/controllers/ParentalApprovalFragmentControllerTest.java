package com.philips.cdp.registration.coppa.ui.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalApprovalFragment;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class ParentalApprovalFragmentControllerTest extends InstrumentationTestCase {

    Context mContext;
    ParentalApprovalFragmentController mParentalApprovalFragmentController;
    CoppaExtension coppaExtension;
    ProgressDialog mProgressDialog;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        ParentalApprovalFragment parentalApprovalFragment = new ParentalApprovalFragment();
        mParentalApprovalFragmentController = new ParentalApprovalFragmentController(parentalApprovalFragment);
        coppaExtension = new CoppaExtension(mContext);
        mProgressDialog = new ProgressDialog(mContext, R.style.reg_Custom_loaderTheme);

    }

    @Test
    public void testParentalApprovalFragmentController() {
        assertNotNull(mParentalApprovalFragmentController);
        assertNotNull(coppaExtension);
    }

    /*@Test
    public void testIsCountryUs() {
        boolean result = mParentalApprovalFragmentController.isCountryUs();
        assertNotNull(mParentalApprovalFragmentController.getCoppaExtension().getConsent().getLocale());
        assertFalse(result);
    }*/

    @Test
    public void testAddReConfirmParentalConsentFragment(){
            Method method = null;
            try {
                method =ConfirmationHandler.class.getDeclaredMethod("addReConfirmParentalConsentFragment");;
                method.setAccessible(true);
                method.invoke(mParentalApprovalFragmentController);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

    }
    @Test
    public void testUpdateUIBasedOnConsentStatus(){
        Method method = null;
        try {
            method =ConfirmationHandler.class.getDeclaredMethod("updateUIBasedOnConsentStatus", CoppaStatus.class);;
            method.setAccessible(true);
            method.invoke(mParentalApprovalFragmentController, CoppaStatus.kDICOPPAConfirmationGiven);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
   /* @Test
    public void testAddParentalConsentFragment(){
        Method method = null;
        try {
            method =ConfirmationHandler.class.getDeclaredMethod("addParentalConsentFragment", CoppaStatus.class);;
            method.setAccessible(true);
            method.invoke(mParentalApprovalFragmentController, CoppaStatus.kDICOPPAConfirmationGiven);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }*/
    @Test
    public void testHoursSinceLastConsent(){
        Method method = null;
        try {
            method =ConfirmationHandler.class.getDeclaredMethod("hoursSinceLastConsent", int.class);;
            method.setAccessible(true);
            method.invoke(mParentalApprovalFragmentController);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
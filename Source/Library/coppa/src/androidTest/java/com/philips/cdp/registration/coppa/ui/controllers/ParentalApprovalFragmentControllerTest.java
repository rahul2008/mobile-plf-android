package com.philips.cdp.registration.coppa.ui.controllers;

import android.app.ProgressDialog;
import android.content.Context;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalApprovalFragment;

import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class ParentalApprovalFragmentControllerTest extends RegistrationApiInstrumentationBase {

    Context mContext;
    @Mock
    ParentalApprovalFragmentController mParentalApprovalFragmentController;
    CoppaExtension coppaExtension;
    ProgressDialog mProgressDialog;
    @Mock
    ParentalApprovalFragment parentalApprovalFragment;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        parentalApprovalFragment = new ParentalApprovalFragment();
        //mParentalApprovalFragmentController = new ParentalApprovalFragmentController(parentalApprovalFragment);
        assertNotNull(parentalApprovalFragment);
         coppaExtension = new CoppaExtension(mContext);
        mProgressDialog = new ProgressDialog(mContext, R.style.reg_Custom_loaderTheme);

    }


    public void testParentalApprovalFragmentController() {
        assertNotNull(coppaExtension);
    }

    /*@Test
    public void testIsCountryUs() {
        boolean result = mParentalApprovalFragmentController.isCountryUs();
        assertNotNull(mParentalApprovalFragmentController.getCoppaExtension().getConsent().getLocale());
        assertFalse(result);
    }*/

  /*  @Test
    public void testAddReConfirmParentalConsentFragment(){
            Method method = null;
            try {
                method =ParentalApprovalFragmentController.class.getDeclaredMethod("addReConfirmParentalConsentFragment");;
                method.setAccessible(true);
                method.invoke(mParentalApprovalFragmentController);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

    }*/
    /*@Test
    public void testUpdateUIBasedOnConsentStatus(){
        Method method = null;
        try {
            method =ParentalApprovalFragmentController.class.getDeclaredMethod("updateUIBasedOnConsentStatus", CoppaStatus.class);;
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
    /*@Test
    public void testHoursSinceLastConsent(){
        Method method = null;
        try {
            method =ParentalApprovalFragmentController.class.getDeclaredMethod("hoursSinceLastConsent", int.class);;
            method.setAccessible(true);
            method.invoke(mParentalApprovalFragmentController);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }*/
}
package com.philips.cdp.prodreg.register;

import android.app.Activity;
import android.content.Context;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.product_registration_lib.BuildConfig;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProdRegHelperTest extends MockitoTestCase {

    private ProdRegHelper prodRegHelper;
    private Context context;
    private ProdRegListener prodRegListener;
    private RegistrationHelper registrationHelper;
    private UserRegistrationListener userRegistrationListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        prodRegHelper = new ProdRegHelper();
        userRegistrationListener = new UserRegistrationListener() {
            @Override
            public void onUserRegistrationComplete(final Activity activity) {

            }

            @Override
            public void onPrivacyPolicyClick(final Activity activity) {

            }

            @Override
            public void onTermsAndConditionClick(final Activity activity) {

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
        };
        prodRegListener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final RegisteredProduct registeredProduct, final UserWithProducts userWithProduct) {

            }

            @Override
            public void onProdRegFailed(final RegisteredProduct registeredProduct, final UserWithProducts userWithProduct) {

            }
        };
    }

  /*  public void testProdRegHelper() {
        prodRegHelper.init(context);
        prodRegHelper.addProductRegistrationListener(prodRegListener);
        registrationHelper = mock(RegistrationHelper.class);
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(userRegistrationListener);
        assertTrue(prodRegHelper.getSignedInUserWithProducts() instanceof UserWithProducts);
    }*/

    public void testBuildVersion() {
        assertEquals(prodRegHelper.getLibVersion(), BuildConfig.VERSION_NAME);
    }
}
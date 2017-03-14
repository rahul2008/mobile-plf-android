package com.philips.cdp.prodreg.register;

import android.content.Context;

import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.product_registration_lib.BuildConfig;
import com.philips.cdp.registration.listener.UserRegistrationListener;

import junit.framework.TestCase;

import org.junit.Before;

import static org.mockito.Mockito.mock;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProdRegHelperTest extends TestCase {

    private ProdRegHelper prodRegHelper;
    private Context context;
    private ProdRegListener prodRegListener;
    private UserRegistrationListener userRegistrationListener;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = mock(Context.class);
        prodRegHelper = new ProdRegHelper();
        prodRegListener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final RegisteredProduct registeredProduct, final UserWithProducts userWithProduct) {

            }

            @Override
            public void onProdRegFailed(final RegisteredProduct registeredProduct, final UserWithProducts userWithProduct) {

            }
        };
    }

    public void testBuildVersion() {
        assertEquals(prodRegHelper.getLibVersion(), BuildConfig.VERSION_NAME);
    }
}
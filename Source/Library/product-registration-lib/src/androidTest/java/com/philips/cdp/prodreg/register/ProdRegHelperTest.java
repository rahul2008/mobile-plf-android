package com.philips.cdp.prodreg.register;

import android.content.Context;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.product_registration_lib.BuildConfig;

import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegHelperTest extends MockitoTestCase {

    private ProdRegHelper prodRegHelper;
    private Context context;
    private ProdRegListener prodRegListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
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

    public void testAddProductRegistrationListener() {
        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        prodRegHelper.addProductRegistrationListener(prodRegListener);
    }

    public void testBuildVersion() {
        assertEquals(prodRegHelper.getLibVersion(), BuildConfig.VERSION_NAME);
    }
}
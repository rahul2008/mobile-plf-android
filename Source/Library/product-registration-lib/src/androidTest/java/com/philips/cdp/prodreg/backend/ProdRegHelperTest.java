package com.philips.cdp.prodreg.backend;

import android.content.Context;

import com.philips.cdp.prodreg.MockitoTestCase;

import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class ProdRegHelperTest extends MockitoTestCase {

    ProdRegHelper prodRegHelper;
    Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        prodRegHelper = new ProdRegHelper();
        mContext = getInstrumentation().getContext();
    }

   /* public void testRegisterProduct() {
        final UserProduct[] userProduct = new UserProduct[1];
        ProdRegHelper prodRegHelper = new ProdRegHelper() {
            @NonNull
            @Override
            protected UserProduct getUserProduct(final Context context) {
                userProduct[0] = mock(UserProduct.class);
                return userProduct[0];
            }
        };
        Product product = mock(Product
                .class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        prodRegHelper.addProductRegistrationListener(prodRegListener);
        prodRegHelper.registerProduct(product);
        verify(userProduct[0]).registerProduct(product, prodRegListener);
    }*/

    public void testGetUserProduct() {
        Product product = mock(Product
                .class);
        assertTrue(prodRegHelper.getUserProduct(mContext) instanceof UserProduct);
    }

    public void testSettingLocale() {
        prodRegHelper.setLocale("en", "GB");
        assertEquals(prodRegHelper.getLocale(), ("en_GB"));
    }
}
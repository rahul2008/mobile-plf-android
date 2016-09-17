package com.philips.cdp.registration;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.dao.ProductRegistrationInfo;
import com.philips.cdp.registration.handlers.ProductRegistrationHandler;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by 310230979 on 0/08/2016.
 */
public class UserWithProductTest extends InstrumentationTestCase {
    UserWithProduct userWithProducts;
     Context mContext;
    ProductRegistrationHandler productRegistrationHandler;
    ProductRegistrationInfo productRegistrationInfo;
    @Before
    public void setUp() throws Exception {
        mContext= getInstrumentation().getContext();
        userWithProducts = new UserWithProduct(mContext);
        productRegistrationHandler= new ProductRegistrationHandler() {
            @Override
            public void onRegisterSuccess(final String response) {

            }

            @Override
            public void onRegisterFailedWithFailure(final int error) {

            }

            @Override
            public void onRefreshLoginSessionInProgress(final String message) {

            }
        };
        productRegistrationInfo= new ProductRegistrationInfo();
    }
    @Test
    public void testUserWithProduct(){
        assertNotNull(userWithProducts);
    }
    @Test
    public void testGetRegisteredProducts(){
        userWithProducts.getRegisteredProducts("","",productRegistrationHandler);
        userWithProducts.getRegisteredProducts(null,null,productRegistrationHandler);


    }
}
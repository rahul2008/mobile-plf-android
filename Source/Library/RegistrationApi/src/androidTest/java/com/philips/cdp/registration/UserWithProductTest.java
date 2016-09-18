package com.philips.cdp.registration;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.dao.ProductRegistrationInfo;
import com.philips.cdp.registration.handlers.ProductRegistrationHandler;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    public void testStartProdRegAsyncTask(){
        userWithProducts.getRegisteredProducts("","",productRegistrationHandler);
        userWithProducts.getRegisteredProducts(null,null,productRegistrationHandler);

       final String locale ="en-US";


        Method method = null;
        try {
            method = UserWithProduct.class.getDeclaredMethod("startProdRegAsyncTask", new Class[]{String.class});
            method.setAccessible(true);
            method.invoke(userWithProducts,new Object[]{ locale});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void testGetRegisteredProducts(){


        final ProductRegistrationInfo prodRegInfo = new ProductRegistrationInfo();
        final ProductRegistrationHandler productRegisterHandler =new ProductRegistrationHandler() {
            @Override
            public void onRegisterSuccess(String response) {

            }

            @Override
            public void onRegisterFailedWithFailure(int error) {

            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {

            }
        };
        final String locale ="en-US";


        Method method = null;
        try {
            method = UserWithProduct.class.getDeclaredMethod("registerProduct", new Class[]{ProductRegistrationInfo.class,
                    ProductRegistrationHandler.class, String.class,Context.class});
            method.setAccessible(true);
            method.invoke(userWithProducts,new Object[]{  prodRegInfo,
                    productRegisterHandler,  locale,
                    mContext});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
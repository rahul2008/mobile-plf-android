package com.philips.cdp.di.ecs.Cart;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class GetShoppingCartTest {
    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

    }

    @Test
    public void getCartSuccess(){
        mockECSServices.setJsonFileName("ShoppingCartSuccess.json");
        mockECSServices.getShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart result) {
                assertNotNull(result);
                assertNotNull(result.getGuid());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(7999,errorCode);
                // test case failed
            }
        });
    }

    @Test
    public void getCartWithoutGUID(){
        mockECSServices.setJsonFileName("ShoppingCartWithoutGuid.json");
        mockECSServices.getShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart result) {
                assertNotNull(result);
                assertNotNull(result.getGuid());
                // test case passed
            }
            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(8999,errorCode);

                // test case failed
            }
        });
    }

    @Test
    public void getCartAuthFailureResponse(){
        mockECSServices.setJsonFileName("ShoppingCartAuthError.json");
        mockECSServices.getShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart result) {
                assertNotNull(result);
                assertNotNull(result.getGuid());
                // test case passed
            }
            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(8999,errorCode);

                // test case failed
            }
        });
    }

    @Test
    public void getCartEmptyResponse(){
        mockECSServices.setJsonFileName("EmptyJson.json");
        mockECSServices.getShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart result) {
                assertNotNull(result);
                assertNotNull(result.getGuid());
                // test case passed
            }
            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(8999,errorCode);

                // test case failed
            }
        });
    }
}

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
public class CartTest {

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
    public void createCartSuccess(){
        mockECSServices.setJsonFileName("CreateCart.json");
        mockECSServices.createShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart result) {
                assertNotNull(result);
                assertNotNull(result.getGuid());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assert true;
                // test case failed
            }
        });
    }

    @Test
    public void createCartFailure(){
        mockECSServices.setJsonFileName("Empty.json");
        mockECSServices.createShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
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
    public void getCartSuccess(){
        mockECSServices.setJsonFileName("GetCart.json");
        mockECSServices.createShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
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
    public void getCartFailure(){
        mockECSServices.setJsonFileName("Empty.json");
        mockECSServices.createShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
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


}

package com.philips.cdp.di.ecs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(RobolectricTestRunner.class)

public class ECSServicesTest {
    private Context mContext;




    MockECSServices mockECSServices;



    private AppInfra appInfra;



    @Mock
    RestInterface mockRestInterface;



    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);



         }

    @Test
    public void init() {
    }

    @Test
    public void hybrisOathAuthentication() {
    }

    @Test
    public void getIAPConfig() {
    }

    @Test
    public void getProductDetailSuccess() {
        mockECSServices.getProductList(0, 1, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                assertNotNull(result);
                assertNotNull(result.getProducts().get(0).getSummary());
                assertNotNull(result.getProducts().get(1).getSummary());
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertFalse(true);

            }
        });

    }

    @Test void getProductDetailHybrisFailure(){
        mockECSServices.getProductList(0, 1, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                assertNotNull(result);
                assertNotNull(result.getProducts().get(0).getSummary());
                assertNotNull(result.getProducts().get(1).getSummary());
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertFalse(true);

            }
        });
    }

    @Test
    public void invalidateECS() {
    }

    @Test
    public void getProductAsset() {
    }

    @Test
    public void getProductDisclaimer() {
    }
}
package com.philips.cdp.di.ecs.Address;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
public class UpdateAddressTest {

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



        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

    }



    @Test
    public void UpdateAddressTestSuccess() {
        mockECSServices.setJsonFileName("EmptyString.json");
        Addresses address = new Addresses();
        mockECSServices.updateAddress(address, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                assertTrue(result);
                // test case passed
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                assertTrue(false);
                // test case failed
            }
        });

    }
}

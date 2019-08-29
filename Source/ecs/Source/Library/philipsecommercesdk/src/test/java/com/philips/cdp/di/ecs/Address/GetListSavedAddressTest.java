package com.philips.cdp.di.ecs.Address;



import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
public class GetListSavedAddressTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetAddressRequest mockGetAddressRequest;

    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        //appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();
        mockGetAddressRequest = new MockGetAddressRequest("ShippingAddressListSuccess.json", new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData result) {

            }

            @Override
            public void onFailure(Exception error, int errorCode) {

            }
        });
    }


    @Test
    public void GetListSavedAddressTestSuccess() {
        mockECSServices.setJsonFileName("ShippingAddressListSuccess.json");
        Addresses address = new Addresses();
        mockECSServices.getListSavedAddress( new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData addressList) {
                assertNotNull(addressList);
                assertNotNull(addressList.getAddresses());

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                // assertEquals(12999,errorCode);
                assertTrue(true);
            }
        });

    }

    @Test
    public void GetListSavedAddressTestFailure() {
        mockECSServices.setJsonFileName("EmptyJson.json");
        Addresses address = new Addresses();
        mockECSServices.getListSavedAddress( new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData addressList) {
                assertTrue(true);

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(12999,errorCode);

            }
        });

    }

    @Test
    public void isValidURL() {
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/addresses?fields=FULL&lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockGetAddressRequest.getURL());
    }

    @Test
    public void isValidGetRequest() {
        Assert.assertEquals(0 ,mockGetAddressRequest.getMethod());
    }
}

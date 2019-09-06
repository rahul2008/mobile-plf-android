package com.philips.cdp.di.ecs.Address;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.error.ECSError;
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
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class DeleteAddressTest {


    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockDeleteAddressRequest mockDeleteAddressRequest;

    ECSCallback ecsCallback;

    Addresses addresses;

    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);



        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();
        addresses = new Addresses();
        addresses.setId("1234");

        ecsCallback = new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };
        mockDeleteAddressRequest = new MockDeleteAddressRequest("EmptyString.json", addresses,ecsCallback );

    }

    @Test
    public void addAddressSingleSuccess() {
        mockECSServices.setJsonFileName("EmptyString.json");
        Addresses address = new Addresses();
        mockECSServices.deleteAddress(address, new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData addressList) {
                assertNotNull(addressList);
                assertNotNull(addressList.getAddresses());

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(false);
            }
        });

    }


    @Test
    public void addAddressSingleFailureInvalidBaseSite() {
        mockECSServices.setJsonFileName("DeleteAddressFailureInvalidBaseSite.json");
        Addresses address = new Addresses();
        mockECSServices.deleteAddress(address, new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData addressList) {
                assertTrue(true);

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(true);
            }
        });

    }

    @Test
    public void isValidURL() {
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/addresses/"+"1234"+"?fields=FULL&lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockDeleteAddressRequest.getURL());
    }

    @Test
    public void isValidDeleteRequest() {
        Assert.assertEquals(3,mockDeleteAddressRequest.getMethod());
    }


    @Test
    public void isValidHeader() {

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("Content-Type", "application/x-www-form-urlencoded");
        expectedMap.put("Authorization", "Bearer " + "acceesstoken");

        Map<String, String> actual = mockDeleteAddressRequest.getHeader();

        assertTrue(expectedMap.equals(actual));
    }

    @Test
    public void verifyOnResponseError() {
        ECSCallback<Boolean, Exception> spy1 = Mockito.spy(ecsCallback);
        mockDeleteAddressRequest = new MockDeleteAddressRequest("CreateAddressSuccess.json", addresses, spy1);
        VolleyError volleyError = new NoConnectionError();
        mockDeleteAddressRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }

    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockDeleteAddressRequest.getStringSuccessResponseListener());
    }

}

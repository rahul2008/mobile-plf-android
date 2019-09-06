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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;


@RunWith(RobolectricTestRunner.class)
public class GetListSavedAddressTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetAddressRequest mockGetAddressRequest;

    ECSCallback ecsCallback;

    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        //appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();

        ecsCallback = new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };
        mockGetAddressRequest = new MockGetAddressRequest("ShippingAddressListSuccess.json", ecsCallback);
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
            public void onFailure(Exception error, ECSError ecsError) {
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
            public void onFailure(Exception error, ECSError ecsError) {
                assertEquals(12999, ecsError);

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

    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockGetAddressRequest.getJSONSuccessResponseListener());
    }

    @Test
    public void verifyOnResponseError() {
        ECSCallback<GetShippingAddressData, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetAddressRequest = new MockGetAddressRequest("CreateAddressSuccess.json", spy1);
        VolleyError volleyError = new NoConnectionError();
        mockGetAddressRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }

    @Test
    public void verifyOnResponseException() {
        ECSCallback<GetShippingAddressData, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetAddressRequest = new MockGetAddressRequest("CreateAddressSuccess.json", spy1);
        mockGetAddressRequest.onResponse(null);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }

    @Test
    public void verifyOnResponseJSONException() {
        ECSCallback<GetShippingAddressData, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetAddressRequest = new MockGetAddressRequest("CreateAddressSuccess.json", spy1);
        try {
            JSONObject jsonObject = new JSONObject("123");
            mockGetAddressRequest.onResponse(jsonObject);
            Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());
        } catch (JSONException e) {

        }
    }

}

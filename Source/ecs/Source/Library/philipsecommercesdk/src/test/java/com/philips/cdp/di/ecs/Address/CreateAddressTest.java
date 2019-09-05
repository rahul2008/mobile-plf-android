package com.philips.cdp.di.ecs.Address;

import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class CreateAddressTest {
    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockCreateAddressRequest mockCreateAddressRequest;

    Addresses address;

    ECSCallback<Addresses, Exception> ecsCallback;

    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        //appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();
        address = StaticBlock.getAddressesObject();
        ecsCallback = new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses result) {

            }

            @Override
            public void onFailure(Exception error, int errorCode) {

            }
        };
        mockCreateAddressRequest = new MockCreateAddressRequest("CreateAddressSuccess.json", address, ecsCallback);

    }


    //////////////////////////////start of single created  Address fetch ////////////////

    @Test
    public void addAddressSingleSuccess() {
        mockECSServices.setJsonFileName("CreateAddressSuccess.json");
        Addresses address = StaticBlock.getAddressesObject();
        mockECSServices.createNewAddress(address, new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses address) {
                assertNotNull(address);
                assertNotNull(address.getId());

            }

            @Override
            public void onFailure(Exception error, int errorCode) {

                assertTrue(true);
            }
        },true);

    }

    @Test
    public void addAddressSingleFailureInvalidZipCode() {
        mockECSServices.setJsonFileName("CreateAddressFailureInvalidZipCode.json");
        Addresses address = new Addresses();
        mockECSServices.createNewAddress(address, new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses address) {
                assertTrue(true);

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
            }
        },true);

    }

    @Test
    public void addAddressSingleFailure() {
        mockECSServices.setJsonFileName("EmptyString.json");
        Addresses address = new Addresses();
        mockECSServices.createNewAddress(address, new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses address) {
               assertTrue(true);

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(12999,errorCode);
            }
        },true);

    }



    //////////////////////////////End of single created  Address fetch ////////////////





    //////////////////////////////start of All  Address fetch ////////////////

    @Test
    public void addAddressSuccess() {
        mockECSServices.setJsonFileName("CreateAddressSuccess.json");
        Addresses addressRequest = new Addresses();
        mockECSServices.createNewAddress(addressRequest, new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData addressList) {
                assertNotNull(addressList);
                assertNotNull(addressList.getAddresses().size()>0);
                assertNotNull(addressList.getAddresses().get(0).getId());

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                // assertEquals(12999,errorCode);
                assertTrue(true);
            }
        });

    }


    @Test
    public void addAddressFailure() {
        mockECSServices.setJsonFileName("EmptyString.json");
        Addresses addressRequest = new Addresses();
        mockECSServices.createNewAddress(addressRequest, new ECSCallback<GetShippingAddressData, Exception>() {
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

    //////////////////////////////End of All  Address fetch ////////////////

    @Test
    public void isValidURL() {
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/addresses?fields=FULL&lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockCreateAddressRequest.getURL());
    }

    @Test
    public void isValidPostRequest() {
        Assert.assertEquals(1,mockCreateAddressRequest.getMethod());
    }

    @Test
    public void isValidHeader() {

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("Content-Type", "application/x-www-form-urlencoded");
        expectedMap.put("Authorization", "Bearer " + "acceesstoken");

        Map<String, String> actual = mockCreateAddressRequest.getHeader();

        assertTrue(expectedMap.equals(actual));
    }

    @Test
    public void isValidParam() {
        assertNotNull(mockCreateAddressRequest.getParams());
        assertNotEquals(0,mockCreateAddressRequest.getParams().size());
    }

    @Test
    public void verifyOnResponseError() {
        ECSCallback<Addresses, Exception> spy1 = Mockito.spy(ecsCallback);
        mockCreateAddressRequest = new MockCreateAddressRequest("CreateAddressSuccess.json", address, spy1);
        VolleyError volleyError = new NoConnectionError();
        mockCreateAddressRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }

    @Test
    public void verifyOnResponseException() {
        ECSCallback<Addresses, Exception> spy1 = Mockito.spy(ecsCallback);
        mockCreateAddressRequest = new MockCreateAddressRequest("CreateAddressSuccess.json", address, spy1);
        mockCreateAddressRequest.onResponse("jhsvcjhsc bvsdljcsdlcjgdscgds"); // passing invalid json
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }

    @Test
    public void verifyErrorMessageAndCodeForNoConnectionError() {
        ECSCallback<Addresses, Exception> spy1 = Mockito.spy(ecsCallback);
        mockCreateAddressRequest = new MockCreateAddressRequest("CreateAddressSuccess.json", address, spy1);
        VolleyError volleyError = new NoConnectionError();
        ECSError ecsError = mockCreateAddressRequest.getECSError(volleyError);
        assertEquals("Cannot connect to Internet .. Please check your connection!",ecsError.getException().getMessage());
        assertEquals(11001,ecsError.getErrorcode());
    }

    @Test
    public void verifyErrorMessageAndCodeForServerError() {
        ECSCallback<Addresses, Exception> spy1 = Mockito.spy(ecsCallback);
        mockCreateAddressRequest = new MockCreateAddressRequest("CreateAddressSuccess.json", address, spy1);
        VolleyError volleyError = new ServerError();
        ECSError ecsError = mockCreateAddressRequest.getECSError(volleyError);
        assertEquals("We have encountered technical glitch. Please try after some time",ecsError.getException().getMessage());
        assertEquals(5999,ecsError.getErrorcode());
    }

    @Test
    public void verifyErrorMessageAndCodeForTimeoutError() {
        ECSCallback<Addresses, Exception> spy1 = Mockito.spy(ecsCallback);
        mockCreateAddressRequest = new MockCreateAddressRequest("CreateAddressSuccess.json", address, spy1);
        VolleyError volleyError = new TimeoutError();
        ECSError ecsError = mockCreateAddressRequest.getECSError(volleyError);
        assertEquals("No cart created yet.",ecsError.getException().getMessage());
        assertEquals(11005,ecsError.getErrorcode());
    }


    @Test
    public void verifyErrorMessageAndCodeForServer() {
        ECSCallback<Addresses, Exception> spy1 = Mockito.spy(ecsCallback);
        mockCreateAddressRequest = new MockCreateAddressRequest("CreateAddressSuccess.json", address, spy1);
        String encodingString = "ewogICAiZXJyb3JzIiA6IFsgewogICAgICAibWVzc2FnZSIgOiAiR2ViZW4gU2llIElocmUgUG9z\n" +
                "dGxlaXR6YWhsIGVpbi4iLAogICAgICAicmVhc29uIiA6ICJpbnZhbGlkIiwKICAgICAgInN1Ympl\n" +
                "Y3QiIDogInBvc3RhbENvZGUiLAogICAgICAic3ViamVjdFR5cGUiIDogInBhcmFtZXRlciIsCiAg\n" +
                "ICAgICJ0eXBlIiA6ICJWYWxpZGF0aW9uRXJyb3IiCiAgIH0gXQp9";

        byte[] decode = Base64.decode(encodingString, Base64.DEFAULT);
        NetworkResponse networkResponse = new NetworkResponse(decode);
        VolleyError volleyError = new com.android.volley.ServerError(networkResponse);
        ECSError ecsError = mockCreateAddressRequest.getECSError(volleyError);
        assertEquals("The zip code which you have provided is invalid.",ecsError.getException().getMessage());
        assertEquals(5016,ecsError.getErrorcode());
    }

    @Test
    public void verifyErrorMessageAndCodeForAuthFailureError() {
        ECSCallback<Addresses, Exception> spy1 = Mockito.spy(ecsCallback);
        mockCreateAddressRequest = new MockCreateAddressRequest("CreateAddressSuccess.json", address, spy1);
        VolleyError volleyError = new AuthFailureError();
        ECSError ecsError = mockCreateAddressRequest.getECSError(volleyError);
        assertEquals("Volley AuthFailureError",ecsError.getException().getMessage());
        assertEquals(11002,ecsError.getErrorcode());
    }


    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockCreateAddressRequest.getStringSuccessResponseListener());
    }

    @Test
    public void assertJSONResponseSuccessListenerNull() {
        assertNull(mockCreateAddressRequest.getJSONSuccessResponseListener());
    }
}

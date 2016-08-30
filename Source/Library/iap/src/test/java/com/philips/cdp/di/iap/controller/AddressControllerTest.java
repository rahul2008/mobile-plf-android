/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.response.addresses.GetUser;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class AddressControllerTest{

    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;

    @Mock
    private AddressController mAddressController;
    @Mock
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(mContext);
    }

    @Test
    public void testNullStoreAndDelegate() throws JSONException{
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetRegions(Message msg) {
                assertEquals(RequestCode.GET_REGIONS, msg.what);
                assertTrue(msg.obj instanceof RegionsList);
            }
        });

        setStoreAndDelegate();
        mAddressController.setHybrisDelegate(null);
        mAddressController.setStore(null);
        mAddressController.getRegions();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "Region.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetRegionsSuccessResponseWithData() throws JSONException {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetRegions(Message msg) {
                assertEquals(RequestCode.GET_REGIONS, msg.what);
                assertTrue(msg.obj instanceof RegionsList);
            }
        });

        setStoreAndDelegate();
        mAddressController.getRegions();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "Region.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetRegionsEmptySuccessResponse() throws JSONException {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetRegions(Message msg) {
                testEmptyResponse(msg, RequestCode.GET_REGIONS);
            }
        });

        setStoreAndDelegate();
        mAddressController.getRegions();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetRegionsErrorResponse() {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetRegions(Message msg) {
                testErrorResponse(msg, RequestCode.GET_REGIONS);
            }
        });

        setStoreAndDelegate();
        mAddressController.getRegions();
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testGetUserSuccessResponseWithData() throws JSONException {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetUser(Message msg) {
                assertEquals(RequestCode.GET_USER, msg.what);
                assertTrue(msg.obj instanceof GetUser);
            }
        });

        setStoreAndDelegate();
        mAddressController.getUser();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "GetUser.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetUserEmptySuccessResponse() throws JSONException {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetUser(Message msg) {
                testEmptyResponse(msg, RequestCode.GET_USER);
            }
        });

        setStoreAndDelegate();
        mAddressController.getUser();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetUserErrorResponse() {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetUser(Message msg) {
                testErrorResponse(msg, RequestCode.GET_USER);
            }
        });

        setStoreAndDelegate();
        mAddressController.getUser();
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testCreateAddressSuccessResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onCreateAddress(final Message msg) {
                assertEquals(RequestCode.CREATE_ADDRESS, msg.what);
                assertTrue(msg.obj instanceof Addresses);
            }
        });

        setStoreAndDelegate();
        AddressFields address = new AddressFields();
        address.setTitleCode("Mr");
        mAddressController.createAddress(address);
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "CreateAddress.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testCreateAddressErrorResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onCreateAddress(final Message msg) {
                testErrorResponse(msg, RequestCode.CREATE_ADDRESS);
            }
        });

        setStoreAndDelegate();
        AddressFields address = new AddressFields();
        address = formAddressBodyParams(address);

        mAddressController.createAddress(address);
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testGetAddressesSuccessResponseWithData() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(Message msg) {
                assertEquals(RequestCode.GET_ADDRESS, msg.what);
                assertTrue(msg.obj instanceof GetShippingAddressData);
            }
        });

        setStoreAndDelegate();
        mAddressController.getAddresses();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "Addresses.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetAddressesEmptyResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(Message msg) {
                testEmptyResponse(msg, RequestCode.GET_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mAddressController.getAddresses();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetAddressesErrorResponse() {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(Message msg) {
                testErrorResponse(msg, RequestCode.GET_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mAddressController.getAddresses();
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testDeleteAddressSuccessResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(final Message msg) {
                testEmptyResponse(msg, RequestCode.DELETE_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mAddressController.deleteAddress("8799470125079");
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testDeleteAddressErrorResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(final Message msg) {
                testErrorResponse(msg, RequestCode.DELETE_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mAddressController.deleteAddress("8799470125079");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testUpdateAddressSuccessResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(final Message msg) {
                testEmptyResponse(msg, RequestCode.UPDATE_ADDRESS);
            }
        });

        setStoreAndDelegate();
        HashMap<String, String> address = new HashMap<>();
        address.put(ModelConstants.ADDRESS_ID, "8799470125079");
        mAddressController.updateAddress(address);
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testUpdateAddressErrorResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(final Message msg) {
                testErrorResponse(msg, RequestCode.UPDATE_ADDRESS);
            }
        });

        setStoreAndDelegate();
        HashMap<String, String> address = new HashMap<>();
        address.put(ModelConstants.ADDRESS_ID, "8799470125079");
        mAddressController.updateAddress(address);
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testSetDeliveryAddressSuccessResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onSetDeliveryAddress(Message msg) {
                testEmptyResponse(msg, RequestCode.SET_DELIVERY_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mAddressController.setDeliveryAddress("8799470125079");
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testSetDeliveryAddressWithNull() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onSetDeliveryAddress(Message msg) {
                testErrorResponse(msg, RequestCode.SET_DELIVERY_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mAddressController.setDeliveryAddress(null);
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testSetDeliveryAddressErrorResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onSetDeliveryAddress(Message msg) {
                testErrorResponse(msg, RequestCode.SET_DELIVERY_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mAddressController.setDeliveryAddress("8799470125079");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testSetDeliveryModeSuccessResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onSetDeliveryMode(final Message msg) {
                testSuccessResponse(msg, RequestCode.SET_DELIVERY_MODE);
            }
        });

        setStoreAndDelegate();
        mAddressController.setDeliveryMode("");
        mNetworkController.sendSuccess(null);
    }

    @Test
    public void testSetDeliveryModeErrorResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onSetDeliveryMode(Message msg) {
                testErrorResponse(msg, RequestCode.SET_DELIVERY_MODE);
            }
        });

        setStoreAndDelegate();
        mAddressController.setDeliveryMode("");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testGetDeliveryModesSuccessResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetDeliveryModes(final Message msg) {
                assertEquals(RequestCode.GET_DELIVERY_MODE, msg.what);
                assertTrue(msg.obj instanceof GetDeliveryModes);
            }
        });

        setStoreAndDelegate();
        mAddressController.getDeliveryModes();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "DeliveryModes.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetDeliveryModesErrorResponse() throws Exception {
        mAddressController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetDeliveryModes(final Message msg) {
                testErrorResponse(msg, RequestCode.GET_DELIVERY_MODE);
            }
        });

        setStoreAndDelegate();
        mAddressController.getDeliveryModes();
        mNetworkController.sendFailure(new VolleyError());
    }

    public AddressFields formAddressBodyParams(AddressFields address) {
        address.setTitleCode("Mr");
        address.setFirstName("Happy");
        address.setLastName("User");
        address.setCountryIsocode("US");
        address.setRegionIsoCode("US-CA");
        address.setLine1("Line1");
        address.setLine2("Line2");
        address.setPostalCode("92821");
        address.setTown("California");
        address.setPhoneNumber("+1877-682-8207");
        return address;
    }

    public void testErrorResponse(Message msg, int requestCode) {
        assertEquals(requestCode, msg.what);
        assertTrue(msg.obj instanceof IAPNetworkError);
    }

    public void testEmptyResponse(Message msg, int requestCode) {
        assertEquals(requestCode, msg.what);
        assertEquals(NetworkConstants.EMPTY_RESPONSE, msg.obj);
    }

    public void testSuccessResponse(Message msg, int requestCode) {
        assertEquals(requestCode, msg.what);
        assertEquals(IAPConstant.IAP_SUCCESS, msg.obj);
    }

    public void setStoreAndDelegate() {
        mAddressController.setHybrisDelegate(mHybrisDelegate);
        mAddressController.setStore(TestUtils.getStubbedStore());
    }
}
package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class AddressControllerTest {

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
        address.setTitleCode("Mr");
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

    public void testUpdateAddress() throws Exception {

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
        mAddressController.setDeliveryAddress("");
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
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
        mAddressController.setDeliveryAddress("");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testSetDeliveryModeSuccessResponse() throws Exception {

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

    public void testErrorResponse(Message msg, int requestCode){
        assertEquals(requestCode, msg.what);
        assertTrue(msg.obj instanceof IAPNetworkError);
    }

    public void testEmptyResponse(Message msg, int requestCode){
        assertEquals(requestCode, msg.what);
        assertEquals(NetworkConstants.EMPTY_RESPONSE, msg.obj);
    }

    public void testSuccessResponse(Message msg, int requestCode){
        assertEquals(requestCode, msg.what);
        assertEquals(IAPConstant.IAP_SUCCESS, msg.obj);
    }

    public void setStoreAndDelegate() {
        mAddressController.setHybrisDelegate(mHybrisDelegate);
        mAddressController.setStore(TestUtils.getStubbedStore());
    }
}
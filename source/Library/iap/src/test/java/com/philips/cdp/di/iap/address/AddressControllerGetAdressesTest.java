package com.philips.cdp.di.iap.address;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.Country;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.response.addresses.Region;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.RequestCode;

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
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
public class AddressControllerGetAdressesTest {


    private MockNetworkController mNetworkController;
    @Mock
    private HybrisDelegate mHybrisDelegate;
    @Mock
    private AddressController mController;
    @Mock
    private Context mContext;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mNetworkController = new MockNetworkController(mContext);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
    }

    private HashMap getQueryString() {
        HashMap<String, String> addressHashMap = new HashMap<>();
        addressHashMap.put(ModelConstants.FIRST_NAME, "Spoorti");
        addressHashMap.put(ModelConstants.LAST_NAME, "Hallur");
        addressHashMap.put(ModelConstants.TITLE_CODE, "mr");
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, "US");
        addressHashMap.put(ModelConstants.LINE_1, "NRI Layout");
        addressHashMap.put(ModelConstants.POSTAL_CODE, "590043");
        addressHashMap.put(ModelConstants.TOWN, "Bangalore");
        addressHashMap.put(ModelConstants.ADDRESS_ID, "8799470125079");
        addressHashMap.put(ModelConstants.LINE_2, "");
        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, "Bangalore");
        addressHashMap.put(ModelConstants.PHONE_1, "2453696");
        return addressHashMap;
    }

    @Test
    public void verifyDeleteAddresses() throws JSONException {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(final Message msg) {
                assertEquals(RequestCode.DELETE_ADDRESS, msg.what);
            }
        });

        setStoreAndDelegate();
        mController.deleteAddress("8799470125079");
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerGetAdressesTest
                .class, "one_addresses.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void verifyUpdateAddresses() throws JSONException {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(final Message msg) {
                assertEquals(RequestCode.UPDATE_ADDRESS, msg.what);
            }
        });

        setStoreAndDelegate();
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ADDRESS_ID,"123");
        mController.updateAddress(query);
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerGetAdressesTest
                .class, "one_addresses.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void verifyCreateAddresses() throws JSONException {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onCreateAddress(final Message msg) {
                assertEquals(RequestCode.CREATE_ADDRESS, msg.what);
            }
        });

        setStoreAndDelegate();

        AddressFields addr = new AddressFields();
        addr.setTitleCode("Mr");
        mController.createAddress(addr);
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerGetAdressesTest
                .class, "one_addresses.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void verifySetDeliveryAddresses() throws JSONException {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onSetDeliveryAddress(final Message msg) {
                assertEquals(RequestCode.SET_DELIVERY_ADDRESS, msg.what);
            }
        });

        setStoreAndDelegate();

        mController.setDeliveryAddress("");
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerGetAdressesTest
                .class, "one_addresses.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void verifySetDeliveryMode() throws JSONException {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onSetDeliveryModes(final Message msg) {
                assertEquals(RequestCode.SET_DELIVERY_MODE, msg.what);
            }
        });

        setStoreAndDelegate();

        mController.setDeliveryMode();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerGetAdressesTest
                .class, "one_addresses.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void verifySetDefaultAddress() {
        mController = new AddressController(mContext, null);
        Addresses addr = mock(Addresses.class);
        when(addr.getCountry()).thenReturn(new Country());
        when(addr.getRegion()).thenReturn(new Region());
        setStoreAndDelegate();
        mController.setDefaultAddress(addr);
    }

    public void setStoreAndDelegate() {
        mController.setHybrisDelegate(mHybrisDelegate);
        mController.setStore(TestUtils.getStubbedStore());
    }

    @Test
    public void verifyAddressDeatilsGetAddresses() throws JSONException {
        mController = new AddressController(mContext, new MockAddressListener() {

            @Override
            public void onGetAddress(Message msg) {
                assertNotNull(msg);
                GetShippingAddressData result = (GetShippingAddressData) msg.obj;
                Addresses addresses = result.getAddresses().get(0);
                assertEquals("Harmeet", addresses.getFirstName());
                assertEquals("Singh", addresses.getLastName());
                assertEquals("test", addresses.getLine1());
                assertEquals("test", addresses.getLine2());
                assertEquals("12-345", addresses.getPostalCode());
                assertEquals("test", addresses.getTown());
                assertEquals("PL", addresses.getCountry().getIsocode());
                assertEquals("8796158590999", addresses.getId());
            }
        });
        setStoreAndDelegate();

        mController.getShippingAddresses();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerGetAdressesTest
                .class, "one_addresses.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void sendErrorAlsoCallsTheSameCallBack() throws JSONException {
        mController = new AddressController(mContext, new MockAddressListener() {

            @Override
            public void onGetAddress(Message msg) {
                assertNotNull(msg);
            }
        });
        setStoreAndDelegate();

        mController.getShippingAddresses();
        JSONObject obj = new JSONObject(TestUtils.readFile(AddressControllerGetAdressesTest
                .class, "one_addresses.txt"));
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void noCrashOnSendingEmptyResponse() throws JSONException {
        mController = new AddressController(mContext, new MockAddressListener());
        setStoreAndDelegate();
        mController.getShippingAddresses();
        JSONObject object = new JSONObject("{}");
        mNetworkController.sendSuccess(object);
    }
}

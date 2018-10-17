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
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
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
public class PaymentControllerTest {
    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;

    @Mock
    private PaymentController mPaymentController;
    @Mock
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(mContext);
    }

    @Test
    public void testNullStoreAndDelegate() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockPaymentListener() {
            @Override
            public void onGetPaymentDetails(Message msg) {
                assertEquals(RequestCode.GET_PAYMENT_DETAILS, msg.what);
                assertTrue(msg.obj instanceof PaymentMethods);
            }
        });

        setStoreAndDelegate();
        mPaymentController.setHybrisDelegate(null);
        mPaymentController.setStore(null);
        mPaymentController.getPaymentDetails();
        JSONObject obj = new JSONObject(TestUtils.readFile(PaymentControllerTest.class, "Payment.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetPaymentDetailsSuccessResponse() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockPaymentListener() {
            @Override
            public void onGetPaymentDetails(Message msg) {
                assertEquals(RequestCode.GET_PAYMENT_DETAILS, msg.what);
                assertTrue(msg.obj instanceof PaymentMethods);
            }
        });

        setStoreAndDelegate();
        mPaymentController.getPaymentDetails();
        JSONObject obj = new JSONObject(TestUtils.readFile(PaymentControllerTest.class, "Payment.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetPaymentDetailsEmptySuccessResponse() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockPaymentListener() {
            @Override
            public void onGetPaymentDetails(Message msg) {
                testEmptyResponse(msg, RequestCode.GET_PAYMENT_DETAILS);
            }
        });

        setStoreAndDelegate();
        mPaymentController.getPaymentDetails();
        JSONObject obj = new JSONObject(TestUtils.readFile(PaymentControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetPaymentDetailsErrorResponse() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockPaymentListener() {
            @Override
            public void onGetPaymentDetails(Message msg) {
                testErrorResponse(msg, RequestCode.GET_PAYMENT_DETAILS);
            }
        });

        setStoreAndDelegate();
        mPaymentController.getPaymentDetails();
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testSetPaymentDetailsSuccessResponse() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockPaymentListener() {
            @Override
            public void onSetPaymentDetails(Message msg) {
                testSuccessResponse(msg, RequestCode.SET_PAYMENT_DETAILS);
            }
        });

        setStoreAndDelegate();
        mPaymentController.setPaymentDetails("");
        mNetworkController.sendSuccess(null);
    }

    @Test
    public void testSetPaymentDetailsErrorResponse() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockPaymentListener() {
            @Override
            public void onSetPaymentDetails(Message msg) {
                testErrorResponse(msg, RequestCode.SET_PAYMENT_DETAILS);
            }
        });

        setStoreAndDelegate();
        mPaymentController.setPaymentDetails("");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testPlaceOrderSuccessResponse() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockMakePaymentListener() {
            @Override
            public void onPlaceOrder(Message msg) {
                assertEquals(RequestCode.PLACE_ORDER, msg.what);
                assertTrue(msg.obj instanceof PlaceOrder);
            }
        });
        setStoreAndDelegate();
        AddressFields address = new AddressFields();
        CartModelContainer.getInstance().setBillingAddress(formAddressBodyParams(address));
        mPaymentController.placeOrder("");
        JSONObject obj = new JSONObject(TestUtils.readFile(PaymentControllerTest.class, "PlaceOrder.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testPlaceOrderErrorResponse() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockMakePaymentListener() {
            @Override
            public void onPlaceOrder(Message msg) {
                testErrorResponse(msg, RequestCode.PLACE_ORDER);
            }
        });

        setStoreAndDelegate();
        AddressFields address = new AddressFields();
        CartModelContainer.getInstance().setBillingAddress(formAddressBodyParams(address));
        mPaymentController.placeOrder("");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testMakePaymentSuccessResponse() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockMakePaymentListener() {
            @Override
            public void onMakePayment(Message msg) {
                assertEquals(RequestCode.MAKE_PAYMENT, msg.what);
            }
        });
        setStoreAndDelegate();

        AddressFields address = new AddressFields();
        CartModelContainer.getInstance().setBillingAddress(formAddressBodyParams(address));
        mPaymentController.makPayment("34256");
        JSONObject obj = new JSONObject(TestUtils.readFile(PaymentControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testMakePaymentErrorResponse() throws JSONException {
        mPaymentController = new PaymentController(mContext, new MockMakePaymentListener() {
            @Override
            public void onMakePayment(Message msg) {
                assertEquals(RequestCode.MAKE_PAYMENT, msg.what);
            }
        });

        setStoreAndDelegate();
        AddressFields address = new AddressFields();
        CartModelContainer.getInstance().setBillingAddress(formAddressBodyParams(address));
        mPaymentController.makPayment("34256");
        mNetworkController.sendFailure(new VolleyError());
    }

    public AddressFields formAddressBodyParams(AddressFields address) {
        address.setTitleCode("Mr");
        address.setFirstName("Happy");
        address.setLastName("User");
        address.setCountryIsocode("US");
        address.setRegionIsoCode("US-CA");
        address.setLine1("Line1");
        address.setPostalCode("92821");
        address.setTown("California");
        address.setPhone1("+1877-682-8207");
        address.setPhone2("+1877-682-8207");
        address.setEmail("testinapp@mailinator.com");
        address.setRegionName("US");
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
        mPaymentController.setHybrisDelegate(mHybrisDelegate);
        mPaymentController.setStore(TestUtils.getStubbedStore());
    }
}
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class RequestCodeTest {

    @Test
    public void testRequestCode() {
        assertEquals(1, RequestCode.GET_CART);
        assertEquals(2, RequestCode.CREATE_CART);
        assertEquals(3, RequestCode.ADD_TO_CART);
        assertEquals(4, RequestCode.GET_ADDRESS);
        assertEquals(5, RequestCode.CREATE_ADDRESS);
        assertEquals(6, RequestCode.UPDATE_ADDRESS);
        assertEquals(7, RequestCode.DELETE_ADDRESS);
        assertEquals(8, RequestCode.SET_DELIVERY_ADDRESS);
        assertEquals(9, RequestCode.SET_DELIVERY_MODE);
        assertEquals(10, RequestCode.GET_PAYMENT_DETAILS);
        assertEquals(11, RequestCode.SET_PAYMENT_DETAILS);
        assertEquals(12, RequestCode.PLACE_ORDER);
        assertEquals(13, RequestCode.MAKE_PAYMENT);
        assertEquals(14, RequestCode.GET_REGIONS);
        assertEquals(15, RequestCode.GET_ORDERS);
        assertEquals(16, RequestCode.GET_ORDER_DETAIL);
        assertEquals(17, RequestCode.SEARCH_PRODUCT);
        assertEquals(18, RequestCode.GET_DELIVERY_MODE);
        assertEquals(19, RequestCode.GET_USER);
        assertEquals(20, RequestCode.GET_PHONE_CONTACT);
        assertEquals(21, RequestCode.DELETE_CART);
    }

}
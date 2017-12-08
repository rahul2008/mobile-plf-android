/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import junit.framework.TestCase;

import org.junit.Test;

public class RequestCodeTest extends TestCase {

    @Test
    public void testRequestCode() {
        assertEquals("Value is Same", 1, RequestCode.GET_CART);
        assertEquals("Value is Same", 2, RequestCode.CREATE_CART);
        assertEquals("Value is Same", 3, RequestCode.ADD_TO_CART);
        assertEquals("Value is Same", 4, RequestCode.GET_ADDRESS);
        assertEquals("Value is Same", 5, RequestCode.CREATE_ADDRESS);
        assertEquals("Value is Same", 6, RequestCode.UPDATE_ADDRESS);
        assertEquals("Value is Same", 7, RequestCode.DELETE_ADDRESS);
        assertEquals("Value is Same", 8, RequestCode.SET_DELIVERY_ADDRESS);
        assertEquals("Value is Same", 9, RequestCode.SET_DELIVERY_MODE);
        assertEquals("Value is Same", 10, RequestCode.GET_PAYMENT_DETAILS);
        assertEquals("Value is Same", 11, RequestCode.SET_PAYMENT_DETAILS);
        assertEquals("Value is Same", 12, RequestCode.PLACE_ORDER);
        assertEquals("Value is Same", 13, RequestCode.MAKE_PAYMENT);
        assertEquals("Value is Same", 14, RequestCode.GET_REGIONS);
        assertEquals("Value is Same", 15, RequestCode.GET_ORDERS);
        assertEquals("Value is Same", 16, RequestCode.GET_ORDER_DETAIL);
        assertEquals("Value is Same", 17, RequestCode.SEARCH_PRODUCT);
        assertEquals("Value is Same", 18, RequestCode.GET_DELIVERY_MODE);
        assertEquals("Value is Same", 19, RequestCode.GET_USER);
        assertEquals("Value is Same", 20, RequestCode.GET_PHONE_CONTACT);
        assertEquals("Value is Same", 21, RequestCode.DELETE_CART);
    }

}
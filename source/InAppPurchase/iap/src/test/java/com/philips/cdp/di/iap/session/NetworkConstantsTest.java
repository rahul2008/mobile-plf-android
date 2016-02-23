package com.philips.cdp.di.iap.session;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Locale;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NetworkConstantsTest extends TestCase {

    @Test
    public void testNetworkConstantValues() {
        assertEquals("https://tst.pl.shop.philips.com/", NetworkConstants.HOST_URL);
        assertEquals("pilcommercewebservices/", NetworkConstants.WEB_ROOT);
        assertEquals("v2/", NetworkConstants.V2);
        assertEquals("US_Tuscany/", NetworkConstants.APP_CONFIG);
        assertEquals("users/null/", "users/null/");
        assertEquals("https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/null/", NetworkConstants.BASE_URL);
        assertEquals("carts/current", NetworkConstants.CURRENT_CART);
    }

    @Test
    public void testCartURLValues() {
        assertEquals("https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/null/carts?fields=FULL", NetworkConstants.GET_CURRENT_CART_URL);
        assertEquals("https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/null/carts/current/entries", NetworkConstants.ADD_TO_CART_URL);
        assertEquals("https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/null/carts/current/entries/%s", NetworkConstants.DELETE_PRODUCT_URL);
        assertEquals("https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/null/carts/current/entries/%s", NetworkConstants.UPDATE_QUANTITY_URL);
    }

    @Test
    public void testAddressConstant() {
        assertEquals("https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/null/addresses?fields=FULL", NetworkConstants.ADDRESS_URL);
    }

    @Test
    public void testUpdateAddressURLConstant() {
        assertEquals("https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/null/addresses/%s", NetworkConstants.UPDATE_OR_DELETE_ADDRESS_URL);
    }

    @Test
    public void testPRXConstant() {
        assertEquals("B2C", NetworkConstants.PRX_SECTOR_CODE);
        assertEquals("en_US", NetworkConstants.PRX_LOCALE);
        assertEquals("CONSUMER", NetworkConstants.PRX_CATALOG_CODE);
        assertEquals("EXTRA_ANIMATIONTYPE", NetworkConstants.EXTRA_ANIMATIONTYPE);
        assertEquals("IS_ONLINE", NetworkConstants.IS_ONLINE);
        assertEquals(Locale.US, NetworkConstants.STORE_LOCALE);
        assertEquals("", NetworkConstants.EMPTY_RESPONSE);
    }
}
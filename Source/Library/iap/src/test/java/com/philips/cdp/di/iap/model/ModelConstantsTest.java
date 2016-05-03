package com.philips.cdp.di.iap.model;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;

/**
 * Created by 310164421 on 5/3/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ModelConstantsTest {

    @Test
    public void assertionofAllConstant() {
        assertEquals("code", ModelConstants.PRODUCT_CODE);
        assertEquals("qty", ModelConstants.PRODUCT_QUANTITY);
        assertEquals("entrycode", ModelConstants.PRODUCT_ENTRYCODE);
        assertEquals("entrynumber", ModelConstants.ENTRY_CODE);

        assertEquals("firstName", ModelConstants.FIRST_NAME);
        assertEquals("lastName", ModelConstants.LAST_NAME);
        assertEquals("titleCode", ModelConstants.TITLE_CODE);
        assertEquals("country.isocode", ModelConstants.COUNTRY_ISOCODE);
        assertEquals("line1", ModelConstants.LINE_1);
        assertEquals("line2", ModelConstants.LINE_2);
        assertEquals("postalCode", ModelConstants.POSTAL_CODE);
        assertEquals("town", ModelConstants.TOWN);
        assertEquals("phoneNumber", ModelConstants.PHONE_NUMBER);
        assertEquals("phone1", ModelConstants.PHONE_1);
        assertEquals("phone2", ModelConstants.PHONE_2);
        assertEquals("region.isocode", ModelConstants.REGION_ISOCODE);
        assertEquals("region.code", ModelConstants.REGION_CODE);
        assertEquals("addressId", ModelConstants.ADDRESS_ID);
        assertEquals("defaultAddress", ModelConstants.DEFAULT_ADDRESS);
        assertEquals("email_address", ModelConstants.EMAIL_ADDRESS);
        assertEquals("deliveryModeId", ModelConstants.DEVLVERY_MODE_ID);
        assertEquals("paymentDetailsId", ModelConstants.PAYMENT_DETAILS_ID);
        assertEquals("webpay_url", ModelConstants.WEBPAY_URL);
        assertEquals("order_number", ModelConstants.ORDER_NUMBER);
        assertEquals("cartId", ModelConstants.CART_ID);
        assertEquals("payment_success_status", ModelConstants.PAYMENT_SUCCESS_STATUS);
        assertEquals("refresh_token", ModelConstants.REFRESH_TOKEN);
    }
}
package com.philips.cdp.digitalcare.util;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by philips on 7/6/17.
 */

public class DigitalCareConstantsRTest extends TestCase {


    @Test
    public void testConstants() {
    new DigitalCareConstants();

        assertEquals("Value is Same", "startAnimation", DigitalCareConstants.START_ANIMATION_ID);
        assertEquals("Value is Same", "stopAnimation", DigitalCareConstants.STOP_ANIMATION_ID);
        assertEquals("Value is Same", "orientation", DigitalCareConstants.SCREEN_ORIENTATION);
        assertEquals("Value is Same", 1010, DigitalCareConstants.IMAGE_PICK);
        assertEquals("Value is Same", 1001, DigitalCareConstants.IMAGE_CAPTURE);
        assertEquals("Value is Same", "phoneNumber", DigitalCareConstants.CDLS_PHONENUMBER);
        assertEquals("Value is Same", "openingHoursWeekdays", DigitalCareConstants.CDLS_OPENINGHOURS_WEEKDAYS);
        assertEquals("Value is Same", "openingHoursSunday", DigitalCareConstants.CDLS_OPENINGHOURS_SUNDAY);
        assertEquals("Value is Same", "openingHoursSaturday", DigitalCareConstants.CDLS_OPENINGHOURS_SATURDAY);
        assertEquals("Value is Same", "optionalData1", DigitalCareConstants.CDLS_OPTIONALDATA_ONE);
        assertEquals("Value is Same", "optionalData2", DigitalCareConstants.CDLS_OPTIONALDATA_TWO);
        assertEquals("Value is Same", "phoneTariff", DigitalCareConstants.CDLS_PHONE_TARIFF_KEY);
        assertEquals("Value is Same", "content", DigitalCareConstants.CDLS_CHAT_CONTENT);
        assertEquals("Value is Same", "script", DigitalCareConstants.CDLS_CHAT_SCRIPT);
        assertEquals("Value is Same", "openingHoursWeekdays", DigitalCareConstants.CDLS_CHAT_OPENINGINGHOURS_WEEKDAYS);
        assertEquals("Value is Same", "openingHoursSaturday", DigitalCareConstants.CDLS_CHAT_OPENINGHOURS_SATURDAY);
        assertEquals("Value is Same", "label", DigitalCareConstants.CDLS_EMAIL_LABEL);
        assertEquals("Value is Same", "contentPath", DigitalCareConstants.CDLS_EMAIL_CONTENTPATH);

        assertEquals("Value is Same", "errorCode", DigitalCareConstants.CDLS_ERROR_CODE);
        assertEquals("Value is Same", "errorMessage", DigitalCareConstants.CDLS_ERROR_MESSAGE);
        assertEquals("Value is Same", "error", DigitalCareConstants.CDLS_ERROR_KEY);
        assertEquals("Value is Same", "success", DigitalCareConstants.CDLS_SUCCESS_KEY);
        assertEquals("Value is Same", "data", DigitalCareConstants.CDLS_DATA_KEY);
        assertEquals("Value is Same", "phone", DigitalCareConstants.CDLS_PHONE_KEY);
        assertEquals("Value is Same", "email", DigitalCareConstants.CDLS_EMAIL_KEY);
        assertEquals("Value is Same", "chat", DigitalCareConstants.CDLS_CHAT_KEY);
        assertEquals("Value is Same", "digitalcare", DigitalCareConstants.DIGITALCARE_FRAGMENT_TAG);

        assertEquals("Value is Same", "cc.cdls", DigitalCareConstants.SERVICE_ID_CC_CDLS);
        assertEquals("Value is Same", "cc.emailformurl", DigitalCareConstants.SERVICE_ID_CC_EMAILFROMURL);
        assertEquals("Value is Same", "cc.prx.category", DigitalCareConstants.SERVICE_ID_CC_PRX_CATEGORY);
        assertEquals("Value is Same", "cc.productreviewurl", DigitalCareConstants.SERVICE_ID_CC_PRODUCTREVIEWURL);
        assertEquals("Value is Same", "cc.atos", DigitalCareConstants.SERVICE_ID_CC_ATOS);

        assertEquals("Value is Same", "productSector", DigitalCareConstants.KEY_PRODUCT_SECTOR);
        assertEquals("Value is Same", "productCatalog", DigitalCareConstants.KEY_PRODUCT_CATALOG);
        assertEquals("Value is Same", "productCategory", DigitalCareConstants.KEY_PRODUCT_CATEGORY);
        assertEquals("Value is Same", "productSubCategory", DigitalCareConstants.KEY_PRODUCT_SUBCATEGORY);
        assertEquals("Value is Same", "productReviewURL", DigitalCareConstants.KEY_PRODUCT_REVIEWURL);
        assertEquals("Value is Same", "appName", DigitalCareConstants.KEY_APPNAME);
        assertEquals("Value is Same", "lattitude", DigitalCareConstants.KEY_LATITUDE);
        assertEquals("Value is Same", "longitude", DigitalCareConstants.KEY_LONGITUDE);
    }

}

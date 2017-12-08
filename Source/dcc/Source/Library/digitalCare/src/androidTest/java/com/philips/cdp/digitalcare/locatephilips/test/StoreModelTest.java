package com.philips.cdp.digitalcare.locatephilips.test;

import android.test.InstrumentationTestCase;

import com.philips.cdp.digitalcare.locatephilips.models.AtosErrorModel;
import com.philips.cdp.digitalcare.locatephilips.models.StoreModel;

/**
 * Created by 310190678 on 09-Mar-16.
 */
public class StoreModelTest extends InstrumentationTestCase {

    private static final String TAG = StoreModelTest.class.getSimpleName();
    private StoreModel storeModel = null;
    private AtosErrorModel errorModel = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        storeModel = new StoreModel();
        errorModel = new AtosErrorModel();
    }
/*
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(TAG);
    }*/

    public void loadPreLoadedData() {
        storeModel.setOpeningHoursSaturday("12/09.12");
        storeModel.setOpeningHoursSunday("02/12/78");
        storeModel.setOpeningHoursWeekdays("01/01/2011");
        storeModel.setPhoneNumber("128920930-923");
    }

    public void loadPreLoadedEmptyData() {
        storeModel.setOpeningHoursSaturday("");
        storeModel.setOpeningHoursSunday("");
        storeModel.setOpeningHoursWeekdays("");
        storeModel.setPhoneNumber("");
    }

    public void loadPreLoadedNullData() {
        storeModel.setOpeningHoursSaturday(null);
        storeModel.setOpeningHoursSunday(null);
        storeModel.setOpeningHoursWeekdays(null);
        storeModel.setPhoneNumber(null);
    }


    public void testWeekEndsDataFromModel() {
        loadPreLoadedData();
        assertEquals("02/12/78", storeModel.getOpeningHoursSunday());
        assertEquals("12/09.12", storeModel.getOpeningHoursSaturday());
    }

    public void testWeekDaysDataFromModel() {
        loadPreLoadedData();
        assertEquals("01/01/2011", storeModel.getOpeningHoursWeekdays());
    }

    public void testProductCustomerCareNumber() {
        loadPreLoadedData();
        assertEquals("128920930-923", storeModel.getPhoneNumber());
    }

    public void loadAtosErrorData() {
        errorModel.setErrorCode("200");
        errorModel.setErrorMessage("SUCCESS");
    }


    public void testWeekEndsDataFromModelSecondAttempt() {
        loadPreLoadedEmptyData();
        assertEquals("", storeModel.getOpeningHoursSunday());
        assertEquals("", storeModel.getOpeningHoursSaturday());
    }

    public void testWeekDaysDataFromModelSecondAttempt() {
        loadPreLoadedEmptyData();
        assertEquals("", storeModel.getOpeningHoursWeekdays());
    }

    public void testProductCustomerCareNumberSecondAttempt() {
        loadPreLoadedEmptyData();
        assertEquals("", storeModel.getPhoneNumber());
    }
/*

    public void testWeekEndsDataFromModelThirdAttempt() {
        loadPreLoadedNullData();
        try {
            storeModel.getOpeningHoursSunday();
            storeModel.getOpeningHoursSaturday();
        } catch (NullPointerException e) {
            assertTrue(true);
        }

    }

    public void testWeekDaysDataFromModelThirdAttempt() {
        loadPreLoadedNullData();
        try {
            assertNull(storeModel.getOpeningHoursWeekdays());
        } catch (NullPointerException e) {
            assertTrue(true);
        }

    }

    public void testProductCustomerCareNumberThirdAttempt() {
        loadPreLoadedNullData();
        try {
            assertNull(storeModel.getPhoneNumber());
        } catch (NullPointerException e) {
            assertTrue(true);
        }

    }*/

    public void testAtosErrorModel() {
        loadAtosErrorData();
        assertEquals("200", errorModel.getErrorCode());
        assertEquals("SUCCESS", errorModel.getErrorMessage());
    }

}

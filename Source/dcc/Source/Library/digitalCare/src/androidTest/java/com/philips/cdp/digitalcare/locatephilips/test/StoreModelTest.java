/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.locatephilips.test;

import com.philips.cdp.digitalcare.locatephilips.models.AtosErrorModel;
import com.philips.cdp.digitalcare.locatephilips.models.StoreModel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by 310190678 on 09-Mar-16.
 */
public class StoreModelTest {

    private static final String TAG = StoreModelTest.class.getSimpleName();
    private StoreModel storeModel = null;
    private AtosErrorModel errorModel = null;

    @Before
    protected void setUp() throws Exception {
        storeModel = new StoreModel();
        errorModel = new AtosErrorModel();
    }

    @Test
    public void testWeekEndsDataFromModel() {
        loadPreLoadedData();
        assertEquals("02/12/78", storeModel.getOpeningHoursSunday());
        assertEquals("12/09.12", storeModel.getOpeningHoursSaturday());
    }

    @Test
    public void testWeekDaysDataFromModel() {
        loadPreLoadedData();
        assertEquals("01/01/2011", storeModel.getOpeningHoursWeekdays());
    }

    @Test
    public void testProductCustomerCareNumber() {
        loadPreLoadedData();
        assertEquals("128920930-923", storeModel.getPhoneNumber());
    }

    @Test
    public void testWeekEndsDataFromModelSecondAttempt() {
        loadPreLoadedEmptyData();
        assertEquals("", storeModel.getOpeningHoursSunday());
        assertEquals("", storeModel.getOpeningHoursSaturday());
    }

    @Test
    public void testWeekDaysDataFromModelSecondAttempt() {
        loadPreLoadedEmptyData();
        assertEquals("", storeModel.getOpeningHoursWeekdays());
    }

    @Test
    public void testProductCustomerCareNumberSecondAttempt() {
        loadPreLoadedEmptyData();
        assertEquals("", storeModel.getPhoneNumber());
    }

    @Test
    public void testAtosErrorModel() {
        loadAtosErrorData();
        assertEquals("200", errorModel.getErrorCode());
        assertEquals("SUCCESS", errorModel.getErrorMessage());
    }

    private void loadPreLoadedData() {
        storeModel.setOpeningHoursSaturday("12/09.12");
        storeModel.setOpeningHoursSunday("02/12/78");
        storeModel.setOpeningHoursWeekdays("01/01/2011");
        storeModel.setPhoneNumber("128920930-923");
    }

    private void loadPreLoadedEmptyData() {
        storeModel.setOpeningHoursSaturday("");
        storeModel.setOpeningHoursSunday("");
        storeModel.setOpeningHoursWeekdays("");
        storeModel.setPhoneNumber("");
    }

    private void loadAtosErrorData() {
        errorModel.setErrorCode("200");
        errorModel.setErrorMessage("SUCCESS");
    }
}

package com.philips.cdp.registration.controller;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class UpdateUserRecordTest extends RegistrationApiInstrumentationBase {

    Context mContext;
    UpdateUserRecord updateUserRecord;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
//        updateUserRecord = new UpdateUserRecord(mContext);
    }

    @Test
    public void testUpdateUserRecord(){
//        assertNotNull(updateUserRecord);
//        updateUserRecord.updateUserRecordLogin();
//        updateUserRecord.updateUserRecordRegister();
    }
}
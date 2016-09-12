package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/31/2016.
 */
public class UpdateUserRecordTest extends InstrumentationTestCase {

    Context mContext;
    UpdateUserRecord updateUserRecord;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        updateUserRecord = new UpdateUserRecord(mContext);
    }

    @Test
    public void testUpdateUserRecord(){
        assertNotNull(updateUserRecord);
//        updateUserRecord.updateUserRecordLogin();
//        updateUserRecord.updateUserRecordRegister();
    }
}
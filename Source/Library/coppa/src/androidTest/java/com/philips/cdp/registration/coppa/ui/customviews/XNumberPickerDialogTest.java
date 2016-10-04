package com.philips.cdp.registration.coppa.ui.customviews;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.listener.NumberPickerListener;

import org.junit.Before;

/**
 * Created by 310230979 on 9/8/2016.
 */
public class XNumberPickerDialogTest extends InstrumentationTestCase{

    Context mContext;
    XNumberPickerDialog mXNumberPickerDialog;
    NumberPickerListener mNumberPickerListener;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        mXNumberPickerDialog= new XNumberPickerDialog(mNumberPickerListener);
        mNumberPickerListener= new NumberPickerListener() {
            @Override
            public void onNumberSelect(final int num) {

            }
        };
    }
    public void testXNumberPickerDialog(){
        assertNotNull(mNumberPickerListener);
    }}
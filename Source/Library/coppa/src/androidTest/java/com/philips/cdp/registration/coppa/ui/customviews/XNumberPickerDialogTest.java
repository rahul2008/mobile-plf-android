package com.philips.cdp.registration.coppa.ui.customviews;

import android.content.Context;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.coppa.listener.NumberPickerListener;

import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class XNumberPickerDialogTest extends RegistrationApiInstrumentationBase {

    Context mContext;
    XNumberPickerDialog mXNumberPickerDialog;
    NumberPickerListener mNumberPickerListener;

    @Before
    public void setUp() throws Exception {
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
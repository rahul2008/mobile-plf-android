package com.philips.cdp.registration.coppa.base;

import android.test.InstrumentationTestCase;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class CoppaConsentUpdateHandlerTest extends InstrumentationTestCase {
    CoppaConsentUpdateHandler mCoppaConsentUpdateHandler;
     CoppaConsentUpdateCallback mCoppaConsentUpdateCallback;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
         mCoppaConsentUpdateCallback = new CoppaConsentUpdateCallback() {
             @Override
             public void onSuccess() {

             }

             @Override
             public void onFailure(int message) {

             }
         };
        mCoppaConsentUpdateHandler = new CoppaConsentUpdateHandler(mCoppaConsentUpdateCallback);

    }

    public void testCoppaConsentUpdateHandler(){

        mCoppaConsentUpdateHandler.onFailure(new CaptureApiError());
        mCoppaConsentUpdateHandler.onSuccess();
        assertNotNull(mCoppaConsentUpdateHandler);
    }
}
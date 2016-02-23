/*
package com.philips.multiproduct;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.multiproduct.prx.Callback;
import com.philips.multiproduct.prx.ProductData;
import com.philips.multiproduct.prx.PrxWrapper;

*/
/**
 * This is the Unittestig class to test the PRX relevant data.
 * <p/>
 * Created by 310190678 on 04-Feb-16.
 *//*

public class PrxWrapperTest extends InstrumentationTestCase {


    PrxWrapper mPrxWrapper = null;
    private String mCtn;
    private String mSectorCode;
    private String mLocale;
    private String mCatalogCode;
    private Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCtn = "RQ1250/17";
        mSectorCode = "B2C";
        mLocale = "en_GB";
        mCatalogCode = "CONSUMER";
        mContext = getInstrumentation().getContext().getApplicationContext();
    }


    public void testPRXWrapperAPITest() {
        mPrxWrapper = new PrxWrapper(mContext, mCtn, mSectorCode, mLocale, mCatalogCode);
        mPrxWrapper.requestPrxSummaryData(new PrxSummaryDataListener() {
            @Override
            public void onSuccess(SummaryModel productData) {

            }

            @Override
            public void onFail(String errorMessage) {

            }
        }, "tag");
    }
}
*/

package com.philips.cdp.productrequest;

import android.content.Context;
import android.test.InstrumentationTestCase;
import com.philips.cdp.prxclient.RequestType;
import org.mockito.Mock;


/**
 * Created by 310230979 on 3/25/2016.
 */
public class ProductMetaRequestTest extends InstrumentationTestCase {

    ProductMetaRequest productMetaRequest;
    Context context;
    @Mock
    String mCtn="HD8967/01";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        productMetaRequest = new ProductMetaRequest(mCtn);
        context = getInstrumentation().getContext();
    }

    public void testGetServerInfo() throws Exception {
        String mServerInfo = productMetaRequest.getServerInfo();
        assertEquals("https://acc.philips.co.uk/prx/registration/", mServerInfo);

    }

        public void testGetRequestType() throws Exception {
        int mIntType = productMetaRequest.getRequestType();
        assertEquals(RequestType.GET.getValue(), mIntType);
    }
}
package com.philips.cdp.backend;

import android.test.InstrumentationTestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegHelperTest extends InstrumentationTestCase {

    ProdRegHelper prodRegHelper;

    public void testUser() throws Exception {
        super.setUp();
        prodRegHelper = new ProdRegHelper();
    }

    /*@Test
    public void testProcessMetadata() {

        ProdRegRequestInfo prodRegRequestInfo = Mockito.mock(ProdRegRequestInfo.class);
        ResponseListener listener = Mockito.mock(ResponseListener.class);
        ProdRegHelper prodRegHelperMock = Mockito.mock(ProdRegHelper.class);
        prodRegHelperMock.processMetadata(getInstrumentation().getContext(), prodRegRequestInfo, listener);
        Mockito.verify(prodRegHelperMock, Mockito.atLeast(1)).processMetadata(getInstrumentation().getContext(), prodRegRequestInfo, listener);
    }*/
}
package com.philips.cdp.backend;

import android.test.InstrumentationTestCase;

import com.philips.cdp.handler.ProdRegListener;
import com.philips.cdp.handler.UserProduct;

import org.junit.Test;
import org.mockito.Mockito;

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

   @Test
    public void testProcessMetadata() {
       ProdRegRequestInfo prodRegRequestInfo = Mockito.mock(ProdRegRequestInfo.class);
       ProdRegListener listener = Mockito.mock(ProdRegListener.class);
       ProdRegHelper prodRegHelperMock = Mockito.mock(ProdRegHelper.class);
       UserProduct userProduct=Mockito.mock(UserProduct.class);
     //Process Metadata
       prodRegHelperMock.processMetadata(getInstrumentation().getContext(), prodRegRequestInfo, listener);
       Mockito.verify(prodRegHelperMock, Mockito.atLeast(1)).processMetadata(getInstrumentation().getContext(), prodRegRequestInfo, listener);


       //getting registered product
       prodRegHelperMock.getRegisteredProduct(getInstrumentation().getContext(), prodRegRequestInfo, listener);
       Mockito.verify(prodRegHelperMock, Mockito.atLeast(1)).getRegisteredProduct(getInstrumentation().getContext(), prodRegRequestInfo, listener);
       Mockito.when(prodRegRequestInfo.getLocale()).thenReturn("en_GB");
       userProduct.getRegisteredProducts(getInstrumentation().getContext(), prodRegRequestInfo, listener);
   }


}
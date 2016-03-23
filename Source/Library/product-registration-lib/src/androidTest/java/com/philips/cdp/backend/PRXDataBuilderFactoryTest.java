package com.philips.cdp.backend;

import com.philips.cdp.MockitoTestCase;
import com.philips.cdp.productrequest.ProductMetaRequest;
import com.philips.cdp.productrequest.RegisteredRequest;
import com.philips.cdp.productrequest.RegistrationRequest;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;

import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PRXDataBuilderFactoryTest extends MockitoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(
                "dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    @Test
    public void testReturnExpectedRequestClass() throws Exception {
        PRXDataBuilderFactory prxDataBuilderFactory = new PRXDataBuilderFactory();
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        PrxRequest prxRequest = prxDataBuilderFactory.createPRXBuilder(PRXRequestType.METADATA, prodRegRequestInfo, "abcd1234");
        assertTrue(prxRequest instanceof ProductMetaRequest);
        PrxRequest prxRequest1 = prxDataBuilderFactory.createPRXBuilder(PRXRequestType.FETCH_PRODUCTS, prodRegRequestInfo, "abcd1234");
        assertTrue(prxRequest1 instanceof RegisteredRequest);
        PrxRequest prxRequest2 = prxDataBuilderFactory.createPRXBuilder(PRXRequestType.REGISTRATION, prodRegRequestInfo, "abcd1234");
        assertTrue(prxRequest2 instanceof RegistrationRequest);
    }
}
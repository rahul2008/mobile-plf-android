package com.philips.cdp.di.iap.integration;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by indrajitkumar on 22/09/16.
 */
public class IAPFlowInputTest {

    private String productCTN;
    private ArrayList<String> productCTNs;

    @Before
    public void setUp() throws Exception {
        productCTN = "HX8033/11";
        productCTNs = new ArrayList<>();
        productCTNs.add("HX8033/11");
        productCTNs.add("HX9012/64");
    }

    @Test
    public void getProductCTN() throws Exception {
        assertSame(productCTN, "HX8033/11");
    }

    @Test
    public void getProductCTNs() throws Exception {
        assertSame(productCTNs.size(), 2);
    }

}
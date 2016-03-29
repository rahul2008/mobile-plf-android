package com.philips.cdp.model;

import com.google.gson.Gson;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.mockito.Mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegMetaDataTest extends TestCase {
    ProdRegMetaData prodRegMetaData;
    @Mock
    ProdRegMetaDataResponse data;

    @Override
    public void setUp() throws Exception {
        prodRegMetaData = new ProdRegMetaData();
    }
    public void testSetData() throws Exception {
        prodRegMetaData.setData(data);
    }
    public void testGetData() throws Exception {
        prodRegMetaData.setData(data);
        assertEquals(data, prodRegMetaData.getData());

    }


}
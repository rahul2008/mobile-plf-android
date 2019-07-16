package com.philips.cdp.di.ecs;

import com.google.gson.Gson;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class ECSServicesTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void init() {
    }

    @Test
    public void hybrisOathAuthentication() {
    }

    @Test
    public void getIAPConfig() {
    }

    @Test
    public void getProductDetail() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("ProductList.json");
        String jsonString= TestUtil.loadJSONFromFile(in);
        ECSProductSummary ecsProductSummary = new Gson().fromJson(jsonString.toString(),
                ECSProductSummary.class);
    }

    @Test
    public void invalidateECS() {
    }

    @Test
    public void getProductAsset() {
    }

    @Test
    public void getProductDisclaimer() {
    }
}
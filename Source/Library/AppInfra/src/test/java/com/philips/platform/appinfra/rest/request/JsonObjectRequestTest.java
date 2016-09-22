package com.philips.platform.appinfra.rest.request;

import com.android.volley.Response;
;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;
@RunWith(RobolectricTestRunner.class)
public class JsonObjectRequestTest {
    @Test
    public void publicMethods() throws Exception {
        // Catch-all test to find API-breaking changes.
        assertNotNull(JsonObjectRequest.class.getConstructor(String.class, JSONObject.class,
                Response.Listener.class, Response.ErrorListener.class));
        assertNotNull(JsonObjectRequest.class.getConstructor(int.class, String.class, JSONObject.class,
                Response.Listener.class, Response.ErrorListener.class));


/*
        assertNotNull(JsonArrayRequest.class.getConstructor(String.class,
                Response.Listener.class, Response.ErrorListener.class));
        assertNotNull(JsonArrayRequest.class.getConstructor(int.class, String.class, JSONArray.class,
                Response.Listener.class, Response.ErrorListener.class));
        assertNotNull(JsonObjectRequest.class.getConstructor(String.class, JSONObject.class,
                Response.Listener.class, Response.ErrorListener.class));
        assertNotNull(JsonObjectRequest.class.getConstructor(int.class, String.class,
                JSONObject.class, Response.Listener.class, Response.ErrorListener.class));*/
    }
}
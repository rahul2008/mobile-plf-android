package com.philips.platform.appinfra.rest.request;

import com.android.volley.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static org.junit.Assert.assertNotNull;
@RunWith(RobolectricTestRunner.class)
public class StringRequestTest {
    @Test
    public void publicMethods() throws Exception {
        // Catch-all test to find API-breaking changes.
        assertNotNull(StringRequest.class.getConstructor(String.class, Response.Listener.class,
                Response.ErrorListener.class));
        assertNotNull(StringRequest.class.getConstructor(int.class, String.class,
                Response.Listener.class, Response.ErrorListener.class));
    }
}
package com.philips.platform.appinfra.rest;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.rest.request.RequestQueue;


/**
 * Created by 310243577 on 11/4/2016.
 */

public class RestManagerTest extends MockitoTestCase {

    private RestInterface mRestInterface = null;
    private Context context;
    private AppInfra mAppInfra;
    private RequestQueue queue;
    String baseURL = "https://hashim.herokuapp.com";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mRestInterface = mAppInfra.getRestClient();
        assertNotNull(mRestInterface);
    }

    private void testgetRequestQueue() {
        queue = mRestInterface.getRequestQueue();
        assertNotNull(queue);
    }

//    private void testStringRequestwithUrl() {
//
//        StringRequest mStringRequest = null;
//        try {
//            mStringRequest = new StringRequest(Request.Method.PUT, urlInput.getText().toString().trim() + "/RCT/test.php?action=data&id=" + idInput.getText().toString().trim(), new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.i("LOG", "" + response);
//                    //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();
//                    showAlertDialog("Success Response", response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.i("LOG", "" + error);
//
//                    //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//
//                    String errorcode = null != error.networkResponse ? error.networkResponse.statusCode + "" : "";
//                }
//            });
//        } catch (HttpForbiddenException e) {
//            Log.i("LOG", "" + e.toString());
//        }
//        if (mStringRequest.getCacheEntry() != null) {
//            String cachedResponse = new String(mStringRequest.getCacheEntry().data);
//            Log.i("CACHED DATA: ", "" + cachedResponse);
//        }
//        // mStringRequest.setShouldCache(false); // set false to disable cache
//
//        if (null != mStringRequest) {
//            mRestInterface.getRequestQueue().add(mStringRequest);
//        }
//    }
//}
//
//});

}

package com.philips.platform.appinfra.rest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.rest.request.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by 310238114 on 9/13/2016.
 */
public class RestClientTest extends MockitoTestCase {

    private Context context;
    private AppInfra mAppInfra;
    private RestInterface mRestInterface;
    String url = "https://www.oldchaphome.nl/RCT/test.php?action=data&id=aa";
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();

        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        mRestInterface = mAppInfra.getRestClient();

        assertNotNull(mRestInterface);

    }

   public void testStringRequest(){
       StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               Log.i("LOG", "StringRequest Response:" + response);
               assertNotNull(response);
               //Toast.makeText(RestClientActivity.this, response, Toast.LENGTH_SHORT).show();

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.i("LOG", "" + error);
               //Toast.makeText(RestClientActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

           }
       });
       // mStringRequest.setShouldCache(false); // set false to disable cache
       mRestInterface.getRequestQueue().add(mStringRequest);
    }

    public void testJsonObjectRequest(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("param1", "value1");

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("LOG", "JsonStringRequest Response:" + response);
                        assertNotNull(response);
                       /* try {
                            Log.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
            }
        });
    }

}

package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.rest.request.StringRequest;


/**
 * Created by 310238655 on 8/24/2016.
 */
public class RestDemo extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = "https://www.oldchaphome.nl/RCT/test.php?action=data&id=as";

// Formulate the request and handle the response.
        Class<String> stringClass = null;

//        AppInfraRequest mAppInfraRequest= new AppInfraRequest(Request.Method.GET,url,RestDemoModel.class, null,
//                new Response.Listener() {
//                    @Override
//                    public void onResponse(Object response) {
//                        // Do something with the response
//                        Log.i("LOG", "" + response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
//                        Log.i("LOG", "" + error);
//                    }
//                });

        StringRequest mStringRequest = null;
        try {
            mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG", "" + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("LOG", "" + error);
                }
            }, null, null,null);
        } catch (Exception e) {
            Log.i("LOG", "" + e.toString());
        }

        AILDemouAppInterface.getInstance().getAppInfra().getRestClient().getRequestQueue().add(mStringRequest);

    }
}

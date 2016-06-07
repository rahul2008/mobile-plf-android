package com.philips.platform.appinfra.servicediscovery;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.philips.cdp.prxclient.network.NetworkWrapper;
import com.philips.cdp.prxclient.network.SSLCertificateManager;
import com.philips.cdp.prxclient.network.VolleyQueue;
import com.philips.platform.appinfra.AppInfraLibraryApplication;
import com.philips.platform.appinfra.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 310238655 on 6/2/2016.
 */
public class RequestManager{

//    RequestQueue mRequestQueue;
    private static final String TAG = NetworkWrapper.class.getSimpleName();
    private Context mContext = null;
    private boolean isHttpsRequest = false;
    private RequestQueue mVolleyRequest;

    public RequestManager(Context context) {
        this.mContext = context;
        Volleyrequester volleyQueue = Volleyrequester.getInstance();
        this.mVolleyRequest = volleyQueue.getRequestQueue(this.mContext);
    }

    public void execute(String url){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String mcountry = null;
//                        try {
//                                mcountry = response.getJSONArray("payload").getString(0);
                        String str = null;
                        try {
                            mcountry = response.getJSONObject("payload").getString("country");
                            JSONObject obj = response.getJSONObject("payload");
                           obj = obj.getJSONObject("matchByLanguage");
                            obj = obj.getJSONObject("results");

                           // str = obj.getJSONArray("configs").toString();
                            JSONArray JSONArrayConfig = obj.getJSONArray("configs");
                            URLModel uRLModel = new URLModel();
                            String[] urls = new String[JSONArrayConfig.length()];

                            for(int count=0;count<JSONArrayConfig.length();count++){
                                urls[count]=JSONArrayConfig.getJSONObject(count).optString("urls",null);
                            }
                            uRLModel.setURLs(urls);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("Responce", " ");
                        InputStream is = null;
//                        try {
//                            is = new ByteArrayInputStream(str.getBytes("UTF-8"));
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                                List list = new ServiceResponseManager().readJsonStream(is);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                            if(mcountry!= null && mcountry.contains("")){
                                SharedPreferences.Editor editor = mContext.getSharedPreferences("PrefNAme", mContext.MODE_PRIVATE).edit();
                                editor.putString("COUNTRY_NAME", mcountry);
                                editor.commit();
                            }

//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        Log.i("Responce", ""+mcountry);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        int  statusCode = error.networkResponse.statusCode;
                        NetworkResponse response = error.networkResponse;

                    }
                });

        if(url.startsWith("https")) {
            this.isHttpsRequest = true;
        } else {
            this.isHttpsRequest = false;
        }

        this.setSSLSocketFactory();
        mVolleyRequest.add(jsObjRequest);
    }
    private void setSSLSocketFactory() {
        if(this.isHttpsRequest) {
            SSLCertificateManager.setSSLSocketFactory();
        }

    }
}

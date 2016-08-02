/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.philips.cdp.prxclient.network.NetworkWrapper;
import com.philips.cdp.prxclient.network.SSLCertificateManager;
import com.philips.platform.appinfra.servicediscovery.model.Config;
import com.philips.platform.appinfra.servicediscovery.model.Error;
import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by 310238655 on 6/2/2016.
 */
public class RequestManager {

    //    RequestQueue mRequestQueue;
    private static final String TAG = NetworkWrapper.class.getSimpleName();
    private Context mContext = null;
    private boolean isHttpsRequest = false;
    private RequestQueue mVolleyRequest;
    static ServiceDiscovery mServiceDiscovery;
    String mcountry = null;
    public static final String COUNTRY_PRREFERENCE = "COUNTRY_PRREFERENCE";
    public static final String COUNTRY_NAME = "COUNTRY_NAME";

    public RequestManager(Context context) {
        this.mContext = context;
        Volleyrequester volleyQueue = Volleyrequester.getInstance();
        this.mVolleyRequest = volleyQueue.getRequestQueue(this.mContext);
    }

    public void execute(final String url, final ServiceDiscoveryInterface.OnRefreshListener listener) {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (null == mServiceDiscovery) {
                                mServiceDiscovery = new ServiceDiscovery();
                                SharedPreferences pref = mContext.getSharedPreferences(RequestManager.COUNTRY_PRREFERENCE, Context.MODE_PRIVATE);
                                mcountry = pref.getString(RequestManager.COUNTRY_NAME, null);
                                if (mcountry == null) {
                                    mcountry = response.getJSONObject("payload").getString("country");
                                    if (mcountry != null) {
                                        SharedPreferences.Editor editor = mContext.getSharedPreferences(COUNTRY_PRREFERENCE, Context.MODE_PRIVATE).edit();
                                        editor.putString(COUNTRY_NAME, mcountry);
                                        editor.commit();
                                        Log.i("Responce", "" + mcountry);
                                    }
                                }
//                                else {
                                ////////////////start of parse///////////
                                mServiceDiscovery.setCountry(response.getJSONObject("payload").optString("country"));

                                // START setting match by country
                                JSONObject payloadJSONObject = response.getJSONObject("payload");
                                mServiceDiscovery.setSuccess(response.optBoolean("success"));
                                if (mServiceDiscovery.isSuccess()) {
                                    mServiceDiscovery.setError(null); // set (if any) previous error to null
                                }
                                JSONObject matchByCountryJSONObject = payloadJSONObject.getJSONObject("matchByCountry");
                                MatchByCountryOrLanguage
                                        matchByCountry = new MatchByCountryOrLanguage();
                                matchByCountry.setAvailable(matchByCountryJSONObject.optBoolean("available"));

                                JSONArray resultsJSONArray = matchByCountryJSONObject.optJSONArray("results");
                                if (null == resultsJSONArray) {
                                    resultsJSONArray = new JSONArray();
                                    resultsJSONArray.put(matchByCountryJSONObject.optJSONObject("results"));
                                }
//                                matchByCountry.setLocale(resultsJSONArray.getJSONObject(0).optString("locale"));
                                ArrayList<Config> matchByCountryConfigs = new ArrayList<Config>();

                                ArrayList<String> localeList = new ArrayList<String>();
                                JSONArray configCountryJSONArray = null;
                                Locale[] multiLocale= Locale.getAvailableLocales();

                                if (resultsJSONArray.length() > 1) {

                                    for(int i=0; i< resultsJSONArray.length(); i++){
                                        localeList.add(resultsJSONArray.getJSONObject(i).optString("locale"));
                                    }

                                }else{
                                    matchByCountry.setLocale(resultsJSONArray.getJSONObject(0).optString("locale"));
                                    configCountryJSONArray = resultsJSONArray.getJSONObject(0).optJSONArray("configs");
                                }

                                for (int i =0 ; i<localeList.size(); i++){
                                    for(int j = 0; j<multiLocale.length; j++){
//                                        if(Locale.getDefault().equals(localeList.get(i))){
//                                            matchByCountry.setLocale(localeList.get(i));
//                                            configCountryJSONArray = resultsJSONArray.getJSONObject(i).optJSONArray("configs");
//                                            break ;
//                                        }else
                                        if(multiLocale[j].equals(localeList.get(i))){
                                            matchByCountry.setLocale(localeList.get(i));
                                            configCountryJSONArray = resultsJSONArray.getJSONObject(i).optJSONArray("configs");
                                            break ;
                                        }
                                    }

                                }


                                for (int configCount = 0; configCount < configCountryJSONArray.length(); configCount++) {
                                    Config config = new Config();

                                    config.setMicrositeId(configCountryJSONArray.optJSONObject(configCount).optString("micrositeId"));
                                    HashMap<String, String> urlHashMap = new HashMap<String, String>();
                                    JSONObject urlJSONObject = configCountryJSONArray.optJSONObject(configCount).optJSONObject("urls");
                                    Iterator iter = urlJSONObject.keys();
                                    while (iter.hasNext()) {
                                        String key = (String) iter.next();
                                        String value = urlJSONObject.getString(key);
                                        urlHashMap.put(key, value);
                                    }
                                    config.setUrls(urlHashMap);

                                    ArrayList<Tag> tagArrayList = new ArrayList<Tag>();
                                    JSONArray tagJSONArray = configCountryJSONArray.optJSONObject(configCount).optJSONArray("tags");
                                    for (int tagCount = 0; tagCount < tagJSONArray.length(); tagCount++) {
                                        Tag tag = new Tag();
                                        tag.setId(tagJSONArray.optJSONObject(tagCount).optString("id"));
                                        tag.setName(tagJSONArray.optJSONObject(tagCount).optString("name"));
                                        tag.setKey(tagJSONArray.optJSONObject(tagCount).optString("key"));
                                        tagArrayList.add(tag);
                                    }
                                    config.setTags(tagArrayList);
                                    matchByCountryConfigs.add(config);
                                }

                                matchByCountry.setConfigs(matchByCountryConfigs);
                                mServiceDiscovery.setMatchByCountry(matchByCountry);
                                // END setting match by country


                                // START setting match by language
                                JSONObject matchByLanguageJSONObject = payloadJSONObject.getJSONObject("matchByLanguage");
                                MatchByCountryOrLanguage matchByLanguage = new MatchByCountryOrLanguage();
                                matchByLanguage.setAvailable(matchByLanguageJSONObject.optBoolean("available"));

                                JSONArray resultsLanguageJSONArray = matchByLanguageJSONObject.optJSONArray("results");
                                if (null == resultsLanguageJSONArray) {
                                    resultsLanguageJSONArray = new JSONArray();
                                    resultsLanguageJSONArray.put(matchByLanguageJSONObject.optJSONObject("results"));
                                }
                                matchByLanguage.setLocale(resultsLanguageJSONArray.getJSONObject(0).optString("locale"));
                                ArrayList<Config> matchByLanguageConfigs = new ArrayList<Config>();
                                JSONArray configLanguageJSONArray = resultsLanguageJSONArray.getJSONObject(0).optJSONArray("configs");
                                for (int configCount = 0; configCount < configLanguageJSONArray.length(); configCount++) {
                                    Config config = new Config();

                                    config.setMicrositeId(configLanguageJSONArray.optJSONObject(configCount).optString("micrositeId"));
                                    HashMap<String, String> urlHashMap = new HashMap<String, String>();
                                    JSONObject urlJSONObject = configLanguageJSONArray.optJSONObject(configCount).optJSONObject("urls");
                                    Iterator iter = urlJSONObject.keys();
                                    while (iter.hasNext()) {
                                        String key = (String) iter.next();
                                        String value = urlJSONObject.getString(key);
                                        urlHashMap.put(key, value);
                                    }
                                    config.setUrls(urlHashMap);

                                    ArrayList<Tag> tagArrayList = new ArrayList<Tag>();
                                    JSONArray tagJSONArray = configLanguageJSONArray.optJSONObject(configCount).optJSONArray("tags");
                                    for (int tagCount = 0; tagCount < tagJSONArray.length(); tagCount++) {
                                        Tag tag = new Tag();
                                        tag.setId(tagJSONArray.optJSONObject(tagCount).optString("id"));
                                        tag.setName(tagJSONArray.optJSONObject(tagCount).optString("name"));
                                        tag.setKey(tagJSONArray.optJSONObject(tagCount).optString("key"));
                                        tagArrayList.add(tag);
                                    }
                                    config.setTags(tagArrayList);
                                    matchByLanguageConfigs.add(config);
                                }

                                matchByLanguage.setConfigs(matchByLanguageConfigs);
                                mServiceDiscovery.setMatchByLanguage(matchByLanguage);

                                listener.onSuccess();
                                // END setting match by language
//                                }
//                                if (!url.contains("country")) {
//                                    String newURL = url + "&country=" + mcountry;
//                                    execute(newURL, listener);
//                                    mServiceDiscovery = null;
//                                }
                            }
                            ////////////////end of parse///////////
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mServiceDiscovery = new ServiceDiscovery();
                        Error volleyError = new Error();
                        ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorValue = null;
                        if (error instanceof TimeoutError) {
                            volleyError.setMessage("TimeoutORNoConnection");
                            errorValue = ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT;
                            Log.i("TimeoutORNoConnection", "" + "TimeoutORNoConnection");
                        } else if (error instanceof NoConnectionError) {
                            volleyError.setMessage("AuthFailureError");
                            Log.i("AuthFailureError", "" + "AuthFailureError");
                            errorValue = ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK;
                        } else if (error instanceof AuthFailureError) {
                            volleyError.setMessage("AuthFailureError");
                            Log.i("AuthFailureError", "" + "AuthFailureError");
                            errorValue = ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR;
                        } else if (error instanceof ServerError) {
                            volleyError.setMessage("ServerError");
                            Log.i("ServerError", "" + "ServerError");
                            errorValue = ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR;
                        } else if (error instanceof NetworkError) {
                            volleyError.setMessage("NetworkError");
                            Log.i("NetworkError", "" + "NetworkError");
                            errorValue = ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.INVALID_RESPONSE;
                        } else if (error instanceof ParseError) {
                            volleyError.setMessage("ParseError");
                            Log.i("ParseError", "" + "ParseError");
                            errorValue = ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.INVALID_RESPONSE;
                        }
                        mServiceDiscovery.setError(volleyError);
                        listener.onError(errorValue, "Error");
                    }
                });

        if (url.startsWith("https")) {
            this.isHttpsRequest = true;
        } else {
            this.isHttpsRequest = false;
        }

        this.setSSLSocketFactory();
        mVolleyRequest.add(jsObjRequest);
    }

    private void setSSLSocketFactory() {
        if (this.isHttpsRequest) {
            SSLCertificateManager.disableAllServerCertificateChecking();
        }

    }
}

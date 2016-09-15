package com.philips.platform.appinfra.servicediscovery;


import android.content.Context;
import android.os.Build;
//import android.os.LocaleList;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestItemManager {
    public interface RequestManagerListener {
        void onResult(ServiceDiscovery result);
    }

    //    RequestQueue mRequestQueue;
    private static final String TAG = "RequestManager";//this.class.getSimpleName();

    private Context mContext = null;
    private RequestQueue mVolleyRequest;

    private Request<JsonObjectRequest> jsonObjRequest = null;

    private AppInfra mAppInfra;

    public RequestItemManager(Context context, AppInfra appInfra) {
        this.mContext = context;
        mAppInfra = appInfra;
        Volleyrequester volleyQueue = Volleyrequester.getInstance();
        this.mVolleyRequest = volleyQueue.getRequestQueue(this.mContext);
    }

    public ServiceDiscovery execute(final String url) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        mVolleyRequest.add(request);
        ServiceDiscovery result = new ServiceDiscovery();

        try {
            JSONObject response = future.get(10, TimeUnit.SECONDS); // Blocks for at most 10 seconds.
            return parseResponse(response);
        } catch (InterruptedException | TimeoutException e) {
            ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "Timed out or interrupted");
            result.setError(err);
            result.setSuccess(false);
        } catch (ExecutionException e) {
            Throwable error = e.getCause();
            ServiceDiscovery.Error volleyError = null;
            if (error instanceof TimeoutError) {
                Log.i("TimeoutORNoConnection", "" + "TimeoutORNoConnection");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "TimeoutORNoConnection");
            } else if (error instanceof NoConnectionError) {
                Log.i("NoConnectionError", "" + "NoConnectionError");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK, "NoConnectionError");
            } else if (error instanceof AuthFailureError) {
                Log.i("AuthFailureError", "" + "AuthFailureError");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "AuthFailureError");
            } else if (error instanceof ServerError) {
                Log.i("ServerError", "" + "ServerError");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ServerError");
            } else if (error instanceof NetworkError) {
                Log.i("NetworkError", "" + "NetworkError");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "NetworkError");
            } else if (error instanceof ParseError) {
                Log.i("ParseError", "" + "ParseError");
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "ServerError");
            } else {
            } // TODO RayKlo
            result.setError(volleyError);
        }
        return result;
    }

    private ServiceDiscovery parseResponse(JSONObject response) {
        ServiceDiscovery result = new ServiceDiscovery();
        try {
            result.setSuccess(response.optBoolean("success"));
            if (!result.isSuccess()) {
                ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Server reports failure");
                result.setError(err);
            } else { // no sense in further processing if server reports error
                // START setting match by country
                JSONObject payloadJSONObject = response.getJSONObject("payload");
                String country = response.getJSONObject("payload").optString("country");
                Log.i("Response", "" + country);
                result.setCountry(country.toUpperCase());

                JSONObject matchByCountryJSONObject = payloadJSONObject.getJSONObject("matchByCountry");
                MatchByCountryOrLanguage
                        matchByCountry = new MatchByCountryOrLanguage();
                matchByCountry.setAvailable(matchByCountryJSONObject.optBoolean("available"));

                JSONArray resultsJSONArray = matchByCountryJSONObject.optJSONArray("results");
                if (null == resultsJSONArray) {
                    resultsJSONArray = new JSONArray();
                    resultsJSONArray.put(matchByCountryJSONObject.optJSONObject("results"));
                }
                ArrayList<MatchByCountryOrLanguage.Config> matchByCountryConfigs = new ArrayList<MatchByCountryOrLanguage.Config>();

                ArrayList<String> localeList = new ArrayList<String>();
                JSONArray configCountryJSONArray = null;
//                LocaleList mLocaleList = mAppInfra.getInternationalization().getLocaleList();

                if (resultsJSONArray.length() > 1) {

                    for (int i = 0; i < resultsJSONArray.length(); i++) {
                        localeList.add(resultsJSONArray.getJSONObject(i).optString("locale"));
                    }

                } else {
                    matchByCountry.setLocale(resultsJSONArray.getJSONObject(0).optString("locale"));
                    configCountryJSONArray = resultsJSONArray.getJSONObject(0).optJSONArray("configs");
                }

//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    for (int i = 0; i < localeList.size(); i++) {
//                        for (int j = 0; j < mLocaleList.size(); j++) {
//                            if (mLocaleList.get(j).equals(localeList.get(i))) {
//                                matchByCountry.setLocale(localeList.get(i));
//                                configCountryJSONArray = resultsJSONArray.getJSONObject(i).optJSONArray("configs");
//                                break;
//                            }
//                        }
//
//                    }
//                }
                if(localeList.size() > 0){
                    Locale mLocale = mAppInfra.getInternationalization().getUILocale();
                    for (int i = 0; i < localeList.size(); i++) {
                            if (mLocale.getDefault().toString().equals(localeList.get(i))) {
                                matchByCountry.setLocale(localeList.get(i));
                                configCountryJSONArray = resultsJSONArray.getJSONObject(i).optJSONArray("configs");
                                break;
                            }else{
                                matchByCountry.setLocale(resultsJSONArray.getJSONObject(0).optString("locale"));
                                configCountryJSONArray = resultsJSONArray.getJSONObject(0).optJSONArray("configs");
                            }

                    }
                }
                // Multi, single Locale verification with locale response object
//                for (int i = 0; i < localeList.size(); i++) {
//                    for (int j = 0; j < multiLocale.length; j++) {
//                        if (multiLocale[j].equals(localeList.get(i))) {
//                            matchByCountry.setLocale(localeList.get(i));
//                            configCountryJSONArray = resultsJSONArray.getJSONObject(i).optJSONArray("configs");
//                            break;
//                        }
//                    }
//
//                }


                for (int configCount = 0; configCount < configCountryJSONArray.length(); configCount++) {
                    MatchByCountryOrLanguage.Config config = new MatchByCountryOrLanguage.Config();

                    config.setMicrositeId(configCountryJSONArray.optJSONObject(configCount).optString("micrositeId"));
                    HashMap<String, String> urlHashMap = new HashMap<String, String>();
                    JSONObject urlJSONObject = configCountryJSONArray.optJSONObject(configCount).optJSONObject("urls");
                    Iterator<String> iter = urlJSONObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        String value = urlJSONObject.getString(key);
                        urlHashMap.put(key, value);
                    }
                    config.setUrls(urlHashMap);

                    ArrayList<MatchByCountryOrLanguage.Config.Tag> tagArrayList = new ArrayList<MatchByCountryOrLanguage.Config.Tag>();
                    JSONArray tagJSONArray = configCountryJSONArray.optJSONObject(configCount).optJSONArray("tags");
                    for (int tagCount = 0; tagCount < tagJSONArray.length(); tagCount++) {
                        MatchByCountryOrLanguage.Config.Tag tag = new MatchByCountryOrLanguage.Config.Tag();
                        tag.setId(tagJSONArray.optJSONObject(tagCount).optString("id"));
                        tag.setName(tagJSONArray.optJSONObject(tagCount).optString("name"));
                        tag.setKey(tagJSONArray.optJSONObject(tagCount).optString("key"));
                        tagArrayList.add(tag);
                    }
                    config.setTags(tagArrayList);
                    matchByCountryConfigs.add(config);
                }

                matchByCountry.setConfigs(matchByCountryConfigs);
                result.setMatchByCountry(matchByCountry);
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
                ArrayList<MatchByCountryOrLanguage.Config> matchByLanguageConfigs = new ArrayList<MatchByCountryOrLanguage.Config>();
                JSONArray configLanguageJSONArray = resultsLanguageJSONArray.getJSONObject(0).optJSONArray("configs");
                for (int configCount = 0; configCount < configLanguageJSONArray.length(); configCount++) {
                    MatchByCountryOrLanguage.Config config = new MatchByCountryOrLanguage.Config();

                    config.setMicrositeId(configLanguageJSONArray.optJSONObject(configCount).optString("micrositeId"));
                    HashMap<String, String> urlHashMap = new HashMap<String, String>();
                    JSONObject urlJSONObject = configLanguageJSONArray.optJSONObject(configCount).optJSONObject("urls");
                    Iterator<String> iter = urlJSONObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        String value = urlJSONObject.getString(key);
                        urlHashMap.put(key, value);
                    }
                    config.setUrls(urlHashMap);

                    ArrayList<MatchByCountryOrLanguage.Config.Tag> tagArrayList = new ArrayList<MatchByCountryOrLanguage.Config.Tag>();
                    JSONArray tagJSONArray = configLanguageJSONArray.optJSONObject(configCount).optJSONArray("tags");
                    for (int tagCount = 0; tagCount < tagJSONArray.length(); tagCount++) {
                        MatchByCountryOrLanguage.Config.Tag tag = new MatchByCountryOrLanguage.Config.Tag();
                        tag.setId(tagJSONArray.optJSONObject(tagCount).optString("id"));
                        tag.setName(tagJSONArray.optJSONObject(tagCount).optString("name"));
                        tag.setKey(tagJSONArray.optJSONObject(tagCount).optString("key"));
                        tagArrayList.add(tag);
                    }
                    config.setTags(tagArrayList);
                    matchByLanguageConfigs.add(config);
                }

                matchByLanguage.setConfigs(matchByLanguageConfigs);
                result.setMatchByLanguage(matchByLanguage);
                //ServiceDiscoveryManager.isDownloadInProgress = false; // TODO RayKlo don't cross scope
                // END setting match by language
//                                }
//                                if (!url.contains("country")) {
//                                    String newURL = url + "&country=" + mcountry;
//                                    execute(newURL, listener);
//                                    mServiceDiscovery = null;
//                                }
                ////////////////end of parse///////////
            }
        } catch (JSONException e) {
            ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Parsing error");
            result.setSuccess(false);
            result.setError(err);
            e.printStackTrace();
        }
        return result;
    }
}

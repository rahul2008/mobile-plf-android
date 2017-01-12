package com.philips.platform.appinfra.servicediscovery;

/**
 * Created by 310243577 on 1/10/2017.
 */

import android.content.Context;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.RequestFuture;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.model.MatchByCountryOrLanguage;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//import android.os.LocaleList;

public class RequestManager {

    //    RequestQueue mRequestQueue;
    private static final String TAG = "RequestManager";//this.class.getSimpleName();

    private Context mContext = null;


    private AppInfra mAppInfra;

    public RequestManager(Context context, AppInfra appInfra) {
        this.mContext = context;
        mAppInfra = appInfra;
    }

    public ServiceDiscovery execute(final String url) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future, null, null, null);
        request.setShouldCache(true);
        mAppInfra.getRestClient().getRequestQueue().add(request);

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
            ServiceDiscovery.Error volleyError;
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
                volleyError = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.UNKNOWN_ERROR, e.getMessage());
            }
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
                JSONArray configCountryJSONArray;
                String resConfig = getActualResultsForLocaleList(matchByCountry, resultsJSONArray);
                if (resConfig != null) {
                    configCountryJSONArray = new JSONArray(resConfig);
                } else {
                    matchByCountry.setLocale(resultsJSONArray.getJSONObject(0).optString("locale")); // return first locale if nothing matches
                    configCountryJSONArray = resultsJSONArray.getJSONObject(0).optJSONArray("configs");
                }

                if (configCountryJSONArray != null) {
                    for (int configCount = 0; configCount < configCountryJSONArray.length(); configCount++) {
                        MatchByCountryOrLanguage.Config config = new MatchByCountryOrLanguage.Config();
                        config.setMicrositeId(configCountryJSONArray.optJSONObject(configCount).optString("micrositeId"));
                        HashMap<String, String> urlHashMap = new HashMap<>();
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
                } else if (resultsLanguageJSONArray.length() > 0) {
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
                }
                result.setMatchByLanguage(matchByLanguage);

            }
        } catch (JSONException e) {
            ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Parsing error");
            result.setSuccess(false);
            result.setError(err);
            e.printStackTrace();
        }
        return result;
    }

    private String getActualResultsForLocaleList(MatchByCountryOrLanguage matchByCountry, JSONArray resultsJSONArray) {
        try {
            ArrayList<String> deviceLocaleList = new ArrayList<>(Arrays.asList(getLocaleList().split(",")));
            for (int i = 0; i < deviceLocaleList.size(); i++) {
                for (int j = 0; j < resultsJSONArray.length(); j++) {
                    String resLocale = resultsJSONArray.getJSONObject(j).optString("locale");
                    System.out.println("COMPARE" + " " + resLocale + " with" + deviceLocaleList.get(i).replaceAll("[\\[\\]]", ""));
                    if (deviceLocaleList.get(i).replaceAll("[\\[\\]]", "").equals(resLocale)) {
                        System.out.println("IF STATEMENT" + " " + resLocale + " with" + deviceLocaleList.get(i).replaceAll("[\\[\\]]", ""));

                        matchByCountry.setLocale(resLocale);
                        return resultsJSONArray.getJSONObject(j).optString("configs");
                    } else if (deviceLocaleList.get(0).replaceAll("[\\[\\]]", "").substring(0, 2).equals(resLocale.substring(0, 2))) {
                        System.out.println("ELSE STATEMENT" + " " + resLocale + " with" + deviceLocaleList.get(i).replaceAll("[\\[\\]]", ""));

                        matchByCountry.setLocale(resLocale);
                        return resultsJSONArray.getJSONObject(0).optString("configs");
                    }
                }
            }
        } catch (JSONException e) {
            ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR, "Parsing error");
            ServiceDiscovery result = new ServiceDiscovery();
            result.setSuccess(false);
            result.setError(err);
            e.printStackTrace();
        }
        return null;
    }

    private String getLocaleList() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().toString();
        } else {
            return mAppInfra.getInternationalization().getUILocaleString();
        }
    }
}

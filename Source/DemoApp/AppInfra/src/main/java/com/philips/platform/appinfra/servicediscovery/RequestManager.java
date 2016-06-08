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
import com.philips.platform.appinfra.servicediscovery.model.Config;
import com.philips.platform.appinfra.servicediscovery.model.MatchByCountry;
import com.philips.platform.appinfra.servicediscovery.model.MatchByLanguage;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    static ServiceDiscovery mServiceDiscovery;

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
                           /* JSONObject obj = response.getJSONObject("payload");
                            obj = obj.getJSONObject("matchByLanguage");
                            obj = obj.getJSONObject("results");

                            // str = obj.getJSONArray("configs").toString();
                            JSONArray JSONArrayConfig = obj.getJSONArray("configs");
                            URLModel uRLModel = new URLModel();
                            String[] urls = new String[JSONArrayConfig.length()];

                            for (int count = 0; count < JSONArrayConfig.length(); count++) {
                                urls[count] = JSONArrayConfig.getJSONObject(count).optString("urls", null);
                            }
                            uRLModel.setURLs(urls);*/
                            ////////////////start of parse///////////
                            if (null == mServiceDiscovery) {
                                mServiceDiscovery = new ServiceDiscovery();
                                mServiceDiscovery.setCountry(response.getJSONObject("payload").optString("country"));

                                // START setting match by country
                                JSONObject payloadJSONObject = response.getJSONObject("payload");
                                JSONObject matchByCountryJSONObject = payloadJSONObject.getJSONObject("matchByCountry");
                                MatchByCountry
                                        matchByCountry = new MatchByCountry();
                                matchByCountry.setAvailable(matchByCountryJSONObject.optBoolean("available"));

                                JSONArray resultsJSONArray = matchByCountryJSONObject.optJSONArray("results");
                                if(null==resultsJSONArray){
                                    resultsJSONArray = new JSONArray();
                                    resultsJSONArray.put(matchByCountryJSONObject.optJSONObject("results"));
                                }
                                matchByCountry.setLocale(resultsJSONArray.getJSONObject(0).optString("locale"));
                                ArrayList<Config> matchByCountryConfigs = new ArrayList<Config>();
                                JSONArray configCountryJSONArray = resultsJSONArray.getJSONObject(0).optJSONArray("configs");
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
                                MatchByLanguage matchByLanguage = new MatchByLanguage();
                                matchByLanguage.setAvailable(matchByLanguageJSONObject.optBoolean("available"));

                                JSONArray resultsLanguageJSONArray = matchByLanguageJSONObject.optJSONArray("results");
                                if(null==resultsLanguageJSONArray){
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
                                // END setting match by language
                            }

                            ////////////////end of parse///////////
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

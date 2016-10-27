/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.appinfra.contentloader;

import android.util.Log;

import com.android.volley.Request;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.RestManager;
import com.philips.platform.appinfra.rest.request.HttpForbiddenException;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Created by 310209604 on 2016-08-10.
 */
public class ContentLoader<Content extends ContentInterface> implements ContentLoaderInterface<Content> {
    // region public methods
    /**
     * Create a content loader of type Content, loading content from the given service using the given Content class type.
     * @param serviceId Id of the service discovery service to download the content from
     * @param maxAgeInHours maximum age of the content, a refresh is recommended if cached content is older
     * @param contentClassType type of the content class (use Content.class)
     * @param contentType name of the content as given in the server JSON structure
     */
    public ContentLoader(String serviceId, int maxAgeInHours, Class<Content> contentClassType, String contentType, AppInfraInterface appInfra) {
        mServiceId = serviceId;
        mMaxAgeInHours = maxAgeInHours;
        mClassType = contentClassType;
        mState = STATE.NOT_INITIALIZED;
        mAppInfra=appInfra;
        mRestInterface = mAppInfra.getRestClient();
        mParams= new HashMap<String,String>();
        mHeaders= new HashMap<String,String>();
    }
    // endregion

    // region ContentLoaderInterface implementation
    @Override
    public void refresh(final OnRefreshListener refreshListener) {

        try {


            mRestInterface.jsonObjectRequestWithServiceID(Request.Method.GET, mServiceId, RestManager.LANGUAGE, getOffsetPath(0), new RestInterface.ServiceIDCallback() {
                        @Override
                        public void onSuccess(Object response) {
                            JSONObject serviceResponse=(JSONObject)response;
                            Log.i("CL REFRSH RESP", "" + serviceResponse);
                            clearParamsAndHeaders();// clear headerd and params from rest client
                            refreshListener.onSuccess(OnRefreshListener.REFRESH_RESULT.REFRESHED_FROM_SERVER);
                        }

                        @Override
                        public void onErrorResponse(String error) {
                            Log.i("CL Error:", "" + error);
                            clearParamsAndHeaders();// clear headerd and params from rest client
                            refreshListener.onError(ERROR.SERVER_ERROR,error.toString());
                        }
                    },
                    mHeaders,mParams);
        } catch (HttpForbiddenException e) {
            Log.e("LOG REST SD", e.toString() );
            e.printStackTrace();
        }

    }

    @Override
    public void clearCache() {
    }

    @Override
    public STATE getStatus() {
        return mState;
    }

    @Override
    public void getAllContent(OnResultListener<String> listener) {
    }

    @Override
    public void getContentById(String id, OnResultListener<Content> listener) {
        // example for how to create a Content instance
        List<Content> result = new ArrayList<Content>(1);
        try {
            Content a = mClassType.newInstance();
            if (a.parseInput("{\"id\":\"blaat\"}") == false) {
                listener.onError(ERROR.SERVER_ERROR, "invalid data format on server");
                return;
            }
            result.add(a);
            listener.onSuccess(result);
        }
        catch (InstantiationException | IllegalAccessException e) {
            listener.onError(ERROR.CONFIGURATION_ERROR, "invalid generic class type provided");
        }
    }

    @Override
    public void getContentById(String[] ids, OnResultListener<Content> listener) {
    }

    @Override
    public void getContentByTag(String tagID, OnResultListener<Content> listener) {
    }

    @Override
    public void getContentByTag(String[] tagIDs, OPERATOR andOr, OnResultListener<Content> listener) {
    }
    // endregion

    // region Private methods
    // endregion

    // region Private members
    private final String mServiceId;
    private URL mServiceURL;
    private Class<Content> mClassType;
    private int mMaxAgeInHours;
    private STATE mState;
    private AppInfraInterface mAppInfra;
    private RestInterface mRestInterface;
    HashMap<String,String> mParams;
    HashMap<String,String> mHeaders;
    // endregion

    private void clearParamsAndHeaders(){
        mHeaders.clear();
        mParams.clear();
    }

    String getOffsetPath(int offset){
        //https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1
        //https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1.offset.(100).limit.(100).json

        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        Integer contentLoaderLimit = (Integer) mAppInfra.getConfigInterface().getPropertyForKey("contentLoader.limitSize", "appinfra",  configError);
        String path=".offset.("+offset+").limit.("+contentLoaderLimit+").json";
        return path;
    }
}

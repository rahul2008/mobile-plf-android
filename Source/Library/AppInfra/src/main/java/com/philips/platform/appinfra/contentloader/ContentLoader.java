/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.appinfra.contentloader;

import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.RestManager;
import com.philips.platform.appinfra.rest.request.HttpForbiddenException;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Created by 310209604 on 2016-08-10.
 */
public class ContentLoader<Content extends ContentInterface> implements ContentLoaderInterface<Content> {

    private int offset = 0;
    private final int downloadLimit;
    private int contentDownloadedCount;

    // region public methods

    /**
     * Create a content loader of type Content, loading content from the given service using the given Content class type.
     *
     * @param serviceId        Id of the service discovery service to download the content from
     * @param maxAgeInHours    maximum age of the content, a refresh is recommended if cached content is older
     * @param contentClassType type of the content class (use Content.class)
     * @param contentType      name of the content as given in the server JSON structure
     */
    public ContentLoader(String serviceId, int maxAgeInHours, Class<Content> contentClassType, String contentType, AppInfraInterface appInfra) {
        mServiceId = serviceId;
        mMaxAgeInHours = maxAgeInHours;
        mClassType = contentClassType;
        mContentType = contentType;
        mState = STATE.NOT_INITIALIZED;
        mAppInfra = appInfra;
        mRestInterface = mAppInfra.getRestClient();
        mParams = new HashMap<String, String>();
        mHeaders = new HashMap<String, String>();
        downloadInProgress = new AtomicBoolean(false);
        downloadLimit = getDownloadLimitFromConfig();
    }
    // endregion

    // region ContentLoaderInterface implementation
    @Override
    public void refresh(final OnRefreshListener refreshListener) {


        if (downloadInProgress.compareAndSet(false, true)) {
            downloadContent(refreshListener);
        } else {
            Log.i("CL REFRSH ERR", "" + "download already in progress");
            refreshListener.onError(ERROR.DOWNLOAD_IN_PROGRESS, "download already in progress");

        }

    }

    private void downloadContent(final OnRefreshListener refreshListener) {
        try {
            // mAppInfra.
            final Gson gson = new Gson();
            final JsonParser parser = new JsonParser();
            JsonObject jsonObjectTree;

            mRestInterface.jsonObjectRequestWithServiceID(Request.Method.GET, mServiceId, RestManager.LANGUAGE, getOffsetPath(offset), new RestInterface.ServiceIDCallback() {
                        @Override
                        public void onSuccess(Object response) {
                            JsonObject jsonObjectTree = null;
                            Log.i("CL REFRSH RESP", "download completed for Offset: " + offset + " and Limit: " + downloadLimit);
                            JSONObject serviceResponseJSON = (JSONObject) response; // cast object to org.json.JSONObject
                            JsonElement serviceResponseJson = gson.fromJson(serviceResponseJSON.toString(), JsonElement.class); // cast org.json.JSONObject to gson.JsonElement
                            Log.i("CL REFRSH RESP", "" + serviceResponseJson);
                            if (mClassType.equals(ContentArticle.class)) { // if conent is ContentArticle
                                if (serviceResponseJson.isJsonObject()) {
                                    jsonObjectTree = serviceResponseJson.getAsJsonObject();
                                    jsonObjectTree = jsonObjectTree.getAsJsonObject("result");
                                }
                                JsonElement content = jsonObjectTree.get(mContentType);
                                JsonArray contentList = null;
                                if (null != content) {
                                    if (content.isJsonArray()) {
                                        contentList = content.getAsJsonArray();
                                    }
                                    contentDownloadedCount = contentList.size();
                                    if (null != contentList && contentList.size() > 0) {
                                        for (int contentCount = 0; contentCount < contentList.size(); contentCount++) {
                                            Log.i("CL Ariticle", "" + contentList.get(contentCount));
                                            ContentArticle contentItem = gson.fromJson(contentList.get(contentCount), ContentArticle.class);
                                            String articleId = contentItem.getId();
                                            Log.i("CL Ariticle", "" + articleId + "  TAGs ");
                                            /* TBD push this item in DB,( id-primary key, tags, modDate, content json)*/
                                        }
                                    }
                                }
                            }
                            if (contentDownloadedCount < downloadLimit) { // download is over
                                Log.i("CL REFRSH RESP", "download completed");
                                clearParamsAndHeaders();// clear headerd and params from rest client
                                downloadInProgress.set(false);
                                offset = 0;
                                contentDownloadedCount = 0;
                                refreshListener.onSuccess(OnRefreshListener.REFRESH_RESULT.REFRESHED_FROM_SERVER);
                            } else {// download next
                                contentDownloadedCount = 0;
                                offset += downloadLimit;// next offset
                                downloadContent(refreshListener); // recursive call for next download
                            }
                        }

                        @Override
                        public void onErrorResponse(String error) {
                            Log.i("CL REFRSH Error:", "" + error);
                            clearParamsAndHeaders();// clear headerd and params from rest client
                            refreshListener.onError(ERROR.SERVER_ERROR, error);
                            downloadInProgress.set(false);
                            contentDownloadedCount = 0;
                            offset = 0;
                        }
                    },
                    mHeaders, mParams);
        } catch (HttpForbiddenException e) {
            Log.e("LOG REST SD", e.toString());
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
            // if (a.parseInput("{\"id\":\"blaat\"}") == false) {
            if (!a.parseInput("{\"id\":\"blaat\"}")) {
                listener.onError(ERROR.SERVER_ERROR, "invalid data format on server");
                return;
            }
            result.add(a);
            listener.onSuccess(result);
        } catch (InstantiationException | IllegalAccessException e) {
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
    private AtomicBoolean downloadInProgress;
    private final String mServiceId;
    private URL mServiceURL;
    private Class<Content> mClassType;
    private String mContentType;
    private int mMaxAgeInHours;
    private STATE mState;
    private AppInfraInterface mAppInfra;
    private RestInterface mRestInterface;
    private HashMap<String, String> mParams;
    private HashMap<String, String> mHeaders;
    // endregion

    private void clearParamsAndHeaders() {
        mHeaders.clear();
        mParams.clear();
    }

    String getOffsetPath(int offset) {
        //https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1
        //https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1.offset.(100).limit.(100).json
        String path = ".offset.(" + offset + ").limit.(" + downloadLimit + ").json";
        return path;
    }

    int getDownloadLimitFromConfig() {
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        Integer contentLoaderLimit = (Integer) mAppInfra.getConfigInterface().getPropertyForKey("contentLoader.limitSize", "appinfra", configError);
        return contentLoaderLimit;
    }
}

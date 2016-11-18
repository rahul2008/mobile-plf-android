/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.appinfra.contentloader;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.appinfra.contentloader.model.ContentItem;
import com.philips.platform.appinfra.contentloader.model.Tag;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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
    private List downloadedContents;
    private ContentDatabaseHandler mContentDatabaseHandler;
    // region public methods

    /**
     * Create a content loader of type Content, loading content from the given service using the given Content class type.
     *
     * @param serviceId        Id of the service discovery service to download the content from
     * @param maxAgeInHours    maximum age of the content, a refresh is recommended if cached content is older
     * @param contentClassType type of the content class (use Content.class)
     * @param contentType      name of the content as given in the server JSON structure
     */
    public ContentLoader(Context context, String serviceId, int maxAgeInHours, Class<Content> contentClassType, String contentType, AppInfraInterface appInfra) {
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
        downloadedContents = new ArrayList<ContentItem>();
        mContentDatabaseHandler = new ContentDatabaseHandler(context);
    }
    // endregion

    // region ContentLoaderInterface implementation
    @Override
    public void refresh(final OnRefreshListener refreshListener) {

        long contentLoaderExpiryTime = mContentDatabaseHandler.getContentLoaderServiceStateExpiry(mServiceId);
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTime().getTime();
        if (contentLoaderExpiryTime < currentTime)// if content loader is expired then refresh
        {
            downloadedContents.clear();
            if (downloadInProgress.compareAndSet(false, true)) {
                downloadContent(refreshListener);
            } else {
                Log.i("CL REFRSH ERR", "" + "download already in progress");
                refreshListener.onError(ERROR.DOWNLOAD_IN_PROGRESS, "download already in progress");

            }
        } else {
            //content loader already updated
            refreshListener.onSuccess(OnRefreshListener.REFRESH_RESULT.NO_REFRESH_REQUIRED);
            Log.i("CL REFRSH NA", "" + "content loader already uptodate");
        }
    }

    private void downloadContent(final OnRefreshListener refreshListener) {
        final Gson gson = new Gson();
        JsonObjectRequest jsonRequest = null;

        // to used when serviceId is ready
        try {
            jsonRequest = new JsonObjectRequest(Request.Method.GET,
                    mServiceId, ServiceIDUrlFormatting.SERVICEPREFERENCE.BYLANGUAGE, getOffsetPath(offset), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
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
                                    ContentArticle contentArticle = gson.fromJson(contentList.get(contentCount), ContentArticle.class);
                                    ContentItem contentItem = new ContentItem();
                                    contentItem.setId(contentArticle.getId());
                                    contentItem.setServiceId(mServiceId);
                                    contentItem.setRawData(contentList.get(contentCount).toString());
                                    contentItem.setVersionNumber(contentArticle.getVersion());
                                    List<Tag> tagList = contentArticle.getTags();
                                    String tags = "";
                                    if (null != tagList && tagList.size() > 0) {
                                        for (Tag tag : tagList) {
                                            tags += tag.getId() + ",";
                                        }
                                        tags = tags.substring(0, tags.length() - 1);// remove last comma
                                    }
                                    contentItem.setTags(tags);
                                    downloadedContents.add(contentItem);
                                    String articleId = contentItem.getId();
                                    Log.i("CL Ariticle", "" + articleId + "  TAGs ");
                                            /* TBD push this item in DB,( id-primary key, tags, modDate, content json)*/
                                }
                            }
                        }
                    }
                    if (contentDownloadedCount < downloadLimit) { // download is over
                        Log.i("CL REFRSH RESP", "download completed");
                        mContentDatabaseHandler.addContents(downloadedContents, mServiceId, expiryTimeforUserInputTime(mMaxAgeInHours));
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

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("CL REFRSH Error:", "" + error);
                    clearParamsAndHeaders();// clear headerd and params from rest client
                    refreshListener.onError(ERROR.SERVER_ERROR, error.toString());
                    downloadInProgress.set(false);
                    contentDownloadedCount = 0;
                    downloadedContents.clear();
                    offset = 0;
                }
            });
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } catch (Exception e) {
            mAppInfra.getLogging().log(LoggingInterface.LogLevel.ERROR, "ContentLoader", e.toString());
        }
        if (null != jsonRequest) {
            mRestInterface.getRequestQueue().add(jsonRequest);
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
        // It was concluded to fetch only content ids and not complete content
        List<String> IDs = mContentDatabaseHandler.getAllContentIds(mServiceId);
        listener.onError(ERROR.DATABASE_ERROR, "could not fetch from DB");
        listener.onSuccess(IDs);
    }

    @Override
    public void getContentById(String id, OnResultListener<Content> listener) {
        // example for how to create a Content instance
        Gson gson = new Gson();
        String[] IDs = new String[1];
        IDs[0] = id;
        List<ContentItem> contentItems = mContentDatabaseHandler.getContentById(mServiceId, IDs);
        if (null != contentItems && contentItems.size() > 0) {
            ContentItem contentItem = contentItems.get(0);
            List<Content> result = new ArrayList<Content>(1);
            try {
                Content a = mClassType.newInstance();
                if (!a.parseInput("{\"id\":\"blaat\"}")) {
                    listener.onError(ERROR.SERVER_ERROR, "invalid data format on server");
                    return;
                }
                //  if (mClassType.equals(ContentArticle.class)) { // if conent is ContentArticle
                a = gson.fromJson(contentItem.getRawData(), mClassType);
                result.add(a);
                listener.onSuccess(result);
            } catch (InstantiationException | IllegalAccessException e) {
                listener.onError(ERROR.CONFIGURATION_ERROR, "invalid generic class type provided");
            }
        } else {
            listener.onError(ERROR.NO_DATA_FOUND_IN_DB, "Given ID not found in DB");
        }
    }

    @Override
    public void getContentById(String[] ids, OnResultListener<Content> listener) {
        // example for how to create a Content instance
        Gson gson = new Gson();
        List<ContentItem> contentItems = mContentDatabaseHandler.getContentById(mServiceId, ids);
        if (null != contentItems && contentItems.size() > 0) {
            ContentItem contentItem = contentItems.get(0);
            List<Content> result = new ArrayList<Content>(1);
            try {
                Content a = mClassType.newInstance();
                if (!a.parseInput("{\"id\":\"blaat\"}")) {
                    listener.onError(ERROR.SERVER_ERROR, "invalid data format on server");
                    return;
                }
                for (ContentItem ci : contentItems) {
                    Content c = mClassType.newInstance();
                    c = gson.fromJson(contentItem.getRawData(), mClassType);
                    result.add(c);
                }

                listener.onSuccess(result);
            } catch (InstantiationException | IllegalAccessException e) {
                listener.onError(ERROR.CONFIGURATION_ERROR, "invalid generic class type provided");
            }
        } else {
            listener.onError(ERROR.NO_DATA_FOUND_IN_DB, "Given ID not found in DB");
        }
    }

    @Override
    public void getContentByTag(String tagID, OnResultListener<Content> listener) {
        String[] tag = new String[1];
        tag[0] = tagID;
        Gson gson = new Gson();
        List<ContentItem> contentItems = mContentDatabaseHandler.getContentByTagId(mServiceId, tag, null);
        if (null != contentItems && contentItems.size() > 0) {
            ContentItem contentItem = contentItems.get(0);
            List<Content> result = new ArrayList<Content>(1);
            try {
                Content a = mClassType.newInstance();
                if (!a.parseInput("{\"id\":\"blaat\"}")) {
                    listener.onError(ERROR.SERVER_ERROR, "invalid data format on server");
                    return;
                }
                for (ContentItem ci : contentItems) {
                    Content c = mClassType.newInstance();
                    c = gson.fromJson(contentItem.getRawData(), mClassType);
                    result.add(c);
                }
                listener.onSuccess(result);
            } catch (InstantiationException | IllegalAccessException e) {
                listener.onError(ERROR.CONFIGURATION_ERROR, "invalid generic class type provided");
            }
        } else {
            listener.onError(ERROR.NO_DATA_FOUND_IN_DB, "Given IDs not found in DB");
        }
    }

    @Override
    public void getContentByTag(String[] tagIDs, OPERATOR andOr, OnResultListener<Content> listener) {
        Gson gson = new Gson();
        List<ContentItem> contentItems = mContentDatabaseHandler.getContentByTagId(mServiceId, tagIDs, andOr.toString());
        if (null != contentItems && contentItems.size() > 0) {
            ContentItem contentItem = contentItems.get(0);
            List<Content> result = new ArrayList<Content>(1);
            try {
                Content a = mClassType.newInstance();
                if (!a.parseInput("{\"id\":\"blaat\"}")) {
                    listener.onError(ERROR.SERVER_ERROR, "invalid data format on server");
                    return;
                }
                for (ContentItem ci : contentItems) {
                    Content c = mClassType.newInstance();
                    c = gson.fromJson(contentItem.getRawData(), mClassType);
                    result.add(c);
                }
                listener.onSuccess(result);
            } catch (InstantiationException | IllegalAccessException e) {
                listener.onError(ERROR.CONFIGURATION_ERROR, "invalid generic class type provided");
            }
        } else {
            listener.onError(ERROR.NO_DATA_FOUND_IN_DB, "Given TAG(s) not found in DB");
        }
    }

    @Override
    public void deleteAllContents() {
        mContentDatabaseHandler.deleteAll(mServiceId);
    }
    // endregion

    // region Private methods
    // endregion

    // region Private members
    private AtomicBoolean downloadInProgress;

    public String getmServiceId() {
        return mServiceId;
    }

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

    private void updateContentDatabase() {
        if (null != downloadedContents && downloadedContents.size() > 0) {

        }
        downloadedContents.clear();
    }

    private long expiryTimeforUserInputTime(int userInputExpiryTime) {
        long expiryTime = 0;
        Calendar expiryDate = Calendar.getInstance();
        expiryDate.add(Calendar.HOUR_OF_DAY, userInputExpiryTime);
        expiryTime = expiryDate.getTime().getTime();
        return expiryTime;
    }

}

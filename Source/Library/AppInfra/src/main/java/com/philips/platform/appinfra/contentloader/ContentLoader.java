/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
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
import com.philips.platform.appinfra.contentloader.model.ContentItem;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by 310243577 on 11/28/2016.
 */

public class ContentLoader<Content extends ContentInterface> implements ContentLoaderInterface<Content> {

    private int offset = 0;
    private int downloadLimit = 0;
    private int contentDownloadedCount;
    private ContentDatabaseHandler mContentDatabaseHandler;
    private final String mServiceId;
    //   private URL mServiceURL;
    private Class<Content> mClassType;
    private String mContentType;
    private int mMaxAgeInHours;

    private AppInfraInterface mAppInfra;
    private RestInterface mRestInterface;
    private AtomicBoolean downloadInProgress;
    //   private boolean isRefreshing = false;
    private Date mLastUpdatedTime;

    /**
     * Create a content loader of type Content, loading content from the given service using the given Content class type.
     *
     * @param serviceId        Id of the service discovery service to download the content from
     * @param maxAgeInHours    maximum age of the content, a refresh is recommended if cached content is older
     * @param contentClassType type of the content class (use Content.class)
     * @param contentType      name of the content as given in the server JSON structure
     */
    public ContentLoader(Context context, String serviceId, int maxAgeInHours, Class<Content> contentClassType,
                         String contentType, AppInfraInterface appInfra) {
        mServiceId = serviceId;
        mMaxAgeInHours = maxAgeInHours;
        mClassType = contentClassType;
        mContentType = contentType;
        //  STATE mState = STATE.NOT_INITIALIZED;
        getStatus();
        mAppInfra = appInfra;
        mRestInterface = mAppInfra.getRestClient();
        downloadInProgress = new AtomicBoolean(false);
        downloadLimit = getDownloadLimitFromConfig();
        mContentDatabaseHandler = new ContentDatabaseHandler(context);
    }


    @Override
    public void refresh(OnRefreshListener refreshListener) {
        if (downloadInProgress.compareAndSet(false, true)) {
            STATE state = getStatus();
            if (state.equals(STATE.NOT_INITIALIZED) || state.equals(STATE.INITIALIZING) ||
                    state.equals(STATE.CACHED_DATA_OUTDATED) || state.equals(STATE.CONFIGURATION_ERROR)) {
                mLastUpdatedTime = new Date();
                downloadContent(refreshListener);
            } else {
                downloadInProgress.set(false);
                refreshListener.onSuccess(OnRefreshListener.REFRESH_RESULT.NO_REFRESH_REQUIRED);
                Log.i("CL REFRSH NA", "" + "content loader already uptodate");
            }
        } else {
            downloadInProgress.set(false);
            Log.i("CL REFRSH ERR", "" + "download already in progress");
            refreshListener.onError(ERROR.DOWNLOAD_IN_PROGRESS, "download already in progress");
        }
    }

    private void downloadContent(final OnRefreshListener refreshListener) {
        final List<ContentItem> downloadedContents = new ArrayList<>();
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
                    JsonElement serviceResponseJson = gson.fromJson(response.toString(), JsonElement.class); // cast org.json.JSONObject to gson.JsonElement
                    Log.i("CL REFRSH RESP", "" + serviceResponseJson);
                    if (serviceResponseJson.isJsonObject()) {
                        jsonObjectTree = serviceResponseJson.getAsJsonObject();
                        jsonObjectTree = jsonObjectTree.getAsJsonObject("result");
                    }
                    JsonArray contentList = null;
                    if (jsonObjectTree != null) {
                        JsonElement content = jsonObjectTree.get(mContentType);
                        if (content.isJsonArray()) {
                            contentList = content.getAsJsonArray();
                            contentDownloadedCount = contentList.size();
                        }
                    }

                    if (contentList != null && contentList.size() > 0) {
                        for (int contentCount = 0; contentCount < contentList.size(); contentCount++) {
                            Log.i("CL Ariticle", "" + contentList.get(contentCount));
                            try {
                                ContentInterface contentInterface = mClassType.newInstance();
                                contentInterface.parseInput(contentList.get(contentCount).toString());
                                ContentItem contentItem = new ContentItem();
                                contentItem.setId(contentInterface.getId().replace("\"", ""));
                                contentItem.setServiceId(mServiceId);
                                contentItem.setRawData(contentList.get(contentCount).toString());
                                contentItem.setVersionNumber(contentInterface.getVersion());
                                String tags = "";

                                List<String> tagList = contentInterface.getTags();
                                if (null != tagList && tagList.size() > 0) {
                                    for (String tagId : tagList) {
                                        tags += tagId + ",";
                                    }
                                }
                                contentItem.setTags(tags);
                                downloadedContents.add(contentItem);
                                String articleId = contentItem.getId();
                                Log.i("CL Ariticle", "" + articleId + "  TAGs ");
                            } catch (InstantiationException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (contentDownloadedCount < downloadLimit) { // download is over
                        Log.i("CL REFRSH RESP", "download completed");
                        Log.e("DOWNLOADED CONTENTS", downloadedContents.toString());
                        mContentDatabaseHandler.addContents(downloadedContents,mLastUpdatedTime, mServiceId, expiryTimeforUserInputTime(mMaxAgeInHours));

                        downloadInProgress.set(false);
                        offset = 0;
                        contentDownloadedCount = 0;
                        refreshListener.onSuccess(OnRefreshListener.REFRESH_RESULT.REFRESHED_FROM_SERVER);
                    } else {// download next
                        contentDownloadedCount = 0;
                        offset += downloadLimit;// next offset
                        //Saving contents page by page
                        //passing maxAge as 0 becasue we need to expire the contents if it is not fully downloaded.
                        mContentDatabaseHandler.addContents(downloadedContents,mLastUpdatedTime, mServiceId, 0);
                        downloadContent(refreshListener); // recursive call for next download
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("CL REFRSH Error:", "" + error);
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
        mContentDatabaseHandler.clearCacheForContentLoader(mServiceId);
    }

    @Override
    public STATE getStatus() {
        if(null==downloadInProgress) return STATE.NOT_INITIALIZED;
        if (!downloadInProgress.get()) return STATE.REFRESHING;
        long contentLoaderExpiryTime = mContentDatabaseHandler.getContentLoaderServiceStateExpiry(mServiceId);
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTime().getTime();
        if (contentLoaderExpiryTime != 0) {
            if (contentLoaderExpiryTime < currentTime) { // if content loader is expired then refresh
                return STATE.CACHED_DATA_OUTDATED;
            } else {
                return STATE.CACHED_DATA_AVAILABLE;
            }
        } else {
            return STATE.NOT_INITIALIZED;
        }
    }

    @Override
    public void getAllContent(OnResultListener<String> listener) {
        // It was concluded to fetch only content ids and not complete content
        List<String> IDs = mContentDatabaseHandler.getAllContentIds(mServiceId);
        if (IDs != null && IDs.size() > 0) {
            listener.onSuccess(IDs);
        } else {
            listener.onError(ERROR.DATABASE_ERROR, "could not fetch from DB");
        }
    }

    @Override
    public void getContentById(String id, OnResultListener<Content> listener) {
        String[] IDs = new String[1];
        IDs[0] = id;
        getContentBySingleOrMultipleID(IDs,listener);
    }

    @Override
    public void getContentById(String[] ids, OnResultListener<Content> listener) {
        getContentBySingleOrMultipleID(ids,listener);
    }

    private void getContentBySingleOrMultipleID(String[] ids, OnResultListener<Content> listener){
        List<ContentItem> contentItems = mContentDatabaseHandler.getContentById(mServiceId, ids);
        if (null != contentItems && contentItems.size() > 0) {
            List<Content> result = new ArrayList<>();
            try {
                for (ContentItem ci : contentItems) {
                    Content c = mClassType.newInstance();
                    c.parseInput(ci.getRawData());
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
        getContentBySingleOrMultipleTag(tag,null,listener);
    }

    @Override
    public void getContentByTag(String[] tagIDs, OPERATOR andOr, OnResultListener<Content> listener) {
        getContentBySingleOrMultipleTag(tagIDs,andOr,listener);
    }

    private void getContentBySingleOrMultipleTag(String[] tagIDs, OPERATOR andOr, OnResultListener<Content> listener){
        String logicalOperator=null;
        if(null!=andOr){
            logicalOperator=andOr.toString();
        }
        List<ContentItem> contentItems = mContentDatabaseHandler.getContentByTagId(mServiceId, tagIDs,logicalOperator );
        if (null != contentItems && contentItems.size() > 0) {
            List<Content> result = new ArrayList<>(1);
            try {
                for (ContentItem ci : contentItems) {
                    Content c = mClassType.newInstance();
                    c.parseInput(ci.getRawData());
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

    private int getDownloadLimitFromConfig() {
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        if (mAppInfra.getConfigInterface() != null) {
            try {
                Object contentLoaderLimit = mAppInfra.getConfigInterface().getPropertyForKey("contentLoader.limitSize",
                        "appinfra", configError);
                if (contentLoaderLimit != null && contentLoaderLimit instanceof Integer)
                    return (Integer) contentLoaderLimit;

            } catch (IllegalArgumentException exception) {
                mAppInfra.getLogging().log(LoggingInterface.LogLevel.ERROR, "ContentLoader", exception.toString());
            }
        }
        return 0;
    }

    public String getmServiceId() {
        return mServiceId;
    }


    private String getOffsetPath(int offset) {
        return ".offset.(" + offset + ").limit.(" + downloadLimit + ").json";
    }

    private long expiryTimeforUserInputTime(int userInputExpiryTime) {
        long expiryTime = 0;
        Calendar expiryDate = Calendar.getInstance();
        expiryDate.add(Calendar.HOUR_OF_DAY, userInputExpiryTime);
        expiryTime = expiryDate.getTime().getTime();
        return expiryTime;
    }
}

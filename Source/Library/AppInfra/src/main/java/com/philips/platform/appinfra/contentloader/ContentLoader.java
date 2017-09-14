/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.contentloader;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Content Loader class.
 */

public class ContentLoader<Content extends ContentInterface> implements ContentLoaderInterface<Content>,Serializable {

    private final int downloadLimit;
    private final ContentDatabaseHandler mContentDatabaseHandler;
    private final String mServiceId;
    //   private URL mServiceURL;
    private final Class<Content> mClassType;
    private final String mContentType;
    private final int mMaxAgeInHours;
    private final AppInfraInterface mAppInfra;
    private final RestInterface mRestInterface;
    private final AtomicBoolean downloadInProgress;
    private int offset = 0;
    private int contentDownloadedCount;
    //   private boolean isRefreshing = false;
    private long mLastUpdatedTime;
    private STATE mContentLoaderState = STATE.NOT_INITIALIZED;

    /**
     * Create a content loader of type Content,
     * loading content from the given service using the given Content class type.
     *
     * @param serviceId        Id of the service discovery service to download the content from
     * @param maxAgeInHours    maximum age of the content, a refresh is recommended if cached content is older
     * @param contentClassType type of the content class (use Content.class)
     * @param contentType      name of the content as given in the server JSON structure
     * @param downloadLimit no of pages that should be downloaded in one attempt. This will override the limit set in the app config. Pass '0' for taking the default limit from app config.
     */
    public ContentLoader(Context context, String serviceId, int maxAgeInHours, Class<Content> contentClassType,
                         String contentType, AppInfraInterface appInfra, int downloadLimit) {
        mServiceId = serviceId;
        mMaxAgeInHours = maxAgeInHours;
        mClassType = contentClassType;
        mContentType = contentType;
        mAppInfra = appInfra;
        VolleyLog.DEBUG = false;
        mRestInterface = mAppInfra.getRestClient();
        downloadInProgress = new AtomicBoolean(false);
        if (downloadLimit > 0) { // if a positive down load limit is set
            this.downloadLimit = downloadLimit;
        } else {  // if  down load limit is set as 0 or any negative value
            this.downloadLimit = getDownloadLimitFromConfig();
        }
        mContentDatabaseHandler = ContentDatabaseHandler.getInstance(context);
        updateContentLoaderState();
    }


    @Override
    public void refresh(OnRefreshListener refreshListener) {
        updateContentLoaderState();
        if (downloadInProgress.compareAndSet(false, true)) {
            final STATE state = getStatus();
            if (!state.equals(STATE.CACHED_DATA_AVAILABLE)) { // if data outdated
                mLastUpdatedTime = new Date().getTime();
                downloadContent(refreshListener);
            } else { // if data already cached
                downloadInProgress.set(false);
                refreshListener.onSuccess(OnRefreshListener.REFRESH_RESULT.NO_REFRESH_REQUIRED);
//                Log.i("CL REFRSH NA", "" + "content loader already uptodate");
            }
        } else {
            mContentLoaderState = STATE.REFRESHING;
            downloadInProgress.set(false);
//            Log.i("CL REFRSH ERR", "" + "download already in progress");
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
//                    Log.i("CL REFRSH RESP", "download completed for Offset: " + offset + " and Limit: " + downloadLimit);
                    final JsonElement serviceResponseJson = gson.fromJson(response.toString(), JsonElement.class); // cast org.json.JSONObject to gson.JsonElement
//                    Log.i("CL REFRSH RESP", "" + serviceResponseJson);
                    if (serviceResponseJson.isJsonObject()) {
                        jsonObjectTree = serviceResponseJson.getAsJsonObject();
                        jsonObjectTree = jsonObjectTree.getAsJsonObject("result");
                    }
                    JsonArray contentList = null;
                    if (jsonObjectTree != null) {
                        final JsonElement content = jsonObjectTree.get(mContentType);
                        if (null == content) {
//                            Log.i("CL REFRSH Error:", "" + "Content type mismatch");
                            mContentLoaderState = STATE.CONFIGURATION_ERROR;
                            downloadInProgress.set(false);
                            contentDownloadedCount = 0;
                            downloadedContents.clear();
                            offset = 0;
                            refreshListener.onError(ERROR.CONFIGURATION_ERROR, "Content type mismatch");
                            return;
                        } else if (content.isJsonArray()) {
                            contentList = content.getAsJsonArray();
                            contentDownloadedCount = contentList.size();
                        }
                    }

                    if (contentList != null && contentList.size() > 0) {
                        for (int contentCount = 0; contentCount < contentList.size(); contentCount++) {
//                            Log.i("CL Ariticle", "" + contentList.get(contentCount));
                            try {
                                final ContentInterface contentInterface = mClassType.newInstance();
                                contentInterface.parseInput(contentList.get(contentCount).toString());
                                final ContentItem contentItem = new ContentItem();
                                contentItem.setId(contentInterface.getId().replace("\"", ""));
                                contentItem.setServiceId(mServiceId);
                                contentItem.setRawData(contentList.get(contentCount).toString());
                                contentItem.setVersionNumber(contentInterface.getVersion());
                                contentItem.setLastUpdatedTime(mLastUpdatedTime); // last updated time
                                String tags = "";

                                final List<String> tagList = contentInterface.getTags();
                                if (null != tagList && !tagList.isEmpty()) {
                                    for (String tagId : tagList) {
                                        tags += tagId + " ";
                                    }
                                }
                                contentItem.setTags(tags);
                                downloadedContents.add(contentItem);
                                String articleId = contentItem.getId();
//                                Log.i("CL Ariticle", "" + articleId + "  TAGs ");
                            } catch (InstantiationException | IllegalAccessException e) {
                            }
                        }
                    }
                    if (contentDownloadedCount < downloadLimit) { // download is over
//                        Log.i("CL REFRSH RESP", "download completed");
//                        Log.e("DOWNLOADED CONTENTS", downloadedContents.toString());
                        mContentDatabaseHandler.addContents(downloadedContents, mServiceId, mLastUpdatedTime, expiryTimeforUserInputTime(mMaxAgeInHours), true);
                        mContentLoaderState = STATE.CACHED_DATA_AVAILABLE;
                        downloadInProgress.set(false);
                        offset = 0;
                        contentDownloadedCount = 0;
                        refreshListener.onSuccess(OnRefreshListener.REFRESH_RESULT.REFRESHED_FROM_SERVER);
                    } else {// download next
                        contentDownloadedCount = 0;
                        offset += downloadLimit;// next offset
                        //Saving contents page by page
                        //passing maxAge as 0 becasue we need to expire the contents if it is not fully downloaded.
                        mContentDatabaseHandler.addContents(downloadedContents, mServiceId, mLastUpdatedTime, 0, false);
                        downloadContent(refreshListener); // recursive call for next download
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.i("CL REFRSH Error:", "" + error);
                    mContentLoaderState = STATE.CACHED_DATA_OUTDATED; // if data download is interrupted
                    downloadInProgress.set(false);
                    contentDownloadedCount = 0;
                    downloadedContents.clear();
                    offset = 0;
                    refreshListener.onError(ERROR.SERVER_ERROR, error.toString());
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
        final boolean isDeleted = mContentDatabaseHandler.clearCacheForContentLoader(mServiceId);
        if (isDeleted) {
            mContentLoaderState = STATE.NOT_INITIALIZED;
        }
    }

    @Override
    public STATE getStatus() {
        if (mContentLoaderState.equals(STATE.CACHED_DATA_AVAILABLE)) {
            updateContentLoaderState();
        }
        return mContentLoaderState;
       /* if(null==downloadInProgress) return STATE.NOT_INITIALIZED;
        if (!downloadInProgress.get()) return STATE.NOT_INITIALIZED;
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
            return STATE.INITIALIZING;
        }*/
    }

    @Override
    public void getAllContent(OnResultListener<String> listener) {
        // It was concluded to fetch only content ids and not complete content
        final List<String> IDs = mContentDatabaseHandler.getAllContentIds(mServiceId);
        if (IDs != null && !IDs.isEmpty()) {
            listener.onSuccess(IDs);
        } else {
            listener.onError(ERROR.DATABASE_ERROR, "could not fetch from DB");
        }
    }

    @Override
    public void getContentById(String id, OnResultListener<Content> listener) {
        final String[] IDs = new String[1];
        IDs[0] = id;
        getContentBySingleOrMultipleID(IDs, listener);
    }

    @Override
    public void getContentById(String[] ids, OnResultListener<Content> listener) {
        getContentBySingleOrMultipleID(ids, listener);
    }

    private void getContentBySingleOrMultipleID(String[] ids, OnResultListener<Content> listener) {
        final List<ContentItem> contentItems = mContentDatabaseHandler.getContentById(mServiceId, ids);
        if (null != contentItems && !contentItems.isEmpty()) {
            contentItems(contentItems, listener);
        } else {
            listener.onError(ERROR.NO_DATA_FOUND_IN_DB, "Given ID not found in DB");
        }
    }

    @Override
    public void getContentByTag(String tagID, OnResultListener<Content> listener) {
        final String[] tag = new String[1];
        tag[0] = tagID;
        getContentBySingleOrMultipleTag(tag, null, listener);
    }

    @Override
    public void getContentByTag(String[] tagIDs, OPERATOR andOr, OnResultListener<Content> listener) {
        getContentBySingleOrMultipleTag(tagIDs, andOr, listener);
    }

    private void getContentBySingleOrMultipleTag(String[] tagIDs, OPERATOR andOr, OnResultListener<Content> listener) {
        String logicalOperator = null;
        if (null != andOr) {
            logicalOperator = andOr.toString();
        }
        final List<ContentItem> contentItems = mContentDatabaseHandler.getContentByTagId(mServiceId, tagIDs, logicalOperator);
        if (null != contentItems && !contentItems.isEmpty()) {
            contentItems(contentItems, listener);
        } else {
            listener.onError(ERROR.NO_DATA_FOUND_IN_DB, "Given TAG(s) not found in DB");
        }
    }


    private void contentItems(List<ContentItem> contentItems, OnResultListener<Content> listener) {
        final List<Content> result = new ArrayList<>();
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
    }

    private int getDownloadLimitFromConfig() {
        final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
        if (mAppInfra.getConfigInterface() != null) {
            try {
                Object contentLoaderLimit = mAppInfra.getConfigInterface().getPropertyForKey("contentLoader.limitSize",
                        "appinfra", configError);
                if (contentLoaderLimit instanceof Integer) {
                    return (Integer) contentLoaderLimit;
                }

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
        long expiryTime = 0L;
        final Calendar expiryDate = Calendar.getInstance();
        expiryDate.add(Calendar.HOUR_OF_DAY, userInputExpiryTime);
        expiryTime = expiryDate.getTime().getTime();
        return expiryTime;
    }

    private void updateContentLoaderState() {
        final long contentLoaderExpiryTime = mContentDatabaseHandler.getContentLoaderServiceStateExpiry(mServiceId);
        final Calendar calendar = Calendar.getInstance();
        final long currentTime = calendar.getTime().getTime();
        if (contentLoaderExpiryTime != 0) {
            if (contentLoaderExpiryTime < currentTime) { // if content loader is expired then refresh
                mContentLoaderState = STATE.CACHED_DATA_OUTDATED;
                clearCache();
            } else {
                mContentLoaderState = STATE.CACHED_DATA_AVAILABLE;
            }
        }
    }
}

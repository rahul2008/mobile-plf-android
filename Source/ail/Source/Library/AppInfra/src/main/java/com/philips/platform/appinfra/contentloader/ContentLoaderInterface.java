/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.contentloader;

import java.io.Serializable;
import java.util.List;

/*
 * The Content loader Interface.
 * Example of article summary download https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1.offset.(0).limit.(100).json
 */
public interface ContentLoaderInterface<Content extends ContentInterface> extends Serializable {
    enum STATE {NOT_INITIALIZED, INITIALIZING, CACHED_DATA_AVAILABLE, CACHED_DATA_OUTDATED, REFRESHING, CONFIGURATION_ERROR};
    enum ERROR {CONFIGURATION_ERROR, SERVER_UNAVAILABLE, SERVER_ERROR, DOWNLOAD_IN_PROGRESS,DATABASE_ERROR,NO_DATA_FOUND_IN_DB};
    enum OPERATOR {AND, OR};

    /**
     * This call back method of OnResultListener, if refersh failure it will calback to onError,if success it will callback to onSuccess
     * @since 2.1.0
     */
    interface OnResultListener<Result> {
        void onError(ERROR error, String message);
        void onSuccess(List<Result> contents);
    }
    /**
     * This call back method of OnRefreshListener, if refersh failure it will calback to onError,if success it will callback to onSuccess
     * @since 2.1.0
     */
    interface OnRefreshListener {
        enum REFRESH_RESULT {LOADED_FROM_LOCAL_CACHE, REFRESHED_FROM_SERVER, NO_REFRESH_REQUIRED,REFRESHED_FAILED};
        void onError(ERROR error, String message);
        void onSuccess(REFRESH_RESULT result);
    }

    /**
     * Refresh content in cache.
     * If database is empty, loads from server.
     * If content is outdated, starts download from server.
     * If content URL has changed (e.g. locale changed) the cached content is automatically cleared and download is started.
     * @param refreshListener asynchronous callback reporting result of refresh
     * @since 1.1.0
     */
    void refresh(OnRefreshListener refreshListener);

    /**
     * Remove all cached content of given content loader
     * @since 1.1.0
     */
    void clearCache();

    /**
     * Get current state of content loader
     * @return state
     *   NOT_INITIALIZED: database is empty
     *   INITIALIZING: database is empty, loading data from server
     *   CACHED_DATA_AVAILABLE: database ready for use, content up to date
     *   CACHED_DATA_OUTDATED: database ready for use, content is outdated refresh should be called
     *   REFRESHING: database ready for use, content is outdated, refresh is in progress
     *   CONFIGURATION_ERROR: database is empty, configuration of content loader is incorrect: unknown service ID or improper content class type
     *   @since 1.1.0
     */
    STATE getStatus();

    /**
     * List of all available content IDs
     * @since 1.1.0
     */
    void getAllContent(OnResultListener<String> listener);

    /**
     * Content object for the given id
     * @since 1.1.0
     */
    void getContentById(String id, OnResultListener<Content> listener);
    /**
     * List of Content objects for the given array of ids
     * @since 1.1.0
     */
    void getContentById(String[] ids, OnResultListener<Content> listener);
    /**
     * List of Content objects that have the given tag ID set
     * @since 1.1.0
     */
    void getContentByTag(String tagID, OnResultListener<Content> listener);
    /**
     * List of Content objects that have the at least one (OR) or all (AND) given tag IDs set
     * @since 1.1.0
     */
    void getContentByTag(String[] tagIDs, OPERATOR andOr, OnResultListener<Content> listener);

}

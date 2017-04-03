/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.languagepack;

public interface LanguagePackInterface {

	/**
	 * download language pack overview file.
	 * If should be called everytime when app is launched
	 *
	 * @param refreshListener asynchronous callback reporting result of refresh eg {LoadedFromLocalCache, REFRESHED_FROM_SERVER, NO_REFRESH_REQUIRED, REFRESH_FAILED}
	 */
	void refresh(OnRefreshListener refreshListener);

	/**
	 *  It activates device matching locale from downloaded overview file
	 *  Calling activate will return path of Language pack through call back listener
	 * @param onActivateListener asynchronous callback reporting result of activate
	 */
	void activate(OnActivateListener onActivateListener);

	interface OnRefreshListener {
		enum AILPRefreshResult {REFRESHED_FROM_SERVER, NO_REFRESH_REQUIRED, REFRESH_FAILED}
		void onError(AILPRefreshResult error, String message);
		void onSuccess(AILPRefreshResult result);
	}

	interface OnActivateListener {
		enum AILPActivateResult {UPDATE_ACTIVATED, NO_UPDATE_STORED, UPDATE_FAILED}
		void onSuccess(String path);
		void onError(AILPActivateResult ailpActivateResult, String message);

		  // TODO  UPDATE_FAILED implementation
		/*
		* UPDATE_ACTIVATED (Success)
		* No Update stored(error)  Already updated (message)
		* UPDATE_FAILED No Language(error) Pack available(message)
		*
		* */
	}
}


package com.philips.platform.appinfra.languagepack;


/**
 * Created by philips on 3/13/17.
 */

public interface LanguagePackInterface {

	/**
	 * download language pack overview file.
	 * If should be called everytime when app is launched
	 *
	 * @param refreshListener asynchronous callback reporting result of refresh eg {LoadedFromLocalCache, RefreshedFromServer, NoRefreshRequired, RefreshFailed}
	 */
	void refresh(OnRefreshListener refreshListener);

	void activate(OnActivateListener onActivateListener);

	interface OnRefreshListener {
		void onError(AILPRefreshResult error, String message);

		void onSuccess(AILPRefreshResult result);

		enum AILPRefreshResult {LoadedFromLocalCache, RefreshedFromServer, NoRefreshRequired, RefreshFailed}
	}

	interface OnActivateListener {
		void onSuccess(String path);

		void onError(AILPActivateResult ailpActivateResult);

		enum AILPActivateResult {REFRESH_NOT_CALLED, SOMETHING_WENT_WRONG}
	}
}


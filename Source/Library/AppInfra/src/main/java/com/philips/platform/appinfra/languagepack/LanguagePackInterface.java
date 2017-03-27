package com.philips.platform.appinfra.languagepack;


/**
 * Created by philips on 3/13/17.
 */

public interface LanguagePackInterface {

	interface OnRefreshListener {
		enum AILPRefreshResult {LoadedFromLocalCache, RefreshedFromServer, NoRefreshRequired, RefreshFailed}

		void onError(AILPRefreshResult error, String message);

		void onSuccess(AILPRefreshResult result);
	}

	interface OnActivateListener {
		void onSuccess(String path);
		void onError(String description);
	}

	/**
	 * download language pack overview file.
	 * If should be called everytime when app is launched
	 *
	 * @param refreshListener asynchronous callback reporting result of refresh eg {LoadedFromLocalCache, RefreshedFromServer, NoRefreshRequired, RefreshFailed}
	 */
	void refresh(OnRefreshListener refreshListener);

	void activate(OnActivateListener onActivateListener);
}


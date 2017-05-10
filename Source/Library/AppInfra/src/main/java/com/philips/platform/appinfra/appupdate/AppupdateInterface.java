package com.philips.platform.appinfra.appupdate;



public interface AppupdateInterface {

	void refresh(OnRefreshListener refreshListener);

	interface OnRefreshListener {

		enum AIAppUpdateRefreshResult {AIAppUpdate_REFRESH_SUCCESS, NO_REFRESH_REQUIRED, AIAppUpdate_REFRESH_FAILED}

		void onError(OnRefreshListener.AIAppUpdateRefreshResult error, String message);

		void onSuccess(OnRefreshListener.AIAppUpdateRefreshResult result);
	}

	boolean isDeprecated();

	boolean isToBeDeprecated();

	boolean isUpdateAvailable();

	String getDeprecateMessage();

	String getToBeDeprecatedMessage();

	String getToBeDeprecatedDate();

	String getUpdateMessage();

	String getMinimumVersion();

	String getMinimumOSverion();


}

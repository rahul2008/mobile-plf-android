/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appupdate;

/**
 *  AppUpdate Interface.
 */
public interface AppupdateInterface {

	/**
	 * download appuppdate json file.
	 * if "appUpdate.autoRefresh":true is set in appconfig ,
	 * appupdate json wil be downloaded when appinfra intialization is done.
	 * @param refreshListener asynchronous callback reporting result of refresh eg {AppUpdate_REFRESH_SUCCESS,
	 * AppUpdate_REFRESH_FAILED}.
	 */
	void refresh(OnRefreshListener refreshListener);

	interface OnRefreshListener {

		enum AIAppUpdateRefreshResult {AppUpdate_REFRESH_SUCCESS, AppUpdate_REFRESH_FAILED}

		void onError(OnRefreshListener.AIAppUpdateRefreshResult error, String message);

		void onSuccess(OnRefreshListener.AIAppUpdateRefreshResult result);
	}

	/**
	 *
	 * @return
	 */
	boolean isDeprecated();

	/**
	 *
	 * @return
	 */
	boolean isToBeDeprecated();

	/**
	 *
	 * @return
	 */
	boolean isUpdateAvailable();

	/**
	 *
	 * @return
	 */
	String getDeprecateMessage();

	/**
	 *
	 * @return
	 */
	String getToBeDeprecatedMessage();

	/**
	 *
	 * @return
	 */
	String getToBeDeprecatedDate();

	/**
	 *
	 * @return
	 */
	String getUpdateMessage();

	/**
	 * This method returns the minimum version
	 * from the appupdate json.
	 * @return minimum version.
	 */
	String getMinimumVersion();

	/**
	 * This method returns the minimumOS version.
	 * @return  minimumOS version.
	 */
	String getMinimumOSverion();


}

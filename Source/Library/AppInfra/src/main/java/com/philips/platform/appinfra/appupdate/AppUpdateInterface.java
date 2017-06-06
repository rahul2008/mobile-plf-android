/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appupdate;

import java.util.Date;

/**
 *  AppUpdate Interface.
 */
public interface AppUpdateInterface {

	/**
	 * refreshes the appupdate info available in the server.
	 * refresh will fail if appupdate.serviceId is missing in appconfig
	 * or service discovery is not configured for appupdate
	 * or the content of the appupdate file is not in the specified format
	 * if "appUpdate.autoRefresh":true is set in appconfig ,
	 * appupdate json wil be downloaded when appinfra intialization is done.
	 * @param refreshListener asynchronous callback reporting result of refresh eg {AppUpdate_REFRESH_SUCCESS,
	 * AppUpdate_REFRESH_FAILED}.
	 */
	void refresh(OnRefreshListener refreshListener);



	interface OnRefreshListener {
		/**
		 * AppUpdate_REFRESH_SUCCESS : AppUpdate info Downloaded from Server.
		 * AppUpdate_REFRESH_FAILED : Refresh Failed.
		 */
		enum AIAppUpdateRefreshResult {AppUpdate_REFRESH_SUCCESS, AppUpdate_REFRESH_FAILED}

		void onError(OnRefreshListener.AIAppUpdateRefreshResult error, String message);

		void onSuccess(OnRefreshListener.AIAppUpdateRefreshResult result);
	}

	/**
	 * This will return true if applicationversion < minimumversion
	 * true when current application version is less than minimum version
	 * true when deprecatedVersion is greater than current application version and deprecationDate is crossed.
	 * @return
	 */
	boolean isDeprecated();

	/**
	 * minimumVersion <= applicationVersion <= tobeDeprecated.
	 * @return true if application is not already deprecated and current version is
	 * lessthan equal to deprecated version.
	 */
	boolean isToBeDeprecated();

	/**
	 * applicationVersion < CurrentVersion.
	 * @return true if current version is less than the
	 * latest verion available in the appstore.
	 */
	boolean isUpdateAvailable();

	/**
	 * Deprecated Version message string.
	 * @return
	 */
	String getDeprecateMessage();

	/**
	 * To be deprecated message string.
	 * @return
	 */
	String getToBeDeprecatedMessage();

	/**
	 * To be deprecated date.
	 * @return
	 */
	Date getToBeDeprecatedDate();

	/**
	 * current version message.
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

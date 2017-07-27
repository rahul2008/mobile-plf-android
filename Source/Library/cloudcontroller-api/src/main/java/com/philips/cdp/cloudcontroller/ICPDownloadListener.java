/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

public interface ICPDownloadListener {
	void onDataDownload(int status, String downloadedData) ;
}

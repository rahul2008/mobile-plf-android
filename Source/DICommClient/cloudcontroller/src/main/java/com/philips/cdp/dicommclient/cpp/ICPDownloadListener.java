/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

public interface ICPDownloadListener {
	void onDataDownload(int status, String downloadedData) ;
}

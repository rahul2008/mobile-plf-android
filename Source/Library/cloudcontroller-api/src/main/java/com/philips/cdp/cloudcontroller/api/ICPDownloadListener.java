/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api;

public interface ICPDownloadListener {
	void onDataDownload(int status, String downloadedData) ;
}

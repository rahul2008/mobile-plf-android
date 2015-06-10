package com.philips.cdp.dicommclient.cpp;

public interface ICPDownloadListener {
	void onDataDownload(int status, String downloadedData) ;
}

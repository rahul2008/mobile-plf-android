package com.philips.cdp.dicomm.cpp;

public interface ICPDownloadListener {
	void onDataDownload(int status, String downloadedData) ;
}

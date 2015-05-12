package com.philips.cdp.dicomm.cpp.listener;

public interface IcpDownloadListener {
	void onDataDownload(int status, String downloadedData) ;
}

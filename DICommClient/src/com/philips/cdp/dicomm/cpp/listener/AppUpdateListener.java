package com.philips.cdp.dicomm.cpp.listener;

import java.io.File;


public interface AppUpdateListener {
	public void onAppUpdateAvailable() ;
	public void onAppUpdateInfoDownloaded();
	public File createFileForAppUpdateDownload();
	public void onAppUpdateDownloadStart(int percentage);
	public void onAppUpdateDownloadProgress(int percentage);
	public void onAppUpdateDownloadComplete();
	public void onAppUpdateDownloadFailed();
}

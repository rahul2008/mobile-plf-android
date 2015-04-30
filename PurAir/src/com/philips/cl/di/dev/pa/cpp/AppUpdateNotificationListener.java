package com.philips.cl.di.dev.pa.cpp;

import java.io.File;


public interface AppUpdateNotificationListener {
	void onAppUpdate() ;
	void onComponentInfoDownloaded();
	File createFileForDownload();
	void onFileDownloadStart(int percentage);
	void onFileDownloadProgress(int percentage);
	void onFileDownloadComplete();
	void onFileDownloadFailed();
}

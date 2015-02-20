package com.philips.cl.di.dev.pa.cpp;

public interface AppUpdateNotificationListener {
	void onAppUpdate() ;
	void onAppUpdateFailed(String message) ;
}

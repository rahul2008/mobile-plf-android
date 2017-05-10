package com.philips.platform.appinfra.appupdate;


import android.content.Context;

import com.philips.platform.appinfra.AppInfra;

public class AppupdateManager implements AppupdateInterface {

	private Context mContext;
	private AppInfra mAppInfra;


	public AppupdateManager(AppInfra appInfra) {
		this.mAppInfra = appInfra;
		this.mContext = appInfra.getAppInfraContext();
	}

	@Override
	public void refresh(OnRefreshListener refreshListener) {

	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

	@Override
	public boolean isToBeDeprecated() {
		return false;
	}

	@Override
	public boolean isUpdateAvailable() {
		return false;
	}

	@Override
	public String getDeprecateMessage() {
		return null;
	}

	@Override
	public String getToBeDeprecatedMessage() {
		return null;
	}

	@Override
	public String getToBeDeprecatedDate() {
		return null;
	}

	@Override
	public String getUpdateMessage() {
		return null;
	}

	@Override
	public String getMinimumVersion() {
		return null;
	}

	@Override
	public String getMinimumOSverion() {
		return null;
	}
}

package com.philips.cl.di.dev.pa.fragment;

public interface PermissionListener {
	void onPermissionReturned(boolean permissionExists);
	void onPermissionRemoved();
	void onPermissionAdded();
	void onCallFailed();
}

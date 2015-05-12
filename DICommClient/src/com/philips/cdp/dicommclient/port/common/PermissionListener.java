package com.philips.cdp.dicommclient.port.common;

public interface PermissionListener {
	void onPermissionReturned(boolean permissionExists);
	void onPermissionRemoved();
	void onPermissionAdded();
	void onCallFailed();
}

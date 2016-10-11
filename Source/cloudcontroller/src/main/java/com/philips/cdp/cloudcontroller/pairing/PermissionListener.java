/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.pairing;

public interface PermissionListener {
	void onPermissionReturned(boolean permissionExists);
	void onPermissionRemoved();
	void onPermissionAdded();
	void onCallFailed();
}

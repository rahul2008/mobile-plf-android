/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

import com.philips.icpinterface.ICPClient;

public interface ICPEventListener {
	void onICPCallbackEventOccurred(int eventType,int status,ICPClient obj) ;
}

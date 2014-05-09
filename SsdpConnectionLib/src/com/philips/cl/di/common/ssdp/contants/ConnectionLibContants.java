package com.philips.cl.di.common.ssdp.contants;

import android.os.Environment;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public final class ConnectionLibContants {

	public static final String FILENAME = Environment.getExternalStorageDirectory().getAbsolutePath()
	        + "/Android/data/deviceinfo";
	public static final String IP_KEY = "ip";
	public static final String LOG_TAG = "ssdpJava";
	public static final String PORT_KEY = "port";

	public static final String SSDP_ALIVE = "alive";
	public static final String SSDP_BYEBYE = "byebye";

	public static final int SSDP_LOOP_DELAY_MS = 5000;
	public static final String SSDP_ROOT_DEVICE = "::upnp:rootdevice";
	public static final String XML_KEY = "xml";
	public static final String SERVER_NAME = "AirPurifier";
}

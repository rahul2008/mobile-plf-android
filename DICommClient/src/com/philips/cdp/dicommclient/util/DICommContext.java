package com.philips.cdp.dicommclient.util;

import android.content.Context;

public class DICommContext {

	private static Context mContext;

	public static synchronized void initialize(Context context) {
		mContext = context;
	}

	public static synchronized Context getContext() {
		return mContext;
	}
}

package com.philips.cdp.registration.wechat.philipsregistration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.janrain.android.utils.LogUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

		api.registerApp("wx855fc0d8fd1ade1d");
        LogUtils.logd("**********************");
        LogUtils.logd(intent.getAction());
        if(intent.getDataString() != null) {
            LogUtils.logd(intent.getDataString());
        }
        LogUtils.logd("**********************");
    }

}
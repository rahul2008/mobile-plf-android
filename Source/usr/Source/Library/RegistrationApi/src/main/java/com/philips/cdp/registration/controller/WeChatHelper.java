package com.philips.cdp.registration.controller;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WeChatHelper {

    private   String weChatAppId = null;
    private   String weChatAppSecret = null;
    private IWXAPI weChatApi = null ;
    private static String TAG = WeChatHelper.class.getSimpleName();
    public boolean isWeChatSupported(){
        if(weChatApi == null){
           return false;
        }
        return  weChatApi.isWXAppInstalled()&& weChatApi.isWXAppSupportAPI();
    }

    public boolean register(Activity context, String weChatAppId, String weChatAppSecret ){
        weChatApi = WXAPIFactory.createWXAPI(context, weChatAppId, false);
        RLog.d(TAG,"register : weChatApi.registerApp");
        return weChatApi.registerApp(weChatAppSecret);
    }


    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra(RegConstants.WECHAT_ERR_CODE);
            String code = intent.getStringExtra(RegConstants.WECHAT_CODE);

            RLog.d(TAG,"BroadcastReceiver : onReceive");
        }
    };

    public BroadcastReceiver getWechatReciever(){
        RLog.d(TAG,"getWechatReciever : is called");
        return mMessageReceiver;
    }


    public void authenticate() {
        RLog.d(TAG,"authenticate : weChatApi.sendReq");
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "123456";
        weChatApi.sendReq(req);
    }
}

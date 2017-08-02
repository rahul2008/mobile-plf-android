package com.philips.cdp.registration.controller;


import android.app.Activity;
import android.content.*;
import android.util.Log;

import com.philips.cdp.registration.ui.utils.RegConstants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.*;

public class WeChatHelper {

    private   String weChatAppId = null;
    private   String weChatAppSecret = null;
    private IWXAPI weChatApi = null ;

    public boolean isWeChatSupported(){
        if(weChatApi == null){
           return false;
        }
        return  weChatApi.isWXAppInstalled()&& weChatApi.isWXAppSupportAPI();
    }

    public boolean register(Activity context, String weChatAppId, String weChatAppSecret ){
        weChatApi = WXAPIFactory.createWXAPI(context, weChatAppId, false);
        return weChatApi.registerApp(weChatAppSecret);
    }


    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra(RegConstants.WECHAT_ERR_CODE);
            String code = intent.getStringExtra(RegConstants.WECHAT_CODE);
            Log.d("receiver", "Got message: " + message+code);

            //Create intentservice for getting secrete and

        }
    };

    public BroadcastReceiver getWechatReciever(){
        return mMessageReceiver;
    }


    public void authenticate() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "123456";
        weChatApi.sendReq(req);
    }
}

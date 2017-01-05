package com.philips.cdp.registration.wechat;

import org.json.JSONObject;

public interface WeChatAuthenticationListener {
    
    void onSuccess(JSONObject jsonObject);

    void onFail();
}

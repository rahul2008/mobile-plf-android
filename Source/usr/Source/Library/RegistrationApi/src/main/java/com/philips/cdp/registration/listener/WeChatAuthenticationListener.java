package com.philips.cdp.registration.listener;

import org.json.JSONObject;

public interface WeChatAuthenticationListener {
    
    void onSuccess(JSONObject jsonObject);

    void onFail();
}

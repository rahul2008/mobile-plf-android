package com.philips.cdp.registration.wechat;


import com.philips.cdp.registration.listener.WeChatAuthenticationListener;
import com.philips.cdp.registration.restclient.URRequest;
import com.philips.cdp.registration.ui.utils.RLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WeChatAuthenticator {
    private String TAG = WeChatAuthenticator.class.getSimpleName();

    private final String WECHAT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";

    public void getWeChatResponse(final String weChatAppId,
                                  final String weChatAppSecrete,
                                  final String weChatAccessCode,
                                  final WeChatAuthenticationListener
                                          weChatAuthenticationListener) {
        new Thread(() -> {
            try {
                String body = "appid=" + weChatAppId + "&secret=" + weChatAppSecrete + "&code=" + weChatAccessCode + "&grant_type=authorization_code";

                RLog.i(TAG, "WeChatAuthenticator: URL " + WECHAT_ACCESS_TOKEN_URL );

                RLog.d(TAG, "JSON Body = " + body);
                Map<String, String> header = new HashMap<>();
                header.put("User-Agent", "wechatLoginDemo");

                URRequest urRequest = new URRequest(WECHAT_ACCESS_TOKEN_URL, body, header, response -> {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        weChatAuthenticationListener.onSuccess(jsonObj);
                        RLog.d(TAG, "getWeChatResponse : onSuccess " + jsonObj.toString());
                    } catch (JSONException e) {
                        RLog.e(TAG, "getWeChatResponse : exception occured " + e.getMessage());
                    }
                }, error -> {
                    RLog.e(TAG, "getWeChatResponse : onFail " + error.getMessage());
                    weChatAuthenticationListener.onFail();
                });

                urRequest.makeRequest(true);
            } catch (Exception ex) {
                RLog.e(TAG, "getWeChatResponse : onFail " + ex.toString());
                weChatAuthenticationListener.onFail();
            }
        }).start();
    }


}


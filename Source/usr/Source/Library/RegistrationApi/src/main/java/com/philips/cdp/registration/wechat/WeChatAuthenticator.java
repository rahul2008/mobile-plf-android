package com.philips.cdp.registration.wechat;


import com.philips.cdp.registration.listener.WeChatAuthenticationListener;
import com.philips.cdp.registration.restclient.URRequest;
import com.philips.cdp.registration.ui.utils.RLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WeChatAuthenticator {

    private final String WECHAT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";

    public void getWeChatResponse(final String weChatAppId,
                                  final String weChatAppSecrete,
                                  final String weChatAccessCode,
                                  final WeChatAuthenticationListener
                                          weChatAuthenticationListener) {
        new Thread(() -> {
            try {
                String body = "appid=" + weChatAppId + "&secret=" + weChatAppSecrete + "&code=" + weChatAccessCode + "&grant_type=authorization_code";

                RLog.d("WECHAT", "JSON Body = " + WECHAT_ACCESS_TOKEN_URL + body);
                Map<String, String> header = new HashMap<>();
                header.put("User-Agent", "wechatLoginDemo");

                URRequest urRequest = new URRequest(WECHAT_ACCESS_TOKEN_URL, body, header, response -> {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        weChatAuthenticationListener.onSuccess(jsonObj);
                        RLog.d("WECHAT", jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    RLog.e("WECHAT", error.getMessage());
                    weChatAuthenticationListener.onFail();
                });

                urRequest.makeRequest();
            } catch (Exception ex) {
                RLog.e("WECHAT", ex.toString());
                weChatAuthenticationListener.onFail();
            }
        }).start();
    }


}


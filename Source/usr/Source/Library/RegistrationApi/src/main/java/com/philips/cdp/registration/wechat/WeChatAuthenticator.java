package com.philips.cdp.registration.wechat;


import com.philips.cdp.registration.listener.*;
import com.philips.cdp.registration.ui.utils.RLog;
import com.squareup.okhttp.*;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WeChatAuthenticator {

    private static final int CONNECTION_TIME_OUT = 30 * 1000;

    public void getWeChatResponse(final String weChatAppId,
                                  final String weChatAppSecrete,
                                  final String weChatAccessCode,
                                  final WeChatAuthenticationListener
                                          weChatAuthenticationListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    client.setConnectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS);
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("appid", weChatAppId)
                            .add("secret", weChatAppSecrete)
                            .add("code", weChatAccessCode)
                            .add("state", "123456")
                            .add("grant_type", "authorization_code")
                            .build();
                    Request request = new Request.Builder()
                            .url("https://api.weixin.qq.com/sns/oauth2/access_token")
                            .post(formBody)
                            .header("User-Agent", "wechatLoginDemo")
                            .header("Content=Type", "application/x-www-form-urlencoded")
                            .build();
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String jsonResponse = response.body().string();
                    JSONObject jsonObj = new JSONObject(jsonResponse);

                    weChatAuthenticationListener.onSuccess(jsonObj);
                } catch (Exception ex) {
                    RLog.e("WECHAT", ex.toString());
                    weChatAuthenticationListener.onFail();
                }
            }

        }).start();
    }
}


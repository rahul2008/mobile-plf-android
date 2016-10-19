package com.philips.cdp.registration;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by 310190722 on 10/19/2016.
 */
public class HttpClientService extends IntentService{

    private String verifiedMobileNumber;

    public HttpClientService(){
        super("HttpClientService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");

        verifiedMobileNumber = intent.getExtras().getString("verifiedMobileNumber");
        String url  = intent.getExtras().getString("url");
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        RequestBody body = RequestBody.create(mediaType, "verification_code="+verifiedMobileNumber);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("cache-control", "no-cache")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        String responseStr = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            responseStr = response.body().string();

            Bundle b = new Bundle();
            b.putString("responseStr", responseStr);
            receiver.send(0, b);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

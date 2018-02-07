//package com.philips.cdp.registration;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.os.*;
//
//import com.squareup.okhttp.*;
//
//import java.io.IOException;
//import java.net.SocketTimeoutException;
//
//
//public class HttpClientService extends IntentService{
//
//    public static final String HTTP_SERVICE_RESPONSE = "responseStr";
//    public static final String HTTP_RECEIVER = "receiver";
//    public static final String HTTP_BODY_CONTENT = "bodyContent";
//    public static final String HTTP_URL_TO_BE_CALLED = "url";
//    public static final String HTTP_SERVICE_REQUEST_CODE = "requestCode";
//
//    public HttpClientService(){
//        super("HttpClientService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        final ResultReceiver receiver = intent.getParcelableExtra(HTTP_RECEIVER);
//
//        String bodyContent = intent.getExtras().getString(HTTP_BODY_CONTENT);
//        String url  = intent.getExtras().getString(HTTP_URL_TO_BE_CALLED);
//        int requestCode = intent.getExtras().getInt(HTTP_SERVICE_REQUEST_CODE, 0);
//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//
//        RequestBody body = RequestBody.create(mediaType,bodyContent);
//        Request request = null;
//            request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .addHeader("cache-control", "no-cache")
//                    .addHeader("content-type", "application/x-www-form-urlencoded")
//                    .build();
//        String responseStr;
//        Response response;
//        Bundle responseBundle = new Bundle();
//        try {
//            response = client.newCall(request).execute();
//            responseStr = response.body().string();
//            responseBundle.putString(HTTP_SERVICE_RESPONSE, responseStr);
//            receiver.send(requestCode, responseBundle);
//        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
//            responseBundle.putString(HTTP_SERVICE_RESPONSE, null);
//            receiver.send(requestCode, responseBundle);
//            return;
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}

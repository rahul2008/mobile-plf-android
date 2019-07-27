package com.philips.cdp.di.ecs.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.network.NetworkController;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.philips.cdp.di.ecs.util.ECSErrors.getDetailErrorMessage;
import static com.philips.cdp.di.ecs.util.ECSErrors.getErrorMessage;

public class AddProductToCartRequest extends OAuthAppInfraAbstractRequest {

    private final ECSCallback<Boolean,Exception> ecsCallback;

    private final String ctn;

    private final Context mContext;

    public AddProductToCartRequest(String ctn, ECSCallback<Boolean, Exception> ecsCallback, Context mContext) {
        this.ecsCallback = ecsCallback;
        this.ctn = ctn;
        this.mContext = mContext;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getAddToCartUrl();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(getErrorMessage(error),getDetailErrorMessage(error),9999);
    }

    @Override
    public Map<String, String> getHeader() {
        HashMap<String, String> authMap = new HashMap<>();
        authMap.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return authMap;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("code", ctn);
        return jsonParams;
    }

    @Override
    public void onResponse(JSONObject response) {

        if(response!=null && response.length()!=0){
            ecsCallback.onResponse(true);
        }
    }

    private RequestQueue createVolleyRequest(Context context) {

      return  VolleyWrapper.newRequestQueue(context, getHurlStack());
    }

    public HurlStack getHurlStack() {
        return new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(final URL url) throws IOException {
                HttpURLConnection connection = super.createConnection(url);
                connection.setInstanceFollowRedirects(true);
                if (connection instanceof HttpsURLConnection) {
                    if (ECSConfig.INSTANCE.getAccessToken()!=null) {
                        connection.setRequestProperty("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
                    }
                }
                return connection;
            }
        };
    }

    IAPJsonRequest getIapJsonRequest() {
        return new IAPJsonRequest(getMethod(), getURL(),
                getParams(), this::onResponse, this::onErrorResponse);
    }

    public void addToVolleyQueue(final IAPJsonRequest jsonRequest) {
        createVolleyRequest(mContext).add(jsonRequest);
    }

    @Override
    public void executeRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                IAPJsonRequest iapJsonRequest = getIapJsonRequest();
                addToVolleyQueue(iapJsonRequest);

            }
        }).start();

    }

}

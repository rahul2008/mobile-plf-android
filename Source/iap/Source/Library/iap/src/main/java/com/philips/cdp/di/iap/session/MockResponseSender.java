package com.philips.cdp.di.iap.session;

import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.integration.IAPMockInterface;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetProductCatalogRequest;
import com.philips.cdp.di.iap.model.OAuthRequest;
import com.philips.cdp.di.iap.model.SetDeliveryAddressRequest;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by philips on 4/4/19.
 */

public class MockResponseSender {
    private final IAPSettings mIapSettings;

    public MockResponseSender(IAPSettings mIapSettings) {
        this.mIapSettings = mIapSettings;
    }

    void sendMockResponse(AbstractModel model, RequestListener requestListener, int requestCode){
        IAPMockInterface iapMockInterface = mIapSettings.getIapMockInterface();
        Message msg = Message.obtain();
        msg.what = requestCode;
        JSONObject mockJsonObject ;
        msg.obj = NetworkConstants.EMPTY_RESPONSE;

        String encode = null;
        try {
            encode = URLEncoder.encode(model.getUrl(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            new IAPNetworkError(new VolleyError("Mock URL is not correct"), requestCode, requestListener);
            return;
        }

        mockJsonObject = iapMockInterface.GetMockJson(getMethodName(model.getMethod())+encode);

        if(model instanceof GetProductCatalogRequest){

            mockJsonObject = iapMockInterface.GetProductCatalogResponse();
        }

        if(model instanceof SetDeliveryAddressRequest){
            msg.obj = IAPConstant.IAP_SUCCESS;
        }

        if(model instanceof OAuthRequest){
            mockJsonObject = iapMockInterface.OAuthResponse();
        }


        if(mockJsonObject!=null){
            msg.obj = model.parseResponse(mockJsonObject);
        }
        requestListener.onSuccess(msg);
    }

    String getMethodName(int method){

        switch (method){

            case 0:
                return "GET";
            case 1:
                return "POST";
            case 2:
                return "PUT";
            case 3:
                return "DELETE";
            case 4:
                return "HEAD";
            case 5:
                return "OPTIONS";
            case 6:
                return "TRACE";
            case 7:
                return "PATCH";
        }

        return "GET";
    }

}

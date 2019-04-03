/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.integration.IAPMockInterface;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CartAddProductRequest;
import com.philips.cdp.di.iap.model.CartCreateRequest;
import com.philips.cdp.di.iap.model.CartDeleteProductRequest;
import com.philips.cdp.di.iap.model.CartUpdateProductQuantityRequest;
import com.philips.cdp.di.iap.model.ContactCallRequest;
import com.philips.cdp.di.iap.model.CreateAddressRequest;
import com.philips.cdp.di.iap.model.DeleteAddressRequest;
import com.philips.cdp.di.iap.model.DeleteCartRequest;
import com.philips.cdp.di.iap.model.DeleteVoucherRequest;
import com.philips.cdp.di.iap.model.GetAddressRequest;
import com.philips.cdp.di.iap.model.GetAppliedVoucherRequest;
import com.philips.cdp.di.iap.model.GetApplyVoucherRequest;
import com.philips.cdp.di.iap.model.GetCartsRequest;
import com.philips.cdp.di.iap.model.GetCurrentCartRequest;
import com.philips.cdp.di.iap.model.GetDeliveryModesRequest;
import com.philips.cdp.di.iap.model.GetPaymentDetailRequest;
import com.philips.cdp.di.iap.model.GetProductCatalogRequest;
import com.philips.cdp.di.iap.model.GetRegionsRequest;
import com.philips.cdp.di.iap.model.GetRetailersInfoRequest;
import com.philips.cdp.di.iap.model.GetUserRequest;
import com.philips.cdp.di.iap.model.OAuthRequest;
import com.philips.cdp.di.iap.model.PaymentRequest;
import com.philips.cdp.di.iap.model.ProductDetailRequest;
import com.philips.cdp.di.iap.model.RefreshOAuthRequest;
import com.philips.cdp.di.iap.model.SetDeliveryAddressModeRequest;
import com.philips.cdp.di.iap.model.SetDeliveryAddressRequest;
import com.philips.cdp.di.iap.model.SetPaymentDetailsRequest;
import com.philips.cdp.di.iap.model.UpdateAddressRequest;
import com.philips.cdp.di.iap.networkEssential.NetworkEssentials;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class NetworkController {
    protected Context context;
    protected HurlStack mIapHurlStack;
    protected RequestQueue mRequestQueue;
    protected StoreListener mStoreListener;
    protected OAuthListener mOAuthListener;
    protected NetworkEssentials mNetworkEssentials;
    private IAPSettings mIapSettings;

    public NetworkController(Context context) {
        this.context = context;
    }

    void initHurlStack(final Context context) {
        mIapHurlStack = mNetworkEssentials.getHurlStack(context, mOAuthListener);
    }

    public void initStore(Context context, IAPSettings iapSettings) {
        mStoreListener = mNetworkEssentials.getStore(context, iapSettings);
    }

    public void hybrisVolleyCreateConnection(Context context) {
        mRequestQueue = VolleyWrapper.newRequestQueue(context, mIapHurlStack);
    }

    void refreshOAuthToken(RequestListener listener) {
        if (mOAuthListener != null) {
            mOAuthListener.refreshToken(listener);
        }
    }

    public void sendHybrisRequest(final int requestCode, final AbstractModel model,
                                  final RequestListener requestListener) {



        if (mStoreListener == null && requestListener != null) {
            Message message = new Message();
            message.obj = IAPConstant.IAP_ERROR;
            requestListener.onError(message);
            return;
        }

        if(model == null || model.getUrl() == null){


            Message message = new Message();
            message.obj = IAPConstant.IAP_ERROR;
            requestListener.onError(message);
            return;
        }else{
            String encode = null;


                try {
                    encode = URLEncoder.encode(model.getUrl(), "utf-8");

                    Log.d("Pabitra" ,encode);
                    Log.d("pabitra",model.getUrl());


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            if(isMocked()){
                sendMockResponse(model,requestListener,requestCode,encode);
                return;
            }

        }



        if (mStoreListener.isNewUser()) {
            mStoreListener.createNewUser(context);
            mOAuthListener.resetAccessToken();
        }

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {

                if (model.getUrl() != null && error != null) {
                    IAPLog.d(IAPLog.LOG, "Response from sendHybrisRequest onError =" + error
                            .getLocalizedMessage() + " requestCode=" + requestCode + "in " +
                            requestListener.getClass().getSimpleName() + " " + model.getUrl().substring(0, 20));
                }
                if (error != null && error.getMessage() != null) {
                    IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                            IAPAnalyticsConstant.ERROR, error.getMessage());
                }
                if (requestListener != null) {
                    new IAPNetworkError(error, requestCode, requestListener);
                }
            }
        };

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(final JSONObject response) {
                
                if (requestListener != null) {
                    Message msg = Message.obtain();
                    msg.what = requestCode;

                    if (response != null && response.length() == 0) {
                        msg.obj = NetworkConstants.EMPTY_RESPONSE;
                    } else {
                        msg.obj = model.parseResponse(response);
                    }

                    requestListener.onSuccess(msg);


                    //For testing purpose
                    if (model.getUrl() != null) {
                        IAPLog.d(IAPLog.LOG, "Response from sendHybrisRequest onFetchOfProductList =" + msg + " requestCode=" + requestCode + "in " +
                                requestListener.getClass().getSimpleName() + "env = " + " " + model.getUrl().substring(0, 15));
                    }
                }
            }
        };

        IAPJsonRequest iapJsonRequest = getIapJsonRequest(model, error, response);
        addToVolleyQueue(iapJsonRequest);
    }

    IAPJsonRequest getIapJsonRequest(final AbstractModel model, final Response.ErrorListener error, final Response.Listener<JSONObject> response) {
        return new IAPJsonRequest(model.getMethod(), model.getUrl(),
                model.requestBody(), response, error);
    }

    public void addToVolleyQueue(final IAPJsonRequest jsonRequest) {
        mRequestQueue.add(jsonRequest);
    }

    public StoreListener getStore() {
        return mStoreListener;
    }

    public void setNetworkEssentials(NetworkEssentials networkEssentials) {
        this.mNetworkEssentials = networkEssentials;
        initStore(context, mIapSettings);
        mOAuthListener = mNetworkEssentials.getOAuthHandler();

        initHurlStack(context);
        hybrisVolleyCreateConnection(context);
    }

    public void setIapSettings(IAPSettings iapSettings) {
        this.mIapSettings = iapSettings;
    }

    boolean isMocked(){
        IAPMockInterface iapMockInterface = mIapSettings.getIapMockInterface();
        return iapMockInterface.isMockEnabled();
    }


    void sendMockResponse(AbstractModel model, RequestListener requestListener, int requestCode , String encodedUrl){

        IAPMockInterface iapMockInterface = mIapSettings.getIapMockInterface();
        Message msg = Message.obtain();
        msg.what = requestCode;
        msg.obj = NetworkConstants.EMPTY_RESPONSE;
        JSONObject mockJsonObject = iapMockInterface.GetMockJson(getMethodName(model.getMethod())+encodedUrl);

            if(model instanceof GetProductCatalogRequest){

                mockJsonObject = iapMockInterface.GetProductCatalogResponse();
            }

        if(model instanceof OAuthRequest){
            mockJsonObject = iapMockInterface.OAuthResponse();
        }
          /*  if(model instanceof CartAddProductRequest){
                mockJsonObject = iapMockInterface.CartAddProductResponse();
            }

            if(model instanceof CartCreateRequest){
                mockJsonObject = iapMockInterface.CartCreateResponse();
            }
            if(model instanceof CartDeleteProductRequest){
                mockJsonObject = iapMockInterface.CartDeleteProductResponse();
            }
            if(model instanceof CartUpdateProductQuantityRequest){
                mockJsonObject = iapMockInterface.CartUpdateProductQuantityResponse();
            }
            if(model instanceof ContactCallRequest){
                mockJsonObject = iapMockInterface.ContactCallResponse();
            }
            if(model instanceof CreateAddressRequest){
                mockJsonObject = iapMockInterface.CreateAddressResponse();
            }
            if(model instanceof DeleteAddressRequest){
                mockJsonObject = iapMockInterface.DeleteAddressResponse();
            }
            if(model instanceof DeleteCartRequest){
                mockJsonObject = iapMockInterface.DeleteCartResponse();
            }
            if(model instanceof DeleteVoucherRequest){
                mockJsonObject = iapMockInterface.DeleteVoucherResponse();
            }
            if(model instanceof GetAddressRequest){
                mockJsonObject = iapMockInterface.GetAddressResponse();
            }
            if(model instanceof GetAppliedVoucherRequest){
                mockJsonObject = iapMockInterface.GetAppliedVoucherResponse();
            }
            if(model instanceof GetApplyVoucherRequest){
                mockJsonObject = iapMockInterface.GetApplyVoucherResponse();
            }
            if(model instanceof GetCartsRequest){
                mockJsonObject = iapMockInterface.GetCartsResponse();
            }
            if(model instanceof GetCurrentCartRequest){
                mockJsonObject = iapMockInterface.GetCurrentCartResponse();
            }
            if(model instanceof GetDeliveryModesRequest){
                mockJsonObject = iapMockInterface.GetDeliveryModesResponse();
            }
            if(model instanceof GetPaymentDetailRequest){
                mockJsonObject = iapMockInterface.GetPaymentDetailResponse();
            }
            if(model instanceof GetProductCatalogRequest){
                mockJsonObject = iapMockInterface.GetProductCatalogResponse();
            }
            if(model instanceof GetRegionsRequest){
                mockJsonObject = iapMockInterface.GetRegionsResponse();
            }
            if(model instanceof GetRetailersInfoRequest){
                mockJsonObject = iapMockInterface.GetRetailersInfoResponse();
            }
            if(model instanceof GetUserRequest){
                mockJsonObject = iapMockInterface.GetUserResponse();
            }
            if(model instanceof OAuthRequest){
                mockJsonObject = iapMockInterface.OAuthResponse();
            }
            if(model instanceof PaymentRequest){
                mockJsonObject = iapMockInterface.PaymentResponse();
            }
            if(model instanceof ProductDetailRequest){
                mockJsonObject = iapMockInterface.ProductDetailResponse();
            }
            if(model instanceof RefreshOAuthRequest){
                mockJsonObject = iapMockInterface.RefreshOAuthResponse();
            }
            if(model instanceof SetDeliveryAddressModeRequest){
                mockJsonObject = iapMockInterface.SetDeliveryAddressModeResponse();
            }
            if(model instanceof SetDeliveryAddressRequest){
                mockJsonObject = iapMockInterface.SetDeliveryAddressResponse();
            }
            if(model instanceof SetPaymentDetailsRequest){
                mockJsonObject = iapMockInterface.SetPaymentDetailsResponse();
            }
            if(model instanceof UpdateAddressRequest){
                mockJsonObject = iapMockInterface.UpdateAddressResponse();
            }
            if(mockJsonObject!=null){
                msg.obj = model.parseResponse(mockJsonObject);
            }*/

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

           return "PUT";
        }

}
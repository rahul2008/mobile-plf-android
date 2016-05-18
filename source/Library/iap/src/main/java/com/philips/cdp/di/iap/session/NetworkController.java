package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.tagging.Tagging;

import org.json.JSONObject;

public class NetworkController {
    HurlStack mIAPHurlStack;
    RequestQueue hybrisVolleyQueue;
    Context context;
    StoreSpec store;
    OAuthHandler oAuthHandler;

    NetworkEssentials mNetworkEssentials;


    public NetworkController(Context context) {
        this(context, null);
    }

    public NetworkController(Context context, NetworkEssentials essentials) {
        this.context = context;
        mNetworkEssentials = essentials;
        initStore();
        oAuthHandler = essentials.getOAuthHandler();
        initHurlStack(context);
        hybrisVolleyCreateConnection(context);
    }

    void initHurlStack(final Context context) {
        mIAPHurlStack = mNetworkEssentials.getHurlStack(context, oAuthHandler);
//        mIAPHurlStack.setContext(context);
    }

    void initStore() {
        store = mNetworkEssentials.getStore(context);
    }

    public void hybrisVolleyCreateConnection(Context context) {
        hybrisVolleyQueue = VolleyWrapper.newRequestQueue(context, mIAPHurlStack);
    }

    void refreshOAuthToken(RequestListener listener) {
        if (oAuthHandler != null) {
            oAuthHandler.refreshToken(listener);
        }
    }

    public void sendHybrisRequest(final int requestCode, final AbstractModel model, final
    RequestListener requestListener) {
        if (store.isUserLoggedOut()) {
            store.setNewUser(context);
            oAuthHandler.resetAccessToken();
        }

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                IAPLog.d(IAPLog.LOG, "Response from sendHybrisRequest onError =" + error
                        .getLocalizedMessage() + " requestCode=" + requestCode + "in " +
                        requestListener.getClass().getSimpleName());
                if (error != null && error.getMessage() != null)
                    Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                            IAPAnalyticsConstant.ERROR, error.getMessage());
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
                    IAPLog.d(IAPLog.LOG, "Response from sendHybrisRequest onSuccess =" + msg + " requestCode=" + requestCode + "in " +
                            requestListener.getClass().getSimpleName());
                }
            }
        };

        IAPJsonRequest jsObjRequest = getIapJsonRequest(model, error, response);
        addToVolleyQueue(jsObjRequest);
    }

    IAPJsonRequest getIapJsonRequest(final AbstractModel model, final Response.ErrorListener error, final Response.Listener<JSONObject> response) {
        return new IAPJsonRequest(model.getMethod(), model.getUrl(),
                    model.requestBody(), response, error);
    }

    public void addToVolleyQueue(final IAPJsonRequest jsObjRequest) {
        hybrisVolleyQueue.add(jsObjRequest);
    }

    public StoreSpec getStore() {
        return store;
    }

}
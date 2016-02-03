package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CartModel;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.DebugUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class NetworkController {
    RequestQueue hybirsVolleyQueue;
    Context context;
    RequestQueue prxVolleyQueue;
    private Store store;
    String hostPort;
    String webRoot;
    private static OAuthHandler oAuthHandler;


    NetworkController(Context context) {
        this.context = context;
        hybirsVolleyQueue = Volley.newRequestQueue(context, new HurlStack(null, buildSslSocketFactory(context)));
        prxVolleyQueue = Volley.newRequestQueue(context);
    }

    //Package level access
    void initStore(String userName, String janRainID) {
        store = new Store(context, hostPort,webRoot,userName,janRainID);
        store.setAuthHandler(oAuthHandler);
    }

    public void sendPRXRequest(int requestCode, final RequestListener requestListener) {
        AbstractModel model = getModel(requestCode);
        prxVolleyQueue.add(createRequest(requestCode, model,requestListener));
    }

    public void sendHybrisRequest(int requestCode, final RequestListener requestListener) {
        AbstractModel model = getModel(requestCode);
        hybirsVolleyQueue.add(createRequest(requestCode, model, requestListener));
    }

    private JsonObjectRequest createRequest(final int requestCode, final AbstractModel model , final RequestListener requestListener) {

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Message msg = Message.obtain();
                msg.what = requestCode;
                requestListener.onError(msg);
            }
        };

        Response.Listener response = new Response.Listener<JsonObject>() {

            @Override
            public void onResponse(final JsonObject response) {
                Message msg = Message.obtain();
                msg.what = requestCode;
                msg.obj = model.parseResponse(response);
                requestListener.onSuccess(msg);
            }
        };

        String url = getTargetUrl(model,requestCode);
        return new JsonObjectRequest(model.getMethod(requestCode), url,
                getJsonParams(model.requestBody()), response, error);
    }

    private String getTargetUrl(AbstractModel model, int requestCode) {
        if(DebugUtils.TEST_MODE) {
            return model.getTestUrl(requestCode);
        }
        return model.getUrl(requestCode);
    }

    //Add model specific implementation
    private AbstractModel getModel(final int requestCode) {
        switch(requestCode){
            case RequestCode.GET_CART:
                return new CartModel(store);
            default:
                return null;
        }
    }

    private SSLSocketFactory buildSslSocketFactory(Context context) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = context.getResources().getAssets().open("test.crt");
            InputStream caInput = new BufferedInputStream(is);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] mngrs = new TrustManager[]{new TestTrustManager()};//tmf.getTrustManagers();
            sslContext.init(null, mngrs, null);
            return sslContext.getSocketFactory();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Forms the json object with the payload passed
     * @param mParams payload bundle
     * @return JsonObject
     */
    private JSONObject getJsonParams(Bundle mParams) {
        JSONObject params = null;

        try {
            if (mParams != null) {
                Set<String> keys = mParams.keySet();

                if (keys.size() > 0) {
                    params = new JSONObject();

                    for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
                        String key = (String) iterator.next();
                        String value = mParams.getString(key);
                        params.put(key, value);
                    }

                    return params;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    private static class TestTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
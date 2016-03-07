package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class NetworkController {
    RequestQueue hybirsVolleyQueue;
    Context context;
    private Store store;
    String hostPort;
    String webRoot;
    private OAuthHandler oAuthHandler;

    public NetworkController(Context context, OAuthHandler oAuthHandler) {
        this.context = context;
        this.oAuthHandler = oAuthHandler;
        hybrisVolleyCreateConnection(context);
    }

    public void hybrisVolleyCreateConnection(Context context) {
        hybirsVolleyQueue = Volley.newRequestQueue(context,getTestEnvHurlStack(context));
    }

    //Package level access

    void initStore(Context context, String userName, String janRainID) {
        store = new Store(context, hostPort, webRoot, userName, janRainID);
        store.setAuthHandler(oAuthHandler);
    }

    public void sendHybrisRequest(final int requestCode, final AbstractModel model, final
    RequestListener requestListener) {
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                IAPLog.d(IAPLog.LOG, "Response from sendHybrisRequest onError =" + error.getLocalizedMessage());
                if (requestListener != null) {
                    Message msg = Message.obtain();
                    msg.what = requestCode;
                    msg.obj = new IAPNetworkError(error);
                    requestListener.onError(msg);
                }
            }
        };

        Response.Listener response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(final JSONObject response) {
                if (requestListener != null) {
                    Message msg = Message.obtain();
                    msg.what = requestCode;

                    if(response != null && response.length() == 0){
                        msg.obj = NetworkConstants.EMPTY_RESPONSE;
                    }else{
                        msg.obj = model.parseResponse(response);
                    }

                    requestListener.onSuccess(msg);
                    IAPLog.d(IAPLog.LOG, "Response from sendHybrisRequest onSuccess =" + msg);
                }
            }
        };

        IAPJsonRequest jsObjRequest = new IAPJsonRequest(model.getMethod(), getTargetUrl(model),
                model.requestBody(), response, error);
        addToVolleyQueue(jsObjRequest);
    }

    public void addToVolleyQueue(final IAPJsonRequest jsObjRequest) {
        hybirsVolleyQueue.add(jsObjRequest);
    }

    /**
     * @param model
     * @return Url String
     */
    private String getTargetUrl(AbstractModel model) {
        if (IAPConstant.TEST_MODE) {
            return model.getTestUrl();
        }
        return model.getProductionUrl();
    }

    public SSLSocketFactory buildSslSocketFactory(Context context) {
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

    private HurlStack getTestEnvHurlStack(Context context) {
       return new HurlStack(null, buildSslSocketFactory(context)) {
            @Override
            protected HttpURLConnection createConnection(final URL url) throws IOException {
                HttpURLConnection connection = super.createConnection(url);
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(final String hostname, final SSLSession session) {
                            return hostname.contains("philips.com");
                        }
                    });
                connection.setRequestProperty("Authorization", "Bearer " + store.getAuthToken());
                }
                return connection;
            }
       };
    }

    public Store getStore() {
        return store;
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
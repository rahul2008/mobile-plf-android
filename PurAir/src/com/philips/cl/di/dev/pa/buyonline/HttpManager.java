package com.philips.cl.di.dev.pa.buyonline;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;


class HttpManager {

    private final static int MAX_TOTAL_CONNECTIONS = 4;
    private final static int WAIT_TIMEOUT = 20000;
    private final static int CONNECT_TIMEOUT = 10000;
    private final static int READ_TIMEOUT = 7000;

    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

    private static HashMap<String, ApsHttpClient> shttpClients = new HashMap<String, ApsHttpClient>(2);
    
    public static ApsHttpClient getHttpClient(String tag){
        ApsHttpClient apsHttpClient = shttpClients.get(tag);
        if (null == apsHttpClient) {
            apsHttpClient = new ApsHttpClient(createHttpClient(),tag); 
        }
        return apsHttpClient;
    }
    
    private static DefaultHttpClient createHttpClient(){
        final HttpParams httpParams = new BasicHttpParams();

        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTIONS);

        ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);

        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);

        HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
        HttpConnectionParams.setStaleCheckingEnabled(httpParams, true);
        HttpClientParams.setRedirecting(httpParams, true);
        HttpProtocolParams.setUserAgent(httpParams, "Android client");

        HttpConnectionParams.setTcpNoDelay(httpParams, true);

        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            schemeRegistry.register(new Scheme("https", sf, 443));
        } catch (Exception ex) {
            // do nothing
        }

        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(manager, httpParams);
        defaultHttpClient.setHttpRequestRetryHandler( new HttpRequestRetryHandler() { 
            public boolean retryRequest(IOException exception, int executionCount, 
                                        HttpContext context) { 
                if (executionCount >= 2) { 
                    return false; 
                } 
                if (exception instanceof NoHttpResponseException) { 
                    return true; 
                } 
                if (exception instanceof SSLHandshakeException) { 
                    return false; 
                } 
                HttpRequest request = (HttpRequest) context 
                        .getAttribute(ExecutionContext.HTTP_REQUEST); 
                boolean idempotent = (request instanceof HttpEntityEnclosingRequest); 
                if (!idempotent) { 
                    return true; 
                } 
                return false; 
            } 
        });

        defaultHttpClient.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1) {
                    keepAlive = 30000;
                }
                return keepAlive;
            }
        });
        return defaultHttpClient;
    }

    private HttpManager() {}

    private static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] {tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}

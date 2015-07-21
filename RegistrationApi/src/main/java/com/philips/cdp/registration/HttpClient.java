
package com.philips.cdp.registration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class HttpClient {

	final int TIME_OUT = 30000;

	private String ACCESS_TOKEN_HEADER = "x-accessToken";

	private String CONTENT_TYPE_HEADER = "Content-Type";

	private String CONTENT_TYPE = "application/x-www-form-urlencoded";

	private String LOG_TAG = "HttpClient";

	// ----- Post Method
	public String postData(String url, List<NameValuePair> nameValuePairs, String accessToken) {

		DefaultHttpClient httpClient = getHttpClient();

		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader(ACCESS_TOKEN_HEADER, accessToken);
			httppost.setHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse httpResponse = httpClient.execute(httppost);
			InputStream inputStream = httpResponse.getEntity().getContent();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}
			Log.i(LOG_TAG, "Returninge of doInBackground :" + stringBuilder.toString());
			return stringBuilder.toString();
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return null;
	}

	// ---- Get Method
	public String connectWithHttpGet(String url, String accessToken) {

		DefaultHttpClient httpClient = getHttpClient();

		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader(ACCESS_TOKEN_HEADER, accessToken);

		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}
			Log.i(LOG_TAG, "Returninge of doInBackground :" + stringBuilder.toString());
			return stringBuilder.toString();

		} catch (ClientProtocolException cpe) {
			Log.e(LOG_TAG, "Exception related to httpResponse :" + cpe);
		} catch (IOException ioe) {
			Log.e(LOG_TAG, "IOException related to httpResponse :" + ioe);
		}

		return null;
	}

	public DefaultHttpClient getHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new HttpsSocketFactory(trustStore);

			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	class HttpsSocketFactory extends SSLSocketFactory {

		private SSLContext mSslContext = SSLContext.getInstance("TLS");

		public HttpsSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
		        KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain, String authType)
				        throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType)
				        throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			mSslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
		        throws IOException, UnknownHostException {
			return mSslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket()
		        throws IOException {
			return mSslContext.getSocketFactory().createSocket();
		}
	}

	public String request(HttpResponse response) {
		String result = null;
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream in = null;
			BufferedReader reader = null;
			try {
				in = response.getEntity().getContent();
				reader = new BufferedReader(new InputStreamReader(in));
				StringBuilder str = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					str.append(line + "\n");
					line = null;
				}
				result = str.toString();
			} catch (Exception ex) {
				result = null;
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				in = null;
				if (reader != null)
					try {
						reader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				reader = null;
			}
		}
		return result;
	}
}

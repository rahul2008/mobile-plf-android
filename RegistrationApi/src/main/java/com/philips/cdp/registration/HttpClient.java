
package com.philips.cdp.registration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import android.support.v4.util.Pair;
import android.util.Log;


public class HttpClient {

	final int TIME_OUT = 30000;

	private String ACCESS_TOKEN_HEADER = "x-accessToken";

	private String CONTENT_TYPE_HEADER = "Content-Type";

	private String REQUEST_METHOD_POST = "POST";

	private String REQUEST_METHOD_GET = "GET";

	private String CONTENT_TYPE = "application/x-www-form-urlencoded";

	private String LOG_TAG = "HttpClient";

	// ----- Post Method
	/*public String postData(String url, List<NameValuePair> nameValuePairs, String accessToken) {

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
	}*/

	public String callPost(String urlString, List<Pair<String, String>> nameValuePairs, String accessToken){
		URL url = null;
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;
		BufferedReader bufferedReader = null;
		StringBuilder inputResponse = null;
		try {
			url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			connection.setRequestProperty(ACCESS_TOKEN_HEADER, accessToken);
			connection.setRequestProperty(CONTENT_TYPE_HEADER, CONTENT_TYPE);
			connection.setRequestMethod(REQUEST_METHOD_POST);
			javax.net.ssl.SSLSocketFactory sf = createSslSocketFactory();
			connection.setSSLSocketFactory(sf);
			connection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			outputStream = connection.getOutputStream();
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
			Log.i(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection input params" + getPostString(nameValuePairs));
			bufferedWriter.write(getPostString(nameValuePairs));
			int responseCode = connection.getResponseCode();


			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String input;
			inputResponse = new StringBuilder();
			if(responseCode == HttpsURLConnection.HTTP_OK) {
				while ((input = bufferedReader.readLine()) != null) {
					inputResponse.append(input);
				}
			}else {
				Log.i(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection response error with code" + responseCode);
			}


		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection" );
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
				outputStream.close();
				bufferedReader.close();
			} catch (IOException e) {
				Log.e(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection" + e.getMessage());
				e.printStackTrace();
			}

		}
		return inputResponse.toString();

	}

	private String getPostString(List<Pair<String, String>> nameValuePairs){
		StringBuilder postString = new StringBuilder();
		boolean firstItem = true;

		for (Pair pair : nameValuePairs)
		{
			if (firstItem)
				firstItem = false;
			else {
				postString.append("&");
			}
			String s = (String)pair.first;
			try {
				postString.append(URLEncoder.encode((String) pair.first, "UTF-8"));
				postString.append("=");
				postString.append(URLEncoder.encode((String) pair.second, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return postString.toString();
	}


	public String callGet(String urlString, String accessToken){
		URL url = null;
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;
		BufferedReader bufferedReader = null;
		StringBuilder inputResponse =  new StringBuilder();
		try{
			url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			connection.setRequestProperty(ACCESS_TOKEN_HEADER, accessToken);
			connection.setRequestProperty(CONTENT_TYPE_HEADER, CONTENT_TYPE);
			connection.setRequestMethod(REQUEST_METHOD_GET);
			javax.net.ssl.SSLSocketFactory sf = createSslSocketFactory();
			connection.setSSLSocketFactory(sf);
			connection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});


			int responseCode = connection.getResponseCode();
			Log.i(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection response code" + responseCode);

			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String input;
			inputResponse = new StringBuilder();

			while((input = bufferedReader.readLine()) != null){
				inputResponse.append(input);
			}

		}catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection" + e.getMessage());
			e.printStackTrace();
		} catch(Exception e){

		}finally {
			try {
				if(bufferedWriter != null) {
					bufferedWriter.close();
				}
				if(outputStream != null) {
					outputStream.close();
				}
				if(bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				Log.e(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection" + e.getMessage());
				e.printStackTrace();
			}

		}

		return inputResponse.toString();

	}


	// ---- Get Method
	/*public String connectWithHttpGet(String url, String accessToken) {

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
	}*/

	/*public DefaultHttpClient getHttpClient() {
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
	}*/

	private  javax.net.ssl.SSLSocketFactory createSslSocketFactory() throws Exception {
		TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}
		} };
		SSLContext sslContext = SSLContext.getInstance("TLS");

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
		sslContext.init(new KeyManager[0], new TrustManager[] { tm }, new SecureRandom());
		return sslContext.getSocketFactory();
	}



	/*class HttpsSocketFactory extends SSLSocketFactory {

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
	}*/

	/*public String request(HttpResponse response) {
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
	}*/
}

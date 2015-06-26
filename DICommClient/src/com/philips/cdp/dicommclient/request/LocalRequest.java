/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.util.DLog;
import com.philips.cdp.dicommclient.util.DICommContext;

public class LocalRequest extends Request {

	private static final int CONNECTION_TIMEOUT = 10 * 1000; // 10secs
	private static final int GETWIFI_TIMEOUT = 3 * 1000; // 3secs
	public static final String BASEURL_PORTS = "http://%s/di/v%d/products/%d/%s";
	private final String mUrl;
	private final LocalRequestType mRequestType;
	private final DISecurity mDISecurity;

	public LocalRequest(NetworkNode networkNode, String portName, int productId, LocalRequestType requestType,Map<String,Object> dataMap,
			ResponseHandler responseHandler, DISecurity diSecurity) {
	    super(networkNode, dataMap, responseHandler);
        mUrl = createPortUrl(networkNode.getIpAddress(),networkNode.getDICommProtocolVersion(),portName,productId);
		mRequestType = requestType;
		mDISecurity = diSecurity;
	}

	private String createPortUrl(String ipAddress, int dicommProtocolVersion, String portName, int productId){
		return String.format(BASEURL_PORTS, ipAddress,dicommProtocolVersion, productId, portName);
	}

	private String createDataToSend(NetworkNode networkNode, Map<String,Object> dataMap){
		if (dataMap == null || dataMap.size() <= 0) return null;

		String data = Request.convertKeyValuesToJson(dataMap);
		DLog.i(DLog.LOCALREQUEST, "Data to send: "+ data);

		if (mDISecurity != null) {
			return mDISecurity.encryptData(data, networkNode);
		}
		DLog.i(DLog.LOCALREQUEST, "Not encrypting data");
		return data;
	}

	@Override
	public Response execute() {
		DLog.d(DLog.LOCALREQUEST, "Start request LOCAL");
		DLog.i(DLog.LOCALREQUEST, "Url: " + mUrl + ", Requesttype: " + mRequestType);
		String result = "";
		InputStream inputStream = null;
		OutputStreamWriter out = null;
		HttpURLConnection conn = null;
		int responseCode = -1;

		try {
			URL urlConn = new URL(mUrl);
			conn = LocalRequest.createConnection(urlConn, mRequestType.getMethod(), CONNECTION_TIMEOUT,GETWIFI_TIMEOUT);
			if (conn == null) {
			    DLog.e(DLog.LOCALREQUEST, "Request failed - no wificonnection available");
			    return new Response(null, Error.NOWIFIAVAILABLE, mResponseHandler);
			}

			if (mRequestType == LocalRequestType.PUT || mRequestType == LocalRequestType.POST) {
				if (mDataMap == null || mDataMap.isEmpty()) {
				    DLog.e(DLog.LOCALREQUEST, "Request failed - no data for Put or Post");
					return new Response(null, Error.NODATA, mResponseHandler);
				}
				out = appendDataToRequestIfAvailable(conn);
			} else if (mRequestType == LocalRequestType.DELETE) {
				appendDataToRequestIfAvailable(conn);
			}
			conn.connect();

			try {
				responseCode = conn.getResponseCode();
			} catch (Exception e) {
				responseCode = HttpURLConnection.HTTP_BAD_GATEWAY;
				DLog.e(DLog.LOCALREQUEST, "Failed to get responsecode");
				e.printStackTrace();
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = conn.getInputStream();
				return handleHttpOk(inputStream);
			}
			else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
				inputStream = conn.getErrorStream();
				return handleBadRequest(inputStream);
			}
			else if(responseCode == HttpURLConnection.HTTP_BAD_GATEWAY){
				return new Response(null, Error.BADGATEWAY, mResponseHandler);
			}
			else{
				inputStream = conn.getErrorStream();
				result = convertInputStreamToString(inputStream);
				DLog.e(DLog.LOCALREQUEST, "REQUESTFAILED - " +result);
				return new Response(result, Error.REQUESTFAILED, mResponseHandler);
			}
		} catch (IOException e) {
			e.printStackTrace();
			DLog.e(DLog.LOCALREQUEST, e.getMessage() != null ? e.getMessage() : "IOException");
			return new Response(null, Error.IOEXCEPTION, mResponseHandler);
		}
		finally {
			closeAllConnections(inputStream, out, conn);
			DLog.d(DLog.LOCALREQUEST, "Stop request LOCAL - responsecode: " + responseCode);
		}
	}

    private Response handleHttpOk(InputStream inputStream) throws IOException, UnsupportedEncodingException {
        String cypher = convertInputStreamToString(inputStream);
        if (cypher == null) {
            DLog.e(DLog.LOCALREQUEST, "Request failed - null reponse");
            return new Response(null, Error.REQUESTFAILED, mResponseHandler) ;
        }

        String data = decryptData(cypher);
        if (data == null) {
        	DLog.e(DLog.LOCALREQUEST, "Request failed - failed to decrypt");
        	return new Response(null, Error.REQUESTFAILED, mResponseHandler) ;
        }

        DLog.i(DLog.LOCALREQUEST, "Received data: " + data);
        return new Response(data, null, mResponseHandler);
    }

	private Response handleBadRequest(InputStream inputStream) throws IOException, UnsupportedEncodingException {
		String errorMessage = convertInputStreamToString(inputStream);
		DLog.e(DLog.LOCALREQUEST, "BAD REQUEST - " + errorMessage);

		if (mDISecurity != null) {
			DLog.e(DLog.LOCALREQUEST, "Request not properly encrypted - notifying listener");
			mDISecurity.notifyEncryptionFailedListener(mNetworkNode);
		}

		return new Response(errorMessage, Error.BADREQUEST, mResponseHandler);
	}

    private String decryptData(String cypher) {
        if (mDISecurity != null) {
            return mDISecurity.decryptData(cypher, mNetworkNode);
        }
        return cypher;
    }

	private OutputStreamWriter appendDataToRequestIfAvailable(HttpURLConnection conn) throws IOException {
	    String data = createDataToSend(mNetworkNode, mDataMap);
	    if (data == null) return null;

		OutputStreamWriter out;
		conn.setDoOutput(true);
		out = new OutputStreamWriter(conn.getOutputStream(),Charset.defaultCharset());
		out.write(data);
		out.flush();
		return out;
	}

	@SuppressLint("NewApi")
	private static HttpURLConnection createConnection(URL url, String requestMethod, int connectionTimeout, int lockTimeout) throws IOException {

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Network wifiNetworkForSocket = LocalRequest.getWifiNetworkForSocket(DICommContext.getContext(),lockTimeout);

			if (wifiNetworkForSocket == null) {
				return null;
			}
			conn = (HttpURLConnection) wifiNetworkForSocket.openConnection(url);
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		conn.setRequestProperty("content-type", "application/json") ;
		conn.setRequestMethod(requestMethod);
		if( connectionTimeout != -1) {
			conn.setConnectTimeout(connectionTimeout) ;
		}
		return conn ;
	}

	@SuppressLint("NewApi")
	private static Network getWifiNetworkForSocket(Context context, int lockTimeout) {
		ConnectivityManager connectionManager =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkRequest.Builder request = new NetworkRequest.Builder();
		request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
		//request.addCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P);

		final Object lock = new Object();
		WifiNetworkCallback networkCallback = new WifiNetworkCallback(lock);

		synchronized (lock) {
			connectionManager.registerNetworkCallback(request.build(),	networkCallback);
			try {
				lock.wait(lockTimeout);
				Log.e(DLog.WIFI, "Timeout error occurred");
			} catch (InterruptedException e) {
			}
		}
		connectionManager.unregisterNetworkCallback(networkCallback);
		return networkCallback.getNetwork();
	}

	/**
	 * Reads an InputStream and converts it to a String.
	 *
	 * @param  inputStream	Input stream to convert to string
	 * @return	Returns 	converted string
	 */
	private static String convertInputStreamToString(InputStream inputStream) throws IOException, UnsupportedEncodingException {
		if (inputStream == null) return "";
		Reader reader = new InputStreamReader(inputStream, "UTF-8");

		int len = 1024;
		char[] buffer = new char[len];
		StringBuilder sb = new StringBuilder(len);
		int count;

		while ((count = reader.read(buffer)) > 0) {
			sb.append(buffer, 0, count);
		}

		return sb.toString();
	}

	private static final void closeAllConnections(InputStream is, OutputStreamWriter out, HttpURLConnection conn) {
		if(is != null ) {
			try {
				is.close() ;
				is = null ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if( out != null ) {
			try {
				out.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
			out = null ;
		}
		if ( conn != null ) {
			conn.disconnect() ;
			conn = null ;
		}
	}
}
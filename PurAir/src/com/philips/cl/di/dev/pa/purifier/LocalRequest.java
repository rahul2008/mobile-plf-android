package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.LocalRequestType;
import com.philips.cl.di.dicomm.communication.Request;
import com.philips.cl.di.dicomm.communication.Response;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

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

		String data = convertKeyValuesToJson(dataMap);
		ALog.i(ALog.LOCALREQUEST, "Data to send: "+ data);

		if (mDISecurity != null) {
			return mDISecurity.encryptData(data, networkNode);
		}
		ALog.i(ALog.LOCALREQUEST, "Not encrypting data");
		return data;
	}

	@Override
	public Response execute() {
		ALog.d(ALog.LOCALREQUEST, "Start request LOCAL " + mNetworkNode.hashCode());
		ALog.i(ALog.LOCALREQUEST, "Url: " + mUrl + ", Requesttype: " + mRequestType);
		String result = "";
		InputStream inputStream = null;
		OutputStreamWriter out = null;
		HttpURLConnection conn = null;
		int responseCode;
		
		try {
			URL urlConn = new URL(mUrl);
			conn = NetworkUtils.getConnection(urlConn, mRequestType.getMethod(), CONNECTION_TIMEOUT,GETWIFI_TIMEOUT);
			if (mRequestType == LocalRequestType.PUT || mRequestType == LocalRequestType.POST) {
				if (mDataMap == null || mDataMap.isEmpty()) {
				    ALog.e(ALog.LOCALREQUEST, "Request failed - no data for Put or Post");
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
				ALog.e(ALog.LOCALREQUEST, "Failed to get responsecode");
				e.printStackTrace();
			}

			ALog.d(ALog.LOCALREQUEST, "Stop request LOCAL - responsecode: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = conn.getInputStream();
				return handleHttpOk(inputStream);
			}
			else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
				inputStream = conn.getErrorStream();
				result = NetworkUtils.convertInputStreamToString(inputStream);
				ALog.e(ALog.LOCALREQUEST, "BAD REQUEST - " +result);
				return new Response(result, Error.BADREQUEST, mResponseHandler);
			}
			else if(responseCode == HttpURLConnection.HTTP_BAD_GATEWAY){
				return new Response(null, Error.BADGATEWAY, mResponseHandler);
			}
			else{
				inputStream = conn.getErrorStream();
				result = NetworkUtils.convertInputStreamToString(inputStream);
				ALog.e(ALog.LOCALREQUEST, "REQUESTFAILED - " +result);
				return new Response(result, Error.REQUESTFAILED, mResponseHandler);
			}
		} catch (IOException e) {
			e.printStackTrace();
			ALog.e(ALog.LOCALREQUEST, e.getMessage() != null ? e.getMessage() : "IOException");
			return new Response(null, Error.IOEXCEPTION, mResponseHandler);
		}
		finally {
			NetworkUtils.closeAllConnections(inputStream, out, conn);
		}
	}

    private Response handleHttpOk(InputStream inputStream) throws IOException, UnsupportedEncodingException {
        String cypher = NetworkUtils.convertInputStreamToString(inputStream);
        if (cypher == null) {
            ALog.e(ALog.LOCALREQUEST, "Request failed - null reponse");
            return new Response(null, Error.REQUESTFAILED, mResponseHandler) ;
        }
        
        String data = decryptData(cypher);
        if (data == null) {
        	ALog.e(ALog.LOCALREQUEST, "Request failed - failed to decrypt");
        	return new Response(null, Error.REQUESTFAILED, mResponseHandler) ;
        }
        
        return new Response(data, null, mResponseHandler);
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
}
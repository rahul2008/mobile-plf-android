package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.Request;
import com.philips.cl.di.dicomm.communication.RequestType;
import com.philips.cl.di.dicomm.communication.Response;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

public class LocalRequest implements Request {

	private static final int CONNECTION_TIMEOUT = 10 * 1000; // 10secs
	public static final String BASEURL_PORTS = "http://%s/di/v%d/products/%s/%d";
	private final String mUrl;
	private final String mData;
	private final NetworkNode mNetworkNode;
	private final RequestType mRequestType;
	private final ResponseHandler mResponseHandler;

	public LocalRequest(NetworkNode networkNode, String portName, int productId, RequestType requestType,Map<String,String> dataMap,ResponseHandler responseHandler) {
		mUrl = createPortUrl(networkNode.getIpAddress(),networkNode.getDICommProtocolVersion(),portName,productId);
		mData = convertDataToJson(networkNode,dataMap);
		mRequestType = requestType;
		mNetworkNode = networkNode;
		mResponseHandler = responseHandler;
		ALog.i("UIUX", "Datatosend: " + mData);
	}
	
	private String createPortUrl(String ipAddress, int dicommProtocolVersion, String portName, int productId){
		return String.format(BASEURL_PORTS, ipAddress,dicommProtocolVersion, portName, productId);
	}
	
	private String convertDataToJson(NetworkNode networkNode, Map<String,String> dataMap){	
		// TODO: DICOMM Refactor, refactor this method
		if (dataMap == null || dataMap.size() <= 0)
			return null;
		StringBuilder builder = new StringBuilder("{");
		Set<String> keySet = dataMap.keySet();
		int index = 1;
		for (String key : keySet) {
			builder.append("\"").append(key).append("\":\"").append(dataMap.get(key)).append("\"");
			if (index < keySet.size()) {
				builder.append(",");
			}
			index++;
		}
		builder.append("}");
		ALog.i(ALog.LOCALREQUEST, builder.toString());
		return new DISecurity(null).encryptData(builder.toString(), networkNode);		
	}

	@Override
	public Response execute() {
		ALog.d(ALog.LOCALREQUEST, "Start request LOCAL");
		String result = "";
		InputStream inputStream = null;
		OutputStreamWriter out = null;
		HttpURLConnection conn = null;
		int responseCode;
		
		try {
			URL urlConn = new URL(mUrl);
			conn = NetworkUtils.getConnection(urlConn, mRequestType.getMethod(), CONNECTION_TIMEOUT);
			if(mRequestType == RequestType.PUT || mRequestType == RequestType.POST) {			
				if (mData == null || mData.isEmpty()) {
					return new Response(null, Error.NODATA, mResponseHandler);
				}
				conn.setDoOutput(true);
				out = new OutputStreamWriter(conn.getOutputStream(),Charset.defaultCharset());
				out.write(mData);
				out.flush();
			}
			conn.connect();
			responseCode = conn.getResponseCode();
			ALog.d(ALog.LOCALREQUEST, "Stop request LOCAL - responsecode: " + responseCode);
			
			if (responseCode == 200) {
				//TODO: DICOMM Refactor, add decryption logic here
				inputStream = conn.getInputStream();
				result = NetworkUtils.convertInputStreamToString(inputStream);
				return new Response(result, null, mResponseHandler);
			}
			else {
				inputStream = conn.getErrorStream();
				result = NetworkUtils.convertInputStreamToString(inputStream);
				ALog.e(ALog.LOCALREQUEST, result);
				return new Response(null, Error.REQUESTFAILED, mResponseHandler);				
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
}
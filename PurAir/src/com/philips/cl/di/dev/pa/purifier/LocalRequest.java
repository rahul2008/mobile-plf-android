package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dicomm.communication.Request;
import com.philips.cl.di.dicomm.communication.RequestType;
import com.philips.cl.di.dicomm.communication.Response;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

public class LocalRequest implements Request {

	private static final int CONN_TIMEOUT_LOCAL_CONTROL = 10 * 1000; // 10secs
	public static final String BASEURL_PORTS = "http://%s/di/v%d/products/%s/%d";
	private String mUrl;
	private int mResponseCode;
	private String mData;
	private NetworkNode mNetworkNode;
	private RequestType mRequestType;

	public LocalRequest(NetworkNode networkNode, String portName, int productId, RequestType requestType,Map<String,String> dataMap,ResponseHandler responseHandler) {
		ALog.i("UIUX", "Datatosend: " + mData);
		ALog.d(ALog.LOCALREQUEST, "Start request LOCAL");
		this.mUrl = createPortUrl(networkNode.getIpAddress(),networkNode.getDICommProtocolVersion(),portName,productId);
		this.mData = convertDataToJson(networkNode,dataMap);
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
		if (mData == null || mData.isEmpty() || mUrl == null
				|| mUrl.isEmpty()) {
			return new Response(null, null, null);
		}
		String result = "";
		InputStream inputStream = null;
		OutputStreamWriter out = null;
		HttpURLConnection conn = null;
		try {
			URL urlConn = new URL(mUrl);
			conn = (HttpURLConnection) NetworkUtils.getConnection(urlConn,
					"PUT", CONN_TIMEOUT_LOCAL_CONTROL);
			conn.setDoOutput(true);
			out = new OutputStreamWriter(conn.getOutputStream(),
					Charset.defaultCharset());
			out.write(mData);
			out.flush();
			conn.connect();
			mResponseCode = conn.getResponseCode();
			if (mResponseCode == 200) {
				inputStream = conn.getInputStream();
				result = NetworkUtils.convertInputStreamToString(inputStream);
			}
			ALog.d(ALog.LOCALREQUEST, "Stop request LOCAL - responsecode: " + mResponseCode);
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			NetworkUtils.closeAllConnections(inputStream, out, conn);
		}
		return new Response(result, null, null);
	}
}
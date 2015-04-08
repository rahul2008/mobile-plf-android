package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.os.AsyncTask;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class LocalSubscription extends AsyncTask<String, Void, String> {
	private String dataToUpload, targerIpAddress;
	private int responseCode;
	private ServerResponseListener listener;
	private String requestMethod;
	
	public LocalSubscription(String dataToUpload, ServerResponseListener listener, String requestMethod) 
	{
		this.listener = listener;
		this.dataToUpload = dataToUpload;
		this.requestMethod = requestMethod ;
	}
	
	@Override
	protected String doInBackground(String... url) {
		String result = "";
		InputStream inputStream = null;
		OutputStreamWriter out = null ;
		HttpURLConnection conn = null ;
		try {
			URL urlConn = new URL(url[0]) ;
			conn = NetworkUtils.getConnection(urlConn,requestMethod, -1,100) ;
			if( conn == null ) return "";
			if(dataToUpload != null && !dataToUpload.isEmpty()) {
				conn.setDoOutput(true) ;
				out = new OutputStreamWriter(conn.getOutputStream(), Charset.defaultCharset());
				out.write(dataToUpload);
				out.flush() ;				
			}			
			targerIpAddress = urlConn.getHost();
			conn.connect();
			responseCode = conn.getResponseCode() ;

			if ( responseCode == 200 ) {
				inputStream = conn.getInputStream();
				result = NetworkUtils.convertInputStreamToString(inputStream);
			}
			else {
				inputStream = conn.getErrorStream();
				result = NetworkUtils.convertInputStreamToString(inputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			ALog.i(ALog.SCHEDULER, "Finally: "+result) ;
			NetworkUtils.closeAllConnections(inputStream, out, conn) ;
			
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if(listener != null )
			listener.receiveServerResponse(responseCode, result, targerIpAddress);
		super.onPostExecute(result);
	}
}

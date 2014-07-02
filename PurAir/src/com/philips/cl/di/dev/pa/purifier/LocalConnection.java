package com.philips.cl.di.dev.pa.purifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.NetworkUtils;
import com.philips.cl.di.dev.pa.util.Utils;

public class LocalConnection extends RoutingStrategy {

	private String url ;
	private int responseCode ;	
	private String dataToSend ;

	public LocalConnection(PurAirDevice purifier, String dataToSend) {
		ALog.i("UIUX","Datatosend: "+ dataToSend) ;
		this.url = Utils.getPortUrl(Port.AIR, purifier.getIpAddress()) ;
		this.dataToSend = dataToSend ;
	}

	@Override
	public String setPurifierDetails() {
		String result = "";
		InputStream inputStream = null;
		OutputStreamWriter out = null ;
		HttpURLConnection conn = null ;
		try {
			URL urlConn = new URL(url);
			conn = (HttpURLConnection) urlConn.openConnection();
			conn.setRequestProperty("content-type", "application/json") ;

			conn.setRequestMethod("PUT");
			conn.setDoOutput(true);
			out = new OutputStreamWriter(conn.getOutputStream(), Charset.defaultCharset());
			out.write(dataToSend);
			out.flush() ;				

			conn.connect();
			responseCode = conn.getResponseCode() ;

			if ( responseCode == 200 ) {
				inputStream = conn.getInputStream();
				result = NetworkUtils.readFully(inputStream);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			if(inputStream != null ) {
				try {
					inputStream.close() ;
					inputStream = null ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
		return result ;
	}
}
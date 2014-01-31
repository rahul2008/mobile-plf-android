package com.philips.cl.di.base;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class DiHelper {
	private static final int connectionTimeoutMillis = 3000;
	private static final int socketTimeoutMillis = 3000;
	static public JSONArray loadDataFromAssets(Context context, int aJsonResource) {
		JSONArray json=null;
		InputStream inputStream = context.getResources().openRawResource(aJsonResource);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int ctr;
		try {
			ctr = inputStream.read();
			while (ctr != -1) {
				byteArrayOutputStream.write(ctr);
				ctr = inputStream.read();
			}
			inputStream.close();
			json = new JSONArray(byteArrayOutputStream.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	static public HttpClient getHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeoutMillis);
		// Set the default socket timeout in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, socketTimeoutMillis);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		return httpClient;
	}

	private static String getRequestOnUrls(String[] stringUrls) {
		String result = null;
			result = getRequestOnUrl(stringUrls[0]);
			if (result==null){
				result = getRequestOnUrl(stringUrls[1]);
			}
			return result;
	}
	
	public static String getRequestOnUrl(String stringUrl) {
		String result = null;
		HttpClient httpclient = DiHelper.getHttpClient();
		HttpGet aqiEndpoint = new HttpGet(stringUrl);

       	HttpParams params = aqiEndpoint.getParams();
        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        params.setLongParameter(ConnManagerPNames.TIMEOUT, 3000);
        aqiEndpoint.setParams(params);
		try {
		HttpResponse response = httpclient.execute(aqiEndpoint);
		if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
		} else {
			//throw Network exception 
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getRequestOnUrlsWithNameValuePair(String[] stringUrls, List<NameValuePair> nameValuePairs) {
		String result = null;
			String finalUrlString = formulateFinalURLFromUrlAndNameValuePairs(stringUrls[0], nameValuePairs);
			result = getRequestOnUrl(finalUrlString);
			if(result==null){
				finalUrlString = formulateFinalURLFromUrlAndNameValuePairs(stringUrls[1], nameValuePairs);
				result = getRequestOnUrl(finalUrlString);
			}
		return result;
	}

	public static String formulateFinalURLFromUrlAndNameValuePairs( String stringUrl,
			List<NameValuePair> nameValuePairs) 
	{
		String finalUrlString = stringUrl + "?";
		for (int index = 0; index < nameValuePairs.size(); index++) {
			finalUrlString += nameValuePairs.get(index).getName() + "=" + nameValuePairs.get(index).getValue();
			if(index != nameValuePairs.size() - 1) {
				finalUrlString += "&";
			}
		}
		return finalUrlString;
	}

	public static String postRequestOnUrlsWithNameValuePair(String[] stringUrls, List<NameValuePair> nameValuePairs) {
		String result = null;
		for( String url :  stringUrls){
			try {
				result = postRequestOnUrlWithNameValuePair(url, nameValuePairs);
				if(result != null){
					return result; 
				}
			} catch (Exception e) {
				Log.e(DiHelper.class.getSimpleName(), "Target not available via internal url, trying internet.");
			}
		}
		return result;
	}
	
	public static String postRequestOnUrlWithNameValuePair(String stringUrl, List<NameValuePair> nameValuePairs){
		String result = null;
		HttpClient httpclient = DiHelper.getHttpClient();    
		HttpPost httppost = new HttpPost(stringUrl);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
		return result;
	}
	public static boolean isJson(String result) {
		if(result == null )return false;
		if( result.length() < 2)return false;
		if(result.charAt(0) != '{'  && result.charAt(0) != '[' ) return false;
		return true;
	}
	public static void updateTextInView(Activity activity, int viewId, String text, int color) {
		TextView status = (TextView) activity.findViewById(viewId);
		if(status!=null){
			status.setText(text);
			status.setTextColor(activity.getResources().getColor(color));
			}
	}
	
	public static String pakckage = "com.philips.cl.di.base"; 

	public static int get(String name) {
		   Class<?> r = null;
	       Class<?>[] classes;
	       Field f;
	       int id = 0;
	        try {
				r = Class.forName(pakckage + ".R");
			

	        classes = r.getClasses();
	        for (int i = 0; i < classes.length; i++) {
	            f = classes[i].getField(name);
	        	if( f !=null)
	        		return f.getInt(classes[i]);
	            }
	        }catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    return id;

	    }
	
	
	public static int getResource(String type ,String name) {
	       Class<?> r = null;
	       int id = 0;
	    try {
	        r = Class.forName(pakckage+ type);
	            id = r.getField(name).getInt(r);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return id;

	    }
	public static int getLayout(String name) {
		return getResource(".R$layout", name);
 }

	public static int getId(String name) {
		return getResource(".R$id", name);
	}
	

}

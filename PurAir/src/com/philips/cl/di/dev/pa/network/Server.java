package com.philips.cl.di.dev.pa.network;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
 
public class Server extends AsyncTask<String, Void, String> {
       private static final String TAG = "Server";
 
       private List<NameValuePair> nameValuePair = null;
       private ServerResponseListener responseListener = null;
       
       private int responseCode ;
       
       public Server(List<NameValuePair> nameValuePair, ServerResponseListener responseListener) {
              this.nameValuePair = nameValuePair;
              this.responseListener = responseListener;
       }
 
       @Override
       protected void onPreExecute() {
              super.onPreExecute();
       }
       
       @Override
       protected String doInBackground(String... url) {
              String result = "";
              // Create a new HttpClient and Post Header
              HttpParams httpParameters = new BasicHttpParams();
              int timeoutSocket = 3500;
              HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
 
              HttpClient httpclient = new DefaultHttpClient(httpParameters);  
              Log.d(TAG, url[0]) ;
              HttpPost httppost = new HttpPost(url[0]);
              
              try {        
                     // Add your data                
                     httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                     
                     HttpResponse response = httpclient.execute(httppost);
                     
                     String nameValuePairs = "";
                     for (NameValuePair nameValuePair : this.nameValuePair) {
                           nameValuePairs += " " + nameValuePair;
                     }
                     responseCode = response.getStatusLine().getStatusCode() ;
                     Log.e(TAG, "responseCode "+response.getStatusLine().getStatusCode() + " for post:" + nameValuePairs);
                     if( responseCode == HttpsURLConnection.HTTP_OK) {
	                     BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	         			 String line = "";
	         		    StringBuilder builder = new StringBuilder() ;
	         	        while ((line = rd.readLine()) != null) {
	         	    	  builder.append(line) ;
	         	        }
	         	       result = builder.toString() ;
              		}
         	        
                     return result;
              } catch (ClientProtocolException e) {
              } catch (IOException e) {        
              }
              return result; 
       }
 
       @Override
       protected void onPostExecute(String result) {
              Log.e(TAG, "response "+result);
              responseListener.receiveServerResponse(responseCode, result);
       }
}

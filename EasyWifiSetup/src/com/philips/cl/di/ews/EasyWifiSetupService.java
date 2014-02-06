 package com.philips.cl.di.ews;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.philips.cl.di.discovery.DiscoverListener;
import com.philips.cl.di.discovery.DiscoverService;
import com.philips.cl.disecurity.DISecurity;
import com.philips.cl.disecurity.KeyDecryptListener;

public class EasyWifiSetupService extends BroadcastReceiver implements KeyDecryptListener {
	/*public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {


            	@Override
            	public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
            };
            X509HostnameVerifier verifier = new X509HostnameVerifier() {

				@Override
				public void verify(String string, SSLSocket ssls) throws IOException {
                }

                public void verify(String string, X509Certificate xc) throws SSLException {
                }

                public void verify(String string, String[] strings, String[] strings1) throws SSLException {
                }

                @Override
				public boolean verify(String string, SSLSession ssls) {
                    return true;
                }

            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(verifier);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }*/
	private static final String TAG = EasyWifiSetupService.class.getName();
	
	public final static String pValue = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C6"
			+ "9A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C0"
			+ "13ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD70"
			+ "98488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0"
			+ "A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708"
			+ "DF1FB2BC2E4A4371";

	public final static String gValue = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507F"
			+ "D6406CFF14266D31266FEA1E5C41564B777E690F5504F213"
			+ "160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1"
			+ "909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A"
			+ "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24"
			+ "855E6EEB22B3B2E5";
	private DISecurity diSecurity;
	protected EasyWifiSetupListener mListener;
	protected EasyWifiSetupInfo mInfo;
	protected Context mContext;
	private Timer timer;
	private WifiInfo mCurrentConnectionInfo;

	public EasyWifiSetupService(Context aContext, EasyWifiSetupListener aListener,
			String aDeviceSsid, String anEndPoint) {
		Log.i(TAG, "EasyWifiSetupService broadcast constructor");
		mListener = aListener;
		mContext =aContext;
		diSecurity = new DISecurity(this);
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		mInfo = new EasyWifiSetupInfo();
		if (mInfo.mCurrentSsid.equals("")){
			mInfo.mCurrentSsid = connectionInfo.getSSID().replace("\"", "");
		}
		if(aDeviceSsid!=null){
			mInfo.mDeviceSsid = aDeviceSsid;
		}
		if(anEndPoint!=null){
			mInfo.mEndPoint = anEndPoint;
		}
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "intent.getAction() in  onReceive= "+ intent.getAction());
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			android.net.NetworkInfo aNetwork = intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (aNetwork.getState() == android.net.NetworkInfo.State.CONNECTED) {
				Log.i(TAG, "Connected in  onReceive= "+ intent.getAction());
				WifiInfo connectionInfo = intent
						.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);

				String ssid = connectionInfo.getSSID();
				Log.i(TAG, "Connected ssid in  onReceive= "+ ssid);
				if (ssid.contains(mInfo.mCurrentSsid)) {
				}
				if (ssid.contains(mInfo.mDeviceSsid)) {
					mListener.connectedToDeviceAp(ssid);
				}
			}
			return;
		}

		if (intent.getAction()
				.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

			List<ScanResult> results = wifiManager.getScanResults();
			processWifiList(results);
			return;
		}

	}

	private void processWifiList(List<ScanResult> results) {
		for (ScanResult scanResult : results) {
			// find ssid that looks like target ssid
			if (scanResult.SSID != null && scanResult.SSID.equals(mInfo.mDeviceSsid)) {
				mListener.foundDeviceAp(scanResult.SSID);
				updateTask.cancel();
			}
			else if (scanResult.SSID != null && scanResult.SSID.equals(mInfo.mCurrentSsid)) {
				mInfo.mCapabilities = scanResult.capabilities;
				scanResult.capabilities.contains("WPA");
			}
		}
	}
	ArrayList<String>  oldDevices=null;

	/**
	 * transition from zero: welcome to one: scanning for device access point
	 */
	public void startScanForDeviceAp() {
		
		mWifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		mCurrentConnectionInfo = mWifiManager.getConnectionInfo();
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		mContext.registerReceiver(this, filter);
		timer = new Timer();
		timer.schedule(updateTask, 0, 10000);
		 
	}
    private WifiManager mWifiManager;
	private TimerTask updateTask = new TimerTask() {
		@Override
	    public void run() {
			if(mWifiManager==null){
				mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			}
			mWifiManager.startScan();
	    }
	  };
	static public void connectTo(Context context,String ssid, String password) {

		WifiConfiguration config= new WifiConfiguration();
		config.SSID = "\"" + ssid.replace("\"", "") + "\"";
		config.status=WifiConfiguration.Status.ENABLED;
		config.priority = 1;
		if(password.equals("")){
			config.allowedKeyManagement.set(KeyMgmt.NONE);
		}else{
			config.preSharedKey ="\""+password+"\"";
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
		}
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		int netId = wifiManager.addNetwork(config);
		if (netId >= 0) {
			wifiManager.enableNetwork(netId, true);
		} else {
			// TODO: toast enable network
		}
	}

    public void connectToCurrentAp(){
    	connectTo(mContext,mInfo.mCurrentSsid,mInfo.mPassword);

	}
	public void sendSSidToDevice() {
		//manzer
		diSecurity.exchangeKey("http://192.168.1.1/di/v1/products/0/security", "devId01");
		//SendNetworkInfoTask task = new SendNetworkInfoTask();
		//task.execute(mInfo);
	}

	public class SendNetworkInfoTask extends
	AsyncTask<EasyWifiSetupInfo, Void, String> {

		@Override
		protected String doInBackground(EasyWifiSetupInfo... infos) {
			String result = "";
			for (EasyWifiSetupInfo info : infos) {
				result += sendNetworkInfo(info);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			mListener.sentSsidToDevice(result);
		}

		String sendNetworkInfo(EasyWifiSetupInfo info) {
			
			skipSslCheck();
			
			
			String result;
			HttpClient httpclient = new DefaultHttpClient();

			try {
				HttpResponse response;
				//sendDiffieInfo(info, httpclient);
				sendDeviceInfo(info, httpclient);
				result = sendWifiInfo(info, httpclient);

			} catch (Exception e) {
				result = e.getMessage();
			}
			return result;

		}

		private String sendWifiInfo(EasyWifiSetupInfo info,
				HttpClient httpclient) throws Exception{
			String result;
			HttpResponse response;
			HttpPut ssidEndpoint = new HttpPut(mInfo.mEndPoint+mInfo.WIFI_URI);
			ssidEndpoint.setEntity(info.toJson(diSecurity));
			response = httpclient.execute(ssidEndpoint);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
				Log.d("SEND", "Send Wifi result: " + result);
			}else{
				result = response.getStatusLine().toString();
				Log.d("SEND", "Send Wifi result: " + result);
			}
			return result;
		}

		private String sendDiffieInfo(EasyWifiSetupInfo info,
				HttpClient httpclient) throws Exception{
			String result;
			HttpResponse response; 
			HttpPut ssidEndpoint = new HttpPut(mInfo.mEndPoint+mInfo.SECURITY_URI);
			ssidEndpoint.setEntity(info.toJsonDiffie());
			response = httpclient.execute(ssidEndpoint);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(result);
				mInfo.calculateKey(json.getString("hellman"));
				Log.d("SEND", "Send Diffie result: " + result);
			}else{
				result = response.getStatusLine().toString();
				Log.d("SEND", "Send Diffie result: " + result);
			}
			return result;
		}

		private void sendDeviceInfo(EasyWifiSetupInfo info,
				HttpClient httpclient) throws JSONException,
				UnsupportedEncodingException, IOException,
				ClientProtocolException {
			String result;
			HttpPut deviceEndpoint = new HttpPut(mInfo.mEndPoint+mInfo.DEVICE_URI);
			deviceEndpoint.setEntity(info.toJsonDevice(diSecurity));
			HttpResponse response = httpclient.execute(deviceEndpoint);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
				Log.d("SEND", "Send Device result: " + result);
			}else{
				result = response.getStatusLine().toString();
				Log.d("SEND", "Send Device result: " + result);
			}
		}

		private void skipSslCheck() {
			SSLContext ctx;
			try {
				ctx = SSLContext.getInstance("TLS");
				ctx.init(null, new TrustManager[] {
						  new X509TrustManager() {
						    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
						    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
						    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
						  }
						}, null);
						HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
						
						HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
							  public boolean verify(String hostname, SSLSession session) {
							    return true;
							  }
							});

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
	
	public void verifyConnectedToHomeNetwork() {
 		connectToCurrentAp();
		VerifyHomeNetworkConnectedTask task = new VerifyHomeNetworkConnectedTask();
		task.execute(mInfo);
	}

	public class VerifyHomeNetworkConnectedTask extends AsyncTask<EasyWifiSetupInfo, Void, String> implements DiscoverListener {

		private boolean mfoundDevice;
		String mBaseUrl;
		DiscoverService mDiscoveryService = new DiscoverService(mContext, this);

		@Override
		protected String doInBackground(EasyWifiSetupInfo... infos) {
			
			mDiscoveryService.startDiscover();
			String result=null;
			result = scanForNewDevice(infos[0]);
			Log.i(TAG, "result in doInBackground= " + result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			mListener.connectedToHomeNetwork(result);
		}

		String scanForNewDevice(EasyWifiSetupInfo info) {
			int retry = 0;	
			String result = null;
			while(retry< 10 && !mfoundDevice){
				result = EasyWifiSetupHelper.deviceScanner();
				if(result!=null){
					mBaseUrl = result;
					mfoundDevice =true;
				}
				retry +=1;
			}
			return mBaseUrl;
		}

		@Override
		public void ResolvedDeviceIp(String uri) {
			Log.i(TAG, "uri in ResolvedDeviceIp= " + uri);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(uri+mInfo.WIFI_URI);
			try {
				HttpResponse response = httpclient.execute(getRequest);
				if(response.getStatusLine().getStatusCode()==200){
					mfoundDevice=true;
					mBaseUrl = uri;
					mDiscoveryService.stopDiscover();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public String getServiceType() {
			
			return "_Philips-DiComm._tcp.";
		}
	}
	

	public void setPassowrd(String aPassword) {
		mInfo.mPassword = aPassword;

	}

	public void setDeviceName(String aDeviceName) {
		mInfo.mDeviceName = aDeviceName;
	}

	public boolean connectedToCurrentSsid() {
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		mInfo = new EasyWifiSetupInfo();
		return (mInfo.mCurrentSsid == connectionInfo.getSSID());
	}

	@Override
	public void keyDecrypt(String key) {
		mInfo.mKey = key; 
		Log.i(TAG, "key= "+key);
		Log.i(TAG, "Maps of keys= "+ DISecurity.securityHashtable);
		if (key != null) {
			SendNetworkInfoTask task = new SendNetworkInfoTask();
			task.execute(mInfo);
		}
	}
}

package com.philips.cl.disecurity.disecuritysample;

import com.philips.cl.disecurity.DISecurity;
import com.philips.cl.disecurity.KeyDecryptListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

	public class DISecuritySampleActivity extends Activity implements KeyDecryptListener {
	
	private static final String TAG = DISecuritySampleActivity.class.getSimpleName();
	
	private DISecurity diSecurity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disecurity_sample);
		
		diSecurity = new DISecurity(this);
		diSecurity.initializeKey("http://192.168.1.1/di/v1/products/0/security", "devId01");
	}

	@Override
	public void keyDecrypt(String key) {
		Log.i(TAG, "key= "+key);
		Log.i(TAG, "Maps of keys= "+ DISecurity.securityHashtable);
	}

}

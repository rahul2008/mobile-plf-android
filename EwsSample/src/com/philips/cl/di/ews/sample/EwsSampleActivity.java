 package com.philips.cl.di.ews.sample;

import com.philips.cl.di.ews.EasyWifiSetupActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class EwsSampleActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ews_sample);
    }
    public static final String EXTRA_TARGET_WIFI = "targetString";
	public static final String EXTRA_END_POINT = "endPoint";
	public static final String EXTRA_BASE_URL = "baseUrl";
	
    public void startEws(View v) {
		Intent intent;
		try {
			intent = new Intent(this, Class.forName("com.philips.cl.di.ews.EasyWifiSetupActivity"));
			intent.putExtra("targetString", "PHILIPS Setup");
			intent.putExtra("endPoint", "http://192.168.1.1");
			startActivityForResult(intent, 324);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
}
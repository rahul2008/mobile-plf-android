/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.aildemo;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.keybag.KeyBagManager;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.philipsdevtools.ServiceDiscoveryManagerCSV;

import java.io.InputStream;
import java.net.URL;

public class KeyBagActivity extends AppCompatActivity {

	private String TAG =getClass().getSimpleName();
	private KeyBagManager keyBagInterface;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keybag);

		keyBagInterface = new KeyBagManager();
		keyBagInterface.init(getRawData());
		testServiceDiscovery();
	}

	String getRawData() {
		try {
			Resources res = getResources();
			InputStream in_s = res.openRawResource(R.raw.AIKeyBag);
			byte[] bytes = new byte[in_s.available()];
			int read = in_s.read(bytes);
			Log.d(getClass()+"",String.valueOf(read));
			return new String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private void testServiceDiscovery() {
		final ServiceDiscoveryManagerCSV sdmCSV =  new ServiceDiscoveryManagerCSV();
		AppInfra.Builder builder = new AppInfra.Builder();
		AppInfra mAppInfra = builder.build(this);
		builder.setServiceDiscovery(sdmCSV);
		sdmCSV.init(mAppInfra);
		sdmCSV.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
			@Override
			public void onSuccess() {

				sdmCSV.getServiceUrlWithCountryPreference("appinfra.localtesting.kindex", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {


					@Override
					public void onSuccess(URL url) {
						System.out.println("************ SUCCESS URL : "+url);
						Log.d(TAG, "Response from Service Discovery : Service ID : 'appinfra.localtesting.kindex' - " + url);
						keyBagInterface.getMapForServiceId("appinfra.localtesting", "clientId", url);

					}

					@Override
					public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String s) {
						System.out.println("************ Failed URL : "+s);
						Log.d(TAG, "Error Response from Service Discovery :" + s);
					}
				});
			}

			@Override
			public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String s) {
				Log.d(TAG, "Error Response from Service Discovery CSV :" + s);
			}
		});
	}
}

/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.aildemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.keybag.KeyBagInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.philipsdevtools.ServiceDiscoveryManagerCSV;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KeyBagActivity extends AppCompatActivity {

	private String TAG =getClass().getSimpleName();

	private EditText serviceIdEditText;
	private TextView responseTextView;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keybag);
		serviceIdEditText = (EditText) findViewById(R.id.service_id_edt);
		responseTextView = (TextView) findViewById(R.id.response_view);

		try {
			initServiceDiscovery();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void initServiceDiscovery() throws FileNotFoundException {
		final ServiceDiscoveryManagerCSV sdmCSV = new ServiceDiscoveryManagerCSV();
		final KeyBagInterface keyBagInterface = AILDemouAppInterface.getInstance().getAppInfra().getKeyBagInterface();
		keyBagInterface.init();
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
						Log.d(TAG, "Response from Service Discovery : Service ID : 'appinfra.localtesting.kindex' - " + "https://www.philips.com/0");
						ArrayList<HashMap> mapForServiceId = keyBagInterface.getMapForServiceId("appinfra.localtesting", url);
						updateView(mapForServiceId);
						Log.d(TAG, "Response from Service Discovery : Service ID : 'appinfra.localtesting.kindex' - " + url);

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

	private void updateView(ArrayList<HashMap> mapForServiceId) {
		StringBuilder stringBuilder = new StringBuilder();
		for (HashMap hashMap : mapForServiceId) {
			Iterator it = hashMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				String key = (String) pair.getKey();
				String value = (String) pair.getValue();
				stringBuilder.append(key);
				stringBuilder.append(":");
				stringBuilder.append(value);
				stringBuilder.append("  ");
			}
			stringBuilder.append("\n");
		}
		responseTextView.setText(stringBuilder.toString());
	}
}

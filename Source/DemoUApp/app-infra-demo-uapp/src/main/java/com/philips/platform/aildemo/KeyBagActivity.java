/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.aildemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.keybag.KeyBagInterface;

import java.io.FileNotFoundException;
import java.util.Map;

public class KeyBagActivity extends AppCompatActivity {


	private EditText serviceIdEditText;
	private TextView responseTextView;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keybag);
		serviceIdEditText = (EditText) findViewById(R.id.service_id_edt);
		responseTextView = (TextView) findViewById(R.id.response_view);
	}

	public void onClick(View view) {
		final KeyBagInterface keyBagInterface = AILDemouAppInterface.getInstance().getAppInfra().getKeyBagInterface();
		try {
			keyBagInterface.init();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Map mapForServiceId = keyBagInterface.getMapForServiceId(serviceIdEditText.getText().toString());
		updateView(mapForServiceId);
	}

	private void updateView(Map map) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Object object : map.entrySet()) {
			Map.Entry pair = (Map.Entry) object;
			String key = (String) pair.getKey();
			String value = (String) pair.getValue();
			stringBuilder.append(key);
			stringBuilder.append(":");
			stringBuilder.append(value);
			stringBuilder.append("  ");
		}
			stringBuilder.append("\n");
		responseTextView.setText(stringBuilder.toString());
	}
}

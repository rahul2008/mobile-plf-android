/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.aildemo;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.keybag.KeyBagInterface;
import com.philips.platform.appinfra.keybag.exception.KeyBagJsonFileNotFoundException;
import com.philips.platform.appinfra.keybag.model.AIKMService;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KeyBagActivity extends AppCompatActivity {


	private EditText serviceIdEditText;
	private TextView responseTextView;
	private AISDResponse.AISDPreference aikmServiceDiscoveryPreference = AISDResponse.AISDPreference.AISDCountryPreference;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keybag);

		serviceIdEditText = (EditText) findViewById(R.id.service_id_edt);
		responseTextView = (TextView) findViewById(R.id.response_view);

		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				if(checkedId == R.id.rbtn_language) {
					aikmServiceDiscoveryPreference = AISDResponse.AISDPreference.AISDLanguagePreference;
				} else
					aikmServiceDiscoveryPreference = AISDResponse.AISDPreference.AISDCountryPreference;
			}
		});
	}

	public void onClick(View view) {
		final KeyBagInterface keyBagInterface = AILDemouAppInterface.getInstance().getAppInfra().getKeyBagInterface();

		String serviceIdsFromEditText = serviceIdEditText.getText().toString();
		if(!TextUtils.isEmpty(serviceIdsFromEditText)) {
			String[] serviceIds = serviceIdsFromEditText.split(",");

			try {
				keyBagInterface.getServicesForServiceIds(new ArrayList<>(Arrays.asList(serviceIds)), aikmServiceDiscoveryPreference, null, new ServiceDiscoveryInterface.OnGetServicesListener() {
					@Override
					public void onSuccess(List<AIKMService> aikmServices) {
						updateView(aikmServices);
					}

					@Override
					public void onError(ERRORVALUES error, String message) {
						Log.e(getClass().getSimpleName(), message);
					}
				});
			} catch (KeyBagJsonFileNotFoundException e) {
				e.printStackTrace();
			}
		} else
			Toast.makeText(KeyBagActivity.this,"Please enter service id",Toast.LENGTH_SHORT).show();
	}

	private void updateView(List<AIKMService> aikmServiceList) {
		StringBuilder stringBuilder = new StringBuilder();
		if (aikmServiceList != null && aikmServiceList.size() != 0) {
			for (int i = 0; i < aikmServiceList.size(); i++) {
				stringBuilder.append("ServiceId: ");
				AIKMService aikmService = aikmServiceList.get(i);
				stringBuilder.append(aikmService.getServiceId());
				stringBuilder.append(", ");
				if(TextUtils.isEmpty(aikmService.getmError())) {
					stringBuilder.append("Url:");
					stringBuilder.append("  ");
					stringBuilder.append(aikmService.getConfigUrls());
				} else {
					stringBuilder.append("Error:");
					stringBuilder.append("  ");
					stringBuilder.append(aikmService.getmError());
				}
				stringBuilder.append(", ");
				Map keyBag = aikmService.getKeyBag();
				if (keyBag != null) {
					for (Object object : keyBag.entrySet()) {
						Map.Entry pair = (Map.Entry) object;
						String key = (String) pair.getKey();
						String value = (String) pair.getValue();

						stringBuilder.append("KeyBag Data --- ");
						stringBuilder.append(key);
						stringBuilder.append(":");
						stringBuilder.append(value);
						stringBuilder.append("  ");
					}
				}
				AIKMService.KEY_BAG_ERROR keyBagError = aikmService.getKeyBagError();
				if (null != keyBagError) {
					stringBuilder.append("error while fetching key bag -- ");
					stringBuilder.append(keyBagError.name());
				}
				stringBuilder.append("\n");
				stringBuilder.append("\n");
			}
		}
		responseTextView.setText(stringBuilder.toString());
	}
}

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

import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.keybag.KeyBagManager;

public class KeyBagActivity extends AppCompatActivity {


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keybag);

		KeyBagManager keyBagManager = new KeyBagManager();
		String obfuscate = keyBagManager.obfuscate("Test data", 0XAE47);

		Log.d(getClass()+"","obfuscated data "+obfuscate);

		obfuscate = keyBagManager.obfuscate(obfuscate,0XAE47);
		Log.d(getClass()+"","deobfuscated data "+obfuscate);

	}
}

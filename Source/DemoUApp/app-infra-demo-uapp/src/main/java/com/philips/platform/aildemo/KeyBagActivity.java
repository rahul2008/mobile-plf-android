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

import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.keybag.KeyBagInterface;
import com.philips.platform.appinfra.keybag.KeyBagManager;

import java.io.InputStream;

public class KeyBagActivity extends AppCompatActivity {


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keybag);

		KeyBagInterface keyBagInterface = new KeyBagManager();
		keyBagInterface.init(getRawData());
	}

	String getRawData() {
		try {
			Resources res = getResources();
			InputStream in_s = res.openRawResource(R.raw.keybag2);
			byte[] bytes = new byte[in_s.available()];
			int read = in_s.read(bytes);
			Log.d(getClass()+"",String.valueOf(read));
			return new String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}

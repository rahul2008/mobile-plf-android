package com.philips.platform.aildemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.keybag.KeyBagImpl;

public class KeyBagActivity extends AppCompatActivity {


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keybag);

		new KeyBagImpl().obfuscate();
	}
}

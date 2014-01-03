package com.philips.cl.di.dev.pa.listeners;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ChildClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		Log.i("ChildClick", "onClick");

	}

}

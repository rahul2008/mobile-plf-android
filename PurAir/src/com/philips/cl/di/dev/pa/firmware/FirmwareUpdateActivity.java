package com.philips.cl.di.dev.pa.firmware;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.NewFirmware;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareUpdateActivity extends BaseActivity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firmware_container);
		initActionBar();
		
		getSupportFragmentManager().beginTransaction()
		.add(R.id.firmware_container, new NewFirmware(), "NewFirmware")
		.commit();
	}
	
	/*Initialize action bar */
	private void initActionBar() {
		ActionBar mActionBar;
		FontTextView actionbarTitle;
		Button actionBarCancelBtn;
		mActionBar = getSupportActionBar();
		mActionBar.setIcon(null);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		mActionBar.setBackgroundDrawable(d);
		View view  = getLayoutInflater().inflate(R.layout.ews_actionbar, null);
		actionbarTitle = (FontTextView) view.findViewById(R.id.ews_actionbar_title);
		actionbarTitle.setText(getString(R.string.firmware));
		actionBarCancelBtn = (Button) view.findViewById(R.id.ews_actionbar_cancel_btn);
		actionBarCancelBtn.setTypeface(Fonts.getGillsansLight(this));
		actionBarCancelBtn.setOnClickListener(this);
		mActionBar.setCustomView(view);	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ews_actionbar_cancel_btn:
			Toast.makeText(this, "Cancel button clicked", 0).show();
			finish();
			break;

		default:
			break;
		}
		
	}
}

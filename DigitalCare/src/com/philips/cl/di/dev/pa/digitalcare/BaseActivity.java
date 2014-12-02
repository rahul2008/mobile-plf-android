package com.philips.cl.di.dev.pa.digitalcare;

import java.util.Locale;

import com.philips.cl.di.dev.pa.digitalcare.customview.FontTextView;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(this.getClass().getSimpleName(), "onCreate");
	}
	
	private void initActionBar() throws ClassCastException {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		actionBar.setBackgroundDrawable(d);
		View viewActionbar = getLayoutInflater().inflate(R.layout.home_action_bar, null);
		rightMenu = (ImageView) viewActionbar.findViewById(R.id.right_menu_img);
		leftMenu = (ImageView) viewActionbar.findViewById(R.id.left_menu_img);
		backToHome = (ImageView) viewActionbar.findViewById(R.id.back_to_home_img);
		addLocation = (ImageView) viewActionbar.findViewById(R.id.add_location_img);
		noOffFirmwareUpdate = (FontTextView) viewActionbar.findViewById(R.id.actionbar_firmware_no_off_update);
		actionBarTitle = (FontTextView) viewActionbar.findViewById(R.id.action_bar_title);
		//If Chinese language selected set font-type-face normal
		if( LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			actionBarTitle.setTypeface(Typeface.DEFAULT);
		}

		rightMenu.setOnClickListener(actionBarClickListener);
		leftMenu.setOnClickListener(actionBarClickListener);
		backToHome.setOnClickListener(actionBarClickListener);
		addLocation.setOnClickListener(actionBarClickListener);
		airPortTaskProgress = (ProgressBar) viewActionbar.findViewById(R.id.actionbar_progressBar);

		actionBar.setCustomView(viewActionbar);
	}
}

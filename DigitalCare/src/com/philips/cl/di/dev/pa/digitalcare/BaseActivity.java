package com.philips.cl.di.dev.pa.digitalcare;

import java.util.Observable;
import java.util.Observer;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.digitalcare.customview.FontTextView;

/*
 *	BaseActivity is the main super class container for Digital Care fragments.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2014
 */
public class BaseActivity extends ActionBarActivity implements Observer {
	private ImageView leftMenu;
	private ImageView backToHome;
	private FontTextView actionBarTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(this.getClass().getSimpleName(), "onCreate");
	}

	public void setFragmentDetails(String actionbarTitle) {
		actionBarTitle.setText(actionbarTitle);
	}

	protected void initActionBar() throws ClassCastException {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d = getResources().getDrawable(R.drawable.ews_nav_bar_2x);
		actionBar.setBackgroundDrawable(d);
		View viewActionbar = getLayoutInflater().inflate(
				R.layout.home_action_bar, null);
		leftMenu = (ImageView) viewActionbar.findViewById(R.id.left_menu_img);
		backToHome = (ImageView) viewActionbar
				.findViewById(R.id.back_to_home_img);
		actionBarTitle = (FontTextView) viewActionbar
				.findViewById(R.id.action_bar_title);
		actionBarTitle.setTypeface(Typeface.DEFAULT);

		leftMenu.setOnClickListener(actionBarClickListener);
		backToHome.setOnClickListener(actionBarClickListener);

		actionBar.setCustomView(viewActionbar);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.left_menu_img:
				finish();
				break;
			case R.id.back_to_home_img:
				finish();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void update(Observable observable, Object title) {
		actionBarTitle.setText(DigitalCareApplication.getAppContext()
				.getObserver().getValue());
	}
}

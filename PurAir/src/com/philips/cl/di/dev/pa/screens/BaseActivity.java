package com.philips.cl.di.dev.pa.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.screens.adapters.MenuListAdapter;
import com.philips.cl.di.dev.pa.screens.fragments.AddCityFragment;
import com.philips.cl.di.dev.pa.screens.fragments.HomeFragment;
import com.philips.cl.di.dev.pa.utils.CollapseAnimation;
import com.philips.cl.di.dev.pa.utils.ExpandAnimation;
import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
public class BaseActivity extends FragmentActivity implements
		OnItemClickListener, OnClickListener {

	private LayoutInflater inflater;
	private FragmentTransaction transaction;
	/** The Constant TAG. */
	private static final String TAG = BaseActivity.class.getName();
	private boolean isExpanded;
	private DisplayMetrics metrics;
	private int menuWidth;
	private RelativeLayout headerPanel;
	private RelativeLayout menuPanel;
	private LinearLayout slidingPanel;
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	private ImageView ivMenu;
	private ImageView ivSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		transaction = getSupportFragmentManager().beginTransaction();

		// Initialize
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		menuWidth = (int) ((metrics.widthPixels) * 0.75);

		headerPanel = (RelativeLayout) findViewById(R.id.rlTopNavigation);
		headerPanelParameters = (LinearLayout.LayoutParams) headerPanel
				.getLayoutParams();
		headerPanelParameters.width = metrics.widthPixels;
		headerPanel.setLayoutParams(headerPanelParameters);

		menuPanel = (RelativeLayout) findViewById(R.id.menu);
		menuPanelParameters = (FrameLayout.LayoutParams) menuPanel
				.getLayoutParams();
		menuPanelParameters.width = menuWidth;
		menuPanel.setLayoutParams(menuPanelParameters);

		slidingPanel = (LinearLayout) findViewById(R.id.slidingPanel);
		slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel
				.getLayoutParams();
		slidingPanelParameters.width = metrics.widthPixels;
		slidingPanel.setLayoutParams(slidingPanelParameters);

		// Slide the Panel
		ivMenu = (ImageView) findViewById(R.id.ivMenu);
		ivMenu.setOnClickListener(this);
		ivSettings = (ImageView) findViewById(R.id.ivSettings);
		ivSettings.setOnClickListener(this);
		initializeMenuList();
		getSupportFragmentManager().beginTransaction().add(R.id.slidingPanel,new HomeFragment()).commit();

	}

	public void initializeMenuList() {
		ListView lvMenu = (ListView) findViewById(R.id.lvMenu);
		lvMenu.setOnItemClickListener(this);
		Utils.getIconArray();
		Utils.getLabelArray();
		lvMenu.setAdapter(new MenuListAdapter(getApplicationContext(), 0, Utils
				.getIconArray(), Utils.getLabelArray()));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		switch (position) {
		case AppConstants.HOME:
			FragmentTransaction ftHome = getSupportFragmentManager()
					.beginTransaction();
			Log.i(TAG, "On Item Click --- HOME ");
			new CollapseAnimation(slidingPanel, menuWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
			isExpanded = false;
			ftHome.replace(R.id.slidingPanel, new HomeFragment());
			ftHome.addToBackStack(null);
			ftHome.commit();
			break;
		case AppConstants.MYCITIES:
			FragmentTransaction ftCity = getSupportFragmentManager()
					.beginTransaction();
			Log.i(TAG, "On Item Click --- CITY");
			new CollapseAnimation(slidingPanel, menuWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
			ftCity.replace(R.id.slidingPanel, new AddCityFragment());
			ftCity.addToBackStack(null);
			ftCity.commit();
			isExpanded = false;
			break;
		case AppConstants.ABOUTAQI:
			Log.i(TAG, "On Item Click --- ABout AQI ");
			new CollapseAnimation(slidingPanel, menuWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
			isExpanded = false;
			break;
		case AppConstants.PRODUCTREG:
			Log.i(TAG, "On Item Click --- Product Reg ");
			break;
		case AppConstants.HELP:
			Log.i(TAG, "On Item Click --- HELP");
			break;
		case AppConstants.SETTINGS:
			Log.i(TAG, "On Item Click --- Settings");
			break;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivMenu:
			if (!isExpanded) {
				isExpanded = true;
				// Expand
				new ExpandAnimation(slidingPanel, menuWidth,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);
			} else {
				isExpanded = false;
				// Collapse
				new CollapseAnimation(slidingPanel, menuWidth,
						TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
						TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0,
						0.0f);

			}
			break;

		case R.id.ivSettings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		}

	}
	
	
}

package com.philips.cl.di.dev.pa.screens;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.animation.ObjectAnimator;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.controller.AirPurifierController;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.screens.adapters.MenuListAdapter;
import com.philips.cl.di.dev.pa.screens.customviews.LeftMenuView;
import com.philips.cl.di.dev.pa.screens.fragments.HomeFragment;
import com.philips.cl.di.dev.pa.screens.fragments.IndoorDetailsFragment;
import com.philips.cl.di.dev.pa.screens.fragments.HomeFragment.OnIndoorRingClick;
import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
public class BaseActivity extends FragmentActivity implements
		OnItemClickListener, OnClickListener, SensorEventListener ,OnIndoorRingClick  {

	/** The Constant TAG. */
	private static final String TAG = BaseActivity.class.getName();

	/** The is expanded. */
	private boolean isExpanded;

	/** The metrics. */
	private DisplayMetrics metrics;

	/** The menu width. */
	private int menuWidth;

	/** The menu panel. */
	private RelativeLayout rlHeaderLeftMenu;

	/** The sliding panel. */
	/* private LinearLayout slidingPanel; */

	/** The sliding panel parameters. */
	FrameLayout.LayoutParams /* menuPanelParameters, */slidingPanelParameters;

	/** The list view parameters. */
	LinearLayout.LayoutParams headerPanelParameters, listViewParameters;

	/** The iv settings. */
	private ImageView ivMenu, ivSettings;

	/** The lv menu. */
	ListView lvMenu;

	/** The sensor data controller. */
	private SensorDataController sensorDataController;

	/** The air purifier controller. */
	private AirPurifierController airPurifierController;

	/** The fl main. */
	private FrameLayout flMain;

	/** The show left menu. */
	private ObjectAnimator translateHomeAway, translateHomeOriginal,
			hideLeftMenu, showLeftMenu;

	/** The lv left menu. */
	private LeftMenuView lvLeftMenu;

	/** The inflater. */
	private LayoutInflater inflater;

	/** The rl main. */
	private RelativeLayout rlMain;

	/** The params left menu. */
	private RelativeLayout.LayoutParams paramsLeftMenu;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		copyFileAssets() ;
		
		initializeViews();
		initializeAnimations();

		getSupportFragmentManager().beginTransaction()
				.add(R.id.llContainer, new HomeFragment(), HomeFragment.TAG)
				.commit();
		sensorDataController = SensorDataController.getInstance(this);
		sensorDataController.startPolling();

		airPurifierController = new AirPurifierController(this);
		airPurifierController.getFilterStatus();
	}
		

	/**
	 * Initialize views.
	 */
	private void initializeViews() {
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		menuWidth = (int) ((metrics.widthPixels) * 0.75);
		inflater = LayoutInflater.from(this);
		flMain = (FrameLayout) findViewById(R.id.flMain);
		lvLeftMenu = (LeftMenuView) inflater.inflate(
				R.layout.activity_leftmenu, null);
		rlHeaderLeftMenu = (RelativeLayout) inflater.inflate(
				R.layout.activity_header_left_menu, lvLeftMenu, false);
		rlHeaderLeftMenu.setClickable(false);
		lvLeftMenu.setOnItemClickListener(this);
		lvLeftMenu.addHeaderView(rlHeaderLeftMenu, null, false);
		lvLeftMenu.setWidth(menuWidth);
		Utils.getIconArray();
		Utils.getLabelArray();
		lvLeftMenu.setAdapter(new MenuListAdapter(getApplicationContext(), 0,
				Utils.getIconArray(), Utils.getLabelArray()));
		lvLeftMenu.setOnItemClickListener(this);

		// Slide the Panel
		ivMenu = (ImageView) findViewById(R.id.ivLeftMenu);
		ivMenu.setOnClickListener(this);
		ivSettings = (ImageView) findViewById(R.id.ivRightDeviceIcon);
		ivSettings.setOnClickListener(this);

		paramsLeftMenu = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		paramsLeftMenu.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		paramsLeftMenu.width = menuWidth;
		rlMain = (RelativeLayout) findViewById(R.id.rlMain);

	}

	/**
	 * Initialize animations.
	 */
	private void initializeAnimations() {
		translateHomeAway = ObjectAnimator.ofFloat(flMain, "translationX", 0f,
				menuWidth);
		translateHomeAway.setDuration(AppConstants.DURATION);

		translateHomeOriginal = ObjectAnimator.ofFloat(flMain, "translationX",
				menuWidth, 0f);
		translateHomeOriginal.setDuration(AppConstants.DURATION);

		hideLeftMenu = ObjectAnimator.ofFloat(lvLeftMenu, "translationX", 0f,
				-menuWidth);
		hideLeftMenu.setDuration(AppConstants.DURATION);

		showLeftMenu = ObjectAnimator.ofFloat(lvLeftMenu, "translationX",
				-menuWidth, 0f);
		showLeftMenu.setDuration(AppConstants.DURATION);

	}

	/**
	 * OnPause of this Activity unRegister the listener.
	 */
	@Override
	protected void onPause() {
		super.onPause();
	    sensorDataController.unRegisterListener(this);
	   
	}

	/**
	 * OnDestroy of the Activity stop polling the Air Purifier.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy() ;
		sensorDataController.stopPolling();
	}

	/**
	 * OnResume of BaseActivity register the listener.
	 */
	@Override
	protected void onResume() {
		Log.i(TAG, "OnResume") ;
		super.onResume();
		
		sensorDataController.registerListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		switch (position) {
		case AppConstants.HOME:
			Log.i(TAG, "On Item Click --- HOME ");
			collapse();
			isExpanded = false;
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.llContainer, new HomeFragment(),
							HomeFragment.TAG).commit();
			break;
		case AppConstants.MYCITIES:
			Log.i(TAG, "On Item Click --- CITY");
			collapse();
			isExpanded = false;
			break;
		case AppConstants.ABOUTAQI:
			collapse();
			isExpanded = false;
			break;
		case AppConstants.PRODUCTREG:
			Log.i(TAG, "On Item Click --- Product Reg ");
			collapse();
			isExpanded = false;
			break;
		case AppConstants.HELP:
			Log.i(TAG, "On Item Click --- HELP");
			collapse();
			isExpanded = false;
			break;
		case AppConstants.SETTINGS:
			Log.i(TAG, "On Item Click --- Settings");
			collapse();
			isExpanded = false;
			Utils.showIpDialog(this);
			break;

		}
	}

	/**
	 * Collapse.
	 */
	private void collapse() {
		translateHomeOriginal.start();
		hideLeftMenu.start();
		isExpanded = !isExpanded;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivLeftMenu:
			if (!isExpanded) {
				translateHomeAway.start();
				if (lvLeftMenu != null && lvLeftMenu.getParent() == rlMain) {
					showLeftMenu.start();
				} else {
					rlMain.addView(lvLeftMenu, paramsLeftMenu);
				}
				isExpanded = !isExpanded;
			} else {
				translateHomeOriginal.start();
				hideLeftMenu.start();
				isExpanded = !isExpanded;
			}
			break;

		case R.id.ivRightDeviceIcon:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		}

	}

	// For Ip address settings
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionItemSettings:
			Utils.showIpDialog(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * OnSensor Event Callback update the User Interface.
	 * 
	 * @param airPurifierEventDto
	 *            the air purifier event dto
	 */
	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (isExpanded) {
			translateHomeOriginal.start();
			hideLeftMenu.start();
			isExpanded = !isExpanded;
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	public void onRingClicked(int aqi) {

		IndoorDetailsFragment newFragment = new IndoorDetailsFragment();
		Bundle args = new Bundle();
		args.putInt(AppConstants.INDOOR_AQI, aqi);
		newFragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack so the user can navigate
		// back
		transaction.replace(R.id.llContainer, newFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();

	}
	
	private void copyFileAssets() {
		if(!isFileExists()) {
			AssetManager assetManager = getAssets();
				try {
					String filenames[] = assetManager.list("") ;
		
					for(String filename : filenames) {
						if( filename.equalsIgnoreCase("purair.db")) {
							Log.i("File name => ",filename);
							InputStream in = null;
							OutputStream out = null;
							try {
								in = assetManager.open(filename);   // if files resides inside the "Files" directory itself
								out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() +"/" + filename);
								copyFile(in, out);
								in.close();
								in = null;
								out.flush();
								out.close();
								out = null;
							} catch(Exception e) {
								Log.e("tag", e.getMessage());
							}
						}
					}
				}
				catch(IOException e) {
				}
		}
}
	
	private boolean isFileExists() {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "purair.db" );
		return file.exists() ;
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}
}

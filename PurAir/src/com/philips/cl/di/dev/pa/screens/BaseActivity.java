package com.philips.cl.di.dev.pa.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.philips.cl.di.dev.pa.controller.AirPurifierController;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.FilterStatusDto;
import com.philips.cl.di.dev.pa.interfaces.FilterStatusInterface;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.screens.adapters.MenuListAdapter;
import com.philips.cl.di.dev.pa.screens.fragments.HomeFragment;
import com.philips.cl.di.dev.pa.utils.CollapseAnimation;
import com.philips.cl.di.dev.pa.utils.ExpandAnimation;
import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
public class BaseActivity extends FragmentActivity implements
		OnItemClickListener, OnClickListener, SensorEventListener, FilterStatusInterface {

	/** The Constant TAG. */
	private static final String TAG = BaseActivity.class.getName();
	
	/** The is expanded. */
	private boolean isExpanded;
	
	/** The metrics. */
	private DisplayMetrics metrics;
	
	/** The menu width. */
	private int menuWidth;
	
	/** The menu panel. */
	private RelativeLayout headerPanel, menuPanel;
	
	/** The sliding panel. */
	private LinearLayout slidingPanel;
	
	/** The sliding panel parameters. */
	FrameLayout.LayoutParams menuPanelParameters, slidingPanelParameters;
	
	/** The list view parameters. */
	LinearLayout.LayoutParams headerPanelParameters, listViewParameters;
	
	/** The iv settings. */
	private ImageView ivMenu,ivSettings;
	
	/** The ll container. */
	private LinearLayout llContainer;
	
	/** The lv menu. */
	ListView lvMenu;
	
	/** The sensor data controller. */
	private SensorDataController sensorDataController;
	
	private AirPurifierController airPurifierController ;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		llContainer = (LinearLayout) findViewById(R.id.llContainer);

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
		ivMenu = (ImageView) findViewById(R.id.ivLeftMenu);
		ivMenu.setOnClickListener(this);
		ivSettings = (ImageView) findViewById(R.id.ivRightDeviceIcon);
		ivSettings.setOnClickListener(this);
		initializeMenuList();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.llContainer, new HomeFragment(), HomeFragment.TAG)
				.commit();
		sensorDataController = SensorDataController.getInstance(this);
		sensorDataController.startPolling();

		airPurifierController = new AirPurifierController(this) ;
		airPurifierController.getFilterStatus() ;
	}

	/**
	 * OnPause of this Activity unRegister the listener.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		//sensorDataController.unRegisterListener(this);

	}

	/**
	 * OnDestroy of the Activity stop polling the Air Purifier.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		sensorDataController.stopPolling();
	}

	/**
	 * OnResume of BaseActivity register the listener.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		//sensorDataController.registerListener(this);
	}

	/**
	 * Initialize menu list.
	 */
	public void initializeMenuList() {
		lvMenu = (ListView) findViewById(R.id.lvMenu);
		lvMenu.setOnItemClickListener(this);
		Utils.getIconArray();
		Utils.getLabelArray();
		lvMenu.setAdapter(new MenuListAdapter(getApplicationContext(), 0, Utils
				.getIconArray(), Utils.getLabelArray()));
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		switch (position) {
		case AppConstants.HOME:
			FragmentTransaction ftHome = getSupportFragmentManager()
					.beginTransaction();
			Log.i(TAG, "On Item Click --- HOME ");
			collapse();
			isExpanded = false;
			ftHome.replace(R.id.slidingPanel, new HomeFragment(),
					HomeFragment.TAG);
			// ftHome.addToBackStack(null);
			ftHome.commit();
			break;
		case AppConstants.MYCITIES:
			FragmentTransaction ftCity = getSupportFragmentManager()
					.beginTransaction();
			Log.i(TAG, "On Item Click --- CITY");
			collapse();
			// ftCity.replace(R.id.slidingPanel, new CityListFragment());
			// ftCity.addToBackStack(null);
			ftCity.commit();
			isExpanded = false;
			break;
		case AppConstants.ABOUTAQI:
			Log.i(TAG, "On Item Click --- ABout AQI ");
			collapse();
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

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivLeftMenu:
		/*case R.id.ivLeftMenu:
			if (!isExpanded) {
				isExpanded = true;
				expand();
			} else {
				isExpanded = false;
				collapse();

			}
			break;*/

		case R.id.ivRightDeviceIcon:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		}

	}

	/**
	 * Collapse.
	 */
	private void collapse() {
		// Collapse
		new CollapseAnimation(slidingPanel, menuWidth,
				TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
		new CollapseAnimation(llContainer, menuWidth,
				TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
	}

	/**
	 * Expand.
	 */
	private void expand() {
		// Expand
		new ExpandAnimation(slidingPanel, menuWidth,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.75f, 0, 0.0f, 0, 0.0f);
		new ExpandAnimation(llContainer, menuWidth, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);
	}

	/**
	 * OnSensor Event Callback update the User Interface.
	 *
	 * @param airPurifierEventDto the air purifier event dto
	 */
	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) {		

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (isExpanded) {
			new CollapseAnimation(slidingPanel, menuWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
			isExpanded = !isExpanded;
		} else
			super.onBackPressed();
	}

	@Override
	public void filterStatusUpdated(FilterStatusDto filterStatusData) {
		// TODO Auto-generated method stub
		
	}

}

package com.philips.cl.di.dev.pa.activity;


import java.util.ArrayList;
import java.util.List;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.ListItemAdapter;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.PairingHandler;
import com.philips.cl.di.dev.pa.cpp.PairingListener;
import com.philips.cl.di.dev.pa.cpp.SignonListener;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEvent;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEventListener;
import com.philips.cl.di.dev.pa.dashboard.HomeFragment;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.demo.AppInDemoMode;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateActivity;
import com.philips.cl.di.dev.pa.fragment.AirQualityFragment;
import com.philips.cl.di.dev.pa.fragment.BuyOnlineFragment;
import com.philips.cl.di.dev.pa.fragment.CancelDialogFragment;
import com.philips.cl.di.dev.pa.fragment.HelpAndDocFragment;
import com.philips.cl.di.dev.pa.fragment.ManagePurifierFragment;
import com.philips.cl.di.dev.pa.fragment.NotificationsFragment;
import com.philips.cl.di.dev.pa.fragment.OutdoorLocationsFragment;
import com.philips.cl.di.dev.pa.fragment.ProductRegistrationStepsFragment;
import com.philips.cl.di.dev.pa.fragment.SettingsFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowVirginFragment;
import com.philips.cl.di.dev.pa.fragment.ToolsFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectPurifier;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice.PAIRED_STATUS;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.EWS_STATE;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PURIFIER_EVENT;
import com.philips.cl.di.dev.pa.outdoorlocations.AddOutdoorLocationActivity;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.registration.CreateAccountFragment;
import com.philips.cl.di.dev.pa.registration.UserRegistrationActivity;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.RightMenuClickListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;
import com.philips.cl.di.dev.pa.view.FilterStatusView;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dev.pa.view.ListViewItem;

public class MainActivity extends BaseActivity implements AirPurifierEventListener, SignonListener, 
		PairingListener, DiscoveryEventListener, NetworkStateListener, DrawerEventListener {

	private static int screenWidth, screenHeight;

	private DrawerLayout mDrawerLayout;
	private ListView mListViewLeft;
	private ScrollView mScrollViewRight;
	private RightMenuClickListener rightMenuClickListener;

	/** Right off canvas elements */
	private TextView tvAirStatusAqiValue;
	private TextView tvConnectionStatus;
	private TextView tvAirStatusMessage;
	private ImageView ivAirStatusBackground;
	private ImageView ivConnectedImage;
	private ImageView ivConnectionError;
	private ToggleButton remoteControlBtn;

	/**
	 * Action bar
	 */
	private ImageView rightMenu;
	private ImageView leftMenu;
	private ImageView addLocation;
	private ImageView backToHome;
	private FontTextView noOffFirmwareUpdate;

	/** Filter status bars */
	private FilterStatusView preFilterView, multiCareFilterView,
	activeCarbonFilterView, hepaFilterView;

	// Filter status texts
	private TextView preFilterText, multiCareFilterText,
	activeCarbonFilterText, hepaFilterText;

	private boolean mRightDrawerOpened, mLeftDrawerOpened;

	private MenuItem rightMenuItem;
	private int mVisits;

	public boolean isTutorialPromptShown = false;

	public boolean isDiagnostics;

	private ProgressDialog progressDialog;
	private ProgressBar airPortTaskProgress;
	private AppInDemoMode appInDemoMode;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ALog.i(ALog.MAINACTIVITY, "onCreate mainActivity");
		setContentView(R.layout.activity_main_aj);

		mVisits = Utils.getNoOfVisit();
		Utils.saveNoOfVisit(mVisits);

		appInDemoMode = new AppInDemoMode(MainActivity.this);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		setScreenWidth(displayMetrics.widthPixels);
		setScreenHeight(displayMetrics.heightPixels);

		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.MAINACTIVITY, "Actionbar: " + e.getMessage());
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		mDrawerLayout.setScrimColor(Color.parseColor("#60FFFFFF"));
		mDrawerLayout.setFocusableInTouchMode(false);

		mDrawerLayout.setDrawerListener(DrawerAdapter.getInstance());

		/** Initialise left menu items and click listener */
		mListViewLeft = (ListView) findViewById(R.id.left_menu_listView);
		mListViewLeft.setAdapter(new ListItemAdapter(this, getLeftMenuItems()));
		mListViewLeft.setOnItemClickListener(new MenuItemClickListener());

		/** Initiazlise right menu items and click listener */
		mScrollViewRight = (ScrollView) findViewById(R.id.right_menu_scrollView);
		rightMenuClickListener = new RightMenuClickListener(this);

		ViewGroup group = (ViewGroup) findViewById(R.id.right_menu_layout);
		setAllButtonListener(group);

		tvAirStatusAqiValue = (TextView) findViewById(R.id.tv_rm_air_status_aqi_value);
		tvConnectionStatus = (TextView) findViewById(R.id.tv_connection_status);
		tvAirStatusMessage = (TextView) findViewById(R.id.tv_rm_air_status_message);
		ivAirStatusBackground = (ImageView) findViewById(R.id.iv_rm_air_status_background);
		ivConnectedImage = (ImageView) findViewById(R.id.iv_connection_status);
		ivConnectionError =  (ImageView) findViewById(R.id.iv_connection_error);
		initFilterStatusViews();

		showFirstFragment();
		initializeCPPController();
		selectPurifier();
		//		checkForUpdatesHockeyApp();
		
	}

	private void selectPurifier() {
		PurAirDevice current = getCurrentPurifier();
		if (PurAirApplication.isDemoModeEnable()) {
			if (current != null) current.setConnectionState(ConnectionState.DISCONNECTED);
			appInDemoMode.connectPurifier();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (PurAirApplication.isDemoModeEnable()) {
			startDemoMode();
		} else if (UserRegistrationController.getInstance().isUserLoggedIn()) {
			startNormalMode();
		}
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.llContainer);

		setActionBar(fragment);

		DrawerAdapter.getInstance().addDrawerListener(this);

		removeFirmwareUpdateUI();
		hideFirmwareUpdateHomeIcon();
		updatePurifierUIFields() ;
		// Enable for release build
//		checkForCrashesHockeyApp(); 
		
	}

	private void startDemoMode() {
		if (appInDemoMode != null) {
			appInDemoMode.addNetworkListenerForDemoMode();
		}

		NetworkReceiver.getInstance().addNetworkStateListener(this);
		PurifierManager.getInstance().addAirPurifierEventListener(this);
		DiscoveryManager.getInstance().stop();
	}

	private void stopDemoMode() {
		if (appInDemoMode != null) {
			appInDemoMode.rmoveNetworkListenerForDemoMode();
		}
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
	}

	private void stopNormalMode() {
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		DiscoveryManager.getInstance().stop();
	}

	private void startNormalMode() {
		NetworkReceiver.getInstance().addNetworkStateListener(this);
		DiscoveryManager.getInstance().start(this);
		PurifierManager.getInstance().addAirPurifierEventListener(this);
	}

	public void setActionBar(Fragment fragment) {
		ALog.i(ALog.MAINACTIVITY, "setActionBar$fragment " + fragment);

		if(fragment instanceof OutdoorLocationsFragment) {
			setRightMenuVisibility(View.INVISIBLE);
			addLocation.setVisibility(View.VISIBLE);
		} else if(!(fragment instanceof HomeFragment)) {
			setRightMenuVisibility(View.INVISIBLE);
		} else {
			if (PurAirApplication.isDemoModeEnable()) {
				addLocation.setVisibility(View.INVISIBLE);
//				setRightMenuVisibility(View.VISIBLE);
				stopNormalMode();
				startDemoMode();
			} else if (Utils.getAppFirstUse()) {
				setRightMenuVisibility(View.INVISIBLE);
			} else {
				addLocation.setVisibility(View.INVISIBLE);
//				setRightMenuVisibility(View.VISIBLE);
				if (fragment instanceof SettingsFragment) {
					stopDemoMode();
					startNormalMode();
				}
			}
		}
	}

	public void setRightMenuVisibility(int visibility) {
		rightMenu.setVisibility(visibility);
	}

	@Override
	protected void onResumeFragments() {
		ALog.i(ALog.MAINACTIVITY, "onResumeFragments") ;
		super.onResumeFragments();
		if (PurifierManager.getInstance().getEwsState() == EWS_STATE.EWS) {
			PurifierManager.getInstance().setEwsSate(EWS_STATE.NONE);
			showFirstFragment();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SetupDialogFactory.getInstance(this).cleanUp();
//		IndoorFragment.resetActivity();

		if(UserRegistrationController.getInstance().isUserLoggedIn()
				|| PurAirApplication.isDemoModeEnable()) {
			appInDemoMode.rmoveNetworkListenerForDemoMode();
			stopNormalMode();
		}
	}

	@Override
	protected void onStop() {
		if (progressDialog != null) {
			progressDialog.cancel();
		}

		DrawerAdapter.getInstance().removeDrawerListener(this);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		CPPController.getInstance(getApplicationContext()).removeSignOnListener(this);
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.llContainer);

		if (fragment instanceof OutdoorLocationsFragment
				&& android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
			invalidateOptionsMenu();
		}

		if (mLeftDrawerOpened || mRightDrawerOpened) {
			mDrawerLayout.closeDrawer(mListViewLeft);
			mDrawerLayout.closeDrawer(mScrollViewRight);
			mLeftDrawerOpened = false;
			mRightDrawerOpened = false;
		} else if (fragment instanceof StartFlowVirginFragment) {
			OutdoorManager.getInstance().clearCityOutdoorInfo() ;
			ConnectPurifier.reset() ;
			finish();
		} else if (!(fragment instanceof HomeFragment)
				&& !(fragment instanceof ProductRegistrationStepsFragment)) {
			manager.popBackStackImmediate(null,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			showFirstFragment();
		} else if (fragment instanceof ProductRegistrationStepsFragment) {
			manager.popBackStack();
		} else {
			OutdoorManager.getInstance().clearCityOutdoorInfo() ;
			ConnectPurifier.reset() ;
			finish();
		}
	}

	private void showFirstFragment() {
		boolean firstUse = Utils.getAppFirstUse();

		if (PurAirApplication.isDemoModeEnable() || !firstUse) {
			showDashboardFragment();
		} else {
			showVirginFlowFragment();
		}
	}

	private void showVirginFlowFragment() {
		showFragment(new StartFlowVirginFragment()) ;
		setTitle(getString(R.string.dashboard_title));
	}

	private void showDashboardFragment() {
		showFragment(getDashboard());
//		setDashboardActionbarIconVisible();
		setTitle(getString(R.string.dashboard_title));
	}

	private void initializeCPPController() {
		CPPController.getInstance(this).setDefaultDcsState() ;
		CPPController.getInstance(this).addSignOnListener(this) ;
	}

	public DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

	public ScrollView getScrollViewRight() {
		return mScrollViewRight;
	}

	public HomeFragment getDashboard() {
		return new HomeFragment();
	}

	/**
	 * @param viewGroup
	 *            loops through the entire view group and adds an
	 *            onClickListerner to the buttons.
	 */
	public void setAllButtonListener(ViewGroup viewGroup) {
		View v;
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			v = viewGroup.getChildAt(i);
			if (v instanceof ViewGroup) {
				setAllButtonListener((ViewGroup) v);
			} else if (v instanceof Button) {
				((Button) v).setOnClickListener(rightMenuClickListener);
			}
		}
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

		rightMenu.setOnClickListener(actionBarClickListener);
		leftMenu.setOnClickListener(actionBarClickListener);
		backToHome.setOnClickListener(actionBarClickListener);
		addLocation.setOnClickListener(actionBarClickListener);
		airPortTaskProgress = (ProgressBar) viewActionbar.findViewById(R.id.actionbar_progressBar);

		actionBar.setCustomView(viewActionbar);
	}

	public void setVisibilityAirPortTaskProgress(final int state) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				airPortTaskProgress.setVisibility(state);
			}
		});
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.right_menu_img:

				if(Utils.getAppFirstUse() && !PurAirApplication.isDemoModeEnable()) return;

				if (mRightDrawerOpened) {
					mDrawerLayout.closeDrawer(mScrollViewRight);
				} else {
					mDrawerLayout.closeDrawer(mListViewLeft);
					mDrawerLayout.openDrawer(mScrollViewRight);
				}
				break;
			case R.id.left_menu_img:
				if (mLeftDrawerOpened) {
					mDrawerLayout.closeDrawer(mListViewLeft);
				} else {
					mDrawerLayout.closeDrawer(mScrollViewRight);
					mDrawerLayout.openDrawer(mListViewLeft);
				}
				break;
			case R.id.add_location_img:
				//TODO : Drop down city list
				Intent intent = new Intent(MainActivity.this, AddOutdoorLocationActivity.class);
				startActivity(intent);
				break;
			case R.id.back_to_home_img:
				//TODO
				break;
			case R.id.btn_rm_remote_enable:
				if (getCurrentPurifier() == null) return;
				ALog.i(ALog.MAINACTIVITY, "Remote control: " + remoteControlBtn.isChecked());
				if(getCurrentPurifier().getConnectionState()==ConnectionState.CONNECTED_REMOTELY){
					ALog.i(ALog.MAINACTIVITY, "Remote control: connected remotely");
					return;
				}
				if (remoteControlBtn.isChecked()) {
					PurAirDevice purifier=getCurrentPurifier();
					if(purifier==null) return;
										
					purifier.setPairing(PurAirDevice.PAIRED_STATUS.NOT_PAIRED);
					setVisibilityAirPortTaskProgress(View.VISIBLE);
					pairToPurifierIfNecessary();
				}else{
					if (getCurrentPurifier().getPairedStatus()== PurAirDevice.PAIRED_STATUS.PAIRED) {
						showDialogFragment(CancelDialogFragment
								.newInstance(getString(R.string.pair_disable_alert), 1, remoteControlBtn));
					}					
				}

				break;
			default:
				break;
			}
		}
	}; 

	private void showDialogFragment(Fragment fragment) {
		try {
			FragmentManager fragMan = getSupportFragmentManager();
			fragMan.beginTransaction().add(
					fragment, "dialogfragment").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.MAINACTIVITY, e.getMessage());
		}
	}

	private void setDashboardActionbarIconVisible() {
		rightMenu.setVisibility(View.INVISIBLE);
		leftMenu.setVisibility(View.VISIBLE);
		backToHome.setVisibility(View.INVISIBLE);
		addLocation.setVisibility(View.INVISIBLE);
	}

	/** Create the left menu items. */
	private List<ListViewItem> getLeftMenuItems() {
		List<ListViewItem> leftMenuItems = new ArrayList<ListViewItem>();

		leftMenuItems.add(new ListViewItem(R.string.list_item_home,
				R.drawable.icon_1_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_air_quality_explained,
				R.drawable.icon_2_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_outdoor_loc,
				R.drawable.icon_3_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_notifications,
				R.drawable.icon_4_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_help_and_doc,
				R.drawable.icon_5_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_settings,
				R.drawable.icon_6_2x));
		// TODO : Add this when enabling non-mandatory firmware update
		//		leftMenuItems.add(new ListViewItem(R.string.list_item_firmware,
		//				R.drawable.icon_8_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_manage_purifier,
				R.drawable.icon_7_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_user_reg,
				R.drawable.icon_8_2x));		
		leftMenuItems.add(new ListViewItem(R.string.list_item_buy_online,
				R.drawable.icon_9_2x));
		leftMenuItems.add(new ListViewItem(R.string.tools, R.drawable.icon_6_2x));
		return leftMenuItems;
	}

	private void setRightMenuAQIValue(float indoorAQI) {
		tvAirStatusAqiValue.setTextSize(18.0f);
		if (indoorAQI <= 1.4f) {
			tvAirStatusAqiValue.setText(getString(R.string.good));
		} else if (indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			tvAirStatusAqiValue.setText(getString(R.string.moderate));
		} else if (indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			tvAirStatusAqiValue.setText(getString(R.string.unhealthy));
		} else if (indoorAQI > 3.5f) {
			tvAirStatusAqiValue.setText(getString(R.string.very_unhealthy_split));
		}
	}

	private void setRightMenuAirStatusMessage(String message) {
		tvAirStatusMessage.setText(message);
	}

	private void setRightMenuAirStatusBackground(float indoorAQI) {
		Drawable imageDrawable = null;

		if (indoorAQI <= 1.4f) {
			imageDrawable = getResources().getDrawable(
					R.drawable.aqi_small_circle_2x);
		} else if (indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			imageDrawable = getResources().getDrawable(
					R.drawable.aqi_small_circle_100_150_2x);
		} else if (indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			imageDrawable = getResources().getDrawable(
					R.drawable.aqi_small_circle_200_300_2x);
		} else if (indoorAQI > 3.5f) {
			imageDrawable = getResources().getDrawable(
					R.drawable.aqi_small_circle_300_500_2x);
		}
		ivAirStatusBackground.setImageDrawable(imageDrawable);
	}

	private void updateRightMenuConnectedStatus() {
		PurAirDevice purifier = this.getCurrentPurifier();
		ConnectionState purifierState = ConnectionState.DISCONNECTED;
		if (purifier != null) purifierState = purifier.getConnectionState();

		final ConnectionState newState = purifierState;
		ALog.i(ALog.MAINACTIVITY, "Connection status: " + purifierState);
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ivConnectionError.setVisibility(View.GONE);
				switch (newState) {
				case CONNECTED_LOCALLY:
					tvConnectionStatus.setText(getString(R.string.connected));
					ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_blue_2x));
					rightMenu.setImageDrawable(getResources().getDrawable(R.drawable.right_bar_icon_blue_2x)); 
					ALog.d(ALog.MAINACTIVITY, "Updating right menu to connected");
					break;
				case CONNECTED_REMOTELY:
					tvConnectionStatus.setText(getString(R.string.connected_via_philips));
					ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_blue_2x));
					rightMenu.setImageDrawable(getResources().getDrawable(R.drawable.right_bar_icon_blue_2x));
					ALog.d(ALog.MAINACTIVITY, "Updating right menu to connected via philips");
					break;
				case DISCONNECTED:
					tvConnectionStatus.setText(getString(R.string.not_connected));
					ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_lost_connection_2x));
					rightMenu.setImageDrawable(getResources().getDrawable(R.drawable.right_bar_icon_orange_2x));
					setRightMenuAirStatusMessage(getString(R.string.rm_air_quality_message));
					rightMenuClickListener.toggleControlPanel(false, null);
					ALog.d(ALog.MAINACTIVITY, "Updating right menu to disconnected");
					break;
				}
			}
		});
	}

	private void initFilterStatusViews() {
		preFilterView = (FilterStatusView) findViewById(R.id.iv_pre_filter);
		multiCareFilterView = (FilterStatusView) findViewById(R.id.iv_multi_care_filter);
		activeCarbonFilterView = (FilterStatusView) findViewById(R.id.iv_active_carbon_filter);
		hepaFilterView = (FilterStatusView) findViewById(R.id.iv_hepa_filter);

		preFilterText = (TextView) findViewById(R.id.tv_rm_pre_filter_status);
		multiCareFilterText = (TextView) findViewById(R.id.tv_rm_multi_care_filter_status);
		activeCarbonFilterText = (TextView) findViewById(R.id.tv_rm_active_carbon_filter_status);
		hepaFilterText = (TextView) findViewById(R.id.tv_rm_hepa_filter_status);

		remoteControlBtn = (ToggleButton) findViewById(R.id.btn_rm_remote_enable);
		remoteControlBtn.setOnClickListener(actionBarClickListener);
	}

	public void showFragment(Fragment fragment) {
		try {
			getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.llContainer, fragment, fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag()) ;
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.MAINACTIVITY, e.getMessage());
		}

		setActionBar(fragment);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		}		
	}

	public void setTitle(String title) {
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		textView.setTypeface(Fonts.getGillsansLight(MainActivity.this));
		textView.setTextSize(24);
		textView.setText(title);
		super.setTitle(title);
	}

	/** Left menu item clickListener */
	private class MenuItemClickListener implements OnItemClickListener {

		private List<Fragment> leftMenuItems = new ArrayList<Fragment>();

		public MenuItemClickListener() {
			initLeftMenu();
		}

		private void initLeftMenu() {
			leftMenuItems.add(getDashboard());
			leftMenuItems.add(new AirQualityFragment());
			leftMenuItems.add(new OutdoorLocationsFragment());
			leftMenuItems.add(new NotificationsFragment());
			leftMenuItems.add(new HelpAndDocFragment());
			leftMenuItems.add(new SettingsFragment());
			leftMenuItems.add(new ManagePurifierFragment());
			leftMenuItems.add(new CreateAccountFragment());
			leftMenuItems.add(new BuyOnlineFragment());
			leftMenuItems.add(new ToolsFragment());
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View listItem,
				int position, long id) {
//			setDashboardActionbarIconVisible();
			mDrawerLayout.closeDrawer(mListViewLeft);
			switch (position) {
			case 0:
				if (PurAirApplication.isDemoModeEnable()) {
					showFragment(leftMenuItems.get(position));
				} else if (!Utils.getAppFirstUse()) {
					showFragment(leftMenuItems.get(position));
				} else {
					showFragment(new StartFlowVirginFragment());
				}
				setTitle(getString(R.string.dashboard_title));
				break;
			case 1:
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_air_quality_explained));
				break;
			case 2:
				// Outdoor locations
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_outdoor_loc));
				break;
			case 3:
				// Notifications
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_notifications));
				break;
			case 4:
				// Help and documentation
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_help_and_doc));
				break;
			case 5:
				// Settings
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_settings));
				break;
			case 6:
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_manage_purifier));
				break;
			case 7:
				// User registration
				Intent userRegistrationIntent = new Intent(MainActivity.this, UserRegistrationActivity.class);
				startActivity(userRegistrationIntent);
				break;
			case 8:
				// Buy Online
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_buy_online));
				break;
			case 9:
				// Tools
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.tools));
				break;
			default:
				break;
			}
		}
	}

	public void startFirmwareUpgradeActivity() {
		if (getCurrentPurifier() == null) {
			ALog.d(ALog.MAINACTIVITY, "Did not start FirmwareUpdateActivity - Current Purifier null");
			Toast.makeText(MainActivity.this, R.string.firmware_toast_nodeviceselected, Toast.LENGTH_SHORT).show();
			return;
		}
		Intent firmwareIntent = new Intent(MainActivity.this, FirmwareUpdateActivity.class);
		startActivity(firmwareIntent);
	}

	private void removeFirmwareUpdateUI() {
		setFirmwareSuperScript(0, false);
	}

	private static void setScreenWidth(int width) {
		screenWidth = width;
	}
	private static void setScreenHeight(int height) {
		screenHeight = height;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}
	public static int getScreenHeight() {
		return screenHeight;
	}

	@Override
	public void onAirPurifierChanged() {
		ALog.d(ALog.MAINACTIVITY, "AirPurifier Change - updating UI") ;
		updatePurifierUIFields();
	}

	@Override
	public void onAirPurifierEventReceived() {
		ALog.d(ALog.MAINACTIVITY, "AirPurifier event received - updating UI") ;
		updatePurifierUIFields();
	}

	@Override
	public void onFirmwareEventReceived() {
		final PurAirDevice purifier = getCurrentPurifier();

		ALog.i(ALog.FIRMWARE, "onFirmwareEventReceived: " + purifier);
	}

	@Override
	public void onErrorOccurred(PURIFIER_EVENT purifierEvent) {
		if (getCurrentPurifier() == null 
				|| getCurrentPurifier().getConnectionState() == ConnectionState.DISCONNECTED) return;

		if( purifierEvent == PURIFIER_EVENT.DEVICE_CONTROL) {
			setVisibilityAirPortTaskProgress(View.INVISIBLE) ;
			ivConnectionError.setVisibility(View.VISIBLE);
			tvConnectionStatus.setText(getString(R.string.lost_connection));
			ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_gray_2x));
		}
	}

	private void hideFirmwareUpdateHomeIcon() {
		noOffFirmwareUpdate.setVisibility(View.INVISIBLE);
	}

	private void updatePurifierUIFields() {
		ALog.i(ALog.MAINACTIVITY, "updatePurifierUIFields");

		final PurAirDevice purifier = getCurrentPurifier();

		Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.llContainer);
		if(fragment instanceof BuyOnlineFragment){
			((BuyOnlineFragment)fragment).updateFilterStatus(purifier); 
		}

		if(purifier == null || purifier.getConnectionState() == ConnectionState.DISCONNECTED) {
			disableRightMenuControls();
			return ;
		}

		ALog.i(ALog.MAINACTIVITY, "Current connectionstate for UI update: " + getCurrentPurifier().getConnectionState());
		final AirPortInfo info = getAirPortInfo(purifier);
		if (info == null) {
			disableRightMenuControls();
			return;
		}

		updateRightMenuConnectedStatus();
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setVisibilityAirPortTaskProgress(View.INVISIBLE) ;
				float indoorAQIUsableValue = info.getIndoorAQI() / 10.0f;
				setRightMenuAQIValue(indoorAQIUsableValue);
				rightMenuClickListener.setSensorValues(info);
				updateFilterStatus(info.getFilterStatus1(),
						info.getFilterStatus2(),
						info.getFilterStatus3(),
						info.getFilterStatus4());
				setRightMenuAirStatusMessage(getString(
						Utils.getIndoorAQIMessage(indoorAQIUsableValue),
						getString(R.string.philips_home)));
				setRightMenuAirStatusBackground(indoorAQIUsableValue);
				rightMenuClickListener.toggleControlPanel(true,info);
				
				if(purifier.getConnectionState()==ConnectionState.CONNECTED_REMOTELY){
					remoteControlBtn.setChecked(true);
					remoteControlBtn.setEnabled(false);
					return;
				}

				if(PurAirApplication.isDemoModeEnable()){
					remoteControlBtn.setClickable(false);
					remoteControlBtn.setChecked(false);
					return;
				}
				
				//For remote control enable and disable in right-canvas
				remoteControlBtn.setClickable(true);
				remoteControlBtn.setEnabled(true);
				if(purifier.getPairedStatus()==PurAirDevice.PAIRED_STATUS.PAIRED)
				{
					remoteControlBtn.setChecked(true);
				}else{
					remoteControlBtn.setChecked(false);
				}
			}
		});

		if(purifier.getPairedStatus()!=PurAirDevice.PAIRED_STATUS.NOT_PAIRED) return;
		ALog.i(ALog.PAIRING, "In updatePurifierUIFields(): "+ purifier.getPairedStatus());
		pairToPurifierIfNecessary();
	}

	private void disableRightMenuControls() {

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setVisibilityAirPortTaskProgress(View.INVISIBLE) ;
				ivConnectionError.setVisibility(View.GONE);
				tvConnectionStatus.setText(getString(R.string.not_connected));
				ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_lost_connection_2x));
				rightMenu.setImageDrawable(getResources().getDrawable(R.drawable.right_bar_icon_orange_2x));
				setRightMenuAirStatusMessage(getString(R.string.rm_air_quality_message));
				rightMenuClickListener.toggleControlPanel(false, null);
				ALog.d(ALog.MAINACTIVITY, "Updating right menu to disconnected");
				rightMenuClickListener.toggleControlPanel(false , null);
				setRightMenuAirStatusMessage(AppConstants.EMPTY_STRING);
				ivAirStatusBackground.setImageResource(R.drawable.aqi_small_circle_grey);
				tvAirStatusAqiValue.setTextSize(14.0f);
				tvAirStatusAqiValue.setText(getString(R.string.no_connection));
				//For remote control enable and disable in right-canvas
				remoteControlBtn.setClickable(false);
				remoteControlBtn.setChecked(false);
				remoteControlBtn.setEnabled(false);
			}
		});
	}

	private void updateFilterStatus(int preFilterStatus,
			int multiCareFilterStatus, int activeCarbonFilterStatus,
			int hepaFilterStatus) {
		/** Update filter bars */
		preFilterView.setPrefilterValue(preFilterStatus);
		multiCareFilterView.setMultiCareFilterValue(multiCareFilterStatus);
		activeCarbonFilterView
		.setActiveCarbonFilterValue(activeCarbonFilterStatus);
		hepaFilterView.setHEPAfilterValue(hepaFilterStatus);

		/** Update filter texts */
		preFilterText.setText(Utils.getPreFilterStatusText(preFilterStatus));
		multiCareFilterText.setText(Utils
				.getMultiCareFilterStatusText(multiCareFilterStatus));
		activeCarbonFilterText.setText(Utils
				.getActiveCarbonFilterStatusText(activeCarbonFilterStatus));
		hepaFilterText.setText(Utils
				.getHEPAFilterFilterStatusText(hepaFilterStatus));
	}

	private AirPortInfo getAirPortInfo(PurAirDevice purifier) {
		if (purifier == null) return null;
		return purifier.getAirPortInfo();
	}

	public void setRightMenuVisibility(boolean visible) {
		rightMenuItem.setVisible(visible);
	}

	public int getVisits() {
		return mVisits;
	}

	public String getVersionNumber() {
		String versionCode = "";
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(),
					0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	private void setFirmwareSuperScript(int superscriptNumber, boolean isUpdateAvailable) {
		ListItemAdapter adapter = (ListItemAdapter) mListViewLeft.getAdapter();
		ListViewItem item = adapter.getItem(6);
		adapter.remove(item);
		if(!isUpdateAvailable) {
			item.setSuperScriptNumber(0);
		} else {
			item.setSuperScriptNumber(superscriptNumber);
		}
		adapter.insert(item, 6);
		adapter.notifyDataSetChanged();
		ALog.i(ALog.FIRMWARE, "LeftMenuList$firmwareItem " + item.getTextId());
	}

	@Override
	public void signonStatus(boolean signon) {		
		if (signon) {
			//should be called only when signOn is successful
			PurifierManager.getInstance().startSubscription(); // TODO fix
		}
	}

	private void pairToPurifierIfNecessary() {
		if (!CPPController.getInstance(this).isSignOn()){
			setProgressDialogInvisible();
			return;
		}

		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null || purifier.getConnectionState() != ConnectionState.CONNECTED_LOCALLY) {
			setProgressDialogInvisible();
			return;
		}
		
		ALog.i(ALog.PAIRING, "In PairToPurifier: "+ purifier.getPairedStatus());

		long lastPairingCheckTime = purifier.getLastPairedTime();		
		long diffInDays = Utils.getDiffInDays(lastPairingCheckTime);
		// First time pairing or on EWS and Everyday check for pairing
		if( purifier.getPairedStatus()==PurAirDevice.PAIRED_STATUS.NOT_PAIRED || diffInDays != 0) {
			purifier.setPairing(PurAirDevice.PAIRED_STATUS.PAIRING);
			startPairing(purifier);			
		}else{
			setProgressDialogInvisible();
		}
	}
	
	
	public void setProgressDialogInvisible(){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setVisibilityAirPortTaskProgress(View.INVISIBLE);
			}
		});
	}

	public void startPairing(PurAirDevice purifier) {
		if (purifier == null) return; // TODO why can this happen?

		PairingHandler pm = new PairingHandler(this, purifier);
		pm.startPairing();
	}

	@Override
	public void onPairingSuccess() {	
		if (getCurrentPurifier() != null && getCurrentPurifier().getConnectionState()==ConnectionState.CONNECTED_REMOTELY 
				&& getCurrentPurifier().getPairedStatus()==PAIRED_STATUS.UNPAIRED) {
			getCurrentPurifier().setConnectionState(ConnectionState.DISCONNECTED);
		}
		updatePurifierUIFields();
	}

	@Override
	public void onPairingFailed() {		
		updatePurifierUIFields();
	}

	public PurAirDevice getCurrentPurifier() {
		// TODO change to field in class
		return PurifierManager.getInstance().getCurrentPurifier();
	}

	//Added for checking right menu icon is orange
	public boolean getRightMenuDisconnectionState() {
		Drawable rightMenuDrawable = rightMenu.getDrawable();
		Drawable disconnRightMenuDrawable = getResources().getDrawable(R.drawable.right_bar_icon_orange_2x);
		if (disconnRightMenuDrawable == null) return false;
		return disconnRightMenuDrawable.getConstantState().equals(rightMenuDrawable.getConstantState());
	}


	@Override
	public void onDiscoveredDevicesListChanged() {
		ALog.d(ALog.MAINACTIVITY, "**************************");
		if (PurAirApplication.isDemoModeEnable()) return;

		DiscoveryManager.getInstance().printDiscoveredDevicesInfo(ALog.MAINACTIVITY);

		ArrayList<PurAirDevice> devices = DiscoveryManager.getInstance().getDiscoveredDevices();
		if (devices.size() <= 0) return;
		ALog.i(ALog.APP_START_UP, "MainAcitivty$onDiscoveredDevicesListChanged devices list size "
				+ devices.size() + " :: " + devices);

		PurAirDevice current = getCurrentPurifier();
		if( current != null && current.getAirPortInfo() != null ) return ;
		PurifierManager.getInstance().startSubscription() ;
	}

	@Override
	public void onConnected(String ssid) {
		ALog.i(ALog.MAINACTIVITY, "onConnected start CPP");
		if (ssid == null) return;
		if (ssid.contains(EWSWifiManager.DEVICE_SSID)) {
			return;
		}

		if (PurAirApplication.isDemoModeEnable()) {
			updateUIInDemoMode();
		} else {
			CPPController.getInstance(this).signOnWithProvisioning();
		}
	}

	@Override
	public void onDisconnected() {
		ALog.i(ALog.MAINACTIVITY, "onDisconnected");
		if (PurAirApplication.isDemoModeEnable()) {
			updateUIInDemoMode();
		}
	}

	private void updateUIInDemoMode() {
		PurAirDevice purifier = PurifierManager.getInstance().getCurrentPurifier();
		if (purifier != null) purifier.setConnectionState(ConnectionState.DISCONNECTED);
		updatePurifierUIFields();
	}

	@Override
	public void onDrawerEvent(DrawerEvent event, View drawerView) {
		switch (event) {
		case DRAWER_CLOSED:
			if (drawerView.getId() == R.id.right_menu_scrollView) {
				ALog.i(ALog.MAINACTIVITY, "Right drawer close");
				mRightDrawerOpened = false;
			} else if (drawerView.getId() == R.id.left_menu_listView) {
				ALog.i(ALog.MAINACTIVITY, "Left drawer close");
				mLeftDrawerOpened = false;
			}
			break;
		case DRAWER_OPENED:
			if (drawerView.getId() == R.id.right_menu_scrollView) {
				mRightDrawerOpened = true;
				ALog.i(ALog.MAINACTIVITY, "Right drawer open");
			} else if (drawerView.getId() == R.id.left_menu_listView) {
				mLeftDrawerOpened = true;
				ALog.i(ALog.MAINACTIVITY, "Left drawer open");
			}
			break;
		case DRAWER_SLIDE_END:
			//TODO : Move showFragment and startFirmwareUpdateActivity here?
			break;
		default:
			break;
		}
	}

	private void checkForCrashesHockeyApp() {
		CrashManager.register(this, AppConstants.HOCKEY_APPID, new CrashManagerListener() {
			public boolean shouldAutoUploadCrashes() {
				return true;
			}
		});
	}

	private void checkForUpdatesHockeyApp() {
		// TODO Remove this for store builds!
		UpdateManager.register(this, AppConstants.HOCKEY_APPID);
	}

}
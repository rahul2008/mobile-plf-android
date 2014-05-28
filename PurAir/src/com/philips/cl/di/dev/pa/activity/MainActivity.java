package com.philips.cl.di.dev.pa.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.ListItemAdapter;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.PairingHandler;
import com.philips.cl.di.dev.pa.cpp.PairingListener;
import com.philips.cl.di.dev.pa.cpp.SignonListener;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEvent;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEventListener;
import com.philips.cl.di.dev.pa.dashboard.HomeFragment;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateActivity;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateFragment;
import com.philips.cl.di.dev.pa.fragment.AirQualityFragment;
import com.philips.cl.di.dev.pa.fragment.BuyOnlineFragment;
import com.philips.cl.di.dev.pa.fragment.HelpAndDocFragment;
import com.philips.cl.di.dev.pa.fragment.NotificationsFragment;
import com.philips.cl.di.dev.pa.fragment.OutdoorLocationsFragment;
import com.philips.cl.di.dev.pa.fragment.PairingDialogFragment;
import com.philips.cl.di.dev.pa.fragment.ProductRegistrationStepsFragment;
import com.philips.cl.di.dev.pa.fragment.SettingsFragment;
import com.philips.cl.di.dev.pa.fragment.ToolsFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.registration.CreateAccountFragment;
import com.philips.cl.di.dev.pa.registration.SignedInFragment;
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

public class MainActivity extends BaseActivity implements AirPurifierEventListener, SignonListener, PairingListener, DiscoveryEventListener, NetworkStateListener, DrawerEventListener {

	private static final String PREFS_NAME = "AIRPUR_PREFS";
	private static final String OUTDOOR_LOCATION_PREFS = "outdoor_location_prefs";

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
	private SharedPreferences mPreferences;
	private int mVisits;

	private SharedPreferences outdoorLocationPrefs;
	private ArrayList<String> outdoorLocationsList;

	public boolean isTutorialPromptShown = false;

	public boolean isDiagnostics;

	private boolean isPairingDialogShown;
	private ProgressDialog progressDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_aj);
		
		mPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		mVisits = mPreferences.getInt("NoOfVisit", 0);
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt("NoOfVisit", ++mVisits);
		editor.commit();

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;

		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.MAINACTIVITY, "Actionbar: " + e.getMessage());
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
		initFilterStatusViews();
		
		getSupportFragmentManager().beginTransaction()
				.add(R.id.llContainer, getDashboard())
				.addToBackStack(null)
				.commit();

		outdoorLocationPrefs = getSharedPreferences(OUTDOOR_LOCATION_PREFS, Context.MODE_PRIVATE);
		HashMap<String, String> outdoorLocationsMap = (HashMap<String, String>) outdoorLocationPrefs.getAll();
		outdoorLocationsList = new ArrayList<String>();
		int size = outdoorLocationsMap.size();
		for (int i = 0; i < size; i++) {
			outdoorLocationsList.add(outdoorLocationsMap.get("" + i));
		}
		outdoorLocationsAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_text, outdoorLocationsList);

		initializeCPPController();
		
		initializeFirstPurifier();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		NetworkReceiver.getInstance().addNetworkStateListener(this);
		DiscoveryManager.getInstance().start(this);
		PurifierManager.getInstance().addAirPurifierEventListener(this);
		DrawerAdapter.getInstance().addDrawerListener(this);
		
		removeFirmwareUpdateUI();
		hideFirmwareUpdateHomeIcon();
		updatePurifierUIFields() ;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (outdoorLocationPrefs != null) {
			Editor editor = outdoorLocationPrefs.edit();
			editor.clear();
			int count = outdoorLocationsAdapter.getCount();
			for (int i = 0; i < count; i++) {
				editor.putString("" + i, outdoorLocationsAdapter.getItem(i));
			}
			editor.commit();
		}
		SetupDialogFactory.getInstance(this).cleanUp();
		
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
		
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		DiscoveryManager.getInstance().stop();

		DrawerAdapter.getInstance().removeDrawerListener(this);
	}

	@Override
	protected void onStop() {
		if (progressDialog != null) {
			progressDialog.cancel();
		}
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
		} else if (!(fragment instanceof HomeFragment)
				&& !(fragment instanceof ProductRegistrationStepsFragment)) {
			manager.popBackStackImmediate(null,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			showFragment(getDashboard());
			setDashboardActionbarIconVisible();
			setTitle(getString(R.string.dashboard_title));
		} else if (fragment instanceof ProductRegistrationStepsFragment) {
			manager.popBackStack();
		} else {
			if (PurAirApplication.isDemoModeEnable()) {
				PurAirApplication.setDemoModeEnable(false);
				PurifierManager.getInstance().removeCurrentPurifier();
			}
			finish();
		}
	}

	private void initializeCPPController() {
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
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setIcon(null);
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
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
		mActionBar.setCustomView(viewActionbar);
		
	}
	
	private OnClickListener actionBarClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.right_menu_img:
				
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
				break;
			case R.id.back_to_home_img:
				//TODO
				break;
			default:
				break;
			}
			
		}
	}; 
	
	private void setDashboardActionbarIconVisible() {
		rightMenu.setVisibility(View.VISIBLE);
		leftMenu.setVisibility(View.VISIBLE);
		backToHome.setVisibility(View.INVISIBLE);
		addLocation.setVisibility(View.INVISIBLE);
	}

	/** Create the left menu items. */
	private List<ListViewItem> getLeftMenuItems() {
		List<ListViewItem> leftMenuItems = new ArrayList<ListViewItem>();

		leftMenuItems.add(new ListViewItem(R.string.list_item_home,
				R.drawable.icon_1_2x));
		leftMenuItems
		.add(new ListViewItem(R.string.list_item_air_quality_explained,
				R.drawable.icon_2_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_outdoor_loc,
				R.drawable.icon_3_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_notifications,
				R.drawable.icon_4_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_help_and_doc,
				R.drawable.icon_5_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_settings,
				R.drawable.icon_6_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_firmware,
				R.drawable.icon_8_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_user_reg,
				R.drawable.icon_7_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_buy_online,
				R.drawable.icon_10_2x));
		leftMenuItems
		.add(new ListViewItem(R.string.tools, R.drawable.icon_6_2x));
		return leftMenuItems;
	}

	private void setRightMenuAQIValue(float indoorAQI) {
		if (indoorAQI <= 1.4f) {
			tvAirStatusAqiValue.setText(getString(R.string.good));
		} else if (indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			tvAirStatusAqiValue.setText(getString(R.string.moderate));
		} else if (indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			tvAirStatusAqiValue.setText(getString(R.string.unhealthy));
		} else if (indoorAQI > 3.5f) {
			tvAirStatusAqiValue.setText(getString(R.string.very_unhealthy));
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
		if (purifier != null) {
			purifierState = purifier.getConnectionState();
		}
		
		final ConnectionState newState = purifierState;
		ALog.i(ALog.MAINACTIVITY, "Connection status: " + purifierState);
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
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
					setRightMenuAirStatusBackground(0);
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
	}

	private ArrayAdapter<String> outdoorLocationsAdapter;

	public ArrayAdapter<String> getOutdoorLocationsAdapter() {
		return outdoorLocationsAdapter;
	}

	public void setOutdoorLocationsAdapter(
			ArrayAdapter<String> outdoorLocationsAdapter) {
		this.outdoorLocationsAdapter = outdoorLocationsAdapter;
	}

	public void showFragment(Fragment fragment) {

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.llContainer, fragment, fragment.getTag());
		fragmentTransaction.addToBackStack(fragment.getTag()) ;
		fragmentTransaction.commit();

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
					.getWindowToken(), 0);
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
			leftMenuItems.add(new FirmwareUpdateFragment());
			leftMenuItems.add(new CreateAccountFragment());
			leftMenuItems.add(new BuyOnlineFragment());
			leftMenuItems.add(new ToolsFragment());
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View listItem,
				int position, long id) {
			setDashboardActionbarIconVisible();
			mDrawerLayout.closeDrawer(mListViewLeft);
			switch (position) {
			case 0:
				showFragment(leftMenuItems.get(position));
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
				rightMenu.setVisibility(View.INVISIBLE);
				addLocation.setVisibility(View.VISIBLE);
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
				// Firmware update
				startFirmwareUpgradeActivity();
				break;
			case 7:
				// User registration
				if(UserRegistrationController.getInstance().isUserLoggedIn()) {
					showFragment(new SignedInFragment());
				} else {
					showFragment(new CreateAccountFragment());
				}
				setTitle(getString(R.string.create_account));
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
		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null) return;
		final FirmwarePortInfo firmwarePortInfo = purifier.getFirmwarePortInfo();
		
		if(firmwarePortInfo == null) return;
		ALog.i(ALog.FIRMWARE, "Firmware event received - Version " + firmwarePortInfo.getVersion() + " Upgrade " + firmwarePortInfo.getUpgrade() + " UpdateAvailable " + firmwarePortInfo.isUpdateAvailable());

		if (firmwarePortInfo.isUpdateAvailable()) {
			ALog.i(ALog.FIRMWARE, "Update Dashboard UI");

			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//TODO
					// Change hardcoded value "1" to number of devices discovered after SSDP once multiple purifiers are implemented.
					updateDashboardOnFirmwareUpdate(1);
					setFirmwareSuperScript(1, true);
				}
			});
		}
	}
	
	private void updateDashboardOnFirmwareUpdate(int purifierCount) {
		showFirmwareUpdateHomeIcon(purifierCount);
	}
	
	private void showFirmwareUpdateHomeIcon(int purifierCount) {
		noOffFirmwareUpdate.setVisibility(View.VISIBLE);
		noOffFirmwareUpdate.setText(String.valueOf(purifierCount));
	}
	
	private void hideFirmwareUpdateHomeIcon() {
		noOffFirmwareUpdate.setVisibility(View.INVISIBLE);
	}

	private void updatePurifierUIFields() {
		ALog.i(ALog.MAINACTIVITY, "updatePurifierUIFields");
		
		final PurAirDevice purifier = getCurrentPurifier();
		if(purifier == null || purifier.getConnectionState() == ConnectionState.DISCONNECTED) {
			disableRightMenuControls();
			return ;
		}
		
		if(!purifier.isPaired()){
			pairToPurifierIfNecessary();
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
			}
		});
	}
	
	private void disableRightMenuControls() {
		
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvConnectionStatus.setText(getString(R.string.not_connected));
				ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_lost_connection_2x));
				rightMenu.setImageDrawable(getResources().getDrawable(R.drawable.right_bar_icon_orange_2x));
				setRightMenuAirStatusMessage(getString(R.string.rm_air_quality_message));
				setRightMenuAirStatusBackground(0);
				rightMenuClickListener.toggleControlPanel(false, null);
				ALog.d(ALog.MAINACTIVITY, "Updating right menu to disconnected");
				rightMenuClickListener.toggleControlPanel(false , null);
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
		PurifierManager.getInstance().startSubscription(); // TODO fix
		if (signon) {
			pairToPurifierIfNecessary() ;
		}
	}

	private void pairToPurifierIfNecessary() {
		// Only show PairingDialog once (can be called via discovery and signon)
		if (isPairingDialogShown) return;

		if (!CPPController.getInstance(this).isSignOn()) return;

		PurAirDevice purifier = getCurrentPurifier();
		if (purifier == null || purifier.getConnectionState() != ConnectionState.CONNECTED_LOCALLY) return;
		
		// TODO move this code
		PurifierDatabase database = new PurifierDatabase();
		long lastPairingCheckTime = database.getPurifierLastPairedOn(purifier);
		database.closeDb();
		if (lastPairingCheckTime <= 0) {
			showPairingDialog(purifier);
			return;
		}

		long diffInDays = Utils.getDiffInDays(lastPairingCheckTime);
		if (diffInDays != 0) {
			startPairingWithoutListener(purifier);
		}
	}

	public void showPairingDialog(PurAirDevice purifier) {
		try
		{
			PairingDialogFragment dialog = PairingDialogFragment.newInstance(purifier, PairingDialogFragment.dialog_type.SHOW_DIALOG);
			FragmentManager fragMan = getSupportFragmentManager();
			dialog.show(fragMan, null);
			isPairingDialogShown = true;
		}catch(IllegalStateException e){
			isPairingDialogShown = false;
		}
	}

	public void startPairing(PurAirDevice purifier) {
		if (purifier == null) return; // TODO why can this happen?
			
		progressDialog = new ProgressDialog(MainActivity.this);
		progressDialog.setMessage(getString(R.string.pairing_progress));
		progressDialog.setCancelable(false);
		progressDialog.show();

		PairingHandler pm = new PairingHandler(this, purifier);
		pm.startPairing();
	}

	private void startPairingWithoutListener(PurAirDevice purifier) {
		PairingHandler pm = new PairingHandler(null, purifier);
		pm.startPairing();
	}

	@Override
	public void onPairingSuccess() {		
		if (progressDialog != null) {
			progressDialog.cancel();
		}
		try{
			PairingDialogFragment dialog = PairingDialogFragment.newInstance(null, PairingDialogFragment.dialog_type.PAIRING_SUCCESS);
			FragmentManager fragMan = getSupportFragmentManager();
			dialog.show(fragMan, null);
		}catch(IllegalStateException ex){
			ex.printStackTrace();
		}
		
	}

	@Override
	public void onPairingFailed() {
		if (progressDialog != null) {
			progressDialog.cancel();
		}
		try{
		PairingDialogFragment dialog = PairingDialogFragment.newInstance(getCurrentPurifier(), PairingDialogFragment.dialog_type.PAIRING_FAILED);
		FragmentManager fragMan = getSupportFragmentManager();
		dialog.show(fragMan, null);
		}catch(IllegalStateException ex){
			ex.printStackTrace();
		}
	}
	
	public PurAirDevice getCurrentPurifier() {
		// TODO change to field in class
		return PurifierManager.getInstance().getCurrentPurifier();
	}
	
	private void initializeFirstPurifier() {
		ArrayList<PurAirDevice> devices = DiscoveryManager.getInstance().getDiscoveredDevices();
		if (devices.size() <= 0) return;
		
		// Select the first locally connected device
		PurAirDevice firstPurifier = devices.get(0);
		
		PurifierManager.getInstance().setCurrentPurifier(firstPurifier);
		ALog.d(ALog.MAINACTIVITY, "First purifier discovered: " + firstPurifier.getName());
	}
	
	@Override
	public void onDiscoveredDevicesListChanged() {
		ALog.d(ALog.MAINACTIVITY, "**************************");
		DiscoveryManager.getInstance().printDiscoveredDevicesInfo(ALog.MAINACTIVITY);
		
		PurAirDevice current = getCurrentPurifier();
		if (current == null) {
			initializeFirstPurifier();
			
			current = getCurrentPurifier();
			if (current == null) return;
		}
		
		//TODO remove quick and dirty fix (necessary to update indoorFragment when purifier goes offline)
		PurifierManager.getInstance().notifyPurifierChangedListeners();
		
		switch (current.getConnectionState()) {
		case DISCONNECTED:
			ALog.d(ALog.MAINACTIVITY, "Current purifier went offline");
			updatePurifierUIFields();
			PurifierManager.getInstance().stopCurrentSubscription();
			break;
		case CONNECTED_LOCALLY:
			ALog.d(ALog.MAINACTIVITY, "Current purifier connected locally");
			PurifierManager.getInstance().startSubscription(); // Right menu updated when response from subscription
			pairToPurifierIfNecessary();
			break;
		case CONNECTED_REMOTELY:
			ALog.d(ALog.MAINACTIVITY, "Current purifier connected remotely");
			PurifierManager.getInstance().startSubscription(); // Right menu updated when response from subscription
			break;
		}
	}

	@Override
	public void onConnected() {
		ALog.i(ALog.MAINACTIVITY, "onConnected start CPP");
		CPPController.getInstance(this).signOnWithProvisioning();
	}

	@Override
	public void onDisconnected() {
		ALog.i(ALog.MAINACTIVITY, "onDisconnected");
	}

	@Override
	public void onDrawerEvent(DrawerEvent event, View drawerView) {
		ALog.i(ALog.TEMP, "MainActivity$onDrawerEvent event " + event + " view " + drawerView);
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

		default:
			break;
		}
	}
}
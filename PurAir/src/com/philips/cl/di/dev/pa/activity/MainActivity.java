package com.philips.cl.di.dev.pa.activity;
import java.util.ArrayList;
import java.util.List;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ScrollView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.ListItemAdapter;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.PairingHandler;
import com.philips.cl.di.dev.pa.cpp.PairingListener;
import com.philips.cl.di.dev.pa.cpp.SignonListener;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEvent;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEventListener;
import com.philips.cl.di.dev.pa.dashboard.GPSLocation;
import com.philips.cl.di.dev.pa.dashboard.HomeFragment;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.demo.AppInDemoMode;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.fragment.AboutFragment;
import com.philips.cl.di.dev.pa.fragment.BuyOnlineFragment;
import com.philips.cl.di.dev.pa.fragment.CongratulationFragment;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.fragment.HelpAndDocFragment;
import com.philips.cl.di.dev.pa.fragment.NotificationsFragment;
import com.philips.cl.di.dev.pa.fragment.OutdoorLocationsFragment;
import com.philips.cl.di.dev.pa.fragment.ProductRegistrationStepsFragment;
import com.philips.cl.di.dev.pa.fragment.SettingsFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowChooseFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowVirginFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectPurifier;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.EWS_STATE;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PurifierEvent;
import com.philips.cl.di.dev.pa.notification.NotificationRegisteringManager;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationHandler;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.InternetConnectionHandler;
import com.philips.cl.di.dev.pa.util.InternetConnectionListener;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.PurifierControlPanel;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;
import com.philips.cl.di.dev.pa.view.ListViewItem;

public class MainActivity extends BaseActivity implements AirPurifierEventListener, SignonListener, 
PairingListener, DiscoveryEventListener, NetworkStateListener, DrawerEventListener, InternetConnectionListener {

	private static int screenWidth, screenHeight;

	private DrawerLayout mDrawerLayout;
	private ListView mListViewLeft;
	private ScrollView mScrollViewRight;
	private PurifierControlPanel rightMenuClickListener;

	/** Right off canvas elements */
	private TextView tvAirStatusAqiValue;
	private TextView tvConnectionStatus;
	private TextView tvAirStatusMessage;
	private ImageView ivAirStatusBackground;
	private ImageView ivConnectedImage;
	private ImageView ivConnectionError;
	private boolean mRightDrawerOpened, mLeftDrawerOpened;
	private MenuItem rightMenuItem;
	private int mVisits;

	public boolean isTutorialPromptShown = false;

	public boolean isDiagnostics;

	private ProgressDialog progressDialog;
	private AppInDemoMode appInDemoMode;
	private boolean internetDialogShown;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ALog.i(ALog.MAINACTIVITY, "onCreate mainActivity");
		setContentView(R.layout.activity_main_aj);
		
		CPPController.getInstance(this).setAppUpdateStatus(false);
		
		//Fetch database data
		OutdoorManager.getInstance();
		//Read data from CLV
		OutdoorLocationHandler.getInstance();

		mVisits = Utils.getNoOfVisit();
		Utils.saveNoOfVisit(mVisits);

		appInDemoMode = new AppInDemoMode(MainActivity.this);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		setScreenWidth(displayMetrics.widthPixels);
		setScreenHeight(displayMetrics.heightPixels);

		OutdoorController.getInstance();

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
		rightMenuClickListener = new PurifierControlPanel(this);

		ViewGroup group = (ViewGroup) findViewById(R.id.right_menu_layout);
		setAllButtonListener(group);

		tvAirStatusAqiValue = (TextView) findViewById(R.id.tv_rm_air_status_aqi_value);
		tvConnectionStatus = (TextView) findViewById(R.id.tv_connection_status);
		tvAirStatusMessage = (TextView) findViewById(R.id.tv_rm_air_status_message);
		ivAirStatusBackground = (ImageView) findViewById(R.id.iv_rm_air_status_background);
		ivConnectedImage = (ImageView) findViewById(R.id.iv_connection_status);
		ivConnectionError =  (ImageView) findViewById(R.id.iv_connection_error);

		showFirstFragment();
		initializeCPPController();
		selectPurifier();
		PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(0);
		internetDialogShown=false;
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
		JPushInterface.onResume(this);
		mListViewLeft.setAdapter(new ListItemAdapter(this, getLeftMenuItems()));
		mListViewLeft.setOnItemClickListener(new MenuItemClickListener());

		if (PurAirApplication.isDemoModeEnable()) {
			startDemoMode();
		} else if (UserRegistrationController.getInstance().isUserLoggedIn()) {
			startNormalMode();
		}

		DrawerAdapter.getInstance().addDrawerListener(this);
		updatePurifierUIFields() ;

		//Check if App in demo mode and WI-FI not connected PHILIPS Setup show dialog
		if (PurAirApplication.isDemoModeEnable()) {
			appInDemoMode.checkPhilipsSetupWifiSelected();
		}

		OutdoorController.getInstance().setLocationProvider();

		OutdoorController.getInstance().setActivity(this);

    	checkForCrashesHockeyApp();
	}

	public void startDemoMode() {
		stopNormalMode();
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

	public void stopNormalMode() {
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		DiscoveryManager.getInstance().stop();
	}

	public void startNormalMode() {
		stopDemoMode();
		NetworkReceiver.getInstance().addNetworkStateListener(this);
		DiscoveryManager.getInstance().start(this);
		PurifierManager.getInstance().addAirPurifierEventListener(this);
	}

	@Override
	protected void onResumeFragments() {
		ALog.i(ALog.MAINACTIVITY, "onResumeFragments") ;
		super.onResumeFragments();
		if (PurifierManager.getInstance().getEwsState() == EWS_STATE.EWS) {
			PurifierManager.getInstance().setEwsSate(EWS_STATE.NONE);
			showFirstFragment();
		} else if (PurifierManager.getInstance().getEwsState() == EWS_STATE.REGISTRATION) {
			PurifierManager.getInstance().setEwsSate(EWS_STATE.NONE);
			showFragment(new StartFlowChooseFragment());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
		SetupDialogFactory.getInstance(this).cleanUp();
		if(UserRegistrationController.getInstance().isUserLoggedIn()
				|| PurAirApplication.isDemoModeEnable()) {
			appInDemoMode.rmoveNetworkListenerForDemoMode();
			stopNormalMode();
		}

		OutdoorController.getInstance().setActivity(null);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Ensure app is registered for notifications
		NotificationRegisteringManager.getNotificationManager().getNotificationRegisteringManager();
		PairingHandler.clear();
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
		clearObjects();
		super.onDestroy();
	}
	
	private void clearObjects() {
		OutdoorManager.getInstance().clearCityOutdoorInfo() ;
		ConnectPurifier.reset() ;
		GPSLocation.reset();
		OutdoorLocationHandler.reset();
		OutdoorController.reset();
		OutdoorManager.getInstance().setOutdoorViewPagerCurrentPage(0);
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
			clearFinishCheckGPS();
		} else if (fragment instanceof CongratulationFragment) {
			return;
		} else if (fragment instanceof ProductRegistrationStepsFragment) {
			manager.popBackStack();
		} else if (fragment instanceof HelpAndDocFragment || fragment instanceof SettingsFragment) {
			showFragment(new AboutFragment());
		} else if (!(fragment instanceof HomeFragment)) {
			manager.popBackStackImmediate(null,	FragmentManager.POP_BACK_STACK_INCLUSIVE);
			showFirstFragment();
		} else {
			clearFinishCheckGPS();
		}
	}

	private void clearFinishCheckGPS(){
		if(GPSLocation.getInstance().isLocationEnabled()){
			showGPSDialogIfRequired();
		}
		else{
			finish();
		}
	}
	
	private void showGPSDialogIfRequired() {		
		Utils.setGPSEnabledDialogShownValue(true);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.location_services_turned_on_title)
		.setMessage(R.string.location_services_turned_on_text_without_location)
		.setCancelable(false)
		.setPositiveButton(R.string.turn_it_off,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Open device location service screen, since
				// android doesn't allow to change location
				// settings in code
				Intent myIntent = new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(myIntent);
				dialog.dismiss();
				finish();
			}
		})
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				finish();
			}
		});
		AlertDialog dialog=builder.create();
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
		    @Override
		    public void onCancel(DialogInterface dialog){
		    	finish();
		    }
		});
		dialog.show();
	}

	public void showFirstFragment() {
		boolean firstUse = Utils.getAppFirstUse();

		if (PurAirApplication.isDemoModeEnable() || !firstUse) {
			showDashboardFragment();
		} else {
			showVirginFlowFragment();
		}
	}

	private void showVirginFlowFragment() {
		if(!LocationUtils.getDashboardWithoutPurifierState()) {
			showFragment(new StartFlowVirginFragment()) ;
		} else {
			Bundle bundle = new Bundle();
			bundle.putBoolean(AppConstants.NO_PURIFIER_FLOW, true);
			HomeFragment homeFragment = new HomeFragment();
			homeFragment.setArguments(bundle);
			showFragment(homeFragment);
			setTitle(getString(R.string.dashboard_title));
		}
		
	}

	private void showDashboardFragment() {
		showFragment(getDashboard());
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

	public void showAlertDialogPairingFailed(final String title, final String message, final String fragTag) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();

					Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(fragTag);
					if (prevFrag != null) {
						fragTransaction.remove(prevFrag);
					}

					fragTransaction.add(DownloadAlerDialogFragement.
							newInstance(title, message), "pairing_failed").commitAllowingStateLoss();
				} catch (IllegalStateException e) {
					ALog.e(ALog.MAINACTIVITY, e.getMessage());
				}


			}
		});
	}

	/** Create the left menu items. */
	private List<ListViewItem> getLeftMenuItems() {
		List<ListViewItem> leftMenuItems = new ArrayList<ListViewItem>();

		leftMenuItems.add(new ListViewItem(R.string.list_item_home,
				R.drawable.icon_1_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_buy_online,
				R.drawable.icon_9_2x));
		return leftMenuItems;
	}

	private void setRightMenuAQIValue(float indoorAQI) {
		tvAirStatusAqiValue.setTextSize(18.0f);
		if (indoorAQI <= 1.4f) {
			tvAirStatusAqiValue.setText(getString(R.string.good_indoor));
		} else if (indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			tvAirStatusAqiValue.setText(getString(R.string.moderate_indoor));
		} else if (indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			tvAirStatusAqiValue.setText(getString(R.string.unhealthy_indoor));
		} else if (indoorAQI > 3.5f) {
			tvAirStatusAqiValue.setText(getString(R.string.very_unhealthy_split_indoor));
		}
	}

	private void setRightMenuAirStatusMessage(String message) {
		tvAirStatusMessage.setText(message);
	}

	private void setRightMenuAirStatusBackground(float indoorAQI) {
		Drawable imageDrawable = null;

		if (indoorAQI <= 1.4f) {
			imageDrawable = getResources().getDrawable(R.drawable.aqi_small_circle_2x);
		} else if (indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			imageDrawable = getResources().getDrawable(R.drawable.aqi_small_circle_100_150_2x);
		} else if (indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			imageDrawable = getResources().getDrawable(R.drawable.aqi_small_circle_200_300_2x);
		} else if (indoorAQI > 3.5f) {
			imageDrawable = getResources().getDrawable(R.drawable.aqi_small_circle_300_500_2x);
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
					ALog.d(ALog.MAINACTIVITY, "Updating right menu to connected");
					break;
				case CONNECTED_REMOTELY:
					tvConnectionStatus.setText(getString(R.string.connected_via_philips));
					ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_blue_2x));
					ALog.d(ALog.MAINACTIVITY, "Updating right menu to connected via philips");
					break;
				case DISCONNECTED:
					tvConnectionStatus.setText(getString(R.string.not_connected));
					ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_lost_connection_2x));
					setRightMenuAirStatusMessage(getString(R.string.rm_air_quality_message));
					rightMenuClickListener.toggleControlPanel(false, null);
					ALog.d(ALog.MAINACTIVITY, "Updating right menu to disconnected");
					break;
				}
			}
		});
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

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		}		
	}

	/** Left menu item clickListener */
	private class MenuItemClickListener implements OnItemClickListener {

		private List<Fragment> leftMenuItems = new ArrayList<Fragment>();

		public MenuItemClickListener() {
			initLeftMenu();
		}

		private void initLeftMenu() {
			leftMenuItems.add(getDashboard());
			leftMenuItems.add(new BuyOnlineFragment());
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View listItem,
				int position, long id) {
			//			setDashboardActionbarIconVisible();
			String leftMenuItemName= listItem.getTag().toString();

			mDrawerLayout.closeDrawer(mListViewLeft);

			if (leftMenuItemName.equals(getString(R.string.list_item_home))){
				showFirstFragment();
				setTitle(getString(R.string.dashboard_title));
			} else if(leftMenuItemName.equals(getString(R.string.list_item_buy_online))){
				// Buy Online
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_buy_online));
			}
		}
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
	public void onErrorOccurred(PurifierEvent purifierEvent) {
		if (getCurrentPurifier() == null 
				|| getCurrentPurifier().getConnectionState() == ConnectionState.DISCONNECTED) return;

		if( purifierEvent == PurifierEvent.DEVICE_CONTROL) {
//			setVisibilityAirPortTaskProgress(View.INVISIBLE) ;
			ivConnectionError.setVisibility(View.VISIBLE);
			tvConnectionStatus.setText(getString(R.string.lost_connection));
			ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_gray_2x));
		}
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
				float indoorAQIUsableValue = info.getIndoorAQI() / 10.0f;
				setRightMenuAQIValue(indoorAQIUsableValue);
				rightMenuClickListener.setSensorValues(info);
				setRightMenuAirStatusMessage(getString(Utils.getIndoorAQIMessage(indoorAQIUsableValue),	getString(R.string.philips_home)));
				setRightMenuAirStatusBackground(indoorAQIUsableValue);
				rightMenuClickListener.toggleControlPanel(true,info);
			}
		});

		pairToPurifierIfNecessary();
	}

	private void disableRightMenuControls() {

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ivConnectionError.setVisibility(View.GONE);
				tvConnectionStatus.setText(getString(R.string.not_connected));
				ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_lost_connection_2x));
				setRightMenuAirStatusMessage(getString(R.string.rm_air_quality_message));
				rightMenuClickListener.toggleControlPanel(false, null);
				ALog.d(ALog.MAINACTIVITY, "Updating right menu to disconnected");
				setRightMenuAirStatusMessage(AppConstants.EMPTY_STRING);
				tvAirStatusAqiValue.setText(getString(R.string.not_connected));
			}
		});
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

	@Override
	public void signonStatus(boolean signon) {		
		if (signon) {
			//should be called only when signOn is successful
			PurifierManager.getInstance().startSubscription();
		}
	} 

	public void pairToPurifierIfNecessary() {
		final PurAirDevice purifier = PurifierManager.getInstance().getCurrentPurifier() ;
		if( PairingHandler.pairPurifierIfNecessary(purifier) && PairingHandler.getPairingAttempts(purifier.getEui64()) < AppConstants.MAX_RETRY) {
			purifier.setPairing(NetworkNode.PAIRED_STATUS.PAIRING);
			ALog.i(ALog.PAIRING, "In pairToPurifierIfNecessary(): "+ " Start internet connection check.");
			checkInternetConnection() ;
		}
	}
	
	private Handler handler = new Handler();
	
	private void checkInternetConnection() {
		new InternetConnectionHandler(this, handler).checkInternetConnection() ;
	}
	
	private void showInternetAlertDialog(){
		if(!internetDialogShown){
			internetDialogShown=true;
			String title = "";
			showAlertDialogPairingFailed(title, getString(R.string.check_network_connection), "internet_check");
		}
	}

	@Override
	public void onPairingSuccess(NetworkNode networkNode) {	
		if(getCurrentPurifier()==null) return;

		if(networkNode.getCppId()==getCurrentPurifier().getNetworkNode().getCppId()){
			cancelPairingDialog();

			FragmentManager manager = getSupportFragmentManager();
			final Fragment fragment = manager.findFragmentById(R.id.llContainer);
			if(fragment instanceof NotificationsFragment) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						((NotificationsFragment) fragment).refreshNotificationLayout() ;
					}
				});

			}
		}
	}

	@Override
	public void onPairingFailed(NetworkNode networkNode) {
		if(getCurrentPurifier()==null) return;

		if(networkNode.getCppId()==getCurrentPurifier().getEui64()){
			cancelPairingDialog();
			FragmentManager manager = getSupportFragmentManager();
			final Fragment fragment = manager.findFragmentById(R.id.llContainer);
			if(fragment instanceof NotificationsFragment) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						((NotificationsFragment) fragment).disableNotificationScreen() ;
					}
				});

			}
		}
		//If pairing failed show alert
		String title = "";
		if (networkNode != null) title = networkNode.getName();
		MetricsTracker.trackActionTechnicalError("Error : Pairing failed Purifier ID " + networkNode.getCppId());
		showAlertDialogPairingFailed(title, getString(R.string.pairing_failed), "pairing_failed");
	}

	private void cancelPairingDialog(){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				Fragment progressDialog= fragmentManager.findFragmentByTag(NotificationsFragment.NOTIFICATION_PROGRESS_DIALOG);
				if(progressDialog != null){
					fragmentTransaction.remove(progressDialog);
					fragmentTransaction.commitAllowingStateLoss();
				}
			}
		});
	}

	public PurAirDevice getCurrentPurifier() {
		// TODO change to field in class
		return PurifierManager.getInstance().getCurrentPurifier();
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
		if( current != null && current.getAirPortInfo() != null) return ;
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
			// NOP : Can be removed
			break;
		default:
			break;
		}
	}
	
	public void closeRightMenu() {
		if (mDrawerLayout != null && mListViewLeft != null) mDrawerLayout.closeDrawer(mScrollViewRight);
	}

	private void checkForCrashesHockeyApp() {
		CrashManager.register(this, AppConstants.HOCKEY_APPID, new CrashManagerListener() {
			public boolean shouldAutoUploadCrashes() {
				return true;
			}
		});
	}
	
	@Override
	public void internetStatus(boolean internetAvailable) {
		if ( internetAvailable ) {
			final PurAirDevice purifier = PurifierManager.getInstance().getCurrentPurifier() ;			
			if (!CPPController.getInstance(PurAirApplication.getAppContext()).isSignOn() || purifier==null) {
				CPPController.getInstance(PurAirApplication.getAppContext()).signOnWithProvisioning() ;
				CPPController.getInstance(PurAirApplication.getAppContext()).addSignOnListener(new SignonListener() {
					@Override
					public void signonStatus(boolean signon) {
						if( signon ) {
							ALog.i(ALog.PAIRING, "Start pairing process" ) ;
							PairingHandler pm = new PairingHandler(MainActivity.this, purifier.getNetworkNode());
							pm.setPairingAttempts(purifier.getEui64());
							pm.startPairing();
						}
					}
				});
			}
			else {
				ALog.i(ALog.PAIRING, "In pairToPurifierIfNecessary(): "+ purifier.getPairedStatus()+ " "+ purifier.getName());
				PairingHandler pm = new PairingHandler(MainActivity.this, purifier.getNetworkNode());
				pm.setPairingAttempts(purifier.getEui64());
				pm.startPairing();
			}
		}
		else {
			final PurAirDevice purifier = PurifierManager.getInstance().getCurrentPurifier() ;
			if (purifier != null) {
				purifier.setPairing(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
				PairingHandler pm = new PairingHandler(MainActivity.this, purifier.getNetworkNode());
				// Sets the max pairing attempt for the Purifier to stop checking for internet connection
				while(PairingHandler.getPairingAttempts(purifier.getEui64()) < AppConstants.MAX_RETRY) {
					pm.setPairingAttempts(purifier.getEui64());
				}
			}
			
			FragmentManager manager = getSupportFragmentManager();
			final Fragment fragment = manager.findFragmentById(R.id.llContainer);
			if(fragment instanceof NotificationsFragment) {
				cancelPairingDialog() ;
				((NotificationsFragment) fragment).disableNotificationScreen() ;
			}
			showInternetAlertDialog();
		}
	}
}
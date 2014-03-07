package com.philips.cl.di.dev.pa.pureairui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mobeta.android.dslv.DragSortListView;
import com.philips.cl.di.common.ssdp.contants.MessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.controller.CPPController;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.customviews.FilterStatusView;
import com.philips.cl.di.dev.pa.customviews.ListViewItem;
import com.philips.cl.di.dev.pa.customviews.adapters.ListItemAdapter;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.City;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.dto.Weatherdto;
import com.philips.cl.di.dev.pa.ews.EWSDialogFactory;
import com.philips.cl.di.dev.pa.interfaces.ICPDeviceDetailsListener;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.listeners.RightMenuClickListener;
import com.philips.cl.di.dev.pa.pureairui.fragments.AirQualityFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.BuyOnlineFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.HelpAndDocFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.HomeFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.NotificationsFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.OutdoorLocationsFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.ProductRegFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.ProductRegistrationStepsFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.SettingsFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.ToolsFragment;
import com.philips.cl.di.dev.pa.utils.Fonts;
import com.philips.cl.di.dev.pa.utils.Utils;
import com.philips.cl.disecurity.DISecurity;
import com.philips.cl.disecurity.KeyDecryptListener;


public class MainActivity extends ActionBarActivity implements SensorEventListener, ICPDeviceDetailsListener, Callback , KeyDecryptListener, OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static final String PREFS_NAME = "AIRPUR_PREFS";
	private static final String OUTDOOR_LOCATION_PREFS = "outdoor_location_prefs";

	private static int screenWidth, screenHeight;

	private ActionBar mActionBar;
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
	private Menu menu;
	private boolean connected;

	/** Filter status bars */
	private FilterStatusView preFilterView, multiCareFilterView, activeCarbonFilterView, hepaFilterView;

	//Filter status texts
	private TextView preFilterText, multiCareFilterText, activeCarbonFilterText, hepaFilterText;

	private static HomeFragment homeFragment;

	private Set<String> deviceList = new HashSet<String>();
	private Set<DeviceModel> deviceModels;
	private SsdpService ssdpService;


	private boolean mRightDrawerOpened = false,  drawerOpen = false;
	private DISecurity diSecurity;
	private ActionBarDrawerToggle mActionBarDrawerToggle;

	private SensorDataController sensorDataController;

	private static AirPurifierEventDto airPurifierEventDto;
	private MenuItem rightMenuItem;
	SharedPreferences mPreferences;
	int mVisits;
	int mActivitySelected;

	private boolean isNetworkAvailable = false;
	private BroadcastReceiver networkReceiver;
	private CPPController cppController ;

	private int isGooglePlayServiceAvailable;
	private TextView cancelSearchItem;
	private SharedPreferences outdoorLocationPrefs;
	private ArrayList<String> outdoorLocationsList;

	private boolean isLocalPollingStarted ;
	private boolean isCPPPollingStarted ;
	public boolean isTutorialPromptShown = false;

	private IntentFilter filter ;

	public static boolean stopService;


	private Context myActivityContext ;
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("test", "onCreate") ;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_aj);
		myActivityContext = this ;


		/**
		 * Diffie Hellman key exchange
		 */
		diSecurity = new DISecurity(this);

		mPreferences=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		mVisits= mPreferences.getInt("NoOfVisit", 0);	
		SharedPreferences.Editor editor=mPreferences.edit();
		editor.putInt("NoOfVisit", ++mVisits);
		editor.commit();

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;

		initActionBar();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		//mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.darker_gray));
		mDrawerLayout.setScrimColor(Color.parseColor("#60FFFFFF"));
		mDrawerLayout.setFocusableInTouchMode(false);

		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_launcher, R.string.app_name, R.string.action_settings) 
		{

			@Override
			public void onDrawerClosed(View drawerView) {
				mRightDrawerOpened = false;
				supportInvalidateOptionsMenu();
				drawerOpen = false;
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				if(drawerView.getId() == R.id.right_menu) {
					mRightDrawerOpened = true;
				}
				drawerOpen = true;
				supportInvalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

		/** Initialise left menu items and click listener*/
		mListViewLeft = (ListView) findViewById(R.id.left_menu);
		mListViewLeft.setAdapter(new ListItemAdapter(this, getLeftMenuItems()));
		mListViewLeft.setOnItemClickListener(new MenuItemClickListener());

		/** Initiazlise right menu items and click listener*/
		mScrollViewRight = (ScrollView) findViewById(R.id.right_menu);
		rightMenuClickListener = new RightMenuClickListener(this);

		ViewGroup group = (ViewGroup)findViewById(R.id.right_menu_layout);
		setAllButtonListener(group);

		tvAirStatusAqiValue = (TextView) findViewById(R.id.tv_rm_air_status_aqi_value);
		tvConnectionStatus = (TextView) findViewById(R.id.tv_connection_status);
		tvAirStatusMessage = (TextView) findViewById(R.id.tv_rm_air_status_message);
		ivAirStatusBackground = (ImageView) findViewById(R.id.iv_rm_air_status_background);
		ivConnectedImage = (ImageView) findViewById(R.id.iv_connection_status);
		initFilterStatusViews();
		connected = false;

		getSupportFragmentManager().beginTransaction()
		.add(R.id.llContainer, getDashboard(), HomeFragment.TAG)
		.addToBackStack(null)
		.commit();

		sensorDataController = SensorDataController.getInstance(this);
		sensorDataController.addListener(this) ;

		createNetworkReceiver();


		filter = new IntentFilter() ;
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION) ;
		filter.addAction(Intent.ACTION_SCREEN_ON) ;
		filter.addAction(Intent.ACTION_SCREEN_OFF) ;

		this.registerReceiver(networkReceiver, filter);

		isGooglePlayServiceAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		outdoorLocationPrefs = getSharedPreferences(OUTDOOR_LOCATION_PREFS, Context.MODE_PRIVATE);
		Log.i(TAG, "outdoorPrefs " + outdoorLocationPrefs.getAll());
		HashMap<String, String> outdoorLocationsMap = (HashMap<String, String>) outdoorLocationPrefs.getAll();
		outdoorLocationsList = new ArrayList<String>();
		int size = outdoorLocationsMap.size();
		for(int i = 0; i < size; i++) {
			outdoorLocationsList.add(outdoorLocationsMap.get(""+i));
		}
		outdoorLocationsAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_text, outdoorLocationsList);


		startDeviceDiscovery() ;

		initializeCPPController() ;
		// Start the CPP connection irrespective of network
		startCPP() ;
	}


	private void initializeCPPController() {
		cppController = CPPController.getInstance(this) ;		
		cppController.addDeviceDetailsListener(this) ;
	}

	private void createNetworkReceiver() {
		networkReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("test",intent.getAction()) ;

				ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifiInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo mobileInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

				if(wifiInfo != null && wifiInfo.isConnected()) {
					Log.i("discover", "Wifi Discovered") ;
					if(SessionDto.getInstance().getOutdoorEventDto()==null)
						getDashboard().startOutdoorAQITask();

					List<Weatherdto> weatherDto = SessionDto.getInstance().getWeatherDetails();
					if(weatherDto==null || weatherDto.size()<1)
						getDashboard().startWeatherDataTask();

				}
				else if( mobileInfo != null && mobileInfo.isConnected() ) {
					Log.i("discover", "Mobile Network Discovered") ;
					getDashboard().startOutdoorAQITask();
					getDashboard().startWeatherDataTask();
					//toggleConnection(false) ;
				}
			}
		};
	}

	private void startDeviceDiscovery() {
		if( ssdpService == null ) {
			ssdpService = SsdpService.getInstance() ;
		}
		ssdpService.startDeviceDiscovery(this) ;
	}

	private void stopDiscovery() {		
		if( ssdpService != null ) {
			ssdpService.stopDeviceDiscovery() ;
			ssdpService = null ;
		}
	}

	private void startCPP() {
		Log.i("poll", "startCPP") ;
		stopLocalConnection() ;
		if (cppController.isSignOn() ||
				(Utils.getAirPurifierID(this) != null &&
				Utils.getAirPurifierID(this).length() > 0 )) {
			if( ! isCPPPollingStarted ) {
				cppController.startDCSService() ;
				sensorDataController.startCPPPolling() ;
				isCPPPollingStarted = true ;
			}				
		}
	}

	private void stopCPP() {
		Log.i("poll", "stopCPP") ;
		isCPPPollingStarted = false ;
		if( sensorDataController != null ) {
			sensorDataController.stopCPPPolling() ;
		}		
		if( cppController != null ) {
			cppController.stopDCSService() ;
		}
	}

	private void startLocalConnection() {
		Log.i("poll", "Start LocalCOnnection") ;
		stopCPP() ;
		isLocalPollingStarted = true ;
		if( timer != null)
			timer.cancel() ;
		sensorDataController.startPolling() ;
	}

	private void stopLocalConnection() {
		Log.i("poll", "stopLocalConnection") ;
		ipAddress = null ;
		if( sensorDataController != null)
			sensorDataController.stopPolling() ;
		isLocalPollingStarted = false ;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if ( hasFocus && !connected) {
			disableRightMenuControls() ;
		}
	}

	@Override
	protected void onPause() {
		Log.i("test", "onPause") ;
		super.onPause();
		if(outdoorLocationPrefs != null) {
			Editor editor = outdoorLocationPrefs.edit();
			editor.clear();
			int count = outdoorLocationsAdapter.getCount();
			Log.i(TAG, "count " + count);
			for (int i = 0; i < count; i++) {
				Log.i(TAG, "saving..." + i + " :: " + outdoorLocationsAdapter.getItem(i));
				editor.putString("" + i, outdoorLocationsAdapter.getItem(i));
			}
			editor.commit();
		}
		EWSDialogFactory.getInstance(this).cleanUp();


		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		boolean isScreenOn = powerManager.isScreenOn();

		if (!isScreenOn) {
			Log.i("test", "screen is locked:") ;
			if(!isClickEvent) {				
				disableRightMenuControls() ;
				stopService = true ;
				stopAllServices() ;
			}
			isClickEvent = false ;
		}
	}

	private String secretKey ;

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onRestart() {
		Log.i("test", "onRestart") ;
		isEWSStarted = false ;
		if( stopService ) {
			Log.i("test", "startDiscovery on Restart") ;
			sensorDataController.addListener(this) ;
			startDeviceDiscovery() ;
			if(!isEWSSuccessful) {
				startCPP() ;
			}
			isEWSSuccessful = false ;
			//toggleConnection(false) ;
			stopService = false ;
		}
		super.onRestart();
	}

	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		FragmentManager manager = getSupportFragmentManager();
		int count = manager.getBackStackEntryCount();
		Fragment fragment = manager.findFragmentById(R.id.llContainer);

		if(fragment instanceof OutdoorLocationsFragment && android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
			invalidateOptionsMenu(); 
		}
		if(drawerOpen) {
			mDrawerLayout.closeDrawer(mListViewLeft);
			mDrawerLayout.closeDrawer(mScrollViewRight);
			drawerOpen = false;
		} else if(count > 1 && !(fragment instanceof HomeFragment) && !(fragment instanceof ProductRegistrationStepsFragment)) {
			manager. popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			showFragment(getDashboard());
			setTitle(getString(R.string.dashboard_title));
		} else if(fragment instanceof ProductRegistrationStepsFragment) {
			manager.popBackStack();			
		}else {
			finish();
		}
	}

	public void stopAllServices() {
		Log.i("test", "Stop ALl Services") ;
		secretKey = null ;
		resetSessionObject() ;
		stopCPP() ;
		stopLocalConnection() ;

		if( timer != null ) {
			timer.cancel() ;
		}

		if (discoveryTimer != null) {
			discoveryTimer.cancel();
		}

		if ( networkReceiver != null ) {
			this.unregisterReceiver(networkReceiver) ;
			networkReceiver = null ;
		}
		stopDiscovery() ;
		ipAddress = null ;
		sensorDataController.removeAllListeners() ;
	}


	private void resetSessionObject() {
		SessionDto.getInstance().reset() ;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();		
		stopAllServices() ;
	}

	/** Need to have only one instance of the HomeFragment */
	public static HomeFragment getDashboard() {
		if(homeFragment == null) {
			homeFragment = new HomeFragment();
		}
		return homeFragment;
	}

	/**
	 * @param viewGroup loops through the entire view group and adds
	 * 					an onClickListerner to the buttons.
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

	private void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setIcon(R.drawable.left_slidermenu_icon_2x);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		mActionBar.setCustomView(R.layout.action_bar);
		setTitle(getString(R.string.dashboard_title));
	}

	/** Create the left menu items. */
	private List<ListViewItem> getLeftMenuItems() {
		List<ListViewItem> leftMenuItems = new ArrayList<ListViewItem>();

		leftMenuItems.add(new ListViewItem(R.string.list_item_home, R.drawable.icon_1_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_air_quality_explained, R.drawable.icon_2_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_outdoor_loc, R.drawable.icon_3_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_notifications, R.drawable.icon_4_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_help_and_doc, R.drawable.icon_5_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_settings, R.drawable.icon_6_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_prod_reg, R.drawable.icon_7_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_buy_online, R.drawable.icon_8_2x));
		leftMenuItems.add(new ListViewItem(R.string.tools, R.drawable.icon_6_2x));
		return leftMenuItems;
	} 

	private void setRightMenuAQIValue(float indoorAQI) {
		//		tvAirStatusAqiValue.setText(String.valueOf(aqiValue));
		if(indoorAQI <= 1.4f) {
			tvAirStatusAqiValue.setText(getString(R.string.good)) ;
		} else if(indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			tvAirStatusAqiValue.setText(getString(R.string.moderate)) ;
		} else if(indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			tvAirStatusAqiValue.setText(getString(R.string.unhealthy)) ;
		} else if(indoorAQI > 3.5f) {
			tvAirStatusAqiValue.setText(getString(R.string.very_unhealthy)) ;
		} 
	}

	private void setRightMenuAirStatusMessage(String message) {
		tvAirStatusMessage.setText(message);
	}

	private void setRightMenuAirStatusBackground(float indoorAQI) {
		Log.i(TAG, "setRightMenuAirStatusBackground " + indoorAQI);
		Drawable imageDrawable = null;

		if(indoorAQI <= 1.4f) {
			imageDrawable = getResources().getDrawable(R.drawable.aqi_small_circle_2x);
		} else if(indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			imageDrawable = getResources().getDrawable(R.drawable.aqi_small_circle_100_150_2x);
		} else if(indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			imageDrawable = getResources().getDrawable(R.drawable.aqi_small_circle_200_300_2x);
		} else if(indoorAQI > 3.5f) {
			imageDrawable = getResources().getDrawable(R.drawable.aqi_small_circle_300_500_2x);
		}
		ivAirStatusBackground.setImageDrawable(imageDrawable);
	}

	private void setRightMenuConnectedStatus(int status) {
		Log.i(TAG, "setRightMenuConnectedStatus ");
		//FragmentManager manager = getSupportFragmentManager();
		//Fragment fragment = manager.findFragmentById(R.id.llContainer);

		/*if(fragment instanceof OutdoorLocationsFragment) {
			Log.i(TAG, "setRightMenuConnectedStatus$OutdoorLocationsFragment");
			rightMenuItem.setIcon(R.drawable.plus_blue);
			return;
		}*/
		Log.i(TAG, "setRightMenuConnectedStatus SET MENU ITEM status " + status);
		MenuItem item = null;
		if(menu != null) {
			item = menu.getItem(0);
			if(status == AppConstants.CONNECTED) {
				Log.i(TAG, "setRightMenuConnectedStatus SET MENU ITEM CONNECTED");
				tvConnectionStatus.setText(getString(R.string.connected));
				ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_blue_2x));
				item.setIcon(R.drawable.right_bar_icon_blue_2x);
			}
			else if (status == AppConstants.CONNECTED_VIA_PHILIPS) {
				Log.i(TAG, "setRightMenuConnectedStatus SET MENU ITEM CONNECTED");
				tvConnectionStatus.setText(getString(R.string.connected_via_philips));
				ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_blue_2x));
				item.setIcon(R.drawable.right_bar_icon_blue_2x);
			}
			else {
				Log.i(TAG, "setRightMenuConnectedStatus SET MENU ITEM NOT CONNECTED");
				tvConnectionStatus.setText(getString(R.string.not_connected));
				ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_lost_connection_2x));
				item.setIcon(R.drawable.right_bar_icon_orange_2x);
			}
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		this.menu = menu;
		MenuItem item = menu.findItem(R.id.right_menu);
		rightMenuItem= menu.findItem(R.id.right_menu);	

		if(connected)
			item.setIcon(R.drawable.right_bar_icon_blue_2x);
		else
			item.setIcon(R.drawable.right_bar_icon_orange_2x);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.llContainer);
		MenuItem item= this.menu.findItem(R.id.add_location_menu);
		if(fragment instanceof OutdoorLocationsFragment)
		{			
			item.setVisible(true);
		}
		else
		{
			item.setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	private ActionMode actionMode;

	private ActionMode.Callback callback = new ActionMode.Callback() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			Log.i(TAG, "onPrepareActionMode");
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			Log.i(TAG, "onDestroyActionMode");
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
			actionMode = null;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			Log.i(TAG, "onCreateActionMode");
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.context_search_bar, menu);
			LinearLayout searchlayout = (LinearLayout) getLayoutInflater().inflate(R.layout.search_bar, null);
			cancelSearchItem = (TextView) searchlayout.findViewById(R.id.tv_cancel_search);
			cancelSearchItem.setOnClickListener(MainActivity.this);
			mode.setCustomView(searchlayout);
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem menu) {
			Log.i(TAG, "onActionItemClicked");
			return true;
		}
	};

	private ArrayAdapter<String> outdoorLocationsAdapter;

	public ArrayAdapter<String> getOutdoorLocationsAdapter() {
		return outdoorLocationsAdapter;
	}

	public void setOutdoorLocationsAdapter(ArrayAdapter<String> outdoorLocationsAdapter) {
		this.outdoorLocationsAdapter = outdoorLocationsAdapter;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(mActionBarDrawerToggle.onOptionsItemSelected(item)) {
			mDrawerLayout.closeDrawer(mScrollViewRight);
			return true;
		}
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.llContainer);

		switch (item.getItemId()) {
		case R.id.right_menu:			
			if(mRightDrawerOpened) {
				mDrawerLayout.closeDrawer(mListViewLeft);
				mDrawerLayout.closeDrawer(mScrollViewRight);
			} else {
				mDrawerLayout.closeDrawer(mListViewLeft);
				mDrawerLayout.openDrawer(mScrollViewRight);
			}
			break;
		case R.id.add_location_menu:
			if(fragment instanceof OutdoorLocationsFragment){


				actionMode = startSupportActionMode(callback);

				String[] cities = {"Shanghai", "Beijing", "Bangalore", "Ghangzhou", "Seoul", "WhatHaveYou"};

				Map<String, City> citiesMap = SessionDto.getInstance().getCityDetails().getCities();
				List<City> citiesList = new ArrayList<City>(citiesMap.values());

				final List<String> cityNamesList = new ArrayList<String>();
				Iterator<City> iterator = citiesList.iterator();

				while(iterator.hasNext()) {
					String cityName = iterator.next().getKey();
					cityName = cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
					cityNamesList.add(cityName);
				}
				Collections.sort(cityNamesList);

				ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cityNamesList);
				final DragSortListView listView = (DragSortListView) findViewById(R.id.outdoor_locations_list);
				AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.actv_cities_list);
				actv.setAdapter(adapter);
				actv.setThreshold(0);
				actv.showDropDown();
				actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Log.i(TAG, "Selected text " + ((TextView) arg1).getText());
						actionMode.finish();

					}
				});

				actv.setValidator(new AutoCompleteTextView.Validator() {

					@Override
					public boolean isValid(CharSequence text) {
						Log.i(TAG, "isAutoCompleteText valid");
						if(cancelSearch) {
							cancelSearch = false;
							return false;
						}
						if(cityNamesList.contains(text.toString())) {
							Log.i(TAG, "OutdoorLocation$isValid " + (outdoorLocationsAdapter.getPosition(text.toString())));
							if((outdoorLocationsAdapter.getPosition(text.toString())) != -1) {
								return false;
							}
							outdoorLocationsAdapter.add(text.toString());
							outdoorLocationsAdapter.notifyDataSetChanged();
							listView.invalidate();
							//							outdoorLocationsList.add(text.toString());
							Log.i(TAG, "Listitem count " + listView.getCount() + " adapter " + outdoorLocationsAdapter.getCount());
							return true;
						} else {
							Toast.makeText(MainActivity.this, "Inalid text", Toast.LENGTH_LONG).show();
							return false;
						}
					}

					@Override
					public CharSequence fixText(CharSequence invalidText) {
						// TODO Auto-generated method stub
						return null;
					}
				});
			}
			break;
		}
		return false;
	}

	public void showFragment(Fragment fragment) {
		isClickEvent = true ;
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.llContainer, fragment, fragment.getTag());
		fragmentTransaction.addToBackStack(null); 

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		Log.i(TAG, "imm " + (imm == null) + " getWindow() " + (getWindow() == null) + " getCurrentFocus() " + (getWindow().getCurrentFocus() == null));// + " getWindowToken() " + (getWindow().getCurrentFocus().getWindowToken() == null));
		if(getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		}

		int id = fragmentTransaction.commit();
		Log.i(TAG, "showFragment position " + fragment + " id " + id);
		mDrawerLayout.closeDrawer(mListViewLeft);
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
			leftMenuItems.add(new ProductRegFragment());
			leftMenuItems.add(new BuyOnlineFragment());
			leftMenuItems.add(new ToolsFragment());
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View listItem, int position, long id) {

			switch (position) {
			case 0:
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.dashboard_title));
				mDrawerLayout.closeDrawer(mListViewLeft);
				break;
			case 1: 
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_air_quality_explained));
				mDrawerLayout.closeDrawer(mListViewLeft);
				break;
			case 2:
				//Outdoor locations
				//rightMenuItem.setIcon(R.drawable.plus_blue);
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_outdoor_loc));
				break;
			case 3:
				//Notifications
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_notifications));
				break;
			case 4:
				//Help and documentation
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_help_and_doc));
				break;
			case 5:
				//Settings
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_settings));
				break;
			case 6:
				//Product registration
				//showFragment(leftMenuItems.get(position));
				//setTitle(getString(R.string.list_item_prod_reg));
				break;
			case 7:
				//Buy Online
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_buy_online));
				break;
			case 8:
				//Tools
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.tools));
				break;

			default:
				break;
			}
		}
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	private int statusCounter = 0;

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				updatePurifierUIFields() ;
			}				
		};
	};

	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierDetails) {

		Log.i("LOCALSENSOR", "Received: "+isLocalPollingStarted) ;
		//		Log.i(TAG, "SensorDataReceived: "+airPurifierDetails) ;

		if ( airPurifierDetails != null ) {
			airPurifierDetails.setConnectionStatus(AppConstants.CONNECTED) ;
			airPurifierEventDto = airPurifierDetails ;
			updatePurifierUIFields() ;
		}
		else  {
			statusCounter ++;
			if(statusCounter >= 3) {
				statusCounter = 0 ;
				secretKey = null ; 
				deviceList.clear() ;
				ipAddress = null ;

				disableRightMenuControls() ;
				toggleConnection(false) ;
			}
		}
	}
	private void updatePurifierUIFields() {
		//		Log.i(TAG, "SensorDataReceived " + (!(airPurifierEventDto == null)) + " statusCounter " + statusCounter) ;

		if ( null != airPurifierEventDto ) {
			connected = true;
			statusCounter = 0;
			float indoorAQIUsableValue = airPurifierEventDto.getIndoorAQI() / 10.0f;
			Log.i(TAG, "Indoor AQI / 100 = " + indoorAQIUsableValue);
			setAirPurifierEventDto(airPurifierEventDto);
			updateDashboardFields(airPurifierEventDto) ;
			setRightMenuAQIValue(indoorAQIUsableValue);
			rightMenuClickListener.setSensorValues(airPurifierEventDto);
			updateFilterStatus(airPurifierEventDto.getFilterStatus1(), airPurifierEventDto.getFilterStatus2(), airPurifierEventDto.getFilterStatus3(), airPurifierEventDto.getFilterStatus4());
			setRightMenuConnectedStatus(airPurifierEventDto.getConnectionStatus());
			setRightMenuAirStatusMessage(getString(Utils.getIndoorAQIMessage(indoorAQIUsableValue)));
			setRightMenuAirStatusBackground(indoorAQIUsableValue);
			rightMenuClickListener.disableControlPanel(connected, airPurifierEventDto);
		}
		homeFragment.rotateOutdoorCircle();
	}

	private void disableRightMenuControls() {
		Log.i(TAG, "Disable Menu controls") ;
		connected = false;
		setRightMenuConnectedStatus(AppConstants.NOT_CONNECTED);
		rightMenuClickListener.disableControlPanel(connected, airPurifierEventDto);
		setRightMenuAirStatusMessage(getString(R.string.rm_air_quality_message));
		setRightMenuAirStatusBackground(0);
		homeFragment.setMode("-");
		homeFragment.setIndoorAQIValue(-1.0f);

		homeFragment.setFilterStatus("-");
		homeFragment.hideIndoorGuage();
	}

	private void updateFilterStatus(int preFilterStatus, int multiCareFilterStatus, int activeCarbonFilterStatus, int hepaFilterStatus) {
		//		Log.i(TAG, "Filter values pre " + preFilterStatus + " multicare " + multiCareFilterStatus + "activecarbon " + activeCarbonFilterStatus + " hepa " + hepaFilterStatus);
		/** Update filter bars*/
		preFilterView.setPrefilterValue(preFilterStatus);
		multiCareFilterView.setMultiCareFilterValue(multiCareFilterStatus);
		activeCarbonFilterView.setActiveCarbonFilterValue(activeCarbonFilterStatus);
		hepaFilterView.setHEPAfilterValue(hepaFilterStatus);

		/** Update filter texts*/
		preFilterText.setText(Utils.getPreFilterStatusText(preFilterStatus));
		multiCareFilterText.setText(Utils.getMultiCareFilterStatusText(multiCareFilterStatus));
		activeCarbonFilterText.setText(Utils.getActiveCarbonFilterStatusText(activeCarbonFilterStatus));
		hepaFilterText.setText(Utils.getHEPAFilterFilterStatusText(hepaFilterStatus));
	}

	private void updateDashboardFields(AirPurifierEventDto airPurifierEventDto) {
		/*if ( homeFragment != null && homeFragment.getActivity() != null) {
			homeFragment.setIndoorAQIValue(airPurifierEventDto.getIndoorAQI()) ;
			homeFragment.setFilterStatus(Utils.getFilterStatusForDashboard(airPurifierEventDto)) ;
			String mode = airPurifierEventDto.getFanSpeed().equals(AppConstants.FAN_SPEED_AUTO) ? "Auto" : "Manual" ;
			homeFragment.setMode(mode) ;
		}*/
		if ( homeFragment != null && homeFragment.getActivity() != null) {
			homeFragment.showIndoorGuage();
			homeFragment.setIndoorAQIValue(airPurifierEventDto.getIndoorAQI()/10.0f) ;
			homeFragment.setFilterStatus(Utils.getFilterStatusForDashboard(airPurifierEventDto)) ;
			homeFragment.setMode(Utils.getMode(airPurifierEventDto.getFanSpeed(), this)) ;
		}
	}

	public static AirPurifierEventDto getAirPurifierEventDto() {
		return airPurifierEventDto;
	}

	private static void setAirPurifierEventDto(AirPurifierEventDto airPurifierEventDto) {
		MainActivity.airPurifierEventDto = airPurifierEventDto;
	}	

	public void setRightMenuVisibility(boolean visible)
	{
		rightMenuItem.setVisible(visible);
	}

	public int getVisits()
	{
		return mVisits;
	}

	public boolean isNetworkAvailable() {
		Log.i(TAG, "isNetworkAvailable " + isNetworkAvailable);
		return isNetworkAvailable;
	}

	public boolean isGooglePlayServiceAvailable() {
		if(ConnectionResult.SUCCESS == isGooglePlayServiceAvailable) {
			return true;
		}
		return false;
	}

	public int getVersionNumber() {
		int versionCode = 0;
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	private boolean cancelSearch = false;

	/**
	 * Receive the device details from CPP
	 * 
	 */
	@Override
	public void onReceivedDeviceDetails(AirPurifierEventDto airPurifierDetails) {
		if ( airPurifierDetails != null ) {
			airPurifierDetails.setConnectionStatus(AppConstants.CONNECTED_VIA_PHILIPS) ;
			airPurifierEventDto = airPurifierDetails ;
			handler.sendEmptyMessage(1) ;

			if( timer != null ) {
				timer.cancel() ;
				timer.start() ;
			}
		}
	}

	public void stopCPPPolling() {
		cppController.setSignon(false) ;
		stopCPP() ;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel_search:
			cancelSearch = true;
			actionMode.finish();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
			break;
		}
	}	

	public void toggleConnection( boolean isLocal ) {

		Log.i("test", "Toggle Connection: "+isLocal) ;
		if ( isLocal ) {
			startLocalConnection();
		}
		else {			
			startCPP() ;			
		}
	}

	private void startLocalPolling() {
		Log.i("LOCALSENSOR", "polling1: "+isLocalPollingStarted) ;
		timer.cancel() ;
		isLocalPollingStarted = true ;
		Log.i("LOCALSENSOR", "polling2: "+isLocalPollingStarted) ;
		sensorDataController.startPolling() ;


		sensorDataController.stopCPPPolling() ;
		isCPPPollingStarted = false ;
		cppController.stopDCSService() ;
	}

	private void startCPPPolling() {
		Log.i("test", "startCPPPolling") ;
		ipAddress = null ;
		sensorDataController.stopPolling() ;
		isLocalPollingStarted = false ;
		if (cppController.isSignOn() ||
				(Utils.getAirPurifierID(this) != null &&
				Utils.getAirPurifierID(this).length() > 0 )) {
			Log.i("connect", "CPP start") ;
			if( ! isCPPPollingStarted ) {
				sensorDataController.startCPPPolling() ;
				cppController.startDCSService() ;
				isCPPPollingStarted = true ;
			}				
		}
	}

	private String ipAddress ;

	@Override
	public boolean handleMessage(Message msg) {
		DeviceModel device = null ;
		if (null != msg) {
			final MessageID message = MessageID.getID(msg.what);
			final InternalMessage internalMessage = (InternalMessage) msg.obj;
			if (null != internalMessage && internalMessage.obj instanceof DeviceModel) {
				device = (DeviceModel) internalMessage.obj;
				Log.i(TAG, "Device Information " + device);
				if (null != deviceModels) {
					deviceModels.add(device);
				}
			}
			String ip = "";
			switch (message) {
			case DEVICE_DISCOVERED:
				Log.i("discover", "DEVICE DISCOVERED ip " + device.getIpAddress()+":"+ipAddress);
				if (device != null && device.getSsdpDevice() != null &&
						device.getSsdpDevice().getModelName().contains(AppConstants.MODEL_NAME) &&
						device.getIpAddress() != null && ipAddress == null && !isEWSStarted) {
					Log.i("discover", "Discovered") ;
					ipAddress = device.getIpAddress() ; 
					if( ipAddress != null) {
						Log.i("discover", "Device IP: "+ipAddress) ;
						com.philips.cl.di.dev.pa.utils.Utils.setIPAddress(device.getIpAddress(), this) ;
						diSecurity.exchangeKey(String.format(AppConstants.URL_SECURITY, device.getIpAddress()), AppConstants.DEVICEID);
					}

				}
				break;
			case DEVICE_LOST:
				ip = msg.getData().getString("ip");
				Log.i(TAG, "DEVICE LOST USN  " + device.getUsn());
				if (device != null && device.getIpAddress() != null) {
					deviceList.remove(device.getIpAddress());
				}
				ip = null;
				break;

			default:
				Log.i(TAG, "default");
				break;
			}
			if (null != ip && (!ip.isEmpty())) {
				deviceList.add(ip);
			}

			return false;
		}
		return false;
	}

	@Override
	public void keyDecrypt(String key) {
		Log.i("discover", "Key decryot:"+key ) ;
		if ( secretKey == null && key != null ) {
			Log.i("discover", "Key decryot:"+secretKey +":" ) ;
			this.secretKey = key ;
			toggleConnection(true) ;
		}
	}

	private CountDownTimer timer = new CountDownTimer(AppConstants.DCS_TIMEOUT,1000) {
		@Override
		public void onTick(long millisUntilFinished) {

		}		
		@Override
		public void onFinish() {
			Log.i("TIMER1", "finish timer") ;
			disableRightMenuControls() ;
		}
	};


	private CountDownTimer discoveryTimer = new CountDownTimer(10000,1000) {
		@Override
		public void onTick(long millisUntilFinished) {

		}		
		@Override
		public void onFinish() {
			toggleConnection(false) ;
		}
	};

	public boolean isEWSStarted ;
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.i("ews", "Got back:"+resultCode) ;
		if ( resultCode == RESULT_OK ) {		
			if( intent != null &&  intent.getExtras()  != null )
				ipAddress = (String) intent.getExtras().get("ipaddress") ;
			if( ipAddress != null ) {
				isEWSSuccessful = true ;
			}
			if( sensorDataController == null ) {
				sensorDataController = SensorDataController.getInstance(this) ;
			}
			toggleConnection(true) ;
			if( ssdpService == null ) {
				ssdpService = SsdpService.getInstance() ;
			}
			ssdpService.startDeviceDiscovery(this) ;

			this.registerReceiver(networkReceiver, filter) ;
		}
		isEWSStarted = false ;
	};

	public boolean isClickEvent;
	public boolean isEWSSuccessful ;
	@Override
	protected void onUserLeaveHint() {
		Log.i("test","user leave hint") ;
		if(!isClickEvent) {
			Log.i("test", "UserLeavehint") ;
			disableRightMenuControls() ;
			stopService = true ;
			stopAllServices() ;
		}
		isClickEvent = false ;
		super.onUserLeaveHint();
	}
}

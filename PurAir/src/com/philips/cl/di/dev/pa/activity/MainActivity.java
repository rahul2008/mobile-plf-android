package com.philips.cl.di.dev.pa.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.os.Handler.Callback;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mobeta.android.dslv.DragSortListView;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.controller.InternalMessage;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceListModel;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.adapter.ListItemAdapter;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.ICPDeviceDetailsListener;
import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.datamodel.City;
import com.philips.cl.di.dev.pa.datamodel.PurifierDetailDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.ews.EWSDialogFactory;
import com.philips.cl.di.dev.pa.fragment.AirQualityFragment;
import com.philips.cl.di.dev.pa.fragment.BuyOnlineFragment;
import com.philips.cl.di.dev.pa.fragment.HelpAndDocFragment;
import com.philips.cl.di.dev.pa.fragment.HomeFragment;
import com.philips.cl.di.dev.pa.fragment.NewFirmware;
import com.philips.cl.di.dev.pa.fragment.NotificationsFragment;
import com.philips.cl.di.dev.pa.fragment.OutdoorLocationsFragment;
import com.philips.cl.di.dev.pa.fragment.ProductRegFragment;
import com.philips.cl.di.dev.pa.fragment.ProductRegistrationStepsFragment;
import com.philips.cl.di.dev.pa.fragment.SettingsFragment;
import com.philips.cl.di.dev.pa.fragment.ToolsFragment;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.purifier.SensorDataController;
import com.philips.cl.di.dev.pa.purifier.SensorEventListener;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.PurifierDBHelper;
import com.philips.cl.di.dev.pa.util.RightMenuClickListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FilterStatusView;
import com.philips.cl.di.dev.pa.view.ListViewItem;


public class MainActivity extends BaseActivity implements SensorEventListener, ICPDeviceDetailsListener, Callback , KeyDecryptListener, OnClickListener {

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

	private boolean isDeviceDiscovered ;


	private boolean mRightDrawerOpened = false,  drawerOpen = false;
	private DISecurity diSecurity;
	private ActionBarDrawerToggle mActionBarDrawerToggle;

	private SensorDataController sensorDataController;

	public static AirPurifierEventDto airPurifierEventDto;
	private MenuItem rightMenuItem;
	private SharedPreferences mPreferences;
	private int mVisits;
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

	private static boolean stopService;
	
	private String purifierName;
	public boolean isDiagnostics;
	private PurifierDatabase purifierDatabase;
	private List<PurifierDetailDto> dbPurifierDetailDtoList;
	private Hashtable<String, PurifierDetailDto> ssdpDeviceInfoTable;
	
	private String localDeviceUsn;
	
	public boolean isClickEvent;
	public boolean isEWSSuccessful ;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_aj);
		purifierName = getString(R.string.philips_home);
		
		/**
		 * Initialize database
		 */
		ssdpDeviceInfoTable = new Hashtable<String, PurifierDetailDto>();
		/**
		 * Create database and tables
		 */
		purifierDatabase = new PurifierDatabase(this);
		dbPurifierDetailDtoList = purifierDatabase.getAllPurifierDetail();
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
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION) ;		

		this.registerReceiver(networkReceiver, filter);

		isGooglePlayServiceAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		outdoorLocationPrefs = getSharedPreferences(OUTDOOR_LOCATION_PREFS, Context.MODE_PRIVATE);
		HashMap<String, String> outdoorLocationsMap = (HashMap<String, String>) outdoorLocationPrefs.getAll();
		outdoorLocationsList = new ArrayList<String>();
		int size = outdoorLocationsMap.size();
		for(int i = 0; i < size; i++) {
			outdoorLocationsList.add(outdoorLocationsMap.get(""+i));
		}
		outdoorLocationsAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_text, outdoorLocationsList);


		startDeviceDiscovery() ;

		initializeCPPController() ;
	}


	private void initializeCPPController() {
		cppController = CPPController.getInstance(this) ;		
		cppController.addDeviceDetailsListener(this) ;
	}

	private void createNetworkReceiver() {
		ALog.i(ALog.MAINACTIVITY, "createNetworkReceiver") ;
		networkReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

				if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION) || intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
					NetworkInfo netInfo = conMan.getActiveNetworkInfo();
					if (netInfo!=null && netInfo.isConnected()) {
						ALog.i(ALog.MAINACTIVITY, "onReceive---CONNECTED") ;
						
						if(cppController!=null)
						{
							cppController.signOnWithProvisioning();
						}
						
						if(SessionDto.getInstance().getOutdoorEventDto() == null)
							getDashboard().startOutdoorAQITask();

						List<Weatherdto> weatherDto = SessionDto.getInstance().getWeatherDetails();
						if(weatherDto==null || weatherDto.size() < 1)
							getDashboard().startWeatherDataTask();
					}
				}
			}
		};
	}

	private void startDeviceDiscovery() {
		SsdpService.getInstance().startDeviceDiscovery(this) ;
	}

	private void stopDiscovery() {		
		SsdpService.getInstance().stopDeviceDiscovery();
	}

	private void stopCPP() {
		isCPPPollingStarted = false ;
		if( sensorDataController != null ) {
			sensorDataController.stopCPPPolling() ;
		}		
		if( cppController != null ) {
			cppController.stopDCSService() ;
		}
	}

	private void startLocalConnection() {
		ALog.i(ALog.MAINACTIVITY, "startLocalConnection") ;
		if(!isLocalPollingStarted) {
			stopCPP() ;
			isLocalPollingStarted = true ;
			if( timer != null)
				timer.cancel() ;
			sensorDataController.startPolling() ;
		}
	}

	private void stopLocalConnection() {
		ALog.i(ALog.MAINACTIVITY, "stopLocalConnection: "+sensorDataController) ;
		isDeviceDiscovered = false ;
		if( sensorDataController != null) {
			
			sensorDataController.stopPolling() ;
		}
		isLocalPollingStarted = false ;
		removeLostDeviceFromMap();
	}
	
	private void removeLostDeviceFromMap() {
		DeviceListModel deviceListModel = new DeviceListModel();
		ALog.i(ALog.MAINACTIVITY, "Going to remove device info from devicemap usn : " + localDeviceUsn);
		if (localDeviceUsn != null && localDeviceUsn.length() > 0) {
			deviceListModel.removeDevice(localDeviceUsn);
		}
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
		super.onPause();
		/**
		 * Close Database
		 */
		purifierDatabase.closeDb();
		/**
		 * 
		 */
		if(outdoorLocationPrefs != null) {
			Editor editor = outdoorLocationPrefs.edit();
			editor.clear();
			int count = outdoorLocationsAdapter.getCount();
			for (int i = 0; i < count; i++) {
				editor.putString("" + i, outdoorLocationsAdapter.getItem(i));
			}
			editor.commit();
		}
		EWSDialogFactory.getInstance(this).cleanUp();


		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		boolean isScreenOn = powerManager.isScreenOn();

		if (!isScreenOn && !isDiagnostics) {
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
	protected void onRestart() {
		ALog.i(ALog.MAINACTIVITY, "onRestart: stopService is: "+stopService) ;
		isEWSStarted = false ;
		if( stopService ) {
			sensorDataController.addListener(this) ;
			startDeviceDiscovery() ;
			isEWSSuccessful = false ;
			stopService = false ;			
			this.registerReceiver(networkReceiver, filter);
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
		} 
		else if(count > 1 && !(fragment instanceof HomeFragment) && !(fragment instanceof ProductRegistrationStepsFragment)) {
			manager. popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			showFragment(getDashboard());
			setTitle(getString(R.string.dashboard_title));
		} else if(fragment instanceof ProductRegistrationStepsFragment) {
			manager.popBackStack();			
		}else {
			manager. popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			finish();
		}
	}
	
	public DrawerLayout getDrawerLayout(){
		return mDrawerLayout;
	}
	
	public ScrollView getScrollViewRight() {
		return mScrollViewRight;
	}

	public void stopAllServices() {
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
			try {
				this.unregisterReceiver(networkReceiver) ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		stopDiscovery() ;
		isDeviceDiscovered = false ;
		sensorDataController.removeAllListeners() ;
		CPPController.reset() ;
	}


	private void resetSessionObject() {
		SessionDto.reset() ;
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
		leftMenuItems.add(new ListViewItem(R.string.list_item_firmware, R.drawable.icon_8_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_prod_reg, R.drawable.icon_7_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_buy_online, R.drawable.icon_8_2x));
		leftMenuItems.add(new ListViewItem(R.string.tools, R.drawable.icon_6_2x));
		return leftMenuItems;
	} 

	private void setRightMenuAQIValue(float indoorAQI) {
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
		MenuItem item = null;
		if(menu != null) {
			item = menu.getItem(0);
			if(status == AppConstants.CONNECTED) {
				tvConnectionStatus.setText(getString(R.string.connected));
				ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_blue_2x));
				item.setIcon(R.drawable.right_bar_icon_blue_2x);
			}
			else if (status == AppConstants.CONNECTED_VIA_PHILIPS) {
				tvConnectionStatus.setText(getString(R.string.connected_via_philips));
				ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_blue_2x));
				item.setIcon(R.drawable.right_bar_icon_blue_2x);
			}
			else {
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
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
			actionMode = null;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
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
						actionMode.finish();

					}
				});

				actv.setValidator(new AutoCompleteTextView.Validator() {

					@Override
					public boolean isValid(CharSequence text) {
						if(cancelSearch) {
							cancelSearch = false;
							return false;
						}
						if(cityNamesList.contains(text.toString())) {
							if(outdoorLocationsAdapter.getPosition(text.toString()) != -1) {
								return false;
							}
							outdoorLocationsAdapter.add(text.toString());
							outdoorLocationsAdapter.notifyDataSetChanged();
							listView.invalidate();
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
		default:
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
		if(getWindow() != null && getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		}
		fragmentTransaction.commit();
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
			leftMenuItems.add(new NewFirmware());
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
				//Firmware update
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_firmware));
				break;
			case 7:
				//Product registration
				break;
			case 8:
				//Buy Online
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_buy_online));
				break;
			case 9:
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
	
	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierDetails) {
		
		if ( airPurifierDetails != null ) {
			ALog.i(ALog.MAINACTIVITY, "SensorData Received") ;
			statusCounter = 0 ;
			airPurifierDetails.setConnectionStatus(AppConstants.CONNECTED) ;
			setAirPurifierEventDto(airPurifierDetails);
			updatePurifierUIFields() ;
		}
		else  {
			ALog.i(ALog.MAINACTIVITY, "SensorData Failed: "+statusCounter) ;
			statusCounter ++;
			if(statusCounter >= 3) {
				statusCounter = 0 ;
				secretKey = null ; 
				isDeviceDiscovered = false ;
				
				disableRightMenuControls() ;
				toggleConnection(false) ;
			}
		}
	}
	private void updatePurifierUIFields() {
		ALog.i(ALog.MAINACTIVITY,"updatePurifierUIFields") ;
		if ( null != airPurifierEventDto ) {
			connected = true;
			statusCounter = 0;
			float indoorAQIUsableValue = airPurifierEventDto.getIndoorAQI() / 10.0f;
			setAirPurifierEventDto(airPurifierEventDto);
			updateDashboardFields(airPurifierEventDto) ;
			setRightMenuAQIValue(indoorAQIUsableValue);
			rightMenuClickListener.setSensorValues(airPurifierEventDto);
			updateFilterStatus(airPurifierEventDto.getFilterStatus1(), airPurifierEventDto.getFilterStatus2(), airPurifierEventDto.getFilterStatus3(), airPurifierEventDto.getFilterStatus4());
			setRightMenuConnectedStatus(airPurifierEventDto.getConnectionStatus());
			setRightMenuAirStatusMessage(getString(Utils.getIndoorAQIMessage(indoorAQIUsableValue), purifierName));
			setRightMenuAirStatusBackground(indoorAQIUsableValue);
			rightMenuClickListener.disableControlPanel(connected, airPurifierEventDto);
		}
		homeFragment.rotateOutdoorCircle();
	}

	private void disableRightMenuControls() {
		ALog.i(ALog.MAINACTIVITY,"disableRightMenuControls") ;
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
		ALog.i(ALog.MAINACTIVITY, "updateDashboardFields") ;
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
		ALog.i(ALog.MAINACTIVITY, "OnReceive device details from DCS: "+airPurifierDetails) ;
		if ( airPurifierDetails != null ) {
			airPurifierDetails.setConnectionStatus(AppConstants.CONNECTED_VIA_PHILIPS) ;
			setAirPurifierEventDto(airPurifierDetails);
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updatePurifierUIFields();
				}
			});

			if( timer != null ) {
				timer.cancel() ;
				timer.start() ;
			}
		}
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
		default:
			break;
		}
	}	

	public void toggleConnection( boolean isLocal ) {
		ALog.i(ALog.MAINACTIVITY, "toggleConnection: "+isLocal) ;
		if ( isLocal ) {
			startLocalConnection();
		}
		else {			
			stopLocalConnection() ;
		}
	}
	
	private CountDownTimer timer = new CountDownTimer(AppConstants.DCS_TIMEOUT,1000) {
		@Override
		public void onTick(long millisUntilFinished) {

		}		
		@Override
		public void onFinish() {
			ALog.i(ALog.MAINACTIVITY, "DCS timeout") ;
			disableRightMenuControls() ;
		}
	};


	private CountDownTimer discoveryTimer = new CountDownTimer(10000,1000) {
		@Override
		public void onTick(long millisUntilFinished) {

		}		
		@Override
		public void onFinish() {
			ALog.i(ALog.MAINACTIVITY, "Discovery timeout") ;
			toggleConnection(false) ;
		}
	};

	public boolean isEWSStarted ;
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		ALog.i(ALog.MAINACTIVITY, "onActivityResult: "+resultCode) ;
		if ( resultCode == RESULT_OK ) {		
			if( intent != null &&  intent.getExtras()  != null ) {
				isDeviceDiscovered = intent.getBooleanExtra("deviceDiscovered", false) ;
				purifierName = intent.getStringExtra("pname");
			}
			homeFragment.setHomeName(purifierName);
			if( isDeviceDiscovered ) {
				isEWSSuccessful = true ;
			}
			if( sensorDataController == null ) {
				sensorDataController = SensorDataController.getInstance(this) ;
				sensorDataController.removeAllListeners(); 
				sensorDataController.addListener(this);
			}
			toggleConnection(true) ;
			
		}
		
		if (dbPurifierDetailDtoList != null && dbPurifierDetailDtoList.size() > 0) {
			dbPurifierDetailDtoList.clear();
		}
		
		dbPurifierDetailDtoList = purifierDatabase.getAllPurifierDetail();
		
		startDeviceDiscovery() ;

		this.registerReceiver(networkReceiver, filter) ;
		
		isEWSStarted = false ;
	};

	@Override
	protected void onUserLeaveHint() {
		ALog.i(ALog.MAINACTIVITY, "onUserLeaveHint") ;
		if(!isClickEvent && !isDiagnostics) {
			disableRightMenuControls() ;
			stopService = true ;
			stopAllServices() ;
		}
		isClickEvent = false ;
		super.onUserLeaveHint();
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		DeviceModel device = null ;
		if (null != msg) {
			final DiscoveryMessageID message = DiscoveryMessageID.getID(msg.what);
			final InternalMessage internalMessage = (InternalMessage) msg.obj;
			if (null != internalMessage && internalMessage.obj instanceof DeviceModel) {
				device = (DeviceModel) internalMessage.obj;
			}
			if (device == null ) {
				return false;
			}
			
			switch (message) {
			case DEVICE_DISCOVERED:
				if (device.getSsdpDevice() == null) {
					return false;
				}
				ALog.i(ALog.MAINACTIVITY, "Device discovered usn: " + device.getUsn() + ", Ip: " + device.getIpAddress()
						+", isDeviceDiscovered: " + isDeviceDiscovered + ", isEWSStarted: " + isEWSStarted);
				if (device.getSsdpDevice().getModelName().contains(AppConstants.MODEL_NAME) 
						&& !isDeviceDiscovered && !isEWSStarted) {
					onFirstDeviceDiscovered(device) ;
				}
				break;
			case DEVICE_LOST:
				onDeviceLost(device) ;
				break;
			default:
				break;
			}
			return false;
		}
		return false;
	}
	
	private boolean onFirstDeviceDiscovered(DeviceModel device) {
		isDeviceDiscovered = true ; 
		Utils.setIPAddress(device.getIpAddress(), this) ;
		
		setPurifierName(device.getSsdpDevice().getFriendlyName()) ;
		String ssdpDiscoveredUsn = device.getUsn();
		if (ssdpDiscoveredUsn == null || ssdpDiscoveredUsn.length() <= 0) {
			return true;
		}
		
		localDeviceUsn = ssdpDiscoveredUsn;
		
		getSharedPreferences("cpp_preferences01", 0).edit().putString(
				"airpurifierid", device.getSsdpDevice().getCppId()).commit();
		
		long ssdpDiscoveredBootId = 0L;
		try {
			ssdpDiscoveredBootId = Long.parseLong(device.getBootID());
		} catch (NumberFormatException e) {
			// NOP
			e.printStackTrace();
		}
		if (ssdpDiscoveredBootId == 0L) {
			startKeyExchange(device);
			
		}
		
		if (dbPurifierDetailDtoList != null ) {
			boolean isDeviceInDb = false;
			for (PurifierDetailDto infoDto : dbPurifierDetailDtoList) {
				String dbUsn = infoDto.getUsn();
				if (dbUsn == null || dbUsn.length() <= 0) {
					continue;
				}
				if (dbUsn.equalsIgnoreCase(ssdpDiscoveredUsn)) {
					isDeviceInDb = true;
					long dbBootId = infoDto.getBootId();
					if (dbBootId == ssdpDiscoveredBootId && infoDto.getDeviceKey() != null ) {
						ALog.i(ALog.MAINACTIVITY, "Device boot id is same: " +dbBootId+" ssdp bootid: "+ssdpDiscoveredBootId) ;
						String cppId = infoDto.getCppId();
						AppConstants.DEVICEID = cppId;
						String key = infoDto.getDeviceKey();
						DISecurity.setKeyIntoSecurityHashTable(cppId, key);
						DISecurity.setUrlIntoUrlsTable(cppId,
								String.format(AppConstants.URL_SECURITY, device.getIpAddress()));

						toggleConnection(true) ;
					} else {
						startKeyExchange(device) ;				
					}
					break;
				} 
			}
			
			if (!isDeviceInDb) {
				startKeyExchange(device);
			}
		}
		return true ;
	}
	
	private boolean onDeviceLost(DeviceModel device) {
		String ssdpDeviceUsn = device.getId();
		if (ssdpDeviceUsn == null || ssdpDeviceUsn.length() <= 0) {
			return false;
		}
		
		if (ssdpDeviceUsn.equalsIgnoreCase(localDeviceUsn)) {
			ALog.i(ALog.MAINACTIVITY, "Device Lost: "+ssdpDeviceUsn) ;
			disableRightMenuControls() ;
			toggleConnection(false);
			isDeviceDiscovered = false;
			secretKey = null ;
		}
		return true ;
	}
	
	private void setPurifierName(String name) {
		purifierName = name;
		/**
		 * set purifier name in dashboard
		 */
		homeFragment.setHomeName(purifierName);
	}
	
	private void startKeyExchange(DeviceModel device) {
		ALog.i(ALog.MAINACTIVITY, "start key exchange: isDeviceDiscovered-"+isDeviceDiscovered) ;
		long bootId = 0;
		try {
			bootId = Long.parseLong(device.getBootID());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String cppId = device.getSsdpDevice().getCppId();
		PurifierDetailDto deviceInfoDto = new PurifierDetailDto();
		deviceInfoDto.setUsn(device.getUsn());
		deviceInfoDto.setBootId(bootId);
		deviceInfoDto.setCppId(cppId);
		deviceInfoDto.setDeviceName(device.getSsdpDevice().getFriendlyName());
		
		ssdpDeviceInfoTable.put(cppId, deviceInfoDto);
		
		if( isDeviceDiscovered) {
			diSecurity.initializeExchangeKeyCounter(cppId);
			diSecurity.exchangeKey(String.format(
					AppConstants.URL_SECURITY, device.getIpAddress()), cppId);
		}
	}

	@Override
	public void keyDecrypt(String key, String devId) {
		ALog.i(ALog.MAINACTIVITY, "Key Decrypt: "+key+" DeviceID: "+devId) ;
		if ( secretKey == null && key != null ) {
			this.secretKey = key ;
			
			toggleConnection(true) ;
			
			PurifierDetailDto deviceInfoDto  = ssdpDeviceInfoTable.get(devId);
			deviceInfoDto.setDeviceKey(key);
			
			purifierDatabase.insertPurifierDetail(deviceInfoDto);
			
			if (dbPurifierDetailDtoList != null && dbPurifierDetailDtoList.size()  > 0) {
				dbPurifierDetailDtoList.clear();
			}
			
			dbPurifierDetailDtoList = purifierDatabase.getAllPurifierDetail();
		}
	}

}

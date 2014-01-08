package com.philips.cl.di.dev.pa.pureairui;

import java.util.ArrayList;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.customviews.FilterStatusView;
import com.philips.cl.di.dev.pa.customviews.ListViewItem;
import com.philips.cl.di.dev.pa.customviews.adapters.ListItemAdapter;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.listeners.RightMenuClickListener;
import com.philips.cl.di.dev.pa.pureairui.fragments.AirQualityFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.BuyOnlineFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.HelpAndDocFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.HomeFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.NotificationsFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.OutdoorLocationsFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.ProductRegFragment;
import com.philips.cl.di.dev.pa.pureairui.fragments.ToolsFragment;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.Utils;

public class MainActivity extends ActionBarActivity implements SensorEventListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static int screenWidth, screenHeight;

	private ActionBar mActionBar;
	private DrawerLayout mDrawerLayout;
	private ListView mListViewLeft;
	private ScrollView mScrollViewRight;
	private RightMenuClickListener rightMenuClickListener;

	/** Right off canvas elements */
	private TextView tvAirStatusAqiValue;
	private TextView tvConnectionStatus;
	private ImageView ivConnectedImage;
	private Menu menu;
	private boolean connected;

	/** Filter status bars */
	private FilterStatusView preFilterView, multiCareFilterView, activeCarbonFilterView, hepaFilterView;

	//Filter status texts
	private TextView preFilterText, multiCareFilterText, activeCarbonFilterText, hepaFilterText;

	private static HomeFragment homeFragment;

	private boolean mRightDrawerOpened = false;
	
	private ActionBarDrawerToggle mActionBarDrawerToggle;

	private SensorDataController sensorDataController;

	private static AirPurifierEventDto airPurifierEventDto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_aj);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;

		initActionBar();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_launcher, R.string.app_name, R.string.action_settings) 
		{

			@Override
			public void onDrawerClosed(View drawerView) {
				mRightDrawerOpened = false;
				supportInvalidateOptionsMenu();

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				if(drawerView.getId() == R.id.right_menu) {
					mRightDrawerOpened = true;
				}
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
		ivConnectedImage = (ImageView) findViewById(R.id.iv_connection_status);
		initFilterStatusViews();
		connected = false;
		
		getSupportFragmentManager().beginTransaction()
		.add(R.id.llContainer, getDashboard(), HomeFragment.TAG)
		.addToBackStack(null)
		.commit();

		sensorDataController = new SensorDataController(this, this) ;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume") ;
		super.onResume();
		sensorDataController.startPolling() ;		
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "OnPause") ;
		sensorDataController.stopPolling();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	public void onBackPressed() {
		FragmentManager manager = getSupportFragmentManager();
		int count = manager.getBackStackEntryCount();
		Fragment fragment = manager.findFragmentById(R.id.llContainer);
		
		if(count > 1 && !(fragment instanceof HomeFragment)) {
			manager. popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			showFragment(getDashboard());
			setTitle(getString(R.string.dashboard_title));
		} else {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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

		leftMenuItems.add(new ListViewItem(R.string.list_item_1, R.drawable.icon_1_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_2, R.drawable.icon_2_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_3, R.drawable.icon_3_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_4, R.drawable.icon_4_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_5, R.drawable.icon_5_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_6, R.drawable.icon_6_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_7, R.drawable.icon_7_2x));
		leftMenuItems.add(new ListViewItem(R.string.list_item_8, R.drawable.icon_8_2x));

		return leftMenuItems;
	} 

	private void setRightMenuAQIValue(int aqiValue) {
		tvAirStatusAqiValue.setText(String.valueOf(aqiValue));
	}

	// TODO : Change param to int.
	private void setRightMenuConnectedStatus(boolean status){
		MenuItem item = menu.getItem(0);
		if(status) {
			tvConnectionStatus.setText(getString(R.string.connected));
			ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_connected));
			item.setIcon(R.drawable.right_bar_icon_blue_2x);
		} else {
			tvConnectionStatus.setText(getString(R.string.not_connected));
			ivConnectedImage.setImageDrawable(getResources().getDrawable(R.drawable.wifi_icon_lost_connection_2x));
			item.setIcon(R.drawable.right_bar_icon_orange_2x);
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
		MenuItem item = menu.getItem(0);

		if(connected)
			item.setIcon(R.drawable.right_bar_icon_blue_2x);
		else
			item.setIcon(R.drawable.right_bar_icon_orange_2x);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(mActionBarDrawerToggle.onOptionsItemSelected(item)) {
			mDrawerLayout.closeDrawer(mScrollViewRight);
			return true;
		}
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
		}
		return false;
	}

	public void showFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.llContainer, fragment, fragment.getTag());
		fragmentTransaction.addToBackStack(null); 
		int id = fragmentTransaction.commit();
		Log.i(TAG, "showFragment position " + fragment + " id " + id);
		mDrawerLayout.closeDrawer(mListViewLeft);
	}

	public void setTitle(String title) {
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		textView.setTypeface(Fonts.getGillsansLight(MainActivity.this));
		textView.setTextSize(24);
		textView.setText(title);
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
			leftMenuItems.add(new ToolsFragment());
			leftMenuItems.add(new ProductRegFragment());
			leftMenuItems.add(new BuyOnlineFragment());
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
				setTitle(getString(R.string.list_item_2));
				mDrawerLayout.closeDrawer(mListViewLeft);
				break;
			case 2:
				//Outdoor locations
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_3));
				break;
			case 3:
				//Notifications
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_4));
				break;
			case 4:
				//Help and documentation
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_5));
				break;
			case 5:
				//Tools
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_6));
				break;
			case 6:
				//Product registration
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_7));
				break;
			case 7:
				//Buy Online
				showFragment(leftMenuItems.get(position));
				setTitle(getString(R.string.list_item_8));
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
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) {

		Log.i(TAG, "SensorDataReceived " + (!(airPurifierEventDto == null)) + " statusCounter " + statusCounter) ;
		rightMenuClickListener.disableControlPanel(connected, airPurifierEventDto);
		if ( null != airPurifierEventDto ) {
			statusCounter = 0;
			setAirPurifierEventDto(airPurifierEventDto);
			updateDashboardFields(airPurifierEventDto) ;
			setRightMenuAQIValue(airPurifierEventDto.getIndoorAQI());
			rightMenuClickListener.setSensorValues(airPurifierEventDto);
			updateFilterStatus(airPurifierEventDto.getFilterStatus1(), airPurifierEventDto.getFilterStatus2(), airPurifierEventDto.getFilterStatus3(), airPurifierEventDto.getFilterStatus4());
			setRightMenuConnectedStatus(true);
			connected = true;
		} else {
			statusCounter++;
			if(statusCounter >= 3) {
				setRightMenuConnectedStatus(false);
				connected = false;
			}
		}

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
		if ( homeFragment != null ) {
			homeFragment.setIndoorAQIValue(airPurifierEventDto.getIndoorAQI()) ;
			homeFragment.setFilterStatus(Utils.getFilterStatusForDashboard(airPurifierEventDto)) ;
			String mode = airPurifierEventDto.getFanSpeed().equals(AppConstants.FAN_SPEED_AUTO) ? "Auto" : "Manual" ;
			homeFragment.setMode(mode) ;
		}
	}

	public static AirPurifierEventDto getAirPurifierEventDto() {
		return airPurifierEventDto;
	}

	private static void setAirPurifierEventDto(AirPurifierEventDto airPurifierEventDto) {
		MainActivity.airPurifierEventDto = airPurifierEventDto;
	}
}


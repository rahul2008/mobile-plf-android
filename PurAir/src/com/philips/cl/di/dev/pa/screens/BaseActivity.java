package com.philips.cl.di.dev.pa.screens;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.Switch;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.controller.AirPurifierController;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.interfaces.SizeCallback;
import com.philips.cl.di.dev.pa.screens.adapters.MenuListAdapter;
import com.philips.cl.di.dev.pa.screens.customviews.CustomHorizontalScrollView;
import com.philips.cl.di.dev.pa.utils.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
public abstract class BaseActivity extends Activity implements
		OnItemClickListener, OnCheckedChangeListener, ServerResponseListener {

	/** The scroll view. */
	protected CustomHorizontalScrollView scrollView;

	/** The left menu , centerview , right settings. */
	protected View leftMenu, centerView, rightSettings;

	/**
	 * Menu must NOT be out/shown to start with.
	 */
	static boolean menuOut = false;

	/** The settings out. */
	static boolean settingsOut = false;

	/** The inflater. */
	protected LayoutInflater inflater;

	/** The screenwidth. */
	private static int screenwidth;

	/** The Constant TAG. */
	private static final String TAG = BaseActivity.class.getName();

	/** The sw power. */
	private Switch swPower;

	/** The airpurifier controller. */
	private AirPurifierController airpurifierController;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "--BaseActivity.onCreate--");
		airpurifierController = new AirPurifierController(this,
				getApplicationContext());
		inflater = LayoutInflater.from(this);
		scrollView = (CustomHorizontalScrollView) inflater.inflate(
				R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		screenwidth = Utils.getScreenWidth(this);
		initializeLeftAndRightNavigationalViews();
		centerView = getCenterView();
		populateScrollView();
		initializeControls();

	}

	/**
	 * Initialize left and right navigational views.
	 */
	void initializeLeftAndRightNavigationalViews() {
		// Left Menu - Common for all activities
		leftMenu = inflater.inflate(R.layout.left_menu, null);
		ListView lvMenu = (ListView) leftMenu.findViewById(R.id.lvMenu);
		lvMenu.setOnItemClickListener(this);
		Utils.getIconArray();
		Utils.getLabelArray();
		lvMenu.setAdapter(new MenuListAdapter(getApplicationContext(), 0, Utils
				.getIconArray(), Utils.getLabelArray()));
		// Settings Page - Common
		rightSettings = inflater.inflate(R.layout.activity_settings, null);

	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		
		switch (position) {
		case AppConstants.HOME:
			Log.i(TAG, "On Item Click --- HOME ");
			int left = leftMenu.getMeasuredWidth();
			scrollView.smoothScrollTo(left, 0);
			menuOut = !menuOut;
			break;
		case AppConstants.MYCITIES:
			Log.i(TAG, "On Item Click --- CITY");
			break;
		case AppConstants.ABOUTAQI:
			Log.i(TAG, "On Item Click --- ABout AQI ");
			break;
		case AppConstants.PRODUCTREG:
			Log.i(TAG, "On Item Click --- Product Reg ");
			break;
		case AppConstants.HELP:
			Log.i(TAG, "On Item Click --- HELP");
			break;
		case AppConstants.SETTINGS:
			Log.i(TAG, "On Item Click --- Settings");
			showIPDialog(this);
			break;

		}

	}
	
	
	/**
	 * Shows ip dialog.
	 *
	 * @param context the context
	 */
	public void showIPDialog(Context context)
	{
		Utils.showIpDialog(context);
	}

	/**
	 * Initialize controls.
	 */
	void initializeControls() {
		swPower = (Switch) findViewById(R.id.sw_power);
		swPower.setOnCheckedChangeListener(this);
	}

	/**
	 * Sets the device power state.
	 * 
	 * @param deviceState
	 *            the new device power state
	 */
	private void setDevicePowerState(boolean deviceState) {
		Log.e(TAG, "setDevicePowerState:" + deviceState);
		airpurifierController.setDevicePowerState(deviceState);
		// TODO

		/*
		 * switchDeviceMode.setEnabled(deviceState);
		 * switchDeviceMode.setChecked(false); //On power state change machine
		 * will be in manual mode RadioGroup radioGroup =
		 * (RadioGroup)findViewById(R.id.radioGroupFanSpeed); if (!deviceState)
		 * { radioGroup.clearCheck(); } else {
		 * radioGroup.check(R.id.radioButtonFanSpeed1); }
		 * setChildViewsEnabledForLayout(linearLayoutDeviceFanSpeed,
		 * deviceState);
		 */}

	/**
	 * Populate scroll view.
	 */
	void populateScrollView() {
		final View[] children = new View[] { leftMenu, centerView,
				rightSettings };
		scrollView.initViews(children, AppConstants.SCROLLTOVIEWID,
				new SizeCallbackForMenu(getApplicationContext()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged
	 * (android.widget.CompoundButton, boolean)
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.sw_power:
			Log.i(TAG, "On Checked Change Power button : " + isChecked);
			setDevicePowerState(isChecked);
			break;
		}

	}

	/**
	 * Back button handler.
	 */
	@Override
	public void onBackPressed() {
		if (menuOut) {
			int left = leftMenu.getMeasuredWidth();
			scrollView.smoothScrollTo(left, 0);
			menuOut = !menuOut;
		} else if (settingsOut) {
			int left = leftMenu.getMeasuredWidth();
			scrollView.smoothScrollTo(left, 0);
			settingsOut = !settingsOut;
		} else {
			super.onBackPressed();
		}
	}

	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.interfaces.ServerResponseListener#receiveServerResponse(int, java.lang.String)
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the center view.
	 * 
	 * @return the center view
	 */
	protected abstract View getCenterView();

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's
	 * width.
	 */
	static class ClickListenerForScrolling implements OnClickListener {

		/** The scroll view. */
		HorizontalScrollView scrollView;

		/** The menu. */
		View menu;

		/**
		 * Menu must NOT be out/shown to start with.
		 * 
		 * @param scrollView
		 *            the scroll view
		 * @param menu
		 *            the menu
		 */
		/*
		 * boolean menuOut = false;
		 *//** The settings out. */
		/*
		 * boolean settingsOut = false;
		 */
		/**
		 * Instantiates a new click listener for scrolling.
		 * 
		 * @param scrollView
		 *            the scroll view
		 * @param menu
		 *            the menu
		 */
		public ClickListenerForScrolling(HorizontalScrollView scrollView,
				View menu) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			int menuWidth = menu.getMeasuredWidth();
			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);
			switch (v.getId()) {
			case R.id.ivMenu:
				if (!menuOut) {
					int left = 0;
					scrollView.smoothScrollTo(left, 0);
				} else {
					int left = menuWidth;
					scrollView.smoothScrollTo(left, 0);
				}
				menuOut = !menuOut;
				break;

			case R.id.ivSettings:
				if (!settingsOut) {
					scrollView.smoothScrollBy(screenwidth, 0);
				} else {
					scrollView.smoothScrollTo(0, 0);
				}
				settingsOut = !settingsOut;
				break;
			}
		}
	}

	/**
	 * Helper that remembers the width of the 'slide' button, so that the
	 * 'slide' button remains in view, even when the menu is showing.
	 */
	static class SizeCallbackForMenu implements SizeCallback {

		/** The width. */
		int Width;
		/** The context. */
		Context context;

		/**
		 * Instantiates a new size callback for menu.
		 * 
		 * @param context
		 *            the context
		 */
		public SizeCallbackForMenu(Context context) {
			super();
			this.context = context;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.philips.cl.di.dev.pa.interfaces.SizeCallback#onGlobalLayout()
		 */
		@Override
		public void onGlobalLayout() {
			Width = Utils.getScreenWidth(context);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.philips.cl.di.dev.pa.interfaces.SizeCallback#getViewSize(int,
		 * int, int, int[])
		 */
		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = (int) ((Width) * (AppConstants.SCALELEFTMENU));
			}
		}
	}

}

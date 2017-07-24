package com.philips.cl.di.dev.pa.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.CppController.SignonState;
import com.philips.cdp.dicommclient.cpp.listener.SignonListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.common.PairingHandler;
import com.philips.cdp.dicommclient.port.common.PairingListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.buyonline.BuyOnlineFragment;
import com.philips.cl.di.dev.pa.buyonline.ProductRegisterFragment;
import com.philips.cl.di.dev.pa.buyonline.PromotionsFragment;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.AppUpdater;
import com.philips.cl.di.dev.pa.dashboard.DeviceControlFragment;
import com.philips.cl.di.dev.pa.dashboard.GPSLocation;
import com.philips.cl.di.dev.pa.dashboard.HomeFragment;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.datamodel.AirPortProperties;
import com.philips.cl.di.dev.pa.demo.AppInDemoMode;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.ews.WifiNetworkCallback;
import com.philips.cl.di.dev.pa.fragment.AboutFragment;
import com.philips.cl.di.dev.pa.fragment.CongratulationFragment;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.fragment.HelpAndDocFragment;
import com.philips.cl.di.dev.pa.fragment.NotificationsFragment;
import com.philips.cl.di.dev.pa.fragment.OutdoorLocationsFragment;
import com.philips.cl.di.dev.pa.fragment.RateAndFeedbackFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowChooseFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowVirginFragment;
import com.philips.cl.di.dev.pa.fragment.TermsAndConditionsFragment;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier.NotificationPermission;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager.EWS_STATE;
import com.philips.cl.di.dev.pa.newpurifier.ConnectPurifier;
import com.philips.cl.di.dev.pa.notification.NotificationRegisteringManager;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationHandler;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.registration.UserRegistrationController;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.InternetConnectionHandler;
import com.philips.cl.di.dev.pa.util.InternetConnectionListener;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements AirPurifierEventListener, SignonListener,
PairingListener, DiscoveryEventListener, NetworkStateListener, InternetConnectionListener {

	private static int screenWidth, screenHeight, statusBarHeight;

	private int mVisits;

	public boolean isTutorialPromptShown = false;

	public boolean isDiagnostics;

	private ProgressDialog progressDialog;
	private AppInDemoMode appInDemoMode;
	private WifiNetworkCallback networkCallback;
	private enum InternetState {Connecting, Connected, Disconnected};
	private InternetState internetState = InternetState.Connecting;
	private DiscoveryManager<? extends DICommAppliance> discoveryManagerIstance;
	private boolean communicationFailedMsgDisplayed;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ALog.i(ALog.MAINACTIVITY, "onCreate mainActivity");
		//App restarting issue fixed
		//App completely restarting when launched by icon press in launcher
		if (!isTaskRoot())
		{
			//http://stackoverflow.com/questions/16126511/app-completely-restarting-when-launched-by-icon-press-in-launcher
			final Intent intent = getIntent();
			final String intentAction = intent.getAction(); 
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
				finish();
				return;       
			}
		}
		setContentView(R.layout.activity_main_aj);
		getMainContainerHeight();
		
		// DICOMM Refactor - check right place to instantiate appupdater
		//CPPController.getInstance(this).setAppUpdateStatus(false);
		AppUpdater appUpdater = AppUpdater.getInstance(getApplicationContext());
		appUpdater.setAppUpdateStatus(false);
		discoveryManagerIstance = DiscoveryManager.getInstance();
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

		showFirstFragment();
		initializeCPPController();
		selectPurifier();
		AirPurifierManager.getInstance().setCurrentIndoorViewPagerPosition(0);
		
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			setStatusBarHeight(getResources().getDimensionPixelSize(resourceId));
		}
		
		checkInternetWhenAppLaunch();
		
	}

	private void getMainContainerHeight() {
		final ViewGroup mainContainer = (LinearLayout) findViewById(R.id.llContainer);
		mainContainer.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				setScreenHeight(mainContainer.getHeight());
			}
		}, 50); //50 millisecond, View is taking time to load on UI
	}

	private void selectPurifier() {
		AirPurifier current = getCurrentPurifier();
		if (PurAirApplication.isDemoModeEnable()) {
			if (current != null) current.setConnectionState(ConnectionState.DISCONNECTED);
			appInDemoMode.connectPurifier();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		updatePurifierUIFields() ;

		//Check if App in demo mode and WI-FI not connected PHILIPS Setup show dialog

		OutdoorController.getInstance().setLocationProvider();

		OutdoorController.getInstance().setActivity(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			registerWifiNetworkForSocket();
		}
		else {
			startDiscovery() ;
		}

        checkForCrashesHockeyApp();
	}
	
	private void startDiscovery() {
		if (PurAirApplication.isDemoModeEnable()) {
			appInDemoMode.checkPhilipsSetupWifiSelected();
			startDemoMode();
		} else if (UserRegistrationController.getInstance().isUserLoggedIn()) {
			startNormalMode();
		}
	}

	public void startDemoMode() {
		stopNormalMode();
		if (appInDemoMode != null) {
			appInDemoMode.addNetworkListenerForDemoMode();
		}

		NetworkReceiver.getInstance().addNetworkStateListener(this);
		AirPurifierManager.getInstance().addAirPurifierEventListener(this);
		DiscoveryManager.getInstance().stop();
	}

	private void stopDemoMode() {
		if (appInDemoMode != null) {
			appInDemoMode.rmoveNetworkListenerForDemoMode();
		}
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
		AirPurifierManager.getInstance().removeAirPurifierEventListener(this);
	}

	public void stopNormalMode() {
		ALog.i("LOLLIPOP","stopNormalMode");
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
		AirPurifierManager.getInstance().removeAirPurifierEventListener(this);
        DiscoveryManager.getInstance().removeDiscoverEventListener(this);
		DiscoveryManager.getInstance().stop();
	}

	public void startNormalMode() {
		ALog.i("LOLLIPOP", "startNormalMode");
		stopDemoMode();
		NetworkReceiver.getInstance().addNetworkStateListener(this);
        DiscoveryManager.getInstance().addDiscoveryEventListener(this);
		DiscoveryManager.getInstance().start();
		AirPurifierManager.getInstance().addAirPurifierEventListener(this);
	}
	
	
	@SuppressLint("NewApi")
	private void registerWifiNetworkForSocket() {
		NetworkRequest.Builder request = new NetworkRequest.Builder();
		request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

		final Object lock = new Object();
		networkCallback = new WifiNetworkCallback(lock);

		synchronized (lock) {
			connectivityManager.registerNetworkCallback(request.build(),	networkCallback);
			try {
				lock.wait(2000);
				Log.e(ALog.WIFI, "Timeout error occurred");
			} catch (InterruptedException e) {
			}
		}
		Network setNetwork = networkCallback.getNetwork();
		if(setNetwork != null) {
			ConnectivityManager.setProcessDefaultNetwork(setNetwork);
		}
		startDiscovery() ;
		
	}

	@Override
	protected void onResumeFragments() {
		ALog.i(ALog.MAINACTIVITY, "onResumeFragments") ;
		super.onResumeFragments();
		if (AirPurifierManager.getInstance().getEwsState() == EWS_STATE.EWS) {
			AirPurifierManager.getInstance().setEwsSate(EWS_STATE.NONE);
			showFirstFragment();
		} else if (AirPurifierManager.getInstance().getEwsState() == EWS_STATE.REGISTRATION) {
			AirPurifierManager.getInstance().setEwsSate(EWS_STATE.NONE);
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

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		CppController.getInstance().removeSignOnListener(this);
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

		if (fragment instanceof StartFlowVirginFragment) {
			clearFinishCheckGPS();
		} else if (fragment instanceof CongratulationFragment) {
			return;
		} else if (fragment instanceof HelpAndDocFragment) {
			showFirstFragment();
		} else if ( fragment instanceof BuyOnlineFragment
				|| fragment instanceof PromotionsFragment
				|| fragment instanceof ProductRegisterFragment
				|| fragment instanceof RateAndFeedbackFragment
				|| fragment instanceof AboutFragment) {
			showFragment(new HelpAndDocFragment());
		} else if (fragment instanceof NotificationsFragment) {
			showFragment(new DeviceControlFragment());
		} else if (fragment instanceof TermsAndConditionsFragment) {
			showFragment(new AboutFragment());
		} else if (!(fragment instanceof HomeFragment)) {
			manager.popBackStackImmediate(null,	FragmentManager.POP_BACK_STACK_INCLUSIVE);
			showFirstFragment();
		} else {
			showDashboardFromDetail(fragment);
		}
	}
	
	private void showDashboardFromDetail(Fragment fragment) {
		if (fragment instanceof HomeFragment && ((HomeFragment) fragment).isTitleVisible()) {
			showFirstFragment();
			return;
		}
		clearFinishCheckGPS();
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
			CppController.getInstance().setDefaultDcsState() ;
			CppController.getInstance().addSignOnListener(this) ;

	}


	public HomeFragment getDashboard() {
		return new HomeFragment();
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
							newInstance(title, message), fragTag).commitAllowingStateLoss();
				} catch (IllegalStateException e) {
					ALog.e(ALog.MAINACTIVITY, e.getMessage());
				}


			}
		});
	}

	public void showFragment(Fragment fragment) {
		try {
			getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);
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

	private static void setScreenWidth(int width) {
		screenWidth = width;
	}
	private static void setScreenHeight(int height) {
		screenHeight = height;
	}
	private static void setStatusBarHeight(int height) {
		statusBarHeight = height;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}
	public static int getScreenHeight() {
		return screenHeight;
	}
	public static int getStatusBarHeight() {
		return statusBarHeight;
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
		final AirPurifier purifier = getCurrentPurifier();

		ALog.i(ALog.FIRMWARE, "onFirmwareEventReceived: " + purifier);
	}

	@Override
	public void onErrorOccurred(Error purifierEventError) { /**NOP*/ }

	private void updatePurifierUIFields() {
		ALog.i(ALog.MAINACTIVITY, "updatePurifierUIFields");
		final AirPurifier purifier = getCurrentPurifier();
		
		if(purifier == null || purifier.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED) {
			return ;
		}
		ALog.i(ALog.MAINACTIVITY, "Current connectionstate for UI update: " + getCurrentPurifier().getNetworkNode().getConnectionState());
		final AirPortProperties info = getAirPortInfo(purifier);
		if (info == null) {
			return;
		}
		pairToPurifierIfNecessary();
	}

	private AirPortProperties getAirPortInfo(AirPurifier purifier) {
		if (purifier == null) return null;
		return purifier.getAirPort().getPortProperties();
	}

	public int getVisits() {
		return mVisits;
	}

	@Override
	public void signonStatus(boolean signon) {		
		if (signon) {
			//should be called only when signOn is successful
			AirPurifierManager.getInstance().startSubscription();
		} else {
			showCommunicationFailedMessage();
		}
		ALog.i(ALog.APP_LAUNCH_CHEK_INTERNET, "MainActivity$signonStatus signon= " + signon);
	} 

	public void pairToPurifierIfNecessary() {
		if (PurAirApplication.isDemoModeEnable() 
				&& OutdoorController.getInstance().isPhilipsSetupWifiSelected()) {
			return;
		}
		final AirPurifier purifier = AirPurifierManager.getInstance().getCurrentPurifier() ;
		if (PurAirApplication.isDemoModeEnable()) return;
		if (PairingHandler.pairApplianceIfNecessary(purifier.getNetworkNode()) && PairingHandler.getPairingAttempts(purifier.getNetworkNode().getCppId()) < AppConstants.MAX_RETRY) {
			purifier.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRING);
			ALog.i(ALog.PAIRING, "In pairToPurifierIfNecessary(): "+ " Start internet connection check.");
			checkInternetConnection() ;
		}
	}
	
	private Handler handler = new Handler();
	
	private void checkInternetConnection() {
		new InternetConnectionHandler(this, handler).checkInternetConnection() ;
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

	public AirPurifier getCurrentPurifier() {
		// TODO change to field in class
		return AirPurifierManager.getInstance().getCurrentPurifier();
	}

	@Override
	public void onDiscoveredAppliancesListChanged() {
		ALog.d(ALog.MAINACTIVITY, "**************************");
		if (PurAirApplication.isDemoModeEnable()) return;

		DiscoveryManager.getInstance().printDiscoveredAppliances(ALog.MAINACTIVITY);

		ArrayList<? extends DICommAppliance> appliances = DiscoveryManager.getInstance().getAllDiscoveredAppliances();
		if (appliances.size() <= 0) return;
		ALog.i(ALog.APP_START_UP, "MainAcitivty$onDiscoveredDevicesListChanged devices list size "
				+ appliances.size() + " :: " + appliances);

		AirPurifier current = getCurrentPurifier();
		if( current != null && current.getAirPort().getPortProperties() != null) return ;
		AirPurifierManager.getInstance().startSubscription() ;
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
			CppController.getInstance().signOnWithProvisioning();
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
		AirPurifier purifier = AirPurifierManager.getInstance().getCurrentPurifier();
		if (purifier != null) purifier.setConnectionState(ConnectionState.DISCONNECTED);
		updatePurifierUIFields();
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
			internetState = InternetState.Connected;
			final AirPurifier purifier = AirPurifierManager.getInstance().getCurrentPurifier() ;			
			if( purifier == null) {
				return ;
			}
			if (!CppController.getInstance().isSignOn() || purifier==null) {
				CppController.getInstance().signOnWithProvisioning() ;
				CppController.getInstance().addSignOnListener(new SignonListener() {
					@Override
					public void signonStatus(boolean signon) {
						if( signon ) {
							ALog.i(ALog.PAIRING, "Start pairing process" ) ;
							PairingHandler pm = new PairingHandler(MainActivity.this, purifier);
							pm.setPairingAttempts(purifier.getNetworkNode().getCppId());
							pm.startPairing();
						} else {
							showCommunicationFailedMessage( R.string.cloud_service_not_available, "communicationFailed");
						}
					}
				});
			}
			else {
				ALog.i(ALog.PAIRING, "In pairToPurifierIfNecessary(): "+ purifier.getNetworkNode().getPairedState()+ " "+ purifier.getName());
				PairingHandler pm = new PairingHandler(MainActivity.this, purifier);
				pm.setPairingAttempts(purifier.getNetworkNode().getCppId());
				pm.startPairing();
			}
		}
		else {
			internetState = InternetState.Disconnected;
			final AirPurifier purifier = AirPurifierManager.getInstance().getCurrentPurifier() ;
			if (purifier != null) {
				ALog.i(ALog.PAIRING, "In last if pairToPurifierIfNecessary()");
				purifier.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.NOT_PAIRED);
				PairingHandler pm = new PairingHandler(MainActivity.this, purifier);
				// Sets the max pairing attempt for the Purifier to stop checking for internet connection
				while(PairingHandler.getPairingAttempts(purifier.getNetworkNode().getCppId()) < AppConstants.MAX_RETRY) {
					pm.setPairingAttempts(purifier.getNetworkNode().getCppId());
				}
			}
			
			FragmentManager manager = getSupportFragmentManager();
			final Fragment fragment = manager.findFragmentById(R.id.llContainer);
			if(fragment instanceof NotificationsFragment) {
				cancelPairingDialog() ;
				((NotificationsFragment) fragment).disableNotificationScreen() ;
			}
			showCommunicationFailedMessage( R.string.enable_internet_access, "communicationFailed");
		}
	}
	
	private void checkInternetWhenAppLaunch() {
		if (PurAirApplication.isDemoModeEnable() 
				&& OutdoorController.getInstance().isPhilipsSetupWifiSelected()) {
			return;
		}
		communicationFailedMsgDisplayed = false;
		internetState = InternetState.Connecting;
		ALog.i(ALog.APP_LAUNCH_CHEK_INTERNET, "MainActivity$checkInternetWhenAppLaunch() internetState= " + internetState
				+ "SignOnState= " + CppController.getInstance().getSignOnState());
		new InternetConnectionHandler(new InternetConnectionListener() {
			
			@Override
			public void internetStatus(boolean status) {
				if (status) {
					internetState = InternetState.Connected;
				} else {
					internetState = InternetState.Disconnected;
					showCommunicationFailedMessage();
				}
				ALog.i(ALog.APP_LAUNCH_CHEK_INTERNET, "MainActivity$internetStatus status= " + status);
			}
		}, handler).checkInternetConnection() ;
	}
	
	private synchronized void showCommunicationFailedMessage() {
		SignonState signonState = CppController.getInstance().getSignOnState();
		ALog.i(ALog.APP_LAUNCH_CHEK_INTERNET, "MainActivity$showCommunicationFailedMessage() internetState= " + internetState
				+ "; SignOnState= " + signonState + "; Added purifierlist size= " + DiscoveryManager.getInstance().getAddedAppliances().size());
		if (discoveryManagerIstance.getAddedAppliances().isEmpty() || signonState == SignonState.SIGNED_ON) return;
		
		if (internetState != InternetState.Connecting && signonState != SignonState.SIGNING) {
			if (internetState == InternetState.Connected) {
				showCommunicationFailedMessage( R.string.cloud_service_not_available, "communicationFailed");
			} else {
				showCommunicationFailedMessage( R.string.enable_internet_access, "communicationFailed");
			}
		}
	}
	
	private void showCommunicationFailedMessage(int msgId, String tag) {
		ALog.i(ALog.APP_LAUNCH_CHEK_INTERNET, "MainActivity$showCommunicationFailedMessage() communicationFailedMsgDisplayed = " + communicationFailedMsgDisplayed);
		if (!communicationFailedMsgDisplayed) {
			communicationFailedMsgDisplayed = true;
			showAlertDialogPairingFailed("", getString(msgId), tag);
		}
	}

	@Override
	public void onPairingSuccess(final DICommAppliance appliance) {
		if(getCurrentPurifier()==null) return;

		if(appliance.getNetworkNode().getCppId()==getCurrentPurifier().getNetworkNode().getCppId()){
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
	public void onPairingFailed(final DICommAppliance appliance) {
		if(getCurrentPurifier()==null) return;

		if(appliance.getNetworkNode().getCppId()==getCurrentPurifier().getNetworkNode().getCppId()){
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
		if (appliance.getNetworkNode() != null) title = appliance.getNetworkNode().getName();
		MetricsTracker.trackActionTechnicalError("Error : Pairing failed Purifier ID " + appliance.getNetworkNode().getCppId());
		showAlertDialogPairingFailed(title, getString(R.string.pairing_failed), "pairing_failed");
	}
}
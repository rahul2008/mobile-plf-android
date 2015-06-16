package com.philips.cl.di.digitalcare;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cl.di.digitalcare.customview.NetworkAlertView;
import com.philips.cl.di.digitalcare.util.DigiCareLogger;
import com.philips.cl.di.digitalcare.util.NetworkReceiver;

/**
 * DigitalCareBaseFragment is <b>Base class</b> for all fragments.
 * 
 * @author: ritesh.jha@philips.com
 * @since: Dec 5, 2014
 */
public abstract class DigitalCareBaseFragment extends Fragment implements
		OnClickListener, NetworkStateListener {

	private static String TAG = DigitalCareBaseFragment.class.getSimpleName();
	protected int mLeftRightMarginPort = 0;
	protected int mLeftRightMarginLand = 0;
	private Activity mFragmentActivityContext = null;
	private NetworkReceiver mNetworkutility = null;
	private static boolean isConnectionAvailable;
	private FragmentManager fragmentManager = getFragmentManager();
	private Thread mUiThread = Looper.getMainLooper().getThread();
	private final Handler mHandler = new Handler(Looper.getMainLooper());

	public abstract void setViewParams(Configuration config);

	public abstract String getActionbarTitle();

	/**
	 * Updating action bar title. The text has to be updated at each fragment
	 * seletion/creation.
	 */
	private void setActionbarTitle() {
		((DigitalCareFontTextView) getActivity().findViewById(
				R.id.action_bar_title)).setText(getActionbarTitle());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnCreate on "
				+ this.getClass().getSimpleName());
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		mFragmentActivityContext = getActivity();
		registerNetWorkReceiver();
	}

	private void registerNetWorkReceiver() {

		IntentFilter mfilter = new IntentFilter(
				"android.net.conn.CONNECTIVITY_CHANGE");
		mNetworkutility = new NetworkReceiver(this);
		getActivity().registerReceiver(mNetworkutility, mfilter);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLeftRightMarginPort = (int) mFragmentActivityContext.getResources()
				.getDimension(R.dimen.activity_margin_port);
		mLeftRightMarginLand = (int) mFragmentActivityContext.getResources()
				.getDimension(R.dimen.activity_margin_land);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnCreateView on "
				+ this.getClass().getSimpleName());
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onStart() {
		DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnStart on "
				+ this.getClass().getSimpleName());
		super.onStart();
	}

	@Override
	public void onResume() {
		DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnResume on "
				+ this.getClass().getSimpleName());
		super.onResume();
		setActionbarTitle();
	}

	@Override
	public void onPause() {
		DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnPause on "
				+ this.getClass().getSimpleName());
		super.onPause();
	}

	@Override
	public void onStop() {
		DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnStop on "
				+ this.getClass().getSimpleName());
		super.onStop();
	}

	@Override
	public void onDestroy() {
		DigiCareLogger.i(DigiCareLogger.FRAGMENT, "onDestroy on "
				+ this.getClass().getSimpleName());
		getActivity().unregisterReceiver(mNetworkutility);
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnDestroyView on "
				+ this.getClass().getSimpleName());
		super.onDestroyView();

		hideKeyboard();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	protected boolean isConnectionAvailable() {
		if (isConnectionAvailable)
			return true;
		else {
			// new NetworkAlertView().showNetworkAlert(getActivity());
			mHandler.postAtFrontOfQueue(new Runnable() {

				@Override
				public void run() {
					new NetworkAlertView().showAlertBox(
							getActivity(),
							"Alert",
							getActivity().getResources().getString(
									R.string.no_internet),
							getActivity().getResources().getString(
									android.R.string.yes));
					AnalyticsTracker
							.trackAction(
									AnalyticsConstants.ACTION_KEY_SET_ERROR,
									AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
									AnalyticsConstants.TECHNICAL_ERROR_NETWORK_CONNECITON);

				}
			});
			return false;

		}
	}

	protected void showAlert(final String message) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				new NetworkAlertView().showAlertBox(
						getActivity(),
						getActivity().getResources().getString(
								android.R.string.dialog_alert_title),
						message,
						getActivity().getResources().getString(
								android.R.string.yes));
				AnalyticsTracker.trackAction(
						AnalyticsConstants.ACTION_KEY_SET_ERROR,
						AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
						AnalyticsConstants.TECHNICAL_ERROR_NETWORK_CONNECITON);

			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		DigiCareLogger.i(TAG, TAG + " : onConfigurationChanged ");
	}

	private void enableActionBarLeftArrow() {

		ImageView backToHome = (ImageView) mFragmentActivityContext
				.findViewById(R.id.back_to_home_img);
		ImageView homeIcon = (ImageView) mFragmentActivityContext
				.findViewById(R.id.home_icon);
		homeIcon.setVisibility(View.GONE);
		backToHome.setVisibility(View.VISIBLE);
		backToHome.bringToFront();
	}

	protected void showFragment(Fragment fragment) {
		try {
			enableActionBarLeftArrow();
			FragmentTransaction fragmentTransaction = mFragmentActivityContext
					.getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.mainContainer, fragment,
					fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			DigiCareLogger.e(TAG, e.getMessage());
		}

		InputMethodManager imm = (InputMethodManager) mFragmentActivityContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mFragmentActivityContext.getWindow() != null
				&& mFragmentActivityContext.getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(mFragmentActivityContext.getWindow()
					.getCurrentFocus().getWindowToken(), 0);
		}
	}

	protected boolean backstackFragment() {
		fragmentManager = getFragmentManager();
		if (fragmentManager.getBackStackEntryCount() == 2) {
			fragmentManager.popBackStack();
			removeCurrentFragment();
		} else {
			fragmentManager.popBackStack();
			removeCurrentFragment();
		}
		return false;
	}

	private void removeCurrentFragment() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		Fragment currentFrag = fragmentManager
				.findFragmentById(R.id.mainContainer);

		if (currentFrag != null) {
			transaction.remove(currentFrag);
		}

		transaction.commit();
	}

	protected void hideKeyboard() {
		View view = getActivity().getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public synchronized static void setStatus(boolean connection) {
		if (connection)
			isConnectionAvailable = true;
		else {
			isConnectionAvailable = false;
		}
	}

	@Override
	public void onNetworkStateChanged(boolean connection) {
		setStatus(connection);
	}

	protected final void updateUI(Runnable runnable) {
		if (Thread.currentThread() != mUiThread) {
			mHandler.post(runnable);
		} else {
			runnable.run();
		}
	}

}

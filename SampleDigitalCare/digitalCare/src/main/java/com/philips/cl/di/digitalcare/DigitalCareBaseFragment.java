package com.philips.cl.di.digitalcare;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
	private FragmentActivity mFragmentActivityContext = null;
	private NetworkReceiver mNetworkutility = null;
	private static boolean isConnectionAvailable;
	private static int mContainerId = 0;
	private FragmentActivity mActivityContext = null;
	private FragmentManager fragmentManager = getFragmentManager();
	private Thread mUiThread = Looper.getMainLooper().getThread();
	private final Handler mHandler = new Handler(Looper.getMainLooper());
	private static ActionbarUpdateListener mActionbarUpdateListener = null;
	private static String mPreviousPageName = null;

	public abstract void setViewParams(Configuration config);

	public abstract String getActionbarTitle();

	public abstract String setPreviousPageName();

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
		mPreviousPageName = setPreviousPageName();
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
							null,
							getActivity().getResources().getString(
									R.string.no_internet),
							getActivity().getResources().getString(
									android.R.string.yes));
					AnalyticsTracker
							.trackAction(
									AnalyticsConstants.ACTION_SET_ERROR,
									AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
									AnalyticsConstants.ACTION_VALUE_TECHNICAL_ERROR_NETWORK_CONNECITON);

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
						null,
						message,
						getActivity().getResources().getString(
								android.R.string.yes));
				AnalyticsTracker
						.trackAction(
								AnalyticsConstants.ACTION_SET_ERROR,
								AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
								AnalyticsConstants.ACTION_VALUE_TECHNICAL_ERROR_NETWORK_CONNECITON);

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
		int containerId = R.id.mainContainer;

		if (mContainerId != 0) {
			containerId = mContainerId;
			mFragmentActivityContext = mActivityContext;
		} else {
			enableActionBarLeftArrow();
			InputMethodManager imm = (InputMethodManager) mFragmentActivityContext
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (mFragmentActivityContext.getWindow() != null
					&& mFragmentActivityContext.getWindow().getCurrentFocus() != null) {
				imm.hideSoftInputFromWindow(mFragmentActivityContext
						.getWindow().getCurrentFocus().getWindowToken(), 0);
			}
		}
		try {
			FragmentTransaction fragmentTransaction = mFragmentActivityContext
					.getSupportFragmentManager().beginTransaction();
			// fragmentTransaction.setCustomAnimations(mEnter, mExit,
			// mPopEnter, mPopExit);
			fragmentTransaction.replace(containerId, fragment,
					fragment.getTag());
			fragmentTransaction.hide(this);
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			DigiCareLogger.e(TAG, "");
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		DigiCareLogger.d(DigiCareLogger.FRAGMENT, "onHiddenChanged : " + hidden
				+ " ---class " + this.getClass().getSimpleName());
		if (mContainerId != 0) {
			updateActionbar();
		}
	}

	/*
	 * This method can be called directly from outside and helps to invoke the
	 * fragments, instead of full screen activity. DigitalCare fragments will be
	 * added in the root container of hosting app. Integrating app has to pass
	 * some parameters in order to do smooth operations.
	 */

	private static String mEnterAnimation = null;
	private static String mExitAnimation = null;

	protected void showFragment(FragmentActivity context, int parentContainer,
			Fragment fragment, ActionbarUpdateListener actionbarUpdateListener,
			int enterAnim, int exitAnim) {
		mContainerId = parentContainer;
		mActivityContext = context;
		mActionbarUpdateListener = actionbarUpdateListener;
//		mEnterAnimation = enterAnim;
//		mExitAnimation = exitAnim;

		// String packageName = context.getPackageName();
		// int enter = context.getResources().getIdentifier(
		// packageName + ":anim/" + mEnterAnimation, null, null);
		// int exit = context.getResources().getIdentifier(
		// packageName + ":anim/" + mExitAnimation, null, null);

		try {
			FragmentTransaction fragmentTransaction = context
					.getSupportFragmentManager().beginTransaction();
			// fragmentTransaction.setCustomAnimations(enter, exit, enter,
			// exit);
			fragmentTransaction.replace(mContainerId, fragment,
					fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			DigiCareLogger.e(TAG, e.getMessage());
		}
	}

	protected boolean backstackFragment() {
		fragmentManager = getFragmentManager();
		// if (fragmentManager.getBackStackEntryCount() == 2) {
		// fragmentManager.popBackStack();
		// removeCurrentFragment();
		// } else {
		fragmentManager.popBackStack();
		// removeCurrentFragment();
		// }
		return false;
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

	/**
	 * Updating action bar title. The text has to be updated at each fragment
	 * seletion/creation.
	 */
	private void setActionbarTitle() {
		if (mContainerId == 0) {
			((DigitalCareFontTextView) getActivity().findViewById(
					R.id.action_bar_title)).setText(getActionbarTitle());
		} else {
			updateActionbar();
		}
	}

	private void updateActionbar() {
		if (this.getClass().getSimpleName()
				.equalsIgnoreCase(SupportHomeFragment.class.getSimpleName())) {
			mActionbarUpdateListener.updateActionbar(getActionbarTitle(), true);
		} else {
			mActionbarUpdateListener.updateActionbar(getActionbarTitle(), false);
		}
	}

	protected String getPreviousName() {
		return mPreviousPageName;
	}
}

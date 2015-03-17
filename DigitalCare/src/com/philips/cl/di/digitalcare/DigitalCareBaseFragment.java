package com.philips.cl.di.digitalcare;

import java.lang.reflect.Field;
import java.util.Observer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.philips.cl.di.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.FragmentObserver;

/**
 * DigitalCareBaseFragment is super class for all fragments.
 * 
 * @author: ritesh.jha@philips.com
 * @since: Dec 5, 2014
 */
public abstract class DigitalCareBaseFragment extends Fragment implements
		OnClickListener {

	private static String TAG = "DigitalCareBaseFragment";
	private static final Field sChildFragmentManagerField;
	protected int mLeftRightMarginPort = 0;
	protected int mLeftRightMarginLand = 0;
	private Activity mFragmentActivityContext = null;
	protected static FragmentObserver mAppObserver = null;

	static {
		Field f = null;
		try {
			f = Fragment.class.getDeclaredField("mChildFragmentManager");
			f.setAccessible(true);
		} catch (NoSuchFieldException e) {
			DLog.e(DLog.FRAGMENT, "Error getting mChildFragmentManager field");
		}
		sChildFragmentManagerField = f;
	}

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
		DLog.d(DLog.FRAGMENT, "OnCreate on " + this.getClass().getSimpleName());
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		mAppObserver = DigitalCareApplication.getFragmentObserverInstance();
		mFragmentActivityContext = getActivity();
		if (mFragmentActivityContext != null)
			mAppObserver.addObserver((Observer) mFragmentActivityContext);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLeftRightMarginPort = (int) mFragmentActivityContext.getResources()
				.getDimension(R.dimen.activity_margin_port);
		mLeftRightMarginLand = (int) mFragmentActivityContext.getResources()
				.getDimension(R.dimen.activity_margin_land);
		setActionbarTitle();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DLog.d(DLog.FRAGMENT, "OnCreateView on "
				+ this.getClass().getSimpleName());
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onStart() {
		DLog.d(DLog.FRAGMENT, "OnStart on " + this.getClass().getSimpleName());
		super.onStart();
	}

	@Override
	public void onResume() {
		DLog.d(DLog.FRAGMENT, "OnResume on " + this.getClass().getSimpleName());
		super.onResume();
	}

	@Override
	public void onPause() {
		DLog.d(DLog.FRAGMENT, "OnPause on " + this.getClass().getSimpleName());
		super.onPause();
	}

	@Override
	public void onStop() {
		DLog.d(DLog.FRAGMENT, "OnStop on " + this.getClass().getSimpleName());
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		DLog.d(DLog.FRAGMENT, "OnDestroyView on "
				+ this.getClass().getSimpleName());
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		DLog.d(DLog.FRAGMENT, "OnDestroy on " + this.getClass().getSimpleName());
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (sChildFragmentManagerField != null) {
			try {
				sChildFragmentManagerField.set(this, null);
			} catch (Exception e) {
				DLog.e(DLog.FRAGMENT,
						"Error setting mChildFragmentManager field");
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		DLog.i(TAG, TAG + " : onConfigurationChanged ");
	}

	private void enableActionBarLeftArrow() {

		ImageView backToHome = (ImageView) mFragmentActivityContext
				.findViewById(R.id.back_to_home_img);
		ImageView homeIcon = (ImageView) mFragmentActivityContext
				.findViewById(R.id.home_icon);
		// DigitalCareFontTextView actionBarTitle = (DigitalCareFontTextView)
		// mFragmentActivityContext.findViewById(R.id.action_bar_title);
		homeIcon.setVisibility(View.GONE);
		// homeIcon.setVisibility(View.INVISIBLE);
		backToHome.setVisibility(View.VISIBLE);
		backToHome.bringToFront();
	}

	protected void showFragment(Fragment fragment) {
		try {
			enableActionBarLeftArrow();
			// getSupportFragmentManager().popBackStackImmediate(null,
			// FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction = mFragmentActivityContext
					.getFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.mainContainer, fragment,
					fragment.getTag());
			fragmentTransaction.addToBackStack(fragment.getTag());
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			DLog.e(TAG, e.getMessage());
		}

		InputMethodManager imm = (InputMethodManager) mFragmentActivityContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mFragmentActivityContext.getWindow() != null
				&& mFragmentActivityContext.getWindow().getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(mFragmentActivityContext.getWindow()
					.getCurrentFocus().getWindowToken(), 0);
		}
	}

	private FragmentManager fragmentManager = getFragmentManager();

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

	// protected void startVideo() {
	// Intent intent = new Intent(Intent.ACTION_VIEW);
	// intent.setDataAndType(
	// Uri.parse("http://www.philips-smartairpurifier.com/movies/infomercial.mp4"),
	// "video/mp4");
	// startActivity(Intent.createChooser(intent, ""));
	// }
}


package com.philips.cl.di.reg.ui.traditional;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsConstants;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsUtils;
import com.philips.cl.di.reg.ui.utils.RLog;

public abstract class RegistrationBaseFragment extends Fragment {

	protected int mLeftRightMarginPort;

	protected int mLeftRightMarginLand;

	public abstract void setViewParams(Configuration config);

	public abstract int getTitleResourceId();

	private int mPrevTitleResourceId = -99;

	@Override
	public void onAttach(Activity activity) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onCreate");
		mLeftRightMarginPort = (int) getResources().getDimension(R.dimen.reg_layout_margin_port);
		mLeftRightMarginLand = (int) getResources().getDimension(R.dimen.reg_layout_margin_land);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onStart");
	}

	@Override
	public void onResume() {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onResume");
		super.onResume();
		setCurrentTitle();
	}

	@Override
	public void onPause() {
		super.onPause();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onDestroyView");
	}

	@Override
	public void onDestroy() {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onDestroy");
		setPrevTiltle();
		super.onDestroy();
	}

	private void setPrevTiltle() {
		TextView tvTitle = ((TextView) getActivity().findViewById(R.id.tv_reg_header_title));
		if (mPrevTitleResourceId != -99) {
			tvTitle.setText(getString(mPrevTitleResourceId));
			tvTitle.setTag(mPrevTitleResourceId);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onDetach");
	}

	private void setCurrentTitle() {
		TextView tvTitle = ((TextView) getActivity().findViewById(R.id.tv_reg_header_title));
		if (null != tvTitle.getTag()) {
			mPrevTitleResourceId = (Integer) tvTitle.getTag();
		}
		tvTitle.setText(getString(getTitleResourceId()));
		tvTitle.setTag(getTitleResourceId());
	}

	public RegistrationActivity getRegistrationMainActivity() {
		Activity activity = getActivity();
		if (activity != null && (activity instanceof RegistrationActivity)) {
			return (RegistrationActivity) activity;
		}
		return null;
	}

	protected void consumeTouch(View view) {
		if (view == null)
			return;
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	protected void applyParams(Configuration config, View view) {
		LinearLayout.LayoutParams mParams = (LayoutParams) view.getLayoutParams();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		view.setLayoutParams(mParams);
	}

	protected void trackCurrentPage(String state) {
		AnalyticsUtils.trackPage(state);
	}

	protected void trackPreviousPage(String state) {
		AnalyticsUtils.trackPage(AnalyticsConstants.PREVIOUS_PAGENAME + ":" + state);
	}

}

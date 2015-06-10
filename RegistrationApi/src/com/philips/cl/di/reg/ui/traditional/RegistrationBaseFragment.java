
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
import com.philips.cl.di.reg.ui.utils.RLog;

public abstract class RegistrationBaseFragment extends Fragment {

	protected int mLeftRightMarginPort = 0;

	protected int mLeftRightMarginLand = 0;

	public abstract void setViewParams(Configuration config);

	public abstract String getActionbarTitle();

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
		setActionbarTitle();
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
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onDetach");
	}

	private void setActionbarTitle() {
		((TextView) getActivity().findViewById(R.id.tv_reg_header_title))
		        .setText(getActionbarTitle());
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

	public void applyParams(Configuration config, View view) {
		LinearLayout.LayoutParams mParams = (LayoutParams) view.getLayoutParams();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		view.setLayoutParams(mParams);
	}

}

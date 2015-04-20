
package com.philips.cl.di.reg.ui.traditional;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.ui.utils.RLog;

public abstract class RegistrationBaseFragment extends Fragment {

	protected int mLeftRightMarginPort = 0;

	protected int mLeftRightMarginLand = 0;

	public abstract void setViewParams(Configuration config);

	public abstract String getActionbarTitle();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLeftRightMarginPort = (int) getResources().getDimension(R.dimen.layout_margin_port);
		mLeftRightMarginLand = (int) getResources().getDimension(R.dimen.layout_margin_land);
	}

	@Override
	public void onResume() {
		super.onResume();
		setActionbarTitle();
	}

	private void setActionbarTitle() {
		((TextView) getActivity().findViewById(R.id.action_bar_title)).setText(getActionbarTitle());
	}

	public RegistrationActivity getRegistrationMainActivity() {
		Activity activity = getActivity();
		if (activity != null && (activity instanceof RegistrationActivity)) {
			return (RegistrationActivity) activity;
		}
		return null;
	}

	

	public void raiseEvent(String event) {
		// TODO Auto-generated method stub
		RLog.i(RLog.NETWORK_STATE, event);
	}

}

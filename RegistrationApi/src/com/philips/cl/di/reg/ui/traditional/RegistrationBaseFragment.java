package com.philips.cl.di.reg.ui.traditional;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.ui.customviews.XTextview;

public abstract class RegistrationBaseFragment extends Fragment {

	protected int mLeftRightMarginPort = 0;
	protected int mLeftRightMarginLand = 0;
	private Activity mFragmentActivityContext = null;

	public abstract void setViewParams(Configuration config);
	public abstract String getActionbarTitle();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentActivityContext = getActivity();
	}

	@Override
	public void onResume() {
		super.onResume();
		setActionbarTitle();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLeftRightMarginPort = (int) mFragmentActivityContext.getResources()
				.getDimension(R.dimen.layout_margin_port);
		mLeftRightMarginLand = (int) mFragmentActivityContext.getResources()
				.getDimension(R.dimen.layout_margin_land);
	}

	private void setActionbarTitle() {
		((XTextview) getActivity().findViewById(R.id.action_bar_title))
				.setText(getActionbarTitle());
	}

}

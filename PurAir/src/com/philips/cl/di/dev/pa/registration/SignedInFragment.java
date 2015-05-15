package com.philips.cl.di.dev.pa.registration;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;

public class SignedInFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.air_registration_signed_in_fragment, container, false);
		
		Button closeBtn = (Button) view.findViewById(R.id.air_registration_close_btn);
//		MetricsTracker.trackPage(TrackPageConstants.USER_SIGNED_IN);
		MetricsTracker.trackPageSuccessLoginUser(TrackPageConstants.USER_SIGNED_IN);
		closeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((UserRegistrationActivity)getActivity()).closeUserRegistration(Utils.getAppFirstUse());
			}
		});
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		ViewGroup container = (RelativeLayout) getView().findViewById(R.id.containerRL);
		setBackground(container, R.drawable.ews_nav_bar_2x, Color.BLACK, .1F);
		super.onActivityCreated(savedInstanceState);
	}
}

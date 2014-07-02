package com.philips.cl.di.dev.pa.registration;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;

public class SignedInFragment extends BaseFragment {
	
	private static final String SHARED_PREFERENCE_NAME = "StartFlowPreferences";
	private static final String SHARED_PREFERENCE_FIRST_USE = "FirstUse";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.air_registration_signed_in_fragment, container, false);
		
		Button closeBtn = (Button) view.findViewById(R.id.air_registration_close_btn);
		closeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
				boolean firstUse = preferences.getBoolean(SHARED_PREFERENCE_FIRST_USE, true);

				((UserRegistrationActivity)getActivity()).closeUserRegistration(firstUse);
			}
		});
		
		return view;
	}
	
	
}

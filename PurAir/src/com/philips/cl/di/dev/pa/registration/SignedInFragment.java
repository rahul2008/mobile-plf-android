package com.philips.cl.di.dev.pa.registration;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.Utils;

public class SignedInFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.air_registration_signed_in_fragment, container, false);
		
		Button closeBtn = (Button) view.findViewById(R.id.air_registration_close_btn);
		closeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((UserRegistrationActivity)getActivity()).closeUserRegistration(Utils.getAppFirstUse());
			}
		});
		
		return view;
	}
	
	
}

package com.philips.cl.di.dev.pa.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;

public class ProgressDialogFragment extends DialogFragment {
	
	public static final String EXTRA_MESSAGE = "message";


	public static ProgressDialogFragment newInstance(String message) {
		ProgressDialogFragment fragment = new ProgressDialogFragment();

		Bundle args = new Bundle();
		args.putString(EXTRA_MESSAGE, message);

		fragment.setArguments(args);		
		return fragment;		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.progress_dialog, container, false);
		getDialog().requestWindowFeature(STYLE_NO_TITLE);
		setCancelable(false);

		return view;
	}

}

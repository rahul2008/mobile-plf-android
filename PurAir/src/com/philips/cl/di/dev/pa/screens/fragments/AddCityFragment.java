package com.philips.cl.di.dev.pa.screens.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;

// TODO: Auto-generated Javadoc
/**
 * The Class AddCityFragment.
 */
public class AddCityFragment extends Fragment implements OnClickListener {
	
	/** The btn submit. */
	private Button btnSubmit ; 

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_addcity, container, false);
		v.setOnClickListener(this);
		btnSubmit= (Button) v.findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		return v;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSubmit:
			Log.i("TAG", "Button Clicked");
			break;
		default:
			break;
		}
		
	}

}

package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.digitalcare.R;

/*
 *	ProductRegistrationFragment will help to register the particular 
 *	product based of category.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 10 Dec 2014
 */
public class ProductRegistrationFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_product_registration,
				container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}

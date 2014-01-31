package com.philips.cl.di.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.base.DiHelper;

public class DiPageFragment extends Fragment {
	public int mPage;
	private String mprefix;
	public DiPageFragment() {
		mprefix = "ews_fragment_page_";
		mPage = 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = getArguments();
		int layout = getActivity().getResources().getIdentifier(args.getString("prefix")+args.getInt("page"), "layout", getActivity().getPackageName());
		View v = inflater.inflate(layout, container, false);
		return v;
	}

}
 
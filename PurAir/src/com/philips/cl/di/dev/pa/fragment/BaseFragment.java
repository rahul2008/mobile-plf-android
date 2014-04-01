package com.philips.cl.di.dev.pa.fragment;

import com.philips.cl.di.dev.pa.utils.ALog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ALog.d(ALog.FRAGMENT, "OnCreate on " + this.getClass().getSimpleName());
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ALog.d(ALog.FRAGMENT, "OnCreateView on " + this.getClass().getSimpleName());
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		ALog.d(ALog.FRAGMENT, "OnStart on " + this.getClass().getSimpleName());
		super.onStart();
	}
	
	@Override
	public void onResume() {
		ALog.d(ALog.FRAGMENT, "OnResume on " + this.getClass().getSimpleName());
		super.onResume();
	}
	
	@Override
	public void onPause() {
		ALog.d(ALog.FRAGMENT, "OnPause on " + this.getClass().getSimpleName());
		super.onPause();
	}
	
	@Override
	public void onStop() {
		ALog.d(ALog.FRAGMENT, "OnStop on " + this.getClass().getSimpleName());
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		ALog.d(ALog.FRAGMENT, "OnDestroyView on " + this.getClass().getSimpleName());
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		ALog.d(ALog.FRAGMENT, "OnDestroy on " + this.getClass().getSimpleName());
		super.onDestroy();
	}
	
}

package com.philips.cl.di.dev.pa.fragment;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.util.ALog;

public class BaseFragment extends Fragment {

	private static final Field sChildFragmentManagerField;
	
	static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            ALog.e(ALog.FRAGMENT, "Error getting mChildFragmentManager field");
        }
        sChildFragmentManagerField = f;
    }
	
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
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {
                ALog.e(ALog.FRAGMENT, "Error setting mChildFragmentManager field");
            }
        }
	}
	
}

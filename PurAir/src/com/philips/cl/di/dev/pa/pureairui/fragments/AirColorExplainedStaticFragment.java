package com.philips.cl.di.dev.pa.pureairui.fragments;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.util.Fonts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AirColorExplainedStaticFragment extends Fragment {

	private int activity;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    MainActivity mainActivity=(MainActivity) getActivity();
	    activity=mainActivity.getAirExplainedActivity();
	    mainActivity.disableNavigationIndicator();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
    	View view= initializeView(activity,inflater, container);
		return view;    	
    }

	private View initializeView(int activity, LayoutInflater inflater, ViewGroup container) {
		View view=null;
		if(activity== 11){
			view= inflater.inflate(R.layout.indoor_pollutants_screen, container, false);			
		}
		else if(activity==12){
			view= inflater.inflate(R.layout.vita_shield_technology_screen, container, false);			
		}
		else if(activity==13){
			view= inflater.inflate(R.layout.guards_environment_screen, container, false);			
		}
		
		return view;
	}	
	
}

package com.philips.cl.di.dev.pa.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.pureairui.fragments.AirQualityFragment;
import com.philips.cl.di.dev.pa.utils.Fonts;

public class AirColorExplainedStaticActivity extends BaseActivity {

	private int activity;
	private ActionBar mActionBar;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    initActionBar();
	    Intent in=getIntent();
	    activity=in.getIntExtra("AIR_QUALITY_ACTIVITY", -1);
	    
	    if(activity== AirQualityFragment.INDOOR_POLLUTANT_SCREEN){
			setContentView(R.layout.indoor_pollutants_screen);	
			setActionBarTitle(R.string.indoor_pollutants);
		}
		else if(activity==AirQualityFragment.VITASHIELD_SCREEN){
			setContentView(R.layout.vita_shield_technology_screen);	
			setActionBarTitle(R.string.vistashield_ips_system);
		}
		else if(activity==AirQualityFragment.GUARD_EVNVIRONMENT_SCREEN){
			setContentView(R.layout.guards_environment_screen);		
			setActionBarTitle(R.string.guards_environment);
		}	    
	}
	
	/*Initialize action bar */
	private void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setIcon(null);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		mActionBar.setCustomView(R.layout.action_bar);			
	}

	/*Sets Action bar title */
	public void setActionBarTitle(int tutorialTitle) {    	
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		textView.setTypeface(Fonts.getGillsansLight(this));
		textView.setTextSize(24);
		textView.setText(this.getText(tutorialTitle));
	}	
}

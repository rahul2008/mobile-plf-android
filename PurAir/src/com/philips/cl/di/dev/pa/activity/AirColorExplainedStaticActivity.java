package com.philips.cl.di.dev.pa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.AirQualityFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;

public class AirColorExplainedStaticActivity extends BaseActivity {

	private int activity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.ERROR, "Actionbar: " + e.getMessage());
		}
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
	private void initActionBar() throws ClassCastException {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = getLayoutInflater().inflate(R.layout.home_action_bar, null);
		((ImageView)view.findViewById(R.id.right_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.left_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.back_to_home_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.add_location_img)).setVisibility(View.GONE);
		actionBar.setCustomView(view);			
	}

	/*Sets Action bar title */
	public void setActionBarTitle(int tutorialTitle) {    	
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		textView.setTypeface(Fonts.getGillsansLight(this));
		textView.setTextSize(24);
		textView.setText(this.getText(tutorialTitle));
	}	
}

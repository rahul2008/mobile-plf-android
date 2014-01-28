package com.philips.cl.di.dev.pa.screens;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.util.Fonts;

public class AirTutorialActivity extends Activity implements OnClickListener {
	
	public static final String SELECTED_PAGE = "tutorial_selected_page";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.air_tutorial);
		
		Resources resources = getResources();
    	float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());    	
		
		Button btnAppNavigation= (Button) findViewById(R.id.btn_app_navigation);
		Button btnAirPurifierControls=(Button) findViewById(R.id.btn_air_purifier_controls);		
		Button btnIndoorAIQReadings=(Button) findViewById(R.id.btn_indoor_air_quality_readings);		
		setMargins(btnIndoorAIQReadings, Math.round(MainActivity.getScreenWidth()/2+(30 * px)), (int)Math.round(((MainActivity.getScreenHeight()*0.7)- (MainActivity.getScreenHeight() * 0.075))/2-(23 * px)), 0, 0);
		
		Button btnOutdoorAIQReadings=(Button) findViewById(R.id.btn_outdoor_aqi_readings);
		Button btnFinish=(Button) findViewById(R.id.btn_finish_tour);
		
		btnAppNavigation.setTypeface(Fonts.getGillsans(this));
		btnAirPurifierControls.setTypeface(Fonts.getGillsans(this));
		btnOutdoorAIQReadings.setTypeface(Fonts.getGillsans(this));
		btnIndoorAIQReadings.setTypeface(Fonts.getGillsans(this));
		btnFinish.setTypeface(Fonts.getGillsans(this));
		
		btnAppNavigation.setOnClickListener(this);
		btnAirPurifierControls.setOnClickListener(this);
		btnOutdoorAIQReadings.setOnClickListener(this);
		btnIndoorAIQReadings.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
	}	

	public void setMargins (Button v, int l, int t, int r, int b) {   	
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	params.setMargins(l, t, r, b);       
    	v.setLayoutParams(params);
    }

	@Override
	public void onClick(View v) {
		Intent in=new Intent(AirTutorialActivity.this, TutorialPagerActivity.class);
		switch (v.getId()){
		case R.id.btn_app_navigation:
			in.putExtra(SELECTED_PAGE, 1);
			startActivity(in);
			break;
		case R.id.btn_air_purifier_controls:
			in.putExtra(SELECTED_PAGE, 2);
			startActivity(in);
			break;
		case R.id.btn_indoor_air_quality_readings:
			in.putExtra(SELECTED_PAGE, 4);
			startActivity(in);
			break;
		case R.id.btn_outdoor_aqi_readings:
			in.putExtra(SELECTED_PAGE, 5);
			startActivity(in);
			break;
		case R.id.btn_finish_tour:
			AirTutorialActivity.this.finish();
			break;
		
		}
			
	}
}

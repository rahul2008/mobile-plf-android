package com.philips.cl.di.dev.pa.activity;

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
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;

public class AirTutorialActivity extends BaseActivity implements OnClickListener {
	
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
		float float1 = 30 * px;
		float double1 = MainActivity.getScreenHeight()*0.5F- MainActivity.getScreenHeight() * 0.075F;
		setMargins(btnIndoorAIQReadings, 
				Math.round(MainActivity.getScreenWidth()/(float)2+float1), Math.round(double1/ (float)2), 0, 0);
		
		Button btnOutdoorAIQReadings=(Button) findViewById(R.id.btn_outdoor_aqi_readings);
		Button btnFinish=(Button) findViewById(R.id.btn_finish_tour);
		
		btnAppNavigation.setTypeface(Fonts.getCentraleSansLight(this));
		btnAirPurifierControls.setTypeface(Fonts.getCentraleSansLight(this));
		btnOutdoorAIQReadings.setTypeface(Fonts.getCentraleSansLight(this));
		btnIndoorAIQReadings.setTypeface(Fonts.getCentraleSansLight(this));
		btnFinish.setTypeface(Fonts.getCentraleSansLight(this));
		
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
	protected void onResume() {
		super.onResume();
		MetricsTracker.trackPage(TrackPageConstants.APP_TUTORIAL);
	}

	@Override
	public void onClick(View v) {
		Intent in=new Intent(AirTutorialActivity.this, TutorialPagerActivity.class);
		switch (v.getId()){
		case R.id.btn_app_navigation:
			in.putExtra(SELECTED_PAGE, 1);
			((Button)findViewById(R.id.btn_app_navigation)).setTextColor(getResources().getColor(R.color.light_gray));
			startActivity(in);
			break;
		case R.id.btn_air_purifier_controls:
			in.putExtra(SELECTED_PAGE, 2);
			((Button)findViewById(R.id.btn_air_purifier_controls)).setTextColor(getResources().getColor(R.color.light_gray));
			startActivity(in);
			break;
		case R.id.btn_indoor_air_quality_readings:
			in.putExtra(SELECTED_PAGE, 4);
			((Button)findViewById(R.id.btn_indoor_air_quality_readings)).setTextColor(getResources().getColor(R.color.light_gray));
			startActivity(in);
			break;
		case R.id.btn_outdoor_aqi_readings:
			in.putExtra(SELECTED_PAGE, 5);
			startActivity(in);
			((Button)findViewById(R.id.btn_outdoor_aqi_readings)).setTextColor(getResources().getColor(R.color.light_gray));
			break;
		case R.id.btn_finish_tour:
			AirTutorialActivity.this.finish();
			break;
		default:
			break;
		}
			
	}
}

package com.philips.cl.di.dev.pa.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AirAnalysisExplainActivity extends BaseActivity {
	
	public static final String TYPE_EXTRA = "type_air_quality_explain";
	public static final String OUTDOOR_EXTRA = "type_outdoor_air_quality_explain";
	public static final String INDOOR_EXTRA = "type_indoor_air_quality_explain";
	
	private String outdoorTitle = PurAirApplication.getAppContext().getString(R.string.good); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (INDOOR_EXTRA.equals(bundle.getString(TYPE_EXTRA))) {
				setContentViewAndTitle(R.layout.indoor_air_quality_explain, R.string.outdoor_aiq_analysis_head1);
				String aqiAnalysis = String.format(getString(R.string.outdoor_analysis_detail2_head100), outdoorTitle, outdoorTitle) ;
				((FontTextView) findViewById(R.id.aqiAnalysisMsg11)).setText(aqiAnalysis);
			} else if (OUTDOOR_EXTRA.equals(bundle.getString(TYPE_EXTRA))) {
				setContentViewAndTitle(R.layout.od_air_quality_explain, R.string.outdoor_aiq_analysis_head1);
			}
		}
		
		
	}
	
	/**
	 * Set content view layout and initialize views
	 * @param layoutId
	 * @param titleId
	 */
	private void setContentViewAndTitle(int layoutId, int titleId) {
		setContentView(layoutId);
		FontTextView title = (FontTextView) findViewById(R.id.heading_name_tv);
		ImageButton backBtn = (ImageButton) findViewById(R.id.heading_back_imgbtn);
		
		title.setText(getString(titleId));
		backBtn.setVisibility(View.VISIBLE);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}

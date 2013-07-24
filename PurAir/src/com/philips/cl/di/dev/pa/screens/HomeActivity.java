package com.philips.cl.di.dev.pa.screens;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.utils.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class HomeActivity.
 */
public class HomeActivity extends BaseActivity {
	
	/** The ImageViews . */
	private ImageView ivLeftMenu , ivRightSettings ;

	/** The textviews . */
	private TextView tvIndoorAQI, tvOutdoorAQI, tvIndoorDateTime, tvOutdoorDateTime,
			tvIndoorLabel, tvIndoorCity, tvOutdoorCity, tvOutdoorCountry,
			tvIndoorLocation;

	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.screens.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setting click listeners for left menu and right settings
		ViewGroup tabBar = (ViewGroup) centerView
				.findViewById(R.id.rlTopNavigation);
		ivLeftMenu = (ImageView) tabBar.findViewById(R.id.ivMenu);
		ivLeftMenu.setOnClickListener(new ClickListenerForScrolling(scrollView,
				leftMenu));
		ivRightSettings = (ImageView) tabBar.findViewById(R.id.ivSettings);
		ivRightSettings.setOnClickListener(new ClickListenerForScrolling(
				scrollView, rightSettings));

		initializeTextViews();
	}

	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.screens.BaseActivity#getCenterView()
	 */
	@Override
	protected View getCenterView() {

		return inflater.inflate(R.layout.activity_home, null);
	}

	/**
	 * Initialize the text views.
	 */
	private void initializeTextViews() {
		tvIndoorAQI = (TextView) findViewById(R.id.tvAQINumber_Indoor);
		tvIndoorAQI.setTypeface(Utils.getTypeFace(getApplicationContext()));
		tvOutdoorAQI = (TextView) findViewById(R.id.tvAQINumber_Outdoor);
		tvOutdoorAQI.setTypeface(Utils.getTypeFace(getApplicationContext()));
		tvIndoorDateTime = (TextView) findViewById(R.id.tvDateTime_Local);
		tvIndoorDateTime
				.setTypeface(Utils.getTypeFace(getApplicationContext()));
		tvOutdoorDateTime = (TextView) findViewById(R.id.tvDateTime_City);
		tvOutdoorDateTime.setTypeface(Utils
				.getTypeFace(getApplicationContext()));
		tvIndoorLocation = (TextView) findViewById(R.id.tvIndoorLocation);
		tvIndoorLocation
				.setTypeface(Utils.getTypeFace(getApplicationContext()));
		tvIndoorCity = (TextView) findViewById(R.id.tvIndoorCity);
		tvIndoorCity.setTypeface(Utils.getTypeFace(getApplicationContext()));
		tvOutdoorCity = (TextView) findViewById(R.id.tvOutdoorCity);
		tvOutdoorCity.setTypeface(Utils.getTypeFace(getApplicationContext()));
		tvOutdoorCountry = (TextView) findViewById(R.id.tvOutdoorCountry);
		tvOutdoorCountry
				.setTypeface(Utils.getTypeFace(getApplicationContext()));

	}

	

}

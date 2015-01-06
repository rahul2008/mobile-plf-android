package com.philips.cl.di.dev.pa.outdoorlocations;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;

public class AddOutdoorLocationActivity extends BaseActivity {
	
	private ListView mOutdoorLocationListView;
	private AddOutdoorLocationAdapter mAdapter;
	
	private Button mActionBarCancelBtn;
	private EditText mEnteredCityName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.outdoor_locations_fragment);
				
		View btn = findViewById(R.id.btn_current_location);
		btn.setVisibility(View.GONE);
		
		View text = findViewById(R.id.tv_lm_outdoor_locations_title);
		text.setVisibility(View.GONE);
		
		mOutdoorLocationListView = (ListView) findViewById(R.id.outdoor_locations_list);
		
		mOutdoorLocationListView.setOnItemClickListener(mOutdoorLocationsItemClickListener);
		
		initActionBar();
		
		populateOutdoorLocations();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MetricsTracker.trackPage(TrackPageConstants.ADD_OUTDOOR_LOCATION);
	}

	private void updateAdapter(String input) {
		List<OutdoorCityInfo> tempOutdoorCityInfoList = new ArrayList<OutdoorCityInfo>() ;
		for (OutdoorCityInfo outdoorCityInfo : outdoorCityInfoList) {
			
			String cityName = getCityNameWithRespectToLanguage(outdoorCityInfo);
			
			input = input.toLowerCase();
			if( !input.isEmpty() && cityName.contains(input)) {
				tempOutdoorCityInfoList.add(outdoorCityInfo) ;
			}
		}
		
		if( !input.isEmpty() || !tempOutdoorCityInfoList.isEmpty()) {
			mAdapter = new AddOutdoorLocationAdapter(this, R.layout.simple_list_item, tempOutdoorCityInfoList);
		}
		else {
			mAdapter = new AddOutdoorLocationAdapter(this, R.layout.simple_list_item, outdoorCityInfoList);
		}
		mOutdoorLocationListView.setAdapter(mAdapter);
	}
	
	private String getCityNameWithRespectToLanguage(OutdoorCityInfo outdoorCityInfo) {
		String cityName = outdoorCityInfo.getCityName().toLowerCase();;
		
		if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
			cityName = outdoorCityInfo.getCityNameCN() ;
		} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			cityName = outdoorCityInfo.getCityNameTW() ;
		}
		
		return cityName;
	}
	
	private void initActionBar() {
		ActionBar actionBar;
		actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		View view  = getLayoutInflater().inflate(R.layout.search_actionbar, null);
		
		mActionBarCancelBtn = (Button) view.findViewById(R.id.search_bar_cancel_btn);
		mActionBarCancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mEnteredCityName = (EditText) view.findViewById(R.id.search_bar_city_name);
		mEnteredCityName.setTypeface(Fonts.getGillsansLight(this));
		mEnteredCityName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				updateAdapter(s.toString().trim());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		actionBar.setCustomView(view);
	}
	private void showAlertDialog(String title, String message) {
		try {
			FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
			
			Fragment prevFrag = getSupportFragmentManager().findFragmentByTag("max_purifier_reached");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}
			
			fragTransaction.add(DownloadAlerDialogFragement.
					newInstance(title, message), "max_purifier_reached").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, "Error: " + e.getMessage());
		}
	}
	
	private OnItemClickListener mOutdoorLocationsItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			
			if(OutdoorManager.getInstance().getUsersCitiesList().size() > 19) {
				showAlertDialog(null, getString(R.string.max_city_reached));
				return;
			}
			
			OutdoorCityInfo outdoorCityInfo = (OutdoorCityInfo) mAdapter.getItem(position);
			
			String city = outdoorCityInfo.getCityName();
			String key = getCityKeyWithRespectDataProvider(outdoorCityInfo);

			MetricsTracker.trackActionLocationWeather(city);
			OutdoorManager.getInstance().addAreaIDToUsersList(key);
			OutdoorManager.getInstance().resetUpdatedTime();
			OutdoorLocationHandler.getInstance().updateSelectedCity(key, true);
			finish();
		}
	};
	
	private String getCityKeyWithRespectDataProvider(OutdoorCityInfo outdoorCityInfo) {
		String key = outdoorCityInfo.getAreaID();
		if (outdoorCityInfo.getDataProvider() == OutdoorDataProvider.US_EMBASSY.ordinal()) {
			key = outdoorCityInfo.getCityName();
		}
		return key;
	}

	private List<OutdoorCityInfo> outdoorCityInfoList ;
	private void populateOutdoorLocations() {
		Map<String, OutdoorCity> outdoorCityMap = OutdoorManager.getInstance().getCitiesMap() ;
		
		outdoorCityInfoList = AddOutdoorLocationHelper.getAllCityInfoList(outdoorCityMap) ;
		
		mAdapter = new AddOutdoorLocationAdapter(this, R.layout.simple_list_item, outdoorCityInfoList);
		mOutdoorLocationListView.setAdapter(mAdapter);
	}
	
}

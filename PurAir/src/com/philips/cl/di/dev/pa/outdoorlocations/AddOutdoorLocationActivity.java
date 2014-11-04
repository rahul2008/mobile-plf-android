package com.philips.cl.di.dev.pa.outdoorlocations;


import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AddOutdoorLocationActivity extends BaseActivity implements OutdoorCityListener {
	
	private ListView mOutdoorLocationListView;
	private CursorAdapter mOutdoorLocationAdapter;
	
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
		
	}
	
	@Override
	public void onResume() {
		
		OutdoorLocationHandler.getInstance().setCityListener(this);
		OutdoorLocationHandler.getInstance().fetchCities(AppConstants.SQL_SELECTION_GET_SHORTLIST_ITEMS_EXCEPT_SELECTED);
		super.onResume();
	}
	
	private void updateAdapter(String input) {
		String cityColumn = AppConstants.KEY_CITY;
		
		if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
			cityColumn = AppConstants.KEY_CITY_CN;
		} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			cityColumn = AppConstants.KEY_CITY_TW;
		}
		
		String selection = AppConstants.SQL_SELECTION_GET_SHORTLIST_ITEMS_EXCEPT_SELECTED;  
		
		if (!input.isEmpty()) {
			selection = AppConstants.SQL_SELECTION_GET_SHORTLIST_ITEMS_EXCEPT_SELECTED + " and " 
					+ cityColumn + " like '%" + input + "%' ";
		}
		
		OutdoorLocationHandler.getInstance().fetchCities(selection);
	}
	
	@Override
	public void onPause() {
		OutdoorLocationHandler.getInstance().removeCityListener();
		super.onPause();
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
				updateAdapter(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		actionBar.setCustomView(view);
	}
	
	private void fillListViewFromDatabase(Cursor cursor) {
		if (cursor != null) {
			mOutdoorLocationAdapter = new CursorAdapter(this, cursor, false) {
				
				@Override
				public View newView(Context context, Cursor cursor, ViewGroup parent) {
					LayoutInflater inflater = LayoutInflater.from(parent.getContext());
					View retView = inflater.inflate(R.layout.simple_list_item, parent, false);
					
					return retView;
				}
				
				@Override
				public void bindView(View view, Context context, Cursor cursor) {
					ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
					FontTextView tvName = (FontTextView) view.findViewById(R.id.list_item_name);
					
					deleteSign.setVisibility(View.GONE);
					
					String city = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY));
					
					if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
						city = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_CN));
					} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
						city = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_TW));
					}
					
					tvName.setText(city);
				}
			};
			
			mOutdoorLocationListView.setAdapter(mOutdoorLocationAdapter);
		}
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
			ALog.e(ALog.ERROR, e.getMessage());
		}
	}
	
	private OnItemClickListener mOutdoorLocationsItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			
			if(OutdoorManager.getInstance().getUsersCitiesList().size() > 19) {
				showAlertDialog(null, getString(R.string.max_city_reached));
				return;
			}
			
			Cursor cursor = (Cursor) mOutdoorLocationAdapter.getItem(position);
			cursor.moveToPosition(position);
			
			String city = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY));
			String cityCN = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_CN));
			String cityTW = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_TW));
			String areaId = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AREA_ID));
			float longitude = cursor.getFloat(cursor.getColumnIndex(AppConstants.KEY_LONGITUDE));
			float latitude = cursor.getFloat(cursor.getColumnIndex(AppConstants.KEY_LATITUDE));

			OutdoorCityInfo info = new OutdoorCityInfo(city, cityCN, cityTW, longitude, latitude, areaId);
			
			ALog.i(ALog.OUTDOOR_LOCATION, "AddOutdoorLocationActivity areaID " + areaId + " cityname " + info.getCityName());
			OutdoorManager.getInstance().addAreaIDToUsersList(areaId);
			OutdoorManager.getInstance().addCityDataToMap(info, null, null, areaId);
			
			OutdoorLocationHandler.getInstance().updateSelectedCity(areaId, true);
			finish();
		}
	};

	@Override
	public void onCityLoad(final Cursor cursor) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				fillListViewFromDatabase(cursor);
			}
		});
		
	}
}

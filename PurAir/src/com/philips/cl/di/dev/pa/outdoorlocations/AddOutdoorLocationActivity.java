package com.philips.cl.di.dev.pa.outdoorlocations;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class AddOutdoorLocationActivity extends BaseActivity {
	
	private ListView mOutdoorLocationListView;
	private CursorAdapter mOutdoorLocationAdapter;
	
	private OutdoorLocationAbstractGetAsyncTask mOutdoorLocationGetAsyncTask;
	private OutdoorLocationAbstractUpdateAsyncTask mOutdoorLocationAbstractUpdateAsyncTask;
	
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
		mOutdoorLocationGetAsyncTask = (OutdoorLocationAbstractGetAsyncTask) new OutdoorLocationAbstractGetAsyncTask() {

			@Override
			protected void onPostExecute(Cursor result) {
				fillListViewFromDatabase(result);
			}
		}.execute(new String[]{null});
		super.onResume();
	}
	
	private void updateAdapter(String input) {
		String selection = AppConstants.KEY_CITY + " like '%" + input + "%'";
		
		mOutdoorLocationGetAsyncTask = (OutdoorLocationAbstractGetAsyncTask) new OutdoorLocationAbstractGetAsyncTask() {

			@Override
			protected void onPostExecute(Cursor result) {
				fillListViewFromDatabase(result);
			}
		}.execute(new String[]{selection});
	}
	
	@Override
	public void onPause() {
		mOutdoorLocationGetAsyncTask.cancel(true);
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
				mOutdoorLocationGetAsyncTask.cancel(true);
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
					String province = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_CN));

					tvName.setText(city + ", " + province);
				}
			};
			
			mOutdoorLocationListView.setAdapter(mOutdoorLocationAdapter);
		}
	}
	
	private OnItemClickListener mOutdoorLocationsItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			
			Cursor cursor = (Cursor) mOutdoorLocationAdapter.getItem(position);
			cursor.moveToPosition(position);
			
			final String areaId = cursor.getString(cursor.getColumnIndexOrThrow(AppConstants.KEY_AREA_ID));
			final String cityName = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY));
			final String cityCN = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_CN));
			
			ALog.i(ALog.OUTDOOR_LOCATION, "AddOutdoorLocationActivity areaID " + areaId + " cityname " + cityName);
			OutdoorManager.getInstance().addAreaIDToList(areaId);
			OutdoorManager.getInstance().addCityDataToMap(areaId, cityName, cityCN, null, null);
			
			mOutdoorLocationAbstractUpdateAsyncTask = (OutdoorLocationAbstractUpdateAsyncTask) new OutdoorLocationAbstractUpdateAsyncTask() {

				@Override
				protected void onPostExecute(Void result) {
					finish();
				}
			}.execute(new String[]{areaId, "true"});
		}
	};
}

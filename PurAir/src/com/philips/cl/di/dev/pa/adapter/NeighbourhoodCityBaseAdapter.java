package com.philips.cl.di.dev.pa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.OutdoorDetailsActivity.NearbyInfoType;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class NeighbourhoodCityBaseAdapter extends BaseAdapter { 
	
	private Context context;
	private List<OutdoorAQI> nearbyLocationAQIs;
	private NearbyInfoType infotype;
	private String parentAreaId;

	//List<mynearbycityData> mynearbycityDataList = mynearbycityData.getmynearbycityData();
	
	public NeighbourhoodCityBaseAdapter(Context context, List<OutdoorAQI> nearbyLocationAQIs, NearbyInfoType infotype, String parentAreaId) {
		this.context = context;
		this.nearbyLocationAQIs = nearbyLocationAQIs;
		this.infotype = infotype;
		this.parentAreaId = parentAreaId;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nearbyLocationAQIs.size();
	}

	@Override
	public OutdoorAQI getItem(int arg0) {
		// TODO Auto-generated method stub
		return nearbyLocationAQIs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {


		LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = infalInflater.inflate(R.layout.near_city_data,	null);
		FontTextView cityNameTV = (FontTextView) view.findViewById(R.id.near_city_name_tv);
		FontTextView nearcitydataTV = (FontTextView) view.findViewById(R.id.near_city_data_tv);


		OutdoorAQI nearbycitydata = nearbyLocationAQIs.get(position);
		
		if (nearbycitydata != null ) {
			String cityName = OutdoorManager.getInstance().getLocalityNameFromAreaId(parentAreaId, nearbycitydata.getAreaID());
			cityNameTV.setText(cityName) ;
			if (NearbyInfoType.PM_25 == infotype) {
				nearcitydataTV.setText(String.valueOf(nearbycitydata.getPM25()));
			} else {
				nearcitydataTV.setText(String.valueOf(nearbycitydata.getAQI()));
			}
		}

		return view;
	}

}

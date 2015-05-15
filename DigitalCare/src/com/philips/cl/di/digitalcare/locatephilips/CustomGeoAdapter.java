package com.philips.cl.di.digitalcare.locatephilips;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.cl.di.digitalcare.R;

public class CustomGeoAdapter extends BaseAdapter {

	Context context;
	// List<GeoDataModel> ListItems;
	ArrayList<ResultsModel> mresultModel;

	CustomGeoAdapter(Context context, ArrayList<ResultsModel> resultModel) {
		this.context = context;
		// this.ListItems = ListItems;

		this.mresultModel = resultModel;

	}

	@Override
	public int getCount() {

		return mresultModel.size();
	}

	@Override
	public Object getItem(int position) {

		return mresultModel.get(position);
	}

	@Override
	public long getItemId(int position) {

		return mresultModel.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.geo_list_item, null);
		}

		TextView txtAddress = (TextView) convertView
				.findViewById(R.id.place_address);
		TextView txtCityState = (TextView) convertView
				.findViewById(R.id.place_city_state);

		TextView txtPhone = (TextView) convertView
				.findViewById(R.id.place_phone);

		ResultsModel resultModel = mresultModel.get(position);

		AddressModel addressModel = resultModel.getmAddressModel();

		txtAddress.setText(addressModel.getAddress1());
		txtCityState.setText(addressModel.getCityState());
		txtPhone.setText(addressModel.getPhone());

		return convertView;

	}

}
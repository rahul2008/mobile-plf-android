package com.philips.cl.di.digitalcare.locatephilips;

/**
 * CustomGeoAdapter is Custom BaseAdapter for Search ListView.
 * 
 * @author : pawan.kumar.deshpande@philips.com
 * 
 * @since : 15 May 2015
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.philips.cl.di.digitalcare.R;

public class CustomGeoAdapter extends BaseAdapter implements Filterable {
	private Context context;
	private ArrayList<AtosResultsModel> mResultModelSet;
	private ArrayList<AtosResultsModel> mOriginalSet;

	private CustomFilter mCustomFilter;

	CustomGeoAdapter(Context context,
			ArrayList<AtosResultsModel> mresultModelSet) {
		this.context = context;
		this.mResultModelSet = mresultModelSet;
		mOriginalSet = mresultModelSet;
	}

	@Override
	public int getCount() {
		return mResultModelSet.size();
	}

	@Override
	public Object getItem(int position) {
		return mResultModelSet.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mResultModelSet.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.geo_list_item, null);
		}

		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.place_title);
		TextView txtAddress = (TextView) convertView
				.findViewById(R.id.place_address);
		TextView txtPhone = (TextView) convertView
				.findViewById(R.id.place_phone);

		AtosResultsModel resultModel = mResultModelSet.get(position);
		AtosAddressModel addressModel = resultModel.getmAddressModel();

		txtTitle.setText(resultModel.getTitle());
		txtAddress.setText(addressModel.getAddress1() + "\n"
				+ addressModel.getCityState());
		txtPhone.setText(addressModel.getPhone());
		return convertView;
	}

	@Override
	public Filter getFilter() {
		if (mCustomFilter == null) {
			mCustomFilter = new CustomFilter();
		}
		return mCustomFilter;
	}

	private class CustomFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			if (constraint != null && constraint.length() > 0) {
				ArrayList<AtosResultsModel> FilteredResultModelSet = new ArrayList<AtosResultsModel>();
				for (int i = 0; i < mOriginalSet.size(); i++) {
					AtosResultsModel resultModel = mOriginalSet.get(i);
					AtosAddressModel addressModel = resultModel
							.getmAddressModel();
					if ((addressModel.getCityState().toUpperCase())
							.contains(constraint.toString().toUpperCase())) {

						AtosResultsModel filteredResultModel = new AtosResultsModel();

						filteredResultModel.setId(resultModel.getId());
						filteredResultModel.setInfoType(resultModel
								.getInfoType());
						filteredResultModel.setTitle(resultModel.getTitle());
						filteredResultModel.setLocationModel(resultModel
								.getLocationModel());
						filteredResultModel.setAddressModel(resultModel
								.getmAddressModel());
						FilteredResultModelSet.add(filteredResultModel);
					} // if

				} // for
				filterResults.count = FilteredResultModelSet.size();
				filterResults.values = FilteredResultModelSet;
			} else {
				synchronized (this) {
					filterResults.count = mOriginalSet.size();
					filterResults.values = mOriginalSet;
				}
			}
			return filterResults;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mResultModelSet = (ArrayList<AtosResultsModel>) results.values;
			notifyDataSetChanged();
		}
	}
}
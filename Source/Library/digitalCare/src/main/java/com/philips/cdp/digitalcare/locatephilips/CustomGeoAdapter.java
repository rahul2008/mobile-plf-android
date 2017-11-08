package com.philips.cdp.digitalcare.locatephilips;

/**
 * CustomGeoAdapter is Custom BaseAdapter for Search ListView.
 *
 * @author : pawan.kumar.deshpande@philips.com
 * @since : 15 May 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.locatephilips.models.AtosAddressModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosLocationModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResultsModel;

import java.util.ArrayList;

@SuppressLint("DefaultLocale")
public class CustomGeoAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<AtosResultsModel> mResultModelSet;
    private ArrayList<AtosResultsModel> mOriginalSet;

    private CustomFilter mCustomFilter;

    public CustomGeoAdapter(Context context,
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

        if (convertView == null && context!=null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(inflater!=null)
              convertView = inflater.inflate(R.layout.consumercare_geo_list_item, null);
        }

        ViewHolder holder = new ViewHolder();

        holder.txtTitle = (TextView) convertView.findViewById(R.id.place_title);
        holder.txtAddress = (TextView) convertView
                .findViewById(R.id.place_address);
        holder.txtPhone = (TextView) convertView.findViewById(R.id.place_phone);
        holder.txtDistance = (TextView) convertView.findViewById(R.id.distance_view);

        AtosResultsModel resultModel = mResultModelSet.get(position);
        AtosAddressModel addressModel = resultModel.getAddressModel();


        AtosLocationModel locationModel = resultModel.getLocationModel();
        double lat = Double.parseDouble(locationModel.getLatitude());
        double lng = Double.parseDouble(locationModel.getLongitude());

        LatLng start = new LatLng(addressModel.getCurrentLat(), addressModel.getCurrentLng());
        LatLng end = new LatLng(lat, lng);
        holder.txtDistance.setText(getDistance(start,end)+" km");
        holder.txtTitle.setText(resultModel.getTitle());
        holder.txtAddress.setText(addressModel.getCityState());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mCustomFilter == null) {
            mCustomFilter = new CustomFilter();
        }
        return mCustomFilter;
    }

    private class ViewHolder {

        TextView txtTitle = null;
        TextView txtAddress = null;
        TextView txtPhone = null;
        TextView txtDistance = null;
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
                            .getAddressModel();
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
                                .getAddressModel());
                        FilteredResultModelSet.add(filteredResultModel);
                    }

                }
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


    public String getDistance(LatLng my_latlong, LatLng frnd_latlong) {
        Location l1 = new Location("Source");
        l1.setLatitude(my_latlong.latitude);
        l1.setLongitude(my_latlong.longitude);

        Location l2 = new Location("Destination");
        l2.setLatitude(frnd_latlong.latitude);
        l2.setLongitude(frnd_latlong.longitude);

        float distance = l1.distanceTo(l2);

        String dist = distance + " M";

        if (distance > 1000.0f) {
            distance = distance / 1000.0f;
            dist = distance + " KM";
        }

        dist = String.format("%.01f", distance);
        return dist;
    }
}
/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.dataUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.philips.platform.catalogapp.R;

import java.util.ArrayList;
import java.util.List;

public class StateListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<String> statesList;
    private List<String> filteredList = new ArrayList<>();
    private Filter stateFilter = new StateListFilter();

    public StateListAdapter(Context context, List<String> list) {
        statesList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null) {
            view = View.inflate(context, R.layout.uid_search_item_one_line, null);
            ((TextView)view).setText((CharSequence) getItem(position));
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return stateFilter;
    }

    private class StateListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<String> resultList = new ArrayList<>();
            if(!TextUtils.isEmpty(constraint)) {
                for(String state: statesList) {
                    if(state.toLowerCase().contains(String.valueOf(constraint).toLowerCase())) {
                        resultList.add(state);
                    }
                }
            }

            results.values = resultList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<String>) results.values;
            StateListAdapter.this.notifyDataSetChanged();
        }


    }
}
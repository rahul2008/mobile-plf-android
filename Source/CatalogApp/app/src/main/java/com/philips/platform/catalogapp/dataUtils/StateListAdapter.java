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
import com.philips.platform.uid.text.utils.UIDSpans;
import com.philips.platform.uid.text.utils.UIDStringUtils;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.ArrayList;
import java.util.List;

public class StateListAdapter extends BaseAdapter implements Filterable, SearchBox.FilterQueryChangedListener {

    private Context context;
    final private List<String> statesList;
    private List<String> filteredList = new ArrayList<>();
    private Filter stateFilter = new StateListFilter();
    private CharSequence query;

    public StateListAdapter(Context context, List<String> list) {
        statesList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public CharSequence getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = View.inflate(context, R.layout.uid_list_item_one_line, null);
        }
        ((TextView) view).setText(UIDSpans.boldSubString(true, context, getItem(position), query));
        return view;
    }

    @Override
    public Filter getFilter() {
        return stateFilter;
    }

    @Override
    public void onQueryTextChanged(CharSequence newQuery) {
        query = newQuery;
    }

    private class StateListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<String> resultList = new ArrayList<>();
            if (!TextUtils.isEmpty(constraint)) {
                for (String state : statesList) {
                    if (UIDStringUtils.indexOfSubString(true, state, constraint) >= 0) {
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
            if ((query.length() > 0) && filteredList.size() <= 0) {
                filteredList.add(context.getResources().getString(R.string.search_box_no_result));
            }
            StateListAdapter.this.notifyDataSetChanged();
        }
    }
}
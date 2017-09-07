package com.philips.cdp.registration.ui.traditional.countrySelection;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.*;
import android.widget.*;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.RegUtility;

import java.util.*;


public class CountrySelectionAdapter extends RecyclerView.Adapter<CountrySelectionAdapter.CountryItemViewHolder> {

    private final List<String> countryList = RegUtility.supportedCountryList();

    public CountrySelectionAdapter() {
        Collections.sort(countryList);
    }

    @Override
    public CountryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_selection_item, parent, false);
        return new CountryItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CountryItemViewHolder holder, int position) {
        String countryName = getCountryName(countryList.get(position));
        holder.countryName.setText(countryName);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class CountryItemViewHolder extends ViewHolder {
        TextView countryName;
        ImageView selected;

        public CountryItemViewHolder(View itemView) {
            super(itemView);
            countryName = (TextView) itemView.findViewById(R.id.usr_countrySelection_countryName);
            countryName.setOnClickListener(v -> selected.setVisibility(View.VISIBLE));
            selected = (ImageView) itemView.findViewById(R.id.usr_countrySelection_countrySelector);
            selected.setVisibility(View.INVISIBLE);
        }
    }

    private String getCountryName(String countryCode) {
        Locale locale = new Locale("", countryCode);
        return locale.getDisplayCountry();
    }

}

package com.philips.cdp.registration.ui.traditional.countrySelection;

import android.os.*;
import android.support.v7.widget.*;
import android.view.*;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.dao.*;
import com.philips.cdp.registration.listener.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.*;

import java.util.*;

public class CountrySelectionAdapter extends RecyclerView.Adapter<CountrySelectionAdapter.CountryPickerHolder> {

    private List<Country> countries;

    private SelectedCountryListener mSelectedCountryListener;

    private int position = 0;

    private Handler handler = new Handler();

    class CountryPickerHolder extends RecyclerView.ViewHolder {
        private Label countryName;
        private Label checked;

        CountryPickerHolder(View view) {
            super(view);
            countryName = (Label) view.findViewById(R.id.title);
            checked = (Label) view.findViewById(R.id.tick);
            checked.setText(R.string.ic_reg_check);
            FontLoader.getInstance().setTypeface(checked, "PUIIcon.ttf");
        }
    }

    public CountrySelectionAdapter(List<Country> countries, SelectedCountryListener selectedCountryListener) {
        super();
        this.countries = countries;
        mSelectedCountryListener = selectedCountryListener;
    }

    @Override
    public CountryPickerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_selection_item, parent, false);
        return new CountryPickerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CountryPickerHolder holder, int position) {
        Country country = countries.get(position);
        holder.countryName.setText(country.getName());
        if (position == this.position) {
            holder.checked.setVisibility(View.VISIBLE);
        } else {
            holder.checked.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(view -> {
            setSelectedPosition(position);
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> mSelectedCountryListener.onCountrySelected(position), 500);
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    private void setSelectedPosition(int posistion) {
        position = posistion;
        notifyDataSetChanged();
    }
}

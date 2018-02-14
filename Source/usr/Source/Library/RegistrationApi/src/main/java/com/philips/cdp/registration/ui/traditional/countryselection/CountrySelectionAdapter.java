package com.philips.cdp.registration.ui.traditional.countryselection;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.dao.Country;
import com.philips.cdp.registration.listener.SelectedCountryListener;
import com.philips.platform.uid.view.widget.Label;

import java.util.List;

public class CountrySelectionAdapter extends RecyclerView.Adapter< RecyclerView.ViewHolder> {

    private static final int HEADER_COUNT = 1;
    private List<Country> countries;

    private SelectedCountryListener mSelectedCountryListener;

    private int selectedPosition = 1; //

    private Handler handler = new Handler();

    private static final int VIEW_TYPE_HEADER= 0;
    private static final int VIEW_TYPE_LIST= 1;

    class CountryPickerHolder extends RecyclerView.ViewHolder {
        private Label countryName;
        private Label checked;

        CountryPickerHolder(View view) {
            super(view);
            countryName = view.findViewById(R.id.title);
            checked = view.findViewById(R.id.tick);
            checked.setText(R.string.dls_checkmark_xbold_32);
        }
    }


    class CountryPickerHeaderHolder extends RecyclerView.ViewHolder {
        CountryPickerHeaderHolder(View view) {
            super(view);
        }
    }

    public CountrySelectionAdapter(List<Country> countries, SelectedCountryListener selectedCountryListener) {
        super();
        this.countries = countries;
        mSelectedCountryListener = selectedCountryListener;
    }


    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if(viewType==VIEW_TYPE_HEADER){

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_selection_header, parent, false);
            viewHolder=new CountryPickerHeaderHolder(itemView);

        }else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_selection_item, parent, false);
            viewHolder=new CountryPickerHolder(itemView);
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder == null || viewHolder instanceof CountryPickerHeaderHolder) return;

        if(viewHolder instanceof CountryPickerHolder){

            CountryPickerHolder holder=(CountryPickerHolder)viewHolder;
            Country country = countries.get(position-HEADER_COUNT); //As we added header in 0th position
            holder.countryName.setText(country.getName());
            if (position == this.selectedPosition) {
                holder.checked.setVisibility(View.VISIBLE);
            } else {
                holder.checked.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(view -> {
                setSelectedPosition(position);
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> mSelectedCountryListener.onCountrySelected(position-HEADER_COUNT), 500);
            });
        }

    }

    @Override
    public int getItemCount() {
        return countries.size()+HEADER_COUNT;
    }

    private void setSelectedPosition(int posistion) {
        selectedPosition = posistion;
        notifyDataSetChanged();
    }
}
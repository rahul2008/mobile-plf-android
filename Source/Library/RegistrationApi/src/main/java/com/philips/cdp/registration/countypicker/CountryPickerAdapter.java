package com.philips.cdp.registration.countypicker;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.listener.SelectedCountryListener;
import com.philips.cdp.registration.ui.customviews.countrypicker.Country;

import java.util.List;

public class CountryPickerAdapter extends RecyclerView.Adapter<CountryPickerAdapter.CountryPickerHolder>{

    private List<Country> countries;
    private SelectedCountryListener mSelectedCountryListener;

    public class CountryPickerHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView imageView;

        public CountryPickerHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.tick);
        }
    }

    public CountryPickerAdapter(Context context, List<Country> countries, SelectedCountryListener selectedCountryListener) {
        super();
        this.countries = countries;
        mSelectedCountryListener = selectedCountryListener;
    }

    @Override
    public CountryPickerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_pick_list, parent, false);
        return new CountryPickerHolder(itemView);
    }

    Handler handler = new Handler();
    @Override
    public void onBindViewHolder(CountryPickerHolder holder, int position) {
        Country country = countries.get(position);

        holder.title.setText(country.getName());
        if(position == 0){
            holder.imageView.setVisibility(View.VISIBLE);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.imageView.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.imageView.setVisibility(View.VISIBLE);
                        mSelectedCountryListener.onCountrySelected(position);
                        holder.imageView.setVisibility(View.VISIBLE);
                        return;
                    }
                },500);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }
}

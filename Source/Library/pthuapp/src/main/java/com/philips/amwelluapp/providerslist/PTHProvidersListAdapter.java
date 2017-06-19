package com.philips.amwelluapp.providerslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.amwelluapp.R;
import com.philips.platform.uid.view.widget.RatingBar;

import java.util.List;

public class PTHProvidersListAdapter extends RecyclerView.Adapter<PTHProvidersListAdapter.MyViewHolder> {
    private List<Provider> providerList;

    public PTHProvidersListAdapter(List<Provider> providerList){
        this.providerList = providerList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, practise, isAvailble;
        public RatingBar providerRating;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.practiceNameLabel);
            practise = (TextView) view.findViewById(R.id.providerNameLabel);
            isAvailble = (TextView) view.findViewById(R.id.isAvailableLabel);
            providerRating = (RatingBar) view.findViewById(R.id.providerRating);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pth_provider_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Provider provider = providerList.get(position);

        holder.providerRating.setRating(provider.getProviderRating());
        holder.name.setText(provider.getProviderName());
        holder.practise.setText(provider.getProviderPractise());

    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }
}

package com.philips.amwelluapp.intake;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.providerslist.PTHProvidersListAdapter;
import com.philips.amwelluapp.utility.PTHManager;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.RatingBar;

public class PTHSymptomsAdapter extends RecyclerView.Adapter<PTHSymptomsAdapter.SymptomsViewHolder>{


    public class SymptomsViewHolder extends RecyclerView.ViewHolder {
      public CheckBox checkBox;

        public SymptomsViewHolder(View view) {
            super(view);
           checkBox = (CheckBox)view.findViewById(R.id.checkbox_topics);

        }
    }

    @Override
    public PTHSymptomsAdapter.SymptomsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pth_topics_item, parent, false);

        return new PTHSymptomsAdapter.SymptomsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PTHSymptomsAdapter.SymptomsViewHolder holder, int position) {
        //ProviderInfo provider = providerList.get(position);

        holder.checkBox.setText("");
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

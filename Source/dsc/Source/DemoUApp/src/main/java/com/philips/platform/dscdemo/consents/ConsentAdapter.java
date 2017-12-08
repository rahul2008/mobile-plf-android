/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.consents;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.dscdemo.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
class ConsentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<? extends ConsentDetail> mConsentDetailList;
    private final ConsentPresenter mConsentPresenter;

    ConsentAdapter(ArrayList<? extends ConsentDetail> consentDetailList, ConsentPresenter consentPresenter) {
        this.mConsentDetailList = consentDetailList;
        this.mConsentPresenter = consentPresenter;
    }

    @Override
    public ConsentDetailViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_consent_item, parent, false);
        return new ConsentDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConsentDetailViewHolder) {
            ConsentDetailViewHolder mConsentViewHolder = (ConsentDetailViewHolder) holder;
            mConsentViewHolder.mConsentDetailSwitch.setText(mConsentDetailList.get(position).getType());

            boolean isAccepted = mConsentPresenter.getConsentDetailStatus(mConsentDetailList.get(position));
            mConsentViewHolder.mConsentDetailSwitch.setChecked(isAccepted);

            mConsentViewHolder.mConsentDetailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mConsentDetailList.get(holder.getAdapterPosition()).setStatus(ConsentDetailStatusType.ACCEPTED.name());
                    } else {
                        mConsentDetailList.get(holder.getAdapterPosition()).setStatus(ConsentDetailStatusType.REFUSED.name());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mConsentDetailList.size();
    }

    void updateConsent() {
        mConsentPresenter.updateConsent((List<ConsentDetail>) mConsentDetailList);
    }

    private class ConsentDetailViewHolder extends RecyclerView.ViewHolder {
        Switch mConsentDetailSwitch;

        ConsentDetailViewHolder(final View itemView) {
            super(itemView);
            mConsentDetailSwitch = (Switch) itemView.findViewById(R.id.switch_consent_detail_type);
        }
    }

    public void setData(ArrayList<? extends ConsentDetail> consentDetails) {
        this.mConsentDetailList = consentDetails;
    }
}

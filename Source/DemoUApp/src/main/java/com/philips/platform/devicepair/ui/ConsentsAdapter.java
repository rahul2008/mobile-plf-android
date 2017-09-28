/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.devicepair.R;

import java.util.ArrayList;
import java.util.List;

public class ConsentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<? extends ConsentDetail> mConsentDetailList;
    private final ConsentsPresenter mConsentDialogPresenter;

    public ConsentsAdapter(final Context context, ArrayList<? extends ConsentDetail> consentDetails, ConsentsPresenter consentDialogPresenter) {
        mContext = context;
        this.mConsentDetailList = consentDetails;
        this.mConsentDialogPresenter = consentDialogPresenter;
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

            boolean isAccepted = mConsentDialogPresenter.getConsentDetailStatus(mConsentDetailList.get(position));
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

    public void updateConsent() {
        mConsentDialogPresenter.updateConsent((List<ConsentDetail>) mConsentDetailList);
    }

    public class ConsentDetailViewHolder extends RecyclerView.ViewHolder {
        public Switch mConsentDetailSwitch;

        public ConsentDetailViewHolder(final View itemView) {
            super(itemView);
            mConsentDetailSwitch = (Switch) itemView.findViewById(R.id.switch_consent_detail_type);
        }
    }

    public void setData(ArrayList<? extends ConsentDetail> consentDetails) {
        this.mConsentDetailList = consentDetails;
    }

    public boolean isAllConsentsAccepted() {
        boolean isAccepted = false;

        for (ConsentDetail ormConsentDetail : mConsentDetailList) {
            if (ormConsentDetail.getStatus().toString().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
                isAccepted = true;
            } else {
                isAccepted = false;
                break;
            }
        }
        return isAccepted;
    }
}

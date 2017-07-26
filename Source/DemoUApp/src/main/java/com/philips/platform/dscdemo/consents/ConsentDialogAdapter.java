package com.philips.platform.dscdemo.consents;

import android.content.Context;
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




/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsentDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<? extends ConsentDetail> consentDetails;
   // private ConsentDetail mConsent;
    private final ConsentDialogPresenter consentDialogPresenter;

    public ConsentDialogAdapter(final Context context, ArrayList<? extends ConsentDetail> consentDetails, ConsentDialogPresenter consentDialogPresenter) {
        mContext = context;
        this.consentDetails = consentDetails;
        this.consentDialogPresenter = consentDialogPresenter;
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
            mConsentViewHolder.mConsentDetailSwitch.setText(consentDetails.get(position).getType());

            boolean isAccepted = consentDialogPresenter.getConsentDetailStatus(consentDetails.get(position));
            mConsentViewHolder.mConsentDetailSwitch.setChecked(isAccepted);

            mConsentViewHolder.mConsentDetailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        consentDetails.get(holder.getAdapterPosition()).setStatus(ConsentDetailStatusType.ACCEPTED.name());
                    } else {
                        consentDetails.get(holder.getAdapterPosition()).setStatus(ConsentDetailStatusType.REFUSED.name());
                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return consentDetails.size();
    }

    public void updateConsent() {
        consentDialogPresenter.updateConsent((List<ConsentDetail>) consentDetails);
    }


    public class ConsentDetailViewHolder extends RecyclerView.ViewHolder {
        public Switch mConsentDetailSwitch;

        public ConsentDetailViewHolder(final View itemView) {
            super(itemView);
            mConsentDetailSwitch = (Switch) itemView.findViewById(R.id.switch_consent_detail_type);
        }
    }


    public void setData(ArrayList<? extends ConsentDetail> consentDetails) {
        this.consentDetails = consentDetails; //new ArrayList(consent.getConsentDetails());
       // this.mConsent = consent;
    }

}

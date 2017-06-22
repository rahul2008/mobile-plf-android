package com.philips.platform.referenceapp.consents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.philips.platform.referenceapp.R;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsentDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<? extends ConsentDetail> mConsentDetailList;
    private final ConsentDialogPresenter mConsentDialogPresenter;

    public ConsentDialogAdapter(final Context context, ArrayList<? extends ConsentDetail> consentDetails, ConsentDialogPresenter consentDialogPresenter) {
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ConsentDetailViewHolder) {
            ConsentDetailViewHolder mConsentViewHolder = (ConsentDetailViewHolder) holder;
            mConsentViewHolder.mConsentDetailSwitch.setText(mConsentDetailList.get(position).getType());

            boolean isAccepted = mConsentDialogPresenter.getConsentDetailStatus(mConsentDetailList.get(position));
            mConsentViewHolder.mConsentDetailSwitch.setChecked(isAccepted);

            mConsentViewHolder.mConsentDetailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mConsentDetailList.get(position).setStatus(ConsentDetailStatusType.ACCEPTED.name());
                    } else {
                        mConsentDetailList.get(position).setStatus(ConsentDetailStatusType.REFUSED.name());
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
}

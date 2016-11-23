package cdp.philips.com.mydemoapp.consents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class ConsentDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<? extends ConsentDetail> consentDetails;
    private Consent mConsent;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ConsentDetailViewHolder) {
            ConsentDetailViewHolder mConsentViewHolder = (ConsentDetailViewHolder) holder;
            mConsentViewHolder.mConsentDetailSwitch.setText(consentDetails.get(position).getType().getDescription());

            boolean isAccepted = consentDialogPresenter.getConsentDetailStatus(consentDetails.get(position));
            mConsentViewHolder.mConsentDetailSwitch.setChecked(isAccepted);

            mConsentViewHolder.mConsentDetailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        consentDetails.get(position).setStatus(ConsentDetailStatusType.ACCEPTED.name());
                    } else {
                        consentDetails.get(position).setStatus(ConsentDetailStatusType.REFUSED.name());
                    }
                    consentDetails.get(position).setBackEndSynchronized(false);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return consentDetails.size();
    }

    public void updateConsentDetails() {
        DataServicesManager.getInstance().save(mConsent);
    }


    public class ConsentDetailViewHolder extends RecyclerView.ViewHolder {
        public Switch mConsentDetailSwitch;

        public ConsentDetailViewHolder(final View itemView) {
            super(itemView);
            mConsentDetailSwitch = (Switch) itemView.findViewById(R.id.switch_consent_detail_type);
        }
    }


    public void setData(OrmConsent consent) {
        this.consentDetails = new ArrayList(consent.getConsentDetails());
        this.mConsent = consent;
    }

}

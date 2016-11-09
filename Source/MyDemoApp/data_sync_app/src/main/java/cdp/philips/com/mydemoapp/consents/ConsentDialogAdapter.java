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
import com.philips.platform.core.datatypes.ConsentDetailType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.temperature.TemperaturePresenter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class ConsentDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    final ArrayList<? extends ConsentDetail> consentDetails;
    private final TemperaturePresenter mPresenter;
    private Consent mConsent;
    private LinkedHashMap<ConsentDetailType, ConsentDetailStatusType> mConsentMap;

    public ConsentDialogAdapter(final Context context, OrmConsent ormConsent, TemperaturePresenter mPresenter) {
        mContext = context;
        mConsent = ormConsent;
        this.consentDetails = new ArrayList(ormConsent.getConsentDetails());
        this.mPresenter = mPresenter;
        mConsentMap = new LinkedHashMap<>();
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

            boolean isAccepted = getConsentDetailStatus(consentDetails.get(position));
            mConsentViewHolder.mConsentDetailSwitch.setChecked(isAccepted);

            mConsentViewHolder.mConsentDetailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        consentDetails.get(position).setStatus(ConsentDetailStatusType.ACCEPTED.name());
                    } else {
                        consentDetails.get(position).setStatus(ConsentDetailStatusType.REFUSED.name());
                    }
                }
            });
        }

    }

    private boolean getConsentDetailStatus(ConsentDetail consentDetail) {

        if (consentDetail.getStatus().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
            return true;
        }
        return false;
    }



    @Override
    public int getItemCount() {
        return consentDetails.size();
    }

    public void updateConsentDetails() {
        mPresenter.updateConsentDetails(mConsent);
    }


    public class ConsentDetailViewHolder extends RecyclerView.ViewHolder {
        public Switch mConsentDetailSwitch;

        public ConsentDetailViewHolder(final View itemView) {
            super(itemView);
            mConsentDetailSwitch = (Switch) itemView.findViewById(R.id.switch_consent_detail_type);
        }
    }


}

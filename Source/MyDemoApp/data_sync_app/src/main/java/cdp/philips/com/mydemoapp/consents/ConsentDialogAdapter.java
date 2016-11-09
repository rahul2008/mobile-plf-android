package cdp.philips.com.mydemoapp.consents;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.ConsentDetailType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetailType;
import cdp.philips.com.mydemoapp.temperature.TemperaturePresenter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class ConsentDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ConsentDetailType[] detailTypes;
    Context mContext;
    final Collection<? extends ConsentDetail> consentDetails;
    private final TemperaturePresenter mPresenter;
    private LinkedHashMap<ConsentDetailType,ConsentDetailStatusType> mConsentMap;

    public ConsentDialogAdapter(final Context context,final List<OrmConsent> ormConsents, TemperaturePresenter mPresenter) {
        mContext = context;
        detailTypes = ConsentDetailType.values();
        this.consentDetails =  ormConsents.get(0).getConsentDetails();
        this.mPresenter = mPresenter;
        mConsentMap=new LinkedHashMap<>();
        for(ConsentDetailType consentDetailType:detailTypes){
            mConsentMap.put(consentDetailType,ConsentDetailStatusType.REFUSED);
        }
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
            final ConsentDetailType consentDetailType=detailTypes[position];
            mConsentViewHolder.mConsentDetailSwitch.setText(consentDetailType.getDescription());

            for (ConsentDetail consentDetail : consentDetails) {

                if (consentDetailType.getId()==(consentDetail.getType().getId())) {
                    boolean isAccepted = getConsentDetailStatus(consentDetail);
                    mConsentViewHolder.mConsentDetailSwitch.setChecked(isAccepted);

                    if(isAccepted){
                        mConsentMap.put(consentDetailType,ConsentDetailStatusType.ACCEPTED);
                    }
                }
            }

            mConsentViewHolder.mConsentDetailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        mConsentMap.put(consentDetailType,ConsentDetailStatusType.ACCEPTED);
                    }else{
                        mConsentMap.put(consentDetailType,ConsentDetailStatusType.REFUSED);
                    }

                    Log.d("Size of MAP==",""+mConsentMap.size());

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
        return detailTypes.length;
    }

    public void updateConsents() {
      //  mPresenter.updateConsent(null,conmConsentMap);
    }


    public class ConsentDetailViewHolder extends RecyclerView.ViewHolder {

        public Switch mConsentDetailSwitch;

        public ConsentDetailViewHolder(final View itemView) {
            super(itemView);
            mConsentDetailSwitch = (Switch) itemView.findViewById(R.id.switch_consent_detail_type);
        }
    }


}

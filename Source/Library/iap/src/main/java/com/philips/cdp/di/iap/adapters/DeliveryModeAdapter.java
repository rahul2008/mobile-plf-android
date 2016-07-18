package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;

import java.util.List;


public class DeliveryModeAdapter extends ArrayAdapter<DeliveryModes> {

    private Context mContext;
    private List<DeliveryModes> mModes;

    public DeliveryModeAdapter(final Context context, int txtViewResourceId, final List<DeliveryModes> modes) {
        super(context, txtViewResourceId, modes);
        mContext = context;
        mModes = modes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.iap_delivery_mode_spinner_item, null);

        TextView mDeliveryModeName = (TextView) v.findViewById(R.id.delivery_mode_name);
        TextView mDeliveryModeDate = (TextView) v.findViewById(R.id.delivery_date);
        TextView mDelivertModePrice = (TextView) v.findViewById(R.id.delivery_mode_price);

        DeliveryModes modes = mModes.get(position);

        if(modes.getName() != null && !modes.getName().equals(""))
            mDeliveryModeName.setText(modes.getName());
        mDeliveryModeDate.setText(modes.getDescription());
        mDelivertModePrice.setText(modes.getDeliveryCost().getFormattedValue());

        return v;
    }
}

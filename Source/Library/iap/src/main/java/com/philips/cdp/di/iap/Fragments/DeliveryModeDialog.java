package com.philips.cdp.di.iap.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.DeliveryModeAdapter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.AddressController.AddressListener;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.List;

public class DeliveryModeDialog
{
    private Context mContext;
    private DialogListener mListener;
    private AddressListener mAddressListener;
    private List<DeliveryModes> mDeliveryModes;
    private ListView lv;
    private DeliveryModeAdapter adapter;

    public interface DialogListener {
        void onItemClick(int position);
    }

    public DeliveryModeDialog(final Context context, DialogListener listener, AddressListener addressListener) {
        mContext = context;
        mListener = listener;
        mAddressListener = addressListener;
    }

    public void showDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        View convertView = (LayoutInflater.from(mContext).inflate(R.layout.iap_delivery_dialog, null));
        alertDialog.setView(convertView);
        lv = (ListView) convertView.findViewById(R.id.lv);
        mDeliveryModes = CartModelContainer.getInstance().getDeliveryModes();
        adapter = new DeliveryModeAdapter(mContext,R.layout.iap_delivery_mode_spinner_item, mDeliveryModes);
        lv.setClickable(true);
        lv.setAdapter(adapter);

        final Dialog dialog = alertDialog.create();
        dialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (!Utility.isProgressDialogShowing())
                    Utility.showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
                AddressController addressController = new AddressController(mContext, mAddressListener);
                addressController.setDeliveryMode(mDeliveryModes.get(position).getCode());
                mListener.onItemClick(position);
            }
        });
    }



}

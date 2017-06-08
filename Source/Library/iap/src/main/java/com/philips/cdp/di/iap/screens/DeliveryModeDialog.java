/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.DeliveryModeAdapter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.List;

public class DeliveryModeDialog {
    private Context mContext;
    private DialogListener mListener;

    public interface DialogListener {
        void onItemClick(int position);
    }

    public DeliveryModeDialog(final Context context, DialogListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void showDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        View convertView = (LayoutInflater.from(mContext).inflate(R.layout.iap_delivery_dialog, null));
        alertDialog.setView(convertView);
        ListView deliveryList = (ListView) convertView.findViewById(R.id.lv);
        List<DeliveryModes> mDeliveryModes = CartModelContainer.getInstance().getDeliveryModes();
        if (mDeliveryModes == null) return;
        DeliveryModeAdapter mDeliveryModeAdapter = new DeliveryModeAdapter(mContext, R.layout.iap_delivery_mode_spinner_item, mDeliveryModes);
        deliveryList.setClickable(true);
        deliveryList.setAdapter(mDeliveryModeAdapter);

        final Dialog dialog = alertDialog.create();
        dialog.show();

        deliveryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                mListener.onItemClick(position);
            }
        });
    }


    /*private void showDialog(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon) {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mContext)
                .setMessage(showLargeContent ? R.string.dialog_screen_long_content_text : R.string.dialog_screen_short_content_text).
                        setPositiveButton(R.string.dialog_screen_positive_button_text, this).
                        setNegativeButton(R.string.dialog_screen_negative_button_text, this);
        if (isWithTitle) {
            builder.setTitle(R.string.dialog_screen_title_text);
            if (showIcon) {
                builder.setIcon(R.drawable.ic_location);
            }
        }
        alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);
    }
*/
}

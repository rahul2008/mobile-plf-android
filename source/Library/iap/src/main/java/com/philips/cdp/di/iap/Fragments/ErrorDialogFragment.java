package com.philips.cdp.di.iap.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorDialogFragment extends BlurDialogFragment implements EventListener {

    private Button mOkBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iap_error_dialog, container, false);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_ON_ERROR), this);
        Bundle bundle = getArguments();
        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_TEXT));

        TextView errorDescription = (TextView) v.findViewById(R.id.dialogDescription);
        errorDescription.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION));

        mOkBtn = (Button) v.findViewById(R.id.btn_dialog_ok);
        mOkBtn.setText(bundle.getString(IAPConstant.MODEL_ALERT_BUTTON_TEXT));

        mOkBtn.setOnClickListener(dismissDialog());
        return v;
    }

    @Override
    public void setShowsDialog(final boolean showsDialog) {
        super.setShowsDialog(showsDialog);
    }

    private View.OnClickListener dismissDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_ON_ERROR);
                setShowsDialog(false);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_ON_ERROR), this);
    }

    @Override
    public void raiseEvent(final String event) {

    }

    @Override
    public void onEventReceived(final String event) {
        android.support.v4.app.Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if(fragment!=null) {
            if (event.equalsIgnoreCase(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_ON_ERROR))) {
                replaceFragment(fragment, ProductCatalogFragment.TAG);
            }
        }else{
            addProductCatalog();
        }
    }

    public void replaceFragment(android.support.v4.app.Fragment newFragment, String newFragmentTag) {
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.replace(R.id.fl_mainFragmentContainer, newFragment, newFragmentTag);
        transaction.commitAllowingStateLoss();
    }

    private void addProductCatalog() {
        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, new ProductCatalogFragment(), ProductCatalogFragment.TAG);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
}

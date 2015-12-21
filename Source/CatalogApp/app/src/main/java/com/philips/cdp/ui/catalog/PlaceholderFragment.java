package com.philips.cdp.ui.catalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class PlaceholderFragment extends Fragment {

    private ModalAlertDemoFragment modalAlertDemoFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.modal_alert_demo, container, false);
        Button showAlert = (Button) rootView.findViewById(R.id.show_modal_dialog);
        showAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalAlertDemoFragment = new ModalAlertDemoFragment();
                modalAlertDemoFragment.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
        return rootView;
    }

    public boolean isShowing() {
        if (modalAlertDemoFragment != null)
            return modalAlertDemoFragment.isVisible();
        else
            return false;
    }

    public void show() {
        modalAlertDemoFragment = new ModalAlertDemoFragment();
        modalAlertDemoFragment.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    public void dismiss() {
        if (modalAlertDemoFragment != null)
            modalAlertDemoFragment.dismiss();
    }
}
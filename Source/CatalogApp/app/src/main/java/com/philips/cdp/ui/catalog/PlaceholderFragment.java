package com.philips.cdp.ui.catalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class PlaceholderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.modal_alert_demo, container, false);
        Button showAlert = (Button) rootView.findViewById(R.id.show_modal_dialog);
        ViewGroup group = (ViewGroup) rootView.findViewById(R.id.splash_layout);
        group.setBackgroundResource(R.drawable.uikit_food);
        showAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModalAlertDemoFragment fragment = new ModalAlertDemoFragment();
                fragment.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
        return rootView;
    }

}
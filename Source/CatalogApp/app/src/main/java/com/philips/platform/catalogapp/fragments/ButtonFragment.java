/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentButtonsAllBinding;

public class ButtonFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentButtonsAllBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_buttons_all, container, false);
        binding.setFrag(this);
        return binding.getRoot();
    }

    public Drawable getShareIcon() {
        return VectorDrawableCompat.create(getResources(), R.drawable.share, getContext().getTheme());
    }

    private void setDisableSwitch(View view) {
        Switch switchForDisable = (Switch) view.findViewById(R.id.disable_switch);
        switchForDisable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewGroup viewById = (ViewGroup) getActivity().findViewById(R.id.buttons_parent);
                disableAllChildViews(isChecked, viewById);
            }

            private void disableAllChildViews(boolean isChecked, View view) {
                if (view instanceof ViewGroup) {
                    for (int i = 1; i < ((ViewGroup) view).getChildCount(); i++) {
                        disableAllChildViews(isChecked, ((ViewGroup) view).getChildAt(i));
                    }
                } else {
                    view.setEnabled(isChecked);
                }
            }
        });
    }

    public void toggleIcons(boolean isIconToggleChecked) {
        Toast.makeText(getContext(), "" + isIconToggleChecked, Toast.LENGTH_SHORT).show();
    }

    public void toggleExtraWideButtons(boolean toggle) {

    }

    public void disableAllButtons() {

    }
}
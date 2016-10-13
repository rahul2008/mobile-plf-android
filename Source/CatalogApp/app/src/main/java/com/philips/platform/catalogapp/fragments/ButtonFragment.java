/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uit.view.widget.Button;
import com.philips.platform.uit.view.widget.ImageButton;

public class ButtonFragment extends BaseFragment {
    ImageButton imageButton;
    Button imageTextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);
        imageButton = (ImageButton) view.findViewById(R.id.demo_image_button);
        imageTextButton = (Button) view.findViewById(R.id.demo_image_text_button);
        imageButton.setImageDrawable(getShareIcon());
        imageTextButton.setImageDrawable(getShareIcon());
        setDisableSwitch(view);
        return view;
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

    @Override
    public int getTitle() {
        return R.string.tittle_buttons;
    }
}
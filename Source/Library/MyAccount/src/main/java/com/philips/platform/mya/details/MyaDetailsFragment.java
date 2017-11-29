/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.mya.details;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.philips.platform.mya.R;
import com.philips.platform.mya.base.mvp.MyaBaseFragment;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyaDetailsFragment extends MyaBaseFragment implements MyaDetailContract.View {

    private ImageButton email_mobile_arrow, email_arrow, mobile_arrow;
    private Label email_address, mobile_number;
    private Label nameLabel, genderLabel, mobile_number_heading, name_value, dob_value, DOB_heading, email_address_heading;
    private View email_divider, name_divider, gender_divider, dob_divider;
    private String MYA_SETTINGS_BUNDLE = "settings_bundle";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_user_detail_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();
        initViews(view);
        setRetainInstance(true);
        MyaDetailPresenter myaDetailPresenter = new MyaDetailPresenter(this);
        if (savedInstanceState == null) {
            myaDetailPresenter.setUserDetails(getArguments());
        } else {
            myaDetailPresenter.setUserDetails(savedInstanceState.getBundle(MYA_SETTINGS_BUNDLE));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(MYA_SETTINGS_BUNDLE, getArguments());
    }

    private void initViews(View view) {
        nameLabel = view.findViewById(R.id.mya_name);
        email_mobile_arrow = view.findViewById(R.id.email_mobile_right_arrow);
        email_arrow = view.findViewById(R.id.email_right_arrow);
        mobile_arrow = view.findViewById(R.id.mobile_right_arrow);
        email_address = view.findViewById(R.id.email_address_value);
        mobile_number = view.findViewById(R.id.mobile_number_value);
        genderLabel = view.findViewById(R.id.gender_value);
        mobile_number_heading = view.findViewById(R.id.mobile_number_heading);
        email_address_heading = view.findViewById(R.id.email_address_heading);
        name_value = view.findViewById(R.id.name_value);
        dob_value = view.findViewById(R.id.dob_value);
        DOB_heading = view.findViewById(R.id.DOB_heading);
        email_divider = view.findViewById(R.id.email_divider);
        name_divider = view.findViewById(R.id.name_divider);
        gender_divider = view.findViewById(R.id.gender_divider);
        dob_divider = view.findViewById(R.id.dob_divider);
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.MYA_My_account);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public void setUserName(String name) {
        if (!TextUtils.isEmpty(name)) {
            name_value.setText(name);
            name_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setCircleText(String data) {
        nameLabel.setText(data);
    }

    @Override
    public void setEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            email_arrow.setVisibility(View.VISIBLE);
            email_address_heading.setVisibility(View.GONE);
            email_address.setText(getString(R.string.MYA_Add_email_address));
        } else {
            email_address.setText(email);
        }
    }

    @Override
    public void setGender(String gender) {
        if (!TextUtils.isEmpty(gender) && !gender.equalsIgnoreCase("null")) {
            genderLabel.setText(gender);
            gender_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
        if (dateOfBirth != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String tempDate = formatter.format(dateOfBirth);
            dob_value.setText(tempDate);
            dob_divider.setVisibility(View.VISIBLE);
        } else {
            dob_value.setVisibility(View.GONE);
            DOB_heading.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMobileNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            mobile_number_heading.setVisibility(View.GONE);
            mobile_arrow.setVisibility(View.GONE);
            mobile_number.setText(getString(R.string.MYA_Add_mobile_number));
            mobile_number.setVisibility(View.GONE);
        } else {
            mobile_number.setText(number);
            email_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void handleArrowVisibility(String email, String mobileNumber) {
        if (email != null && mobileNumber != null)
            email_mobile_arrow.setVisibility(View.VISIBLE);

    }
}

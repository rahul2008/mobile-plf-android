/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.mya.details;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBaseFragment;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.philips.platform.mya.launcher.MyaInterface.USER_PLUGIN;


public class MyaDetailsFragment extends MyaBaseFragment implements MyaDetailContract.View {

    private ImageButton email_mobile_arrow, email_arrow, mobile_arrow;
    private Label email_address, mobile_number;
    private Label nameLabel, genderLabel, mobile_number_heading, name_value, dob_value, email_address_heading;
    private View email_divider;
    private final String DETAILS_BUNDLE = "details_bundle";
    private MyaDetailPresenter myaDetailPresenter;
    private View emailAddressLayout,loginDetailsLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_user_detail_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();
        initViews(view);
        setRetainInstance(true);
        myaDetailPresenter = new MyaDetailPresenter(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(DETAILS_BUNDLE, getArguments());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle;
        if (savedInstanceState == null) {
            bundle = getArguments();
        } else {
            bundle = savedInstanceState.getBundle(DETAILS_BUNDLE);
        }
        if (bundle != null)
            myaDetailPresenter.setUserDetails((UserDataModelProvider) bundle.getSerializable(USER_PLUGIN));
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
        loginDetailsLabel = view.findViewById(R.id.login_details_label);
        emailAddressLayout = view.findViewById(R.id.email_address_layout);
        name_value = view.findViewById(R.id.name_value);
        dob_value = view.findViewById(R.id.dob_value);
        email_divider = view.findViewById(R.id.email_divider);
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle(Context context) {
        return context.getString(R.string.MYA_My_details);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public void setUserName(String name) {
        if (!TextUtils.isEmpty(name)) {
            name_value.setText(name);
        }
    }

    @Override
    public void setCircleText(String data) {
        nameLabel.setText(data);
    }

    @Override
    public void setEmail(String email) {
        if (TextUtils.isEmpty(email) || email.equalsIgnoreCase("null")) {
//            email_arrow.setVisibility(View.VISIBLE);
            email_address_heading.setVisibility(View.GONE);
//            email_address.setText(getString(R.string.MYA_Add_email_address));
            emailAddressLayout.setVisibility(View.GONE);
            loginDetailsLabel.setVisibility(View.GONE);
        } else {
            email_address.setText(email);
        }
    }

    @Override
    public void setGender(String gender) {
        if (!TextUtils.isEmpty(gender) && !gender.equalsIgnoreCase("null")) {
            genderLabel.setText(gender);
        } else {
            genderLabel.setText(getString(R.string.MYA_Not_Available));
        }
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
        if (dateOfBirth != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String tempDate = formatter.format(dateOfBirth);
            dob_value.setText(tempDate);
        } else
            dob_value.setText(getString(R.string.MYA_Not_Available));
    }

    @Override
    public void setMobileNumber(String number) {
        if (TextUtils.isEmpty(number) || number.equalsIgnoreCase("null")) {
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

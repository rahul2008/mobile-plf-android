/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.mya.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.philips.platform.mya.R;
import com.philips.platform.mya.base.mvp.MyaBaseFragment;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.uappadaptor.UserDataModel;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyaDetailsFragment extends MyaBaseFragment {

    private ImageButton email_mobile_arrow, email_arrow, mobile_arrow;
    private Label email_address, mobile_number;
    private Label name, gender, mobile_number_heading, name_value, dob_value, DOB_heading, email_address_heading;
    private UserDataModel userDataModel;
    private View email_divider, name_divider, gender_divider, dob_divider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_user_detail_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();
        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            UserDataModelProvider userDataModelProvider = (UserDataModelProvider) bundle.getSerializable("user_plugin");
            if (userDataModelProvider != null) {
                userDataModel = (UserDataModel) userDataModelProvider.getData(DataModelType.USER);
            }
        }

        return view;
    }

    private void initViews(View view) {
        name = view.findViewById(R.id.mya_name);
        email_mobile_arrow = view.findViewById(R.id.email_mobile_right_arrow);
        email_arrow = view.findViewById(R.id.email_right_arrow);
        mobile_arrow = view.findViewById(R.id.mobile_right_arrow);
        email_address = view.findViewById(R.id.email_address_value);
        mobile_number = view.findViewById(R.id.mobile_number_value);
        gender = view.findViewById(R.id.gender_value);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserDetails();
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

    String printFirstCharacter(String nameString) {
        StringBuilder finalName = new StringBuilder();
        Pattern pattern = Pattern.compile("\\b[a-zA-z[$&+,:;=?@#|'<>.-^*()%!]0-9]");
        Matcher matcher = pattern.matcher(nameString);
        while (matcher.find()) {
            String matchString = matcher.group();
            finalName.append(matchString.toString());

        }
        if (finalName.toString().length() == 1) {
            return nameString.toString().length() == 1 ? nameString : nameString.substring(0, 2).toUpperCase();
        } else if (finalName.toString().length() > 2) {
            return finalName.substring(0, 2).toUpperCase();
        }
        return finalName.toString().toUpperCase();
    }

    private void setUserDetails() {

        if (userDataModel.getGivenName() != null) {
            name.setText(printFirstCharacter(userDataModel.getGivenName()));
        }

        if (userDataModel.getEmail() == null) {
            email_arrow.setVisibility(View.VISIBLE);
            email_address_heading.setVisibility(View.GONE);
            email_address.setText(getString(R.string.MYA_Add_email_address));
        } else {
            email_address.setText(userDataModel.getEmail());
        }

        if (userDataModel.getMobileNumber() == null) {
            mobile_number_heading.setVisibility(View.GONE);
            mobile_arrow.setVisibility(View.GONE);
            mobile_number.setText(getString(R.string.MYA_Add_mobile_number));
            mobile_number.setVisibility(View.GONE);

        } else {
            mobile_number.setText(userDataModel.getMobileNumber());
            email_divider.setVisibility(View.VISIBLE);
        }


        if (userDataModel.getEmail() != null && userDataModel.getMobileNumber() != null) {
            email_mobile_arrow.setVisibility(View.VISIBLE);
        }

        if (userDataModel.getGender() != null) {
            gender.setText(userDataModel.getGender());
            gender_divider.setVisibility(View.VISIBLE);
        }
        if (userDataModel.getGivenName() != null) {
            name_value.setText(userDataModel.getGivenName());
            name_divider.setVisibility(View.VISIBLE);
        }


        if (userDataModel.getBirthday() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String tempDate = formatter.format(userDataModel.getBirthday());
            dob_value.setText(tempDate);
            dob_divider.setVisibility(View.VISIBLE);
        } else {
            dob_value.setVisibility(View.GONE);
            DOB_heading.setVisibility(View.GONE);

        }
    }
}

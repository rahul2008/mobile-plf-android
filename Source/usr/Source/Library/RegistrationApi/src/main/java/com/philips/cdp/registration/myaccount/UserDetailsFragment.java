/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.cdp.registration.myaccount;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UserDetailsFragment extends RegistrationBaseFragment implements MyaDetailContract.View {

    private Label nameLabel;
    private Label email_address_value;
    private Label email_address_header;
    private Label mobile_number_value;
    private Label mobile_number_header;
    private Label gender_value;
    private Label gender_header;
    private Label name_value;
    private Label name_header;
    private Label dob_value;
    private Label dob_header;
    private View dobDivider;
    private Label addressHeader;
    private Label addressValue;

    private final String DETAILS_BUNDLE = "details_bundle";
    private UserDetailPresenter myaDetailPresenter;
    private User user;

    @Override
    protected void setViewParams(Configuration config, int width) {
        // Do not do anything
    }

    @Override
    protected void handleOrientation(View view) {
        // Do not do anything
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_MyDetails_TitleTxt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_user_detail_fragment, container, false);
        initViews(view);
        setRetainInstance(true);
        myaDetailPresenter = new UserDetailPresenter(this);
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

        user = getUser();
        UserDataModelProvider userDataModelProvider = new UserDataModelProvider(user);
        userDataModelProvider.fillUserData();
        myaDetailPresenter.setUserDetails(userDataModelProvider);
    }

    @NonNull
    private User getUser() {
        return new User(getActivity());
    }

    @VisibleForTesting
    public void setUser(User user) {
        this.user = user;
    }

    private void initViews(View view) {
        nameLabel = view.findViewById(R.id.usr_myDetailsScreen_label_name);

        email_address_value = view.findViewById(R.id.usr_myDetailsScreen_label_emailAddressValue);
        email_address_header = view.findViewById(R.id.usr_myDetailsScreen_label_emailAddressHeading);

        mobile_number_value = view.findViewById(R.id.usr_myDetailsScreen_label_mobileNumberValue);
        mobile_number_header = view.findViewById(R.id.usr_myDetailsScreen_label_mobileNumberHeading);

        gender_value = view.findViewById(R.id.usr_myDetailsScreen_label_genderValue);
        gender_header = view.findViewById(R.id.usr_myDetailsScreen_label_genderHeading);

        name_value = view.findViewById(R.id.usr_myDetailsScreen_label_nameValue);
        name_header = view.findViewById(R.id.usr_myDetailsScreen_label_nameHeading);

        dob_value = view.findViewById(R.id.usr_myDetailsScreen_label_dobValue);
        dob_header = view.findViewById(R.id.usr_myDetailsScreen_label_dobHeading);

        dobDivider=view.findViewById(R.id.usr_myDetailsScreen_view_dobDivider);
        addressHeader=view.findViewById(R.id.usr_myDetailsScreen_label_addressHeading);
        addressValue=view.findViewById(R.id.usr_myDetailsScreen_label_AddressValue);
    }

    @Override
    public void setUserName(String name) {
        if (TextUtils.isEmpty(name) || name.equalsIgnoreCase("null")) {

        }else {
            name_value.setText(name);
            name_value.setVisibility(View.VISIBLE);
            name_header.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setCircleText(String data) {
        nameLabel.setText(data);
    }

    @Override
    public void setEmail(String email) {
        if (TextUtils.isEmpty(email) || email.equalsIgnoreCase("null")) {

        } else {
            email_address_value.setText(email);
            email_address_value.setVisibility(View.VISIBLE);
            email_address_header.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setGender(String gender) {
        if (!TextUtils.isEmpty(gender) && !gender.equalsIgnoreCase("null")) {
            gender_value.setText(gender);
            gender_value.setVisibility(View.VISIBLE);
            gender_header.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
        if (dateOfBirth != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String tempDate = formatter.format(dateOfBirth);
            dob_value.setText(tempDate);
            dob_value.setVisibility(View.VISIBLE);
            dob_header.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setMobileNumber(String number) {
        if (TextUtils.isEmpty(number) || number.equalsIgnoreCase("null")) {

        } else {
            mobile_number_value.setText(number);
            mobile_number_value.setVisibility(View.VISIBLE);
            mobile_number_header.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setAddress(String address) {

        if (TextUtils.isEmpty(address) || address.equalsIgnoreCase("null")) {

        } else {
            addressValue.setText(address);
            dobDivider.setVisibility(View.VISIBLE);
            addressHeader.setVisibility(View.VISIBLE);
            addressValue.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean exitMyAccounts() {
        return false;
    }

}

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
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UserDetailsFragment extends RegistrationBaseFragment implements MyaDetailContract.View {

    private Label name_label;
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
    private View dob_divider;
    private Label address_header;
    private Label address_value;

    private final String DETAILS_BUNDLE = "details_bundle";
    private UserDetailPresenter myaDetailPresenter;
    private User user;

    private String TAG = UserDetailsFragment.class.getSimpleName();
    private Context mContext;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_user_detail_fragment, container, false);
        initViews(view);
        setRetainInstance(true);
        myaDetailPresenter = new UserDetailPresenter(this);
        RLog.d(TAG, "onCreateView : is called");
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(DETAILS_BUNDLE, getArguments());
        RLog.d(TAG, "onSaveInstanceState : is called");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(TAG, "onActivityCreated : is called");
        user = getUser();
        UserDataModelProvider userDataModelProvider = new UserDataModelProvider(user);
        userDataModelProvider.fillUserData();
        myaDetailPresenter.setUserDetails(userDataModelProvider);
    }

    @NonNull
    private User getUser() {
        RLog.d(TAG, "getUser : is called");
        return new User(mContext);
    }

    @VisibleForTesting
    public void setUser(User user) {
        RLog.d(TAG, "setUser : is called");
        this.user = user;
    }

    private void initViews(View view) {
        name_label = view.findViewById(R.id.usr_myDetailsScreen_label_name);

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

        dob_divider = view.findViewById(R.id.usr_myDetailsScreen_view_dobDivider);
        address_header = view.findViewById(R.id.usr_myDetailsScreen_label_addressHeading);
        address_value = view.findViewById(R.id.usr_myDetailsScreen_label_AddressValue);
    }

    @Override
    public void setUserName(String name) {
        if (TextUtils.isEmpty(name) || name.equalsIgnoreCase("null")) {
            RLog.d(TAG, "setUserName : name is null");
        } else {
            name_value.setText(name);
            myaDetailPresenter.makeVisible(name_value, name_header);
        }
    }

    @Override
    public void setCircleText(String data) {
        name_label.setText(data);
    }

    @Override
    public void setEmail(String email) {
        if (TextUtils.isEmpty(email) || email.equalsIgnoreCase("null")) {
            RLog.d(TAG, "setEmail : email is null");
        } else {
            email_address_value.setText(email);
            myaDetailPresenter.makeVisible(email_address_value, email_address_header);
        }
    }

    @Override
    public void setGender(String gender) {
        if (!TextUtils.isEmpty(gender) && !gender.equalsIgnoreCase("null")) {
            gender_value.setText(gender);
            myaDetailPresenter.makeVisible(gender_value, gender_header);
        } else {
            RLog.d(TAG, "setGender : gender is null");
        }
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
        if (dateOfBirth != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String tempDate = formatter.format(dateOfBirth);
            dob_value.setText(tempDate);
            myaDetailPresenter.makeVisible(dob_value, dob_header);
        } else {
            RLog.d(TAG, "setDateOfBirth : Date is null");
        }
    }

    @Override
    public void setMobileNumber(String number) {
        if (TextUtils.isEmpty(number) || number.equalsIgnoreCase("null")) {
            RLog.d(TAG, "setMobileNumber : number is null");
        } else {
            mobile_number_value.setText(number);
            myaDetailPresenter.makeVisible(mobile_number_value, mobile_number_header);
        }
    }

    @Override
    public void setAddress(String address) {

        if (TextUtils.isEmpty(address) || address.equalsIgnoreCase("null")) {
            RLog.d(TAG, "setAddress : address is null");
        } else {
            address_value.setText(address);
            myaDetailPresenter.makeVisible(dob_divider, address_header, address_value);
        }
    }

}

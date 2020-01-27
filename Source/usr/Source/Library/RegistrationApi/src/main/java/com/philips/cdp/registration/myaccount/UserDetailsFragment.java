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
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UserDetailsFragment extends RegistrationBaseFragment implements MyaDetailContract.View {

    private Label nameLabel;
    private Label emailAddressValue;
    private Label emailAddressHeader;
    private Label mobileNumberValue;
    private Label mobileNumberHeader;
    private Label genderValue;
    private Label genderHeader;
    private Label nameValue;
    private Label nameHeader;
    private Label dobValue;
    private Label dobHeader;
    private View dobDivider;
    private Label addressHeader;
    private Label addressValue;

    private final String DETAILS_BUNDLE = "details_bundle";
    private UserDetailPresenter myaDetailPresenter;
    private User user;

    private String TAG = "UserDetailsFragment";
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
        return R.string.USR_MyDetails_TitleTxt;
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
        nameLabel = view.findViewById(R.id.usr_myDetailsScreen_label_name);

        emailAddressValue = view.findViewById(R.id.usr_myDetailsScreen_label_emailAddressValue);
        emailAddressHeader = view.findViewById(R.id.usr_myDetailsScreen_label_emailAddressHeading);

        mobileNumberValue = view.findViewById(R.id.usr_myDetailsScreen_label_mobileNumberValue);
        mobileNumberHeader = view.findViewById(R.id.usr_myDetailsScreen_label_mobileNumberHeading);

        genderValue = view.findViewById(R.id.usr_myDetailsScreen_label_genderValue);
        genderHeader = view.findViewById(R.id.usr_myDetailsScreen_label_genderHeading);

        nameValue = view.findViewById(R.id.usr_myDetailsScreen_label_nameValue);
        nameHeader = view.findViewById(R.id.usr_myDetailsScreen_label_nameHeading);

        dobValue = view.findViewById(R.id.usr_myDetailsScreen_label_dobValue);
        dobHeader = view.findViewById(R.id.usr_myDetailsScreen_label_dobHeading);

        dobDivider = view.findViewById(R.id.usr_myDetailsScreen_view_dobDivider);
        addressHeader = view.findViewById(R.id.usr_myDetailsScreen_label_addressHeading);
        addressValue = view.findViewById(R.id.usr_myDetailsScreen_label_AddressValue);
    }

    @Override
    public void onResume() {
        super.onResume();
        trackPage(AppTaggingPages.USER_PROFILE);
    }

    @Override
    public void setUserName(String name) {
        if (TextUtils.isEmpty(name) || name.equalsIgnoreCase("null")) {
            RLog.d(TAG, "setUserName : name is null");
        } else {
            nameValue.setText(name);
            myaDetailPresenter.makeVisible(nameValue, nameHeader);
        }
    }

    @Override
    public void setCircleText(String data) {
        nameLabel.setText(data);
    }

    @Override
    public void setEmail(String email) {
        if (TextUtils.isEmpty(email) || email.equalsIgnoreCase("null")) {
            RLog.d(TAG, "setEmail : email is null");
        } else {
            emailAddressValue.setText(email);
            myaDetailPresenter.makeVisible(emailAddressValue, emailAddressHeader);
        }
    }

    @Override
    public void setGender(String gender) {
        if (!TextUtils.isEmpty(gender) && !gender.equalsIgnoreCase("null")) {
            genderValue.setText(gender);
            myaDetailPresenter.makeVisible(genderValue, genderHeader);
        } else {
            RLog.d(TAG, "setGender : gender is null");
        }
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
        if (dateOfBirth != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String tempDate = formatter.format(dateOfBirth);
            dobValue.setText(tempDate);
            myaDetailPresenter.makeVisible(dobValue, dobHeader);
        } else {
            RLog.d(TAG, "setDateOfBirth : Date is null");
        }
    }

    @Override
    public void setMobileNumber(String number) {
        if (TextUtils.isEmpty(number) || number.equalsIgnoreCase("null")) {
            RLog.d(TAG, "setMobileNumber : number is null");
        } else {
            mobileNumberValue.setText(number);
            myaDetailPresenter.makeVisible(mobileNumberValue, mobileNumberHeader);
        }
    }

    @Override
    public void setAddress(String address) {

        if (TextUtils.isEmpty(address) || address.equalsIgnoreCase("null")) {
            RLog.d(TAG, "setAddress : address is null");
        } else {
            addressValue.setText(address);
            myaDetailPresenter.makeVisible(dobDivider, addressHeader, addressValue);
        }
    }

    @Override
    public void notificationInlineMsg(String msg) {
        //NOP
    }
}

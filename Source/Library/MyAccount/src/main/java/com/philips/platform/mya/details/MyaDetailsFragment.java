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
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.uappadaptor.UserDataModel;
import com.philips.platform.myaplugin.user.UserDataModelProvider;


public class MyaDetailsFragment extends MyaBaseFragment {

    ImageButton email_mobile_arrow,email_arrow,mobile_arrow;
    Label email_address,mobile_number;
    Label name,gender,mobile_number_heading,name_value,dob_value,email_address_heading;
    UserDataModel userDataModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_user_detail_fragment, container, false);
        UIDHelper.injectCalligraphyFonts();

        name=(Label) view.findViewById(R.id.mya_name);

        email_mobile_arrow=(ImageButton)view.findViewById(R.id.email_mobile_right_arrow);
        email_arrow=(ImageButton)view.findViewById(R.id.email_right_arrow);
        mobile_arrow=(ImageButton)view.findViewById(R.id.mobile_right_arrow);
        email_address= (Label) view.findViewById(R.id.email_address_value);
        mobile_number= (Label) view.findViewById(R.id.mobile_number_value);
        gender= (Label) view.findViewById(R.id.gender_value);
        mobile_number_heading= (Label) view.findViewById(R.id.mobile_number_heading);
        email_address_heading= (Label) view.findViewById(R.id.email_address_heading);
        name_value= (Label) view.findViewById(R.id.name_value);
        dob_value=(Label) view.findViewById(R.id.dob_value);

        Bundle bundle = getArguments();
        if (bundle != null) {
            UserDataModelProvider userDataModelProvider = (UserDataModelProvider) bundle.getSerializable("user_plugin");
            if (userDataModelProvider != null) {
                 userDataModel = (UserDataModel) userDataModelProvider.getData(DataModelType.USER);

            }
        }

        return view;
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

    String printFirstCharacter(String nameString)
    {
        StringBuilder finalName=new StringBuilder();
        Pattern pattern=Pattern.compile("\\b[a-zA-z]");
        Matcher matcher=pattern.matcher(nameString);
        while (matcher.find())
        {
            String temp=matcher.group();
            finalName.append(temp.toString());

        }
        if(finalName.toString().length()==1)
        {
            return nameString.toString().length()==1?nameString:nameString.substring(0,2);
        }
        return finalName.toString();
    }

    private void setUserDetails(){

        if(userDataModel.getGivenName()!=null) {
            name.setText(printFirstCharacter(userDataModel.getGivenName()));
        }

        if(userDataModel.getEmail()==null)
        {
            email_arrow.setVisibility(View.VISIBLE);
            email_address_heading.setVisibility(View.GONE);
            email_address.setText(getString(R.string.MYA_Add_email_address));
        } else {
            email_address.setText(userDataModel.getEmail());
        }

        if(userDataModel.getMobileNumber()==null)
        {
            mobile_number_heading.setVisibility(View.INVISIBLE);
            mobile_arrow.setVisibility(View.VISIBLE);
            mobile_number.setText(getString(R.string.MYA_Add_mobile_number));

        }else {
            mobile_number.setText(userDataModel.getMobileNumber());
        }


        if( userDataModel.getEmail()!=null &&  userDataModel.getMobileNumber()!=null)
        {
            email_mobile_arrow.setVisibility(View.VISIBLE);
        }

        if(userDataModel.getGender()!=null)
        {
            gender.setText(userDataModel.getGender());
        }
        if (userDataModel.getName()!=null)
        {
            name_value.setText(userDataModel.getGivenName());
        }


        if(userDataModel.getBirthday()!=null)
        {
            SimpleDateFormat formatter=new SimpleDateFormat("dd MMMM yyyy");
            String tempDate=formatter.format(userDataModel.getBirthday());
            dob_value.setText(tempDate);
        }
    }
}

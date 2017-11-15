/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.mya.details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.philips.platform.mya.R;
import com.philips.platform.mya.util.mvp.MyaBaseFragment;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyaDetailsFragment extends MyaBaseFragment {

    ImageButton email_mobile_arrow,email_arrow,mobile_arrow;
    String email_mobile,mobile="abc",email="abc";
    Label email_address,mobile_number;
    Label name;

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


        name.setText(printFirstCharacter("Philips Bangalore"));
        Log.e("MyaDetails","printFirstCharacter"+printFirstCharacter("Philips Bangalore"));

        if(email==null)
        {
            email_arrow.setVisibility(View.VISIBLE);
            email_address.setText(getString(R.string.MYA_Add_email_address));
        }
        else if( mobile==null)
        {
            mobile_arrow.setVisibility(View.VISIBLE);
            mobile_number.setText(getString(R.string.MYA_Add_mobile_number));
        }

        else if( email!=null &&  mobile!=null)
        {
            email_mobile_arrow.setVisibility(View.VISIBLE);
        }
        return view;
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
        return false;
    }
}

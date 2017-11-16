package com.philips.platform.ths.settings;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.R.id.ths_customer_support_phone_number_id;

public class THSCustomerSupportFragment extends THSBaseFragment implements View.OnClickListener{
    public static final String TAG = THSCustomerSupportFragment.class.getSimpleName();

    Label mPhoneNumber;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_customer_support_detail, container, false);
        mPhoneNumber  = (Label)view.findViewById(ths_customer_support_phone_number_id);
        mPhoneNumber.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBarListener actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(getString(R.string.ths_customer_support),true);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==ths_customer_support_phone_number_id){
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + mPhoneNumber.getText().toString()));
            startActivity(intent);
        }

    }
}

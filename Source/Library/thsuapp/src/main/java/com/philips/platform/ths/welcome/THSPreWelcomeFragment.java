/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;

public class THSPreWelcomeFragment extends THSBaseFragment implements View.OnClickListener{
    public static final String TAG = THSPreWelcomeFragment.class.getSimpleName();
    protected THSPreWelcomePresenter mThsPreWelcomeScreenPresenter;
    private Button mBtnGoSeeProvider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_pre_welcome_screen, container, false);
        mThsPreWelcomeScreenPresenter = new THSPreWelcomePresenter(this);
        mBtnGoSeeProvider = (Button) view.findViewById(R.id.ths_go_see_provider);
        mBtnGoSeeProvider.setOnClickListener(this);
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        return view;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.ths_go_see_provider){
            mThsPreWelcomeScreenPresenter.onEvent(R.id.ths_go_see_provider);
        }
    }

   /* @Override
    public boolean handleBackEvent() {
        int backStackCount = getFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = getFragmentManager().getBackStackEntryAt(i).getId();
            getFragmentManager().popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        return true;
    }*/
}

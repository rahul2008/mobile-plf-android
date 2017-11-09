/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.THS_CONFIRM_T_AND_C;
import static com.philips.platform.ths.utility.THSConstants.THS_TERMS_AND_CONDITION;

public class THSPreWelcomeFragment extends THSBaseFragment implements View.OnClickListener{
    public static final String TAG = THSPreWelcomeFragment.class.getSimpleName();
    protected THSPreWelcomePresenter mThsPreWelcomeScreenPresenter;
    private Button mBtnGoSeeProvider;
    private Label mLabelSeeHowItWorks;
    private Label mLabelTermsAndConditions;
    private RelativeLayout mRelativeLayoutContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_pre_welcome_screen, container, false);
        mThsPreWelcomeScreenPresenter = new THSPreWelcomePresenter(this);
        mBtnGoSeeProvider = (Button) view.findViewById(R.id.ths_go_see_provider);
        mLabelSeeHowItWorks = (Label) view.findViewById(R.id.ths_video_consults);
        mLabelTermsAndConditions = (Label) view.findViewById(R.id.ths_licence);
        mRelativeLayoutContainer = (RelativeLayout) view.findViewById(R.id.ths_pre_welcome_screen);
        mLabelTermsAndConditions.setOnClickListener(this);
        mLabelSeeHowItWorks.setOnClickListener(this);
        mBtnGoSeeProvider.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.ths_go_see_provider){
            mThsPreWelcomeScreenPresenter.onEvent(R.id.ths_go_see_provider);
        }else if(viewId == R.id.ths_video_consults){
            mThsPreWelcomeScreenPresenter.onEvent(R.id.ths_video_consults);
        }else if(viewId == R.id.ths_licence){
            createCustomProgressBar(mRelativeLayoutContainer,BIG);
            mThsPreWelcomeScreenPresenter.onEvent(R.id.ths_licence);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBarListener actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(getString(R.string.ths_welcome),true);
        }
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_CONFIRM_T_AND_C,null,null);
    }

    public void showTermsAndConditions(String url) {
        hideProgressBar();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_TERMS_AND_CONDITION,null,null);
    }
}

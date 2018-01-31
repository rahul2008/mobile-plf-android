/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.uappclasses.THSCompletionProtocol;
import com.philips.platform.ths.utility.THSTagUtils;
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
    private ImageView ths_terms_and_conditions_cross;
    private Toolbar toolBar;
    private Drawable navigationIconDrawable;

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
        View view = getActivity().findViewById(R.id.uid_toolbar);
        if(view instanceof Toolbar) {
            this.toolBar = (Toolbar)view;
            this.navigationIconDrawable = this.toolBar.getNavigationIcon();
        }

        setBackArrowButton();
        THSTagUtils.doTrackPageWithInfo(THS_CONFIRM_T_AND_C,null,null);
    }

    private void setBackArrowButton() {
        if(this.isToolBarContainsNavigationIcon() && null != toolBar) {
            this.toolBar.setNavigationIcon(getResources().getDrawable(R.mipmap.dls_cross_bold, getActivity().getTheme()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        restoreBackIcon();
    }

    private void restoreBackIcon() {
        if(null != toolBar) {
            this.toolBar.setNavigationIcon(getResources().getDrawable(R.drawable.uid_back_icon, getActivity().getTheme()));
        }
    }

    public void showTermsAndConditions(String url) {
        hideProgressBar();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        THSTagUtils.doTrackPageWithInfo(THS_TERMS_AND_CONDITION,null,null);
    }

    private boolean isToolBarContainsNavigationIcon() {
        return this.toolBar != null && this.navigationIconDrawable != null;
    }

    @Override
    public boolean handleBackEvent() {
        exitFromAmWell(THSCompletionProtocol.THSExitType.Other);
        return true;
    }
}

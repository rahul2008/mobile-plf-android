package com.philips.platform.ths.onboarding;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.onboardingtour.OnBoardingTourPresenter;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;


@SuppressWarnings("serial")
public class OnBoardingFragment extends THSBaseFragment implements View.OnClickListener {

    public static final String TAG = OnBoardingFragment.class.getSimpleName();
    protected OnBoardingPresenter onBoardingPresenter;
    private RelativeLayout relativeLayout;
    private boolean isPagerFlow = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            isPagerFlow = arguments.getBoolean(OnBoardingTourPresenter.ARG_PAGER_FLOW, true);
        }

        onBoardingPresenter = new OnBoardingPresenter(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_terms_conditions, container, false);
        Button btn_agree_and_continue = view.findViewById(R.id.btn_continue);
        btn_agree_and_continue.setOnClickListener(this);
        Label link_termsAndConditions = view.findViewById(R.id.breif_2);
        link_termsAndConditions.setOnClickListener(this);
        relativeLayout = view.findViewById(R.id.onboarding_view);

        if (!THSManager.getInstance().getOnBoradingABFlow().equalsIgnoreCase(THSConstants.THS_ONBOARDING_ABFLOW1)&& isPagerFlow) {
            btn_agree_and_continue.setVisibility(View.GONE);
        }
        else if(!THSManager.getInstance().getOnBoradingABFlow().equalsIgnoreCase(THSConstants.THS_ONBOARDING_ABFLOW1) && !isPagerFlow){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btn_agree_and_continue.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            btn_agree_and_continue.setLayoutParams(layoutParams);
        }
        ActionBarListener actionBarListener = getActionBarListener();
        if (actionBarListener != null) {
            actionBarListener.updateActionBar(R.string.ths_Welcome_nav_title, false);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.breif_2) {
            createCustomProgressBar(relativeLayout, BIG);
        }
        onBoardingPresenter.onEvent(v.getId());
    }

    @Override
    public void onResume() {
        super.onResume();

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.uid_toolbar);
        if(toolbar!=null) {
            final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ths_cross_icon);
            toolbar.setNavigationIcon(drawable);
        }
    }


    @Override
    public boolean handleBackEvent() {
        THSTagUtils.doExitToPropositionWithCallBack();
        return true;
    }

    public void showTermsAndConditions(String url) {
        hideProgressBar();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}

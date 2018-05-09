/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;


import static com.philips.platform.ths.utility.THSConstants.THS_NOPP_PAGE;

public class THSNoticeOfPrivacyPracticesFragment extends THSBaseFragment {
    public static final String TAG = THSNoticeOfPrivacyPracticesFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    Label legalTextsLabel;
    THSBasePresenter mTHSNoppPresenter;
    private RelativeLayout mRelativeLayoutNopContainer;
    static final long serialVersionUID = 157L;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_nopp_fragment, container, false);
        legalTextsLabel = (Label) view.findViewById(R.id.ths_intake_nopp_agreement_text);
        mRelativeLayoutNopContainer = (RelativeLayout) view.findViewById(R.id.nop_container);
        mTHSNoppPresenter = new THSNoticeOfPrivacyPracticesPresenter(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        ((THSNoticeOfPrivacyPracticesPresenter) mTHSNoppPresenter).showLegalTextForNOPP();
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_NOPP_PAGE,null,null);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("NOPP", true);
        }
    }
}

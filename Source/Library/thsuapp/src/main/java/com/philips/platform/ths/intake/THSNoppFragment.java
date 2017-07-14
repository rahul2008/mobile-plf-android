package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;

/**
 * Created by philips on 7/4/17.
 */

public class THSNoppFragment extends THSBaseFragment {
    public static final String TAG = THSNoppFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    Label legalTextsLabel;
    THSBasePresenter mTHSNoppPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_nopp_fragment, container, false);
        legalTextsLabel = (Label) view.findViewById(R.id.ths_intake_nopp_agreement_text);
        mTHSNoppPresenter = new THSNoppPresenter(this);

        createCustomProgressBar(view, BIG);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        ((THSNoppPresenter) mTHSNoppPresenter).showLegalTextForNOPP();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("NOPP", true);
        }
    }
}

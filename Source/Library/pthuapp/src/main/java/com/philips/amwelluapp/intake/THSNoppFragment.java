package com.philips.amwelluapp.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;

/**
 * Created by philips on 7/4/17.
 */

public class THSNoppFragment extends PTHBaseFragment {
    public static final String TAG = THSNoppFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    Label legalTextsLabel;
    PTHBasePresenter mTHSNoppPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_nopp_fragment, container, false);
        legalTextsLabel = (Label) view.findViewById(R.id.ths_intake_nopp_agreement_text);
        mTHSNoppPresenter = new THSNoppPresenter(this);

        createCustomProgressBar(view,BIG);
        view.addView(mPTHBaseFragmentProgressBar);
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

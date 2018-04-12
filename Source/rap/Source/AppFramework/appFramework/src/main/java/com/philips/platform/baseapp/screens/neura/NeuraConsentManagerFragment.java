/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.neura;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractOnboardingBaseFragment;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.uappframework.listener.BackEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeuraConsentManagerFragment extends AbstractOnboardingBaseFragment implements BackEventListener, View.OnClickListener, FragmentView {


    public static final String TAG = NeuraConsentManagerFragment.class.getSimpleName();
    private static final long serialVersionUID = 4394954556057838520L;
    private Button allow, mayBelater, philipsPrivacy;
    private AbstractUIBasePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_neura_consent_manager, container, false);
        allow = inflate.findViewById(R.id.allowButton);
        mayBelater = inflate.findViewById(R.id.mayBeLater);
        philipsPrivacy = inflate.findViewById(R.id.philipsPrivacy);
        allow.setOnClickListener(this);
        mayBelater.setOnClickListener(this);
        philipsPrivacy.setOnClickListener(this);
        presenter = new NeuraConsentManagerPresentor(this);
        return inflate;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBar(R.string.RA_Allow_Sharing, true);
    }

    @Override
    public boolean handleBackEvent() {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) getFragmentActivity().getApplication();
        BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        targetFlowManager.getBackState(targetFlowManager.getCurrentState());
        hideActionBar();
        getFragmentActivity().getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onClick(View view) {
        presenter.onEvent(view.getId());
    }

}

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
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.uappframework.listener.BackEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeuraConsentManagerFragment extends Fragment implements BackEventListener {


    public static final String TAG = NeuraConsentManagerFragment.class.getSimpleName();
    private static final long serialVersionUID = 4394954556057838520L;
    private Button allow,mayBeLater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_neura_consent_manager, container, false);
        allow = inflate.findViewById(R.id.allowButton);
        mayBeLater = inflate.findViewById(R.id.mayBeLater);
        return inflate;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBarIcon(true);
    }

    @Override
    public boolean handleBackEvent() {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) getActivity().getApplication();
        BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        targetFlowManager.getBackState(targetFlowManager.getCurrentState());
        getActivity().getSupportFragmentManager().popBackStack();
        return false;
    }
}

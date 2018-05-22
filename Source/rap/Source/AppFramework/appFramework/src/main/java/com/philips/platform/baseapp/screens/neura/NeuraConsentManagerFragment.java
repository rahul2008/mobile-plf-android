/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.neura;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractOnboardingBaseFragment;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeuraConsentManagerFragment extends AbstractOnboardingBaseFragment implements BackEventListener, View.OnClickListener, FragmentView {


    public static final String TAG = NeuraConsentManagerFragment.class.getSimpleName();
    private static final long serialVersionUID = 4394954556057838520L;
    private com.philips.platform.uid.view.widget.Button allow, mayBeLater;
    private Label philipsPrivacy, neuraPrivacyPolicy, whatDoesItMean;
    private AbstractUIBasePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_neura_consent_manager, container, false);
        initViews(inflate);
        allow.setOnClickListener(this);
        mayBeLater.setOnClickListener(this);
        philipsPrivacy.setOnClickListener(this);
        neuraPrivacyPolicy.setOnClickListener(this);
        whatDoesItMean.setOnClickListener(this);
        presenter = new NeuraConsentManagerPresenter(this);
        applyOverLayBackground(inflate);
        return inflate;
    }

    void initViews(View inflate) {
        allow = inflate.findViewById(R.id.csw_justInTimeView_consentOk_button);
        mayBeLater = inflate.findViewById(R.id.csw_justInTimeView_consentLater_label);
        philipsPrivacy = inflate.findViewById(R.id.csw_justInTimeView_privacyPhilips_label);
        neuraPrivacyPolicy = inflate.findViewById(R.id.csw_justInTimeView_privacyNeura_label);
        whatDoesItMean = inflate.findViewById(R.id.csw_justInTimeView_consentHelplink_button);

        underlineLabels();

    }

    private void underlineLabels() {
        SpannableString contentWhatDoesItMean = new SpannableString(whatDoesItMean.getText());
        contentWhatDoesItMean.setSpan(new UnderlineSpan(),0,contentWhatDoesItMean.length(),0);
        whatDoesItMean.setText(contentWhatDoesItMean);

        SpannableString contentPhilipsPrivacy = new SpannableString(philipsPrivacy.getText());
        contentPhilipsPrivacy.setSpan(new UnderlineSpan(),0,contentPhilipsPrivacy.length(),0);
        philipsPrivacy.setText(contentPhilipsPrivacy);

        SpannableString contentNeuraPrivacyPolicy = new SpannableString(neuraPrivacyPolicy.getText());
        contentNeuraPrivacyPolicy.setSpan(new UnderlineSpan(),0,contentNeuraPrivacyPolicy.length(),0);
        neuraPrivacyPolicy.setText(contentNeuraPrivacyPolicy);
    }


    private void applyOverLayBackground(View inflate) {
        View overLayView = inflate.findViewById(R.id.neura_overLap_view);
        //TODO - need to change attribute as per design team requirements
        int colorFromAttribute = UIDHelper.getColorFromAttribute(getFragmentActivity().getTheme(), com.philips.platform.uid.R.attr.uidNavigationSecondaryBackgroundColor, R.color.blue);
        overLayView.setBackgroundColor(colorFromAttribute);
        overLayView.getBackground().setAlpha(204);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBar(R.string.csw_justInTimeView_toolbar_title, true);
    }

    @Override
    public boolean handleBackEvent() {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) getFragmentActivity().getApplication();
        BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        try {
            targetFlowManager.getBackState(targetFlowManager.getCurrentState());
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            Toast.makeText(getFragmentActivity(), getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
        }
        hideActionBar();
        getFragmentActivity().getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onClick(View view) {
        presenter.onEvent(view.getId());
    }

}

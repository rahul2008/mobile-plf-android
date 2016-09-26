/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.registration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.trackers.Tracker;

import javax.inject.Inject;

import cdp.philips.com.mydemoapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserRegistrationFragment extends Fragment implements
        UserRegistrationListener {


    @Inject
    Eventing eventing;

    @Inject
    Tracker tracker;

    /**
     * The Constant TAG.
     */
    public static final String TAG = UserRegistrationFragment.class.getSimpleName();
    public UserRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.af_com_philips_user_registration,
                container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
/*        RegistrationHelper mRegistrationHelper = RegistrationHelper.getInstance();
        mRegistrationHelper.initializeUserRegistration(getActivity(), PicassoUtility.setRegistrationAppLocale(getActivity()));
        Tagging.init(Locale.getDefault(), getActivity(), "Grooming");
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);*/
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onCreate");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationSampleActivity register: UserRegistrationListener");
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
        launchRegistrationFragment(R.id.llUserRegistrationContainer, getActivity());
    }

    /**
     * Launch registration fragment
     */
    private void launchRegistrationFragment(int container, FragmentActivity fragmentActivity) {
        try {
            FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
           // registrationFragment.setOnUpdateTitleListener(this);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(container, registrationFragment, RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {

        }
    }
/*
    @Override
    public void onUserRegistrationComplete(Activity activity) {
        moveToNextState();
    }

    @Override
    public void onPrivacyPolicyClick(final Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(final Activity activity) {

    }*/

    private void moveToNextState() {
       /* UIFlowManager flowManager = ((AppFrameworkApplication) AppFrameworkApplication.getContext()).getFlowManager();
        final BaseUIState uiState = FlowManager.getInstance(getActivity()).getNextState(AppStates.REGISTRATION);
        if (uiState != null) {
            Log.i(TAG, "Next State : " + uiState.getClass().getSimpleName());
            uiState.navigate(getStackActivity(),null);
        }*/


    }




   /* @Override
    public void onPrivacyPolicyClick(Activity activity) {
        Intent intent = new Intent(getActivity(), TermsPolicyAndLicenseActivity.class);
        intent.putExtra(IAppConstants.FILEPATH, IAppConstants.PRIVACY_FILEPATH);
        intent.putExtra(IAppConstants.ACTIVITYTYPE, getResources().getString(R.string.com_philips_lumea_about_screen_privacy));
        startActivity(intent);
    }*/

    /*@Override
    public void onTermsAndConditionClick(Activity activity) {
        Intent i = new Intent(getActivity(), TermsPolicyAndLicenseActivity.class);
        i.putExtra(IAppConstants.FILEPATH, IAppConstants.TERMS_FILEPATH);
        i.putExtra(IAppConstants.ACTIVITYTYPE, getResources().getString(R.string.com_philips_lumea_about_screen_terms));
        startActivity(i);
    }*/

    @Override
    public void onUserLogoutSuccess() {
    }

    @Override
    public void onUserLogoutFailure() {
        Toast.makeText(getActivity(), "Logout Failed", Toast.LENGTH_SHORT);
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {

    }

    /*@Override
    public void updateRegistrationTitleWithOutBack(int titleResourceID) {
    }*/

    /*@Override
    public void updateRegistrationTitle(int titleResourceID) {
        if (isAdded()) {
            (getStackActivity()).setActionBarLayout(R.layout.com_philips_lumea_action_bar_back, titleResourceID);
            Log.i(TAG, "updateRegistrationTitle : " + getResources().getString(titleResourceID));
        }
    }*/

    /*@Override
    public void updateRegistrationTitleWithBack(int titleResourceID) {
        if (isAdded()) {
            (getStackActivity()).setActionBarLayout(R.layout.com_philips_lumea_action_bar_back, titleResourceID);
            Log.i(TAG, "updateRegistrationTitle : " + getResources().getString(titleResourceID));
        }
    }
*/
    @Override
    public void onDestroy() {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
        super.onDestroy();
    }

  /*  @Override
    public void handleBackKey() {
        super.handleBackKey();
        moveToNextState();
    }*/
}

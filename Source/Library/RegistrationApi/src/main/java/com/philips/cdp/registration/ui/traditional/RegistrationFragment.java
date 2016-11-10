/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.janrain.android.Jump;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.social.AlmostDoneFragment;
import com.philips.cdp.registration.ui.social.MergeAccountFragment;
import com.philips.cdp.registration.ui.social.MergeSocialToSocialAccountFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.dhpclient.BuildConfig;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import org.json.JSONObject;


public class RegistrationFragment extends Fragment implements NetworStateListener,
        OnClickListener, BackEventListener {


    private FragmentManager mFragmentManager;

    private Activity mActivity;

    private ActionBarListener mActionBarListener;

    private int titleResourceID = -99;

    private boolean isAccountSettings = true;

    public UserRegistrationUIEventListener getUserRegistrationUIEventListener() {
        return userRegistrationUIEventListener;
    }

    public void setUserRegistrationUIEventListener(UserRegistrationUIEventListener
                                                           userRegistrationUIEventListener) {
        this.userRegistrationUIEventListener = userRegistrationUIEventListener;
    }

    private UserRegistrationUIEventListener userRegistrationUIEventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onCreate");
        RLog.i(RLog.VERSION, "Jump Version :" + Jump.getJumpVersion());
        RLog.i(RLog.VERSION, "LocaleMatch Version :" + PILLocaleManager.getLacaleMatchVersion());
        RLog.i(RLog.VERSION, "Registration Version :" +
                RegistrationHelper.getRegistrationApiVersion());
        RLog.i(RLog.VERSION, "HSDP Version :" + BuildConfig.VERSION_CODE);
        RegistrationBaseFragment.mWidth = 0;
        RegistrationBaseFragment.mHeight = 0;
        Bundle bunble = getArguments();
        if (bunble != null) {
            isAccountSettings = bunble.getBoolean(RegConstants.ACCOUNT_SETTINGS, true);
        }

        RLog.d("RegistrationFragment", "isAccountSettings : " + isAccountSettings);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.reg_fragment_registration, container, false);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationFragment  Register: NetworStateListener");
        mFragmentManager = getChildFragmentManager();
        if (mFragmentManager.getBackStackEntryCount() < 1) {
            loadFirstFragment();
        }

        return view;
    }

    @Override
    public void onStart() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        mActivity = getActivity();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onResume");

        super.onResume();
    }

    @Override
    public void onPause() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationFragment Unregister: NetworStateListener,Context");
        RegistrationBaseFragment.mWidth = 0;
        RegistrationBaseFragment.mHeight = 0;
        setPrevTiltle();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void setPrevTiltle() {
        if (mPreviousResourceId != -99)
            mActionBarListener.updateActionBar(getPreviousResourceId(), true);
        //mActionBarListener.updateRegistrationTitle(getPreviousResourceId());
    }


    public boolean onBackPressed() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onBackPressed");
        hideKeyBoard();
        return handleBackStack();
    }

    private boolean handleBackStack() {
        if (mFragmentManager != null) {
            int count = mFragmentManager.getBackStackEntryCount();
            if (count == 0) {
                return true;
            }
            Fragment fragment = mFragmentManager.getFragments().get(count);
            if (fragment instanceof WelcomeFragment) {
                navigateToHome();
                trackPage(AppTaggingPages.HOME);
            } else {
                if (fragment instanceof AlmostDoneFragment) {
                    ((AlmostDoneFragment) (fragment)).clearUserData();
                }
                trackHandler();
                mFragmentManager.popBackStack();
            }
            if (fragment instanceof AccountActivationFragment) {
                RegUtility.setCreateAccountStartTime(System.currentTimeMillis());
            }
        } else {
            getActivity().finish();
        }
        return false;
    }

    private void trackHandler() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            String prevPage;
            String curPage;
            if (mFragmentManager.getFragments() != null) {
                Fragment currentFragment = mFragmentManager.getFragments().get(count);
                Fragment preFragment = mFragmentManager.getFragments().get(count - 1);
                prevPage = getTackingPageName(currentFragment);
                curPage = getTackingPageName(preFragment);
                RLog.i("BAck identification", "Pre Page: " + prevPage + " Current : " + curPage);
                trackPage(curPage);
            }
        }

    }

    private String getTackingPageName(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            return AppTaggingPages.HOME;

        } else if (fragment instanceof CreateAccountFragment) {
            return AppTaggingPages.CREATE_ACCOUNT;

        } else if (fragment instanceof SignInAccountFragment) {
            return AppTaggingPages.CREATE_ACCOUNT;

        } else if (fragment instanceof AccountActivationFragment) {
            return AppTaggingPages.ACCOUNT_ACTIVATION;

        } else if (fragment instanceof WelcomeFragment) {
            return AppTaggingPages.WELCOME;

        } else if (fragment instanceof AlmostDoneFragment) {
            return AppTaggingPages.ALMOST_DONE;
        } else {
            return AppTaggingPages.MERGE_ACCOUNT;
        }
    }

    private void loadFirstFragment() {
        try {
            handleUserLoginStateFragments();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationFragment :FragmentTransaction Exception occured in loadFirstFragment  :"
                            + e.getMessage());
        }
    }

    public Fragment getWelcomeFragment() {
        return mFragmentManager.findFragmentByTag(AppTaggingPages.WELCOME);
    }

    private void handleUserLoginStateFragments() {
        User mUser = new User(mActivity.getApplicationContext());
        if (isAccountSettings) {
            if (mUser.isUserSignIn() && mUser.getEmailVerificationStatus()) {
                AppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().
                        getAppInfraInstance().getTagging();
                AppTagging.trackFirstPage(AppTaggingPages.USER_PROFILE);
                replaceWithLogoutFragment();
                return;
            }

            if (mUser.isUserSignIn() && !RegistrationConfiguration.getInstance().
                    isEmailVerificationRequired()) {
                AppTagging.trackFirstPage(AppTaggingPages.USER_PROFILE);
                replaceWithLogoutFragment();
                return;
            }
            AppTagging.trackFirstPage(AppTaggingPages.HOME);
            replaceWithHomeFragment();
        } else {
            if (mUser.isUserSignIn() && mUser.getEmailVerificationStatus()) {
                AppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().
                        getAppInfraInstance().getTagging();
                AppTagging.trackFirstPage(AppTaggingPages.WELCOME);
                // replaceWithLogoutFragment();
                //replace with welcome
                replaceWithWelcomeFragment();
                return;
            }
            if (mUser.isUserSignIn() && !RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
                AppTagging.trackFirstPage(AppTaggingPages.WELCOME);
                replaceWithWelcomeFragment();
                return;
            }
            AppTagging.trackFirstPage(AppTaggingPages.HOME);
            replaceWithHomeFragment();
        }

    }

    private void trackPage(String currPage) {
        AppTagging.trackPage(currPage);
    }

    public void replaceWithHomeFragment() {
        try {
            if (null != mFragmentManager) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_reg_fragment_container, new HomeFragment());
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }

    public void addFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
        hideKeyBoard();
    }

    public void replaceWelcomeFragmentOnLogin(Fragment fragment) {
        navigateToHome();
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, fragment, AppTaggingPages.WELCOME);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
        hideKeyBoard();
    }

    public void navigateToHome() {
        FragmentManager fragmentManager = getChildFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        try {
            for (int i = fragmentCount; i >= 0; i--) {
                fragmentManager.popBackStack();
            }
        } catch (IllegalStateException ignore) {
        } catch (Exception ignore) {
        }
    }

    public void addWelcomeFragmentOnVerification() {
        navigateToHome();
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        replaceFragment(welcomeFragment, AppTaggingPages.WELCOME);
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, fragment, fragmentTag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
        hideKeyBoard();
    }

    private void replaceWithWelcomeFragment() {
        try {
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, welcomeFragment,
                    AppTaggingPages.WELCOME);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }

    private void replaceWithLogoutFragment() {
        try {
            LogoutFragment logoutFragment = new LogoutFragment();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, logoutFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }


    public void addAlmostDoneFragment(JSONObject preFilledRecord, String provider,
                                      String registrationToken) {
        AlmostDoneFragment socialAlmostDoneFragment = new AlmostDoneFragment();
        Bundle socialAlmostDoneFragmentBundle = new Bundle();
        socialAlmostDoneFragmentBundle.putString(RegConstants.SOCIAL_TWO_STEP_ERROR,
                preFilledRecord.toString());
        socialAlmostDoneFragmentBundle.putString(RegConstants.SOCIAL_PROVIDER, provider);
        socialAlmostDoneFragmentBundle.putString(RegConstants.SOCIAL_REGISTRATION_TOKEN,
                registrationToken);
        socialAlmostDoneFragmentBundle.putBoolean(RegConstants.IS_FOR_TERMS_ACCEPATNACE, true);
        socialAlmostDoneFragment.setArguments(socialAlmostDoneFragmentBundle);
        addFragment(socialAlmostDoneFragment);
    }

    public void addPlainAlmostDoneFragment() {
        AlmostDoneFragment almostDoneFragment = new AlmostDoneFragment();
        addFragment(almostDoneFragment);
    }

    public void addAlmostDoneFragmentforTermsAcceptance() {
        AlmostDoneFragment almostDoneFragment = new AlmostDoneFragment();
        Bundle almostDoneFragmentBundle = new Bundle();
        almostDoneFragmentBundle.putBoolean(RegConstants.IS_FOR_TERMS_ACCEPATNACE, true);
        addFragment(almostDoneFragment);
    }

    public void addPhilipsNewsFragment() {
        PhilipsNewsFragment philipsNewsFragment = new PhilipsNewsFragment();
        addFragment(philipsNewsFragment);
    }

    public void addMergeAccountFragment(String registrationToken, String provider, String emailId) {
        MergeAccountFragment mergeAccountFragment = new MergeAccountFragment();
        Bundle mergeFragmentBundle = new Bundle();
        mergeFragmentBundle.putString(RegConstants.SOCIAL_PROVIDER, provider);
        mergeFragmentBundle.putString(RegConstants.SOCIAL_MERGE_TOKEN, registrationToken);
        mergeFragmentBundle.putString(RegConstants.SOCIAL_MERGE_EMAIL, emailId);
        mergeAccountFragment.setArguments(mergeFragmentBundle);
        addFragment(mergeAccountFragment);
    }

    public void addMergeSocialAccountFragment(Bundle bundle) {
        MergeSocialToSocialAccountFragment mergeAccountFragment
                = new MergeSocialToSocialAccountFragment();
        mergeAccountFragment.setArguments(bundle);
        addFragment(mergeAccountFragment);
    }

    public void launchAccountActivationFragmentForLogin() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegConstants.IS_SOCIAL_PROVIDER, true);
        trackPage(AppTaggingPages.ACCOUNT_ACTIVATION);
        AccountActivationFragment accountActivationFragment = new AccountActivationFragment();
        accountActivationFragment.setArguments(bundle);
        addFragment(accountActivationFragment);
    }

    public void addResetPasswordFragment() {
        ForgotPasswordFragment resetPasswordFragment = new ForgotPasswordFragment();
        addFragment(resetPasswordFragment);
    }

    public void hideKeyBoard() {
        if (mActivity != null) {
            InputMethodManager imm = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mActivity.getWindow() != null && mActivity.getWindow().getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(mActivity.getWindow().
                        getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public void showKeyBoard() {
        if (mActivity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_reg_back) {
            onBackPressed();
        }
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        if (!isOnline && !UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
            UserRegistrationInitializer.getInstance().resetInitializationState();
        }
        if (!UserRegistrationInitializer.getInstance().isJanrainIntialized() &&
                !UserRegistrationInitializer.getInstance().isJumpInitializationInProgress()) {
            RLog.d(RLog.NETWORK_STATE, "RegistrationFragment :onNetWorkStateReceived");
            RegistrationHelper registrationSettings = RegistrationHelper.getInstance();
            registrationSettings
                    .initializeUserRegistration(mActivity
                            .getApplicationContext());
            RLog.d(RLog.JANRAIN_INITIALIZE,
                    "RegistrationFragment : Janrain reinitialization with locale : "
                            + RegistrationHelper.getInstance().getLocale(getContext()));
        }
    }

    public Activity getParentActivity() {
        return mActivity;
    }

    public int getFragmentCount() {
        FragmentManager fragmentManager = getChildFragmentManager();
        int fragmentCount = fragmentManager.getFragments().size();
        return fragmentCount;
    }

    public ActionBarListener getUpdateTitleListener() {
        return mActionBarListener;
    }

    public void setOnUpdateTitleListener(ActionBarListener listener) {
        this.mActionBarListener = listener;
    }

    public void setResourceID(int titleResourceId) {
        titleResourceID = titleResourceId;
    }

    public int getResourceID() {
        return titleResourceID;
    }

    int mPreviousResourceId = -99;

    public void setPreviousResourceId(int previousResourceId) {
        mPreviousResourceId = previousResourceId;
    }

    public int getPreviousResourceId() {
        return mPreviousResourceId;
    }

    int currentTitleResource;

    public void setCurrentTitleResource(int currentTitleResource) {
        this.currentTitleResource = currentTitleResource;
    }

    public int getCurrentTitleResource() {
        return currentTitleResource;

    }

    @Override
    public boolean handleBackEvent() {
        return !(onBackPressed());
    }


}

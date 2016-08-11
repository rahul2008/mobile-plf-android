/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 *
 */

package com.philips.cdp.registration.coppa.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.janrain.android.Jump;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.ui.activity.RegistrationCoppaActivity;
import com.philips.cdp.registration.coppa.utils.AppTaggingCoppaPages;
import com.philips.cdp.registration.coppa.utils.CoppaConstants;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaHelper;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.dhpclient.BuildConfig;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

public class RegistrationCoppaFragment extends Fragment implements NetworStateListener,
        OnClickListener ,BackEventListener{

    private static final String REGISTRATION_VERSION_TAG = "registrationVersion";

    private static FragmentManager mFragmentManager;

    private static Activity mActivity;
    private static boolean isParentalConsent = true;
    private static boolean isParentConsentRequested;
    private static boolean isParentalFragmentLaunched;
    private static int lastKnownResourceId = -99;
    private static boolean isRegistrationLunched;
    private static ProgressDialog mProgressDialog;
    private static UserRegistrationListener mUserRegistrationListener =
            new UserRegistrationListener() {
                @Override
                public void onUserRegistrationComplete(Activity activity) {
                    //Launch the Approval fragment
                    isRegistrationLunched = false;
                    isParentalFragmentLaunched = true;
                    showRefreshProgress(activity);

                    final User user = new User(activity.getApplicationContext());
                    user.refreshLoginSession(new RefreshLoginSessionHandler() {
                        @Override
                        public void onRefreshLoginSessionSuccess() {
                            user.refreshUser(new RefreshUserHandler() {
                                @Override
                                public void onRefreshUserSuccess() {
                                    hideRefreshProgress();
                                    handleConsentState();
                                }

                                @Override
                                public void onRefreshUserFailed(int error) {
                                    hideRefreshProgress();
                                }
                            });
                        }

                        @Override
                        public void onRefreshLoginSessionFailedWithError(int error) {
                            hideRefreshProgress();
                        }

                        @Override
                        public void onRefreshLoginSessionInProgress(String message) {
                            hideRefreshProgress();
                        }
                    });
                }

                @Override
                public void onPrivacyPolicyClick(Activity activity) {
                    if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                        RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                                notifyOnPrivacyPolicyClickEventOccurred(activity);
                    }
                }

                @Override
                public void onTermsAndConditionClick(Activity activity) {
                    if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                        RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                                notifyOnTermsAndConditionClickEventOccurred(activity);
                    }
                }

                @Override
                public void onUserLogoutSuccess() {
                    replaceWithParentalAccess();
                    if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                        RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                                notifyOnUserLogoutSuccess();
                    }
                }

                @Override
                public void onUserLogoutFailure() {
                    if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                        RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                                notifyOnUserLogoutFailure();
                    }
                }

                @Override
                public void onUserLogoutSuccessWithInvalidAccessToken() {
                    replaceWithParentalAccess();
                    if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                        RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                                notifyOnLogoutSuccessWithInvalidAccessToken();
                    }
                }
            };
    private ActionBarListener mActionBarListener;
    private int mtitleResourceId = -99;
    private boolean isAccountSettings = true;
    private CoppaExtension coppaExtension;

    public static void replaceWithParentalAccess() {

        try {
            performReplaceWithPerentalAccess();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaFragment :FragmentTransaction Exception " +
                            "occured in before scheduling  :"
                            + e.getMessage());
            RegPreferenceUtility.storePreference(getParentActivity().getApplicationContext(),
                    "doPopBackStack", true);
        }
    }

    private static void performReplaceWithPerentalAccess() {

        if (mFragmentManager != null) {
            mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            final ParentalAccessFragment parentalAccessFragment = new ParentalAccessFragment();
            final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, parentalAccessFragment,
                    "Parental Access");
            fragmentTransaction.commitAllowingStateLoss();
            RegPreferenceUtility.storePreference(getParentActivity().getApplicationContext(),
                    "doPopBackStack", false);
        }
    }

    public static Activity getParentActivity() {
        return mActivity;
    }

    private static void addParentalApprovalFragment() {
        //update coppa with prev as registration and adding on top

        if (mFragmentManager != null) {
            try {
                final ParentalApprovalFragment parentalAccessFragment =
                        new ParentalApprovalFragment();
                final int count = mFragmentManager.getBackStackEntryCount();
                RegistrationFragment registrationFragment = null;
                if (count != 0 && registrationFragment instanceof RegistrationFragment) {
                    registrationFragment = (RegistrationFragment)
                            mFragmentManager.getFragments().get(count);
                }
                if (registrationFragment != null) {
                    parentalAccessFragment.setPrevTitleResourceId(lastKnownResourceId);
                }
                final FragmentTransaction fragmentTransaction =
                        mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_reg_fragment_container,
                        parentalAccessFragment, "Parental Access");
                //fragmentTransaction.addToBackStack(parentalAccessFragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                RLog.e(RLog.EXCEPTION,
                        "RegistrationCoppaFragment :FragmentTransaction Exception" +
                                " occured in addFragment  :" + e.getMessage());
            }
        }
    }

    private static void addParentalApprovalFragmentonLaunch() {
        //update coppa with prev as registration and adding on top

        if (mFragmentManager != null) {
            try {
                ParentalApprovalFragment parentalAccessFragment = new ParentalApprovalFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(RegConstants.IS_FROM_PARENTAL_CONSENT, isParentalConsent);
                parentalAccessFragment.setArguments(bundle);
                int count = mFragmentManager.getBackStackEntryCount();
                RegistrationFragment registrationFragment = null;
                if (count != 0 && registrationFragment instanceof RegistrationFragment) {
                    registrationFragment = (RegistrationFragment)
                            mFragmentManager.getFragments().get(count);
                }
                if (registrationFragment != null) {
                    parentalAccessFragment.setPrevTitleResourceId(lastKnownResourceId);
                }
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_reg_fragment_container,
                        parentalAccessFragment, "Parental Access");
                fragmentTransaction.addToBackStack(parentalAccessFragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                RLog.e(RLog.EXCEPTION,
                        "RegistrationCoppaFragment :FragmentTransaction Exception " +
                                "occured in addFragment  :" + e.getMessage());
            }
        }
    }

    public static void showRefreshProgress(Activity activity) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity, R.style.reg_Custom_loaderTheme);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
    }

    public static void hideRefreshProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private static void handleConsentState() {
        RLog.i("Coppa Consent", "Handle Consent State");

        CoppaExtension mCoppaExtension;
        mCoppaExtension = new CoppaExtension(getParentActivity().getApplicationContext());
        mCoppaExtension.buildConfiguration();
        if (!isParentConsentRequested) {
            if (mCoppaExtension.getCoppaEmailConsentStatus() ==
                    CoppaStatus.kDICOPPAConfirmationGiven ||
                    mCoppaExtension.getCoppaEmailConsentStatus() ==
                            CoppaStatus.kDICOPPAConsentNotGiven ||
                    mCoppaExtension.getCoppaEmailConsentStatus() ==
                            CoppaStatus.kDICOPPAConfirmationNotGiven) {
                if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                    RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                            notifyonUserRegistrationCompleteEventOccurred(getParentActivity());
                }
            } else {
                addParentalApprovalFragmentonLaunch();
            }
        } else {
            addParentalApprovalFragmentonLaunch();
            isParentConsentRequested = false;
        }
    }

    public static UserRegistrationListener getUserRegistrationListener() {
        return mUserRegistrationListener;
    }

    private static void trackPage(String currPage) {
        AppTagging.trackPage(currPage);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onCreate");
        RLog.i(RLog.VERSION, "Jump Version :" + Jump.getJumpVersion());
        RLog.i(RLog.VERSION, "LocaleMatch Version :" + PILLocaleManager.getLacaleMatchVersion());
        RLog.i(RLog.VERSION, "Registration Version :" + RegistrationHelper.
                getRegistrationApiVersion());
        RLog.i(RLog.VERSION, "HSDP Version :" + BuildConfig.VERSION_CODE);

        AppTaggingInterface aiAppTaggingInterface = RegistrationHelper.getInstance().
                getAppTaggingInterface();
        aiAppTaggingInterface.setPreviousPage("demoapp:home");
        RegistrationCoppaBaseFragment.mWidth = 0;
        RegistrationCoppaBaseFragment.mHeight = 0;
        Bundle bunble = getArguments();
        if (bunble != null) {
            isAccountSettings = bunble.getBoolean(RegConstants.ACCOUNT_SETTINGS, true);
            isParentConsentRequested = bunble.getBoolean(
                    CoppaConstants.LAUNCH_PARENTAL_FRAGMENT, false);
        }
        RLog.d("RegistrationCoppaFragment", "isAccountSettings : " + isAccountSettings);
        RLog.d("RegistrationCoppaFragment", "isParentConsentRequested : "
                + isParentConsentRequested);

        lastKnownResourceId = -99;
        coppaExtension = new CoppaExtension(getContext());
        if (savedInstanceState != null) {
            savedInstanceState.putBoolean("isRegistrationLaunched", isRegistrationLunched);

        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        final View view = inflater.inflate(R.layout.reg_fragment_registration, container, false);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaFragment  Register: NetworStateListener");
        mFragmentManager = getChildFragmentManager();
        if (mFragmentManager.getBackStackEntryCount() < 1) {
            loadFirstFragment();
        }
        return view;
    }

    @Override
    public void onStart() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onResume");
        mActivity = getActivity();
        mFragmentManager = getChildFragmentManager();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onResume"
                + RegPreferenceUtility.getStoredState(
                getParentActivity().getApplicationContext(), "doPopBackStack"));
        if (RegPreferenceUtility.getStoredState(
                getParentActivity().getApplicationContext(), "doPopBackStack")) {
            performReplaceWithPerentalAccess();
        }
    }

    @Override
    public void onPause() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaFragment Unregister: " +
                "NetworStateListener,Context");
        mFragmentManager = null;
        RegistrationCoppaBaseFragment.mWidth = 0;
        RegistrationCoppaBaseFragment.mHeight = 0;
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isRegistrationLaunched", isRegistrationLunched);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isRegistrationLunched = savedInstanceState.getBoolean("isRegistrationLaunched");
            if (!isRegistrationLunched) {
                this.setOnUpdateTitleListener((RegistrationCoppaActivity) getActivity());
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    private boolean onBackPressed() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onBackPressed");
        return handleBackStack();
    }

    private boolean handleBackStack() {
        if (mFragmentManager != null) {
            final int count = mFragmentManager.getBackStackEntryCount();
            RLog.i("Back count ", "" + count);
            if (count == 0) {
                return false;
            }
            if (count > 1) {
                final Fragment fragment = mFragmentManager.getFragments().get(count);
                if (fragment != null && fragment instanceof RegistrationFragment) {
                    final boolean isRegFragHandledBack = ((RegistrationFragment) fragment)
                            .onBackPressed();
                    //true  not consumed
                    //false  consumed
                    if (isRegFragHandledBack) {
                        //Message for killing fragmet app but by pass this condition not
                        trackHandler();
                        if (count >= 1) {
                            mFragmentManager.popBackStack();
                            return true;
                        } else {
                            return false;
                        }
                    }
                } else {
                    if (!(fragment instanceof ParentalApprovalFragment)) {
                        trackHandler();
                    }
                    mFragmentManager.popBackStack();
                }
            } else {
                mFragmentManager.popBackStack();
            }
        }
        return true;
    }

    private void trackHandler() {
        final int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            String prevPage = null;
            String curPage = null;
            if (mFragmentManager.getFragments() != null) {
                final Fragment currentFragment = mFragmentManager.getFragments().get(count);
                final Fragment preFragment = mFragmentManager.getFragments().get(count - 1);
                prevPage = getTackingPageName(currentFragment);
                curPage = getTackingPageName(preFragment);
                RLog.i("BAck identification", "Pre Page: " + prevPage + " Current : " + curPage);
                trackPage(curPage);
            }
        }
    }

    private String getTackingPageName(Fragment fragment) {
        if (fragment instanceof ParentalAccessFragment) {
            return AppTaggingCoppaPages.COPPA_PARENTAL_ACCESS;
        } else if (fragment instanceof ParentalAccessConfirmFragment) {
            return AppTaggingCoppaPages.COPPA_AGE_VERIFICATION;
        } else if (fragment instanceof ParentalApprovalFragment) {
            if (coppaExtension.getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConsentPending) {
                return AppTaggingCoppaPages.COPPA_FIRST_CONSENT;
            } else if (coppaExtension.getCoppaEmailConsentStatus() ==
                    CoppaStatus.kDICOPPAConfirmationPending) {
                return AppTaggingCoppaPages.COPPA_SECOND_CONSENT;
            }
        }
        return null;
    }

    private void loadFirstFragment() {

        final User user = new User(mActivity);
        if (user.isUserSignIn()) {
            if (!isParentConsentRequested) {
                launchRegistrationFragmentOnLoggedIn(isAccountSettings);
            } else {
                addParentalApprovalFragment();
            }
        } else {
            AppTagging.trackFirstPage(AppTaggingCoppaPages.COPPA_PARENTAL_ACCESS);
            replaceWithParentalAccess();
        }
    }

    public void addParentalConfirmFragment() {
        final ParentalAccessConfirmFragment parentalAccessConfirmFragment =
                new ParentalAccessConfirmFragment();
        addFragment(parentalAccessConfirmFragment);
        trackPage(AppTaggingCoppaPages.COPPA_AGE_VERIFICATION);
    }


    public int getFragmentBackStackCount() {

      //  FragmentManager fragmentManager = getChildFragmentManager();
        int fragmentCount = mFragmentManager.getFragments().size();
        return fragmentCount;

       // return mFragmentManager.getBackStackEntryCount();
    }

    public ActionBarListener getUpdateTitleListener() {
        return mActionBarListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_reg_back) {
            onBackPressed();
        }
    }

    public void setOnUpdateTitleListener(ActionBarListener listener) {
        this.mActionBarListener = listener;
    }

    public void setResourceId(int titleResourceId) {
        mtitleResourceId = titleResourceId;
    }

    public int getResourceID() {
        return mtitleResourceId;
    }


    @Override
    public void onNetWorkStateReceived(boolean isOnline) {

        if (!isOnline && !UserRegistrationInitializer.
                getInstance().isJanrainIntialized()) {
            UserRegistrationInitializer.getInstance().
                    resetInitializationState();
        }

        if (!UserRegistrationInitializer.getInstance().isJanrainIntialized() &&
                !UserRegistrationInitializer.getInstance().
                        isJumpInitializationInProgress()) {
            RLog.d(RLog.NETWORK_STATE, "RegistrationCoppaFragment " +
                    ":onNetWorkStateReceived");
            final RegistrationHelper registrationSettings = RegistrationHelper.getInstance();
            registrationSettings
                    .initializeUserRegistration(mActivity
                            .getApplicationContext());
            RLog.d(RLog.JANRAIN_INITIALIZE,
                    "RegistrationCoppaFragment : Janrain reinitialization with locale : "
                            + RegistrationHelper.getInstance().getLocale(getContext()));
        }
    }

    public void launchRegistrationFragment() {
        try {
            final RegistrationFragment registrationFragment = new RegistrationFragment();
            final Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
            registrationFragment.setPreviousResourceId(mtitleResourceId);
            registrationFragment.setOnUpdateTitleListener(new ActionBarListener() {
                @Override
                public void updateActionBar(int titleResourceID, boolean isShowBack) {
                    lastKnownResourceId = titleResourceID;
                    if (mActionBarListener != null) {

                        mActionBarListener.
                                updateActionBar(titleResourceID,true);

                        /*mActionBarListener.
                                updateRegistrationTitleWithBack(titleResourceId);*/
                    }
                }
            });
            /*registrationFragment.setOnUpdateTitleListener(new RegistrationTitleBarListener() {
                @Override
                public void updateRegistrationTitle(int titleResourceId) {
                    lastKnownResourceId = titleResourceId;
                    if (mActionBarListener != null) {
                        mActionBarListener.
                                updateRegistrationTitleWithBack(titleResourceId);
                    }
                }

                @Override
                public void updateRegistrationTitleWithBack(int titleResourceId) {
                    lastKnownResourceId = titleResourceId;
                    if (mActionBarListener != null) {
                        mActionBarListener.
                                updateRegistrationTitleWithBack(titleResourceId);
                    }
                }

                @Override
                public void updateRegistrationTitleWithOutBack(int titleResourceId) {
                    lastKnownResourceId = titleResourceId;
                    if (mActionBarListener != null) {
                        mActionBarListener.
                                updateRegistrationTitleWithBack(titleResourceId);
                    }
                }
            });*/
            final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(registrationFragment.getTag());

            fragmentTransaction.add(R.id.fl_reg_fragment_container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);

            isRegistrationLunched = true;
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaActivity :FragmentTransaction Exception occured " +
                            "in addFragment  :" + e.getMessage());
        }
    }

    private void launchRegistrationFragmentOnLoggedIn(boolean isAccountSettings) {
        try {
            final RegistrationFragment registrationFragment = new RegistrationFragment();
            final Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);

            registrationFragment.setOnUpdateTitleListener(new ActionBarListener() {
                @Override
                public void updateActionBar(int titleResourceID, boolean isShowBack) {
                        lastKnownResourceId = titleResourceID;
                        if (mActionBarListener != null) {
                            mActionBarListener.updateActionBar(
                                    titleResourceID,isShowBack);
                        }
                }
            });

           /* registrationFragment.setOnUpdateTitleListener(new RegistrationTitleBarListener() {
                @Override
                public void updateRegistrationTitle(int titleResourceId) {
                    lastKnownResourceId = titleResourceId;
                    if (mActionBarListener != null) {
                        mActionBarListener.updateRegistrationTitle(titleResourceId);
                    }
                }

                @Override
                public void updateRegistrationTitleWithBack(int titleResourceId) {
                    lastKnownResourceId = titleResourceId;
                    if (mActionBarListener != null) {
                        mActionBarListener.updateRegistrationTitleWithBack(
                                titleResourceId);
                    }
                }

                @Override
                public void updateRegistrationTitleWithOutBack(int titleResourceId) {
                    lastKnownResourceId = titleResourceId;
                    if (mActionBarListener != null) {
                        mActionBarListener.updateRegistrationTitleWithBack(
                                titleResourceId);
                    }
                }
            });*/
            final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaActivity :FragmentTransaction Exception " +
                            "occured in addFragment  :" + e.getMessage());
        }
    }

    private void addFragment(Fragment fragment) {
        try {
            final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaFragment :FragmentTransaction Exception occured " +
                            "in addFragment  :" + e.getMessage());
        }
    }

    @Override
    public boolean handleBackEvent() {
        return (onBackPressed());
    }
}

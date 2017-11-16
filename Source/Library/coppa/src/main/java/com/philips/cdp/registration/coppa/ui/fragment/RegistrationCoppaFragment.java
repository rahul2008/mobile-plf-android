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
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.utils.AppTaggingCoppaPages;
import com.philips.cdp.registration.coppa.utils.CoppaConstants;
import com.philips.cdp.registration.coppa.utils.CoppaInterface;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.NetworkStateReceiver;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.RegistrationContentConfiguration;
import com.philips.dhpclient.BuildConfig;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

public class RegistrationCoppaFragment extends Fragment implements NetworkStateListener,
        OnClickListener, BackEventListener {

    private NetworkUtility networkUtility;

    private static final String REGISTRATION_VERSION_TAG = "registrationVersion";

    private static FragmentManager mFragmentManager;

    private RegistrationLaunchMode mRegistrationLaunchMode = RegistrationLaunchMode.DEFAULT;
    RegistrationContentConfiguration registrationContentConfiguration;


    public UserRegistrationUIEventListener getUserRegistrationUIEventListener() {
        return userRegistrationCoppaUIEventListener;
    }

    public void setUserRegistrationUIEventListener(UserRegistrationUIEventListener
                                                           userRegistrationUIEventListener) {
        userRegistrationCoppaUIEventListener = userRegistrationUIEventListener;
    }

    private UserRegistrationUIEventListener userRegistrationCoppaUIEventListener;

    private NetworkStateReceiver mNetworkReceiver = new NetworkStateReceiver();

    private Activity mActivity;
    private static boolean isParentalConsent = true;
    private static boolean isParentConsentRequested;
    private static boolean isParentalFragmentLaunched;
    private static int lastKnownResourceId = -99;
    private static ProgressDialog mProgressDialog;
    private UserRegistrationUIEventListener userRegistrationUIEventListener = new UserRegistrationUIEventListener() {
        @Override
        public void onUserRegistrationComplete(Activity activity) {
            //Launch the Approval fragment
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
            if (getUserRegistrationUIEventListener() != null) {
                getUserRegistrationUIEventListener().
                        onPrivacyPolicyClick(activity);
            }
        }

        @Override
        public void onTermsAndConditionClick(Activity activity) {
            if (getUserRegistrationUIEventListener() != null) {
                getUserRegistrationUIEventListener().
                        onTermsAndConditionClick(activity);
            }
        }
    };



    private ActionBarListener mActionBarListener;

    private int mtitleResourceId = -99;
    private CoppaExtension coppaExtension;

    public void replaceWithParentalAccess() {

        try {
            performReplaceWithPerentalAccess();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaFragment :FragmentTransaction Exception " +
                            "occured in before scheduling  :"
                            + e.getMessage());
            RegPreferenceUtility.storePreference(getParentActivity().getApplicationContext(),
                    "PopBackStack", "doPopBackStack");
        }
    }

    private void performReplaceWithPerentalAccess() {

        if (mFragmentManager != null) {
            mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            final ParentalAccessFragment parentalAccessFragment = new ParentalAccessFragment();
            final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, parentalAccessFragment,
                    "Parental Access");
            fragmentTransaction.commitAllowingStateLoss();
            RegPreferenceUtility.storePreference(getParentActivity().getApplicationContext(),
                    "PopBackStack", "noPopBackStack");
        }
    }

    public Activity getParentActivity() {
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

    private void handleConsentState() {
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
                if (getUserRegistrationUIEventListener() != null) {
                    getUserRegistrationUIEventListener().
                            onUserRegistrationComplete(getParentActivity());
                }
            } else {
                addParentalApprovalFragmentonLaunch();
            }
        } else {
            addParentalApprovalFragmentonLaunch();
            isParentConsentRequested = false;
        }
    }



    private static void trackPage(String currPage) {
        AppTagging.trackPage(currPage);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onCreate");
        RLog.i(RLog.VERSION, "Jump Version :" + Jump.getJumpVersion());
        RLog.i(RLog.VERSION, "Registration Version :" + RegistrationHelper.
                getRegistrationApiVersion());
        RLog.i(RLog.VERSION, "HSDP Version :" + BuildConfig.VERSION_CODE);

        RegistrationCoppaBaseFragment.mWidth = 0;
        RegistrationCoppaBaseFragment.mHeight = 0;
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRegistrationLaunchMode = (RegistrationLaunchMode) bundle.get(RegConstants.REGISTRATION_LAUNCH_MODE);
            registrationContentConfiguration = (RegistrationContentConfiguration) bundle.get(RegConstants.REGISTRATION_CONTENT_CONFIG);

            isParentConsentRequested = bundle.getBoolean(
                    CoppaConstants.LAUNCH_PARENTAL_FRAGMENT, false);
        }
        RLog.d("RegistrationCoppaFragment", "mRegistrationLaunchMode : " + mRegistrationLaunchMode);
        RLog.d("RegistrationCoppaFragment", "isParentConsentRequested : "
                + isParentConsentRequested);

        lastKnownResourceId = -99;
        coppaExtension = new CoppaExtension(getContext());
        super.onCreate(savedInstanceState);
    }
    public RegistrationContentConfiguration getContentConfiguration(){
        return registrationContentConfiguration;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        networkUtility = CoppaInterface.getComponent().getNetworkUtility();
        final View view = inflater.inflate(R.layout.reg_fragment_registration, container, false);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaFragment  Register: NetworkStateListener");
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
//        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onResume"
//                + RegPreferenceUtility.getStoredState(
//                getParentActivity().getApplicationContext(), "doPopBackStack"));

        try {
            if (RegPreferenceUtility.getStoredState(
                    getParentActivity().getApplicationContext(), "doPopBackStack")) {
                RegPreferenceUtility.storePreference(getParentActivity().getApplicationContext(),
                        "PopBackStack", "doPopBackStack");
                RegPreferenceUtility.deletePreference("doPopBackStack");
            }

            if (RegPreferenceUtility.getPreferenceValue(
                    getParentActivity().getApplicationContext(), "PopBackStack", "doPopBackStack")) {
                performReplaceWithPerentalAccess();
            }

        } catch (Exception e){
            RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : popbackstack pref" +e);

        }
        networkUtility.registerNetworkListener(mNetworkReceiver);
    }

    @Override
    public void onPause() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onPause");
        super.onPause();
        networkUtility.unRegisterNetworkListener(mNetworkReceiver);
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
                "NetworkStateListener,Context");
        mFragmentManager = null;
        RegistrationCoppaBaseFragment.mWidth = 0;
        RegistrationCoppaBaseFragment.mHeight = 0;
        setUserRegistrationUIEventListener(null);
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
                launchRegistrationFragmentOnLoggedIn(mRegistrationLaunchMode);
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
            bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, mRegistrationLaunchMode);
            bundle.putSerializable(RegConstants.REGISTRATION_CONTENT_CONFIG,getContentConfiguration());
            registrationFragment.setArguments(bundle);
            registrationFragment.setPreviousResourceId(mtitleResourceId);
            registrationFragment.setUserRegistrationUIEventListener(userRegistrationUIEventListener);
            registrationFragment.setOnUpdateTitleListener(new ActionBarListener() {
                @Override
                public void updateActionBar(int titleResourceID, boolean isShowBack) {
                    lastKnownResourceId = titleResourceID;
                    if (mActionBarListener != null) {

                        mActionBarListener.
                                updateActionBar(titleResourceID, true);

                        /*mActionBarListener.
                                updateRegistrationTitleWithBack(titleResourceId);*/
                    }
                }

                @Override
                public void updateActionBar(String s, boolean b) {

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
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaActivity :FragmentTransaction Exception occured " +
                            "in addFragment  :" + e.getMessage());
        }
    }

    private void launchRegistrationFragmentOnLoggedIn(RegistrationLaunchMode registrationLaunchMode) {
        try {
            final RegistrationFragment registrationFragment = new RegistrationFragment();
            final Bundle bundle = new Bundle();
            registrationFragment.setUserRegistrationUIEventListener(userRegistrationUIEventListener);
            bundle.putSerializable(RegConstants.REGISTRATION_LAUNCH_MODE, registrationLaunchMode);
            bundle.putSerializable(RegConstants.REGISTRATION_CONTENT_CONFIG,getContentConfiguration());
            registrationFragment.setArguments(bundle);

            registrationFragment.setOnUpdateTitleListener(new ActionBarListener() {
                @Override
                public void updateActionBar(int titleResourceID, boolean isShowBack) {
                    lastKnownResourceId = titleResourceID;
                    if (mActionBarListener != null) {
                        mActionBarListener.updateActionBar(
                                titleResourceID, isShowBack);
                    }
                }

                @Override
                public void updateActionBar(String s, boolean b) {

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
            if (null != mFragmentManager) {
                final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
                fragmentTransaction.addToBackStack(fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (Exception e) {
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

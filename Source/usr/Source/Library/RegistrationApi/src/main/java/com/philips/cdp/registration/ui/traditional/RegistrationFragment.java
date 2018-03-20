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
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.janrain.android.Jump;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.events.CounterHelper;
import com.philips.cdp.registration.events.CounterListener;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.myaccount.UserDetailsFragment;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.social.AlmostDoneFragment;
import com.philips.cdp.registration.ui.social.MergeAccountFragment;
import com.philips.cdp.registration.ui.social.MergeSocialToSocialAccountFragment;
import com.philips.cdp.registration.ui.utils.NetworkStateReceiver;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.NotificationBarHandler;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.RegistrationContentConfiguration;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.dhpclient.BuildConfig;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import javax.inject.Inject;


public class RegistrationFragment extends Fragment implements NetworkStateListener,BackEventListener, CounterListener {

    @Inject
    NetworkUtility networkUtility;
    private static final long serialVersionUID = 1128016096756071386L;


    private FragmentManager mFragmentManager;

    private Activity mActivity;

    private ActionBarListener mActionBarListener;

    private RegistrationLaunchMode mRegistrationLaunchMode ;

    RegistrationContentConfiguration registrationContentConfiguration;
    Intent msgIntent;
    private static long RESEND_DISABLED_DURATION = 60 * 1000;
    private static final long INTERVAL = 1 * 1000;
    static public MyCountDownTimer myCountDownTimer;

    private int titleResourceID = -99;

    private NetworkStateReceiver mNetworkReceiver;

    private boolean isCounterRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onCreate");
        RLog.d(RLog.VERSION, "Jump Version :" + Jump.getJumpVersion());
        RLog.d(RLog.VERSION, "Registration Version :" +
                RegistrationHelper.getRegistrationApiVersion());
        RLog.d(RLog.VERSION, "HSDP Version :" + BuildConfig.VERSION_CODE);
        RegistrationBaseFragment.mWidth = 0;
        RegistrationBaseFragment.mHeight = 0;
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRegistrationLaunchMode = (RegistrationLaunchMode) bundle.get(RegConstants.REGISTRATION_LAUNCH_MODE);

            registrationContentConfiguration = (RegistrationContentConfiguration) bundle.get(RegConstants.REGISTRATION_CONTENT_CONFIG);
        }
        RLog.d("RegistrationFragment", "mRegistrationLaunchMode : " + mRegistrationLaunchMode);
        CounterHelper.getInstance()
                .registerCounterEventNotification(RegConstants.COUNTER_TICK, this);
        CounterHelper.getInstance()
                .registerCounterEventNotification(RegConstants.COUNTER_FINISH, this);
        myCountDownTimer = new MyCountDownTimer(RESEND_DISABLED_DURATION, INTERVAL);

        super.onCreate(savedInstanceState);
    }

    public RegistrationContentConfiguration getContentConfiguration() {
        return registrationContentConfiguration;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.reg_fragment_registration, container, false);
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationFragment  Register: NetworkStateListener");
        mFragmentManager = getChildFragmentManager();
        if (mFragmentManager.getBackStackEntryCount() < 1) {
            loadFirstFragment();
        }
        mNetworkReceiver = new NetworkStateReceiver();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        mActivity = getActivity();
        super.onResume();
        networkUtility.registerNetworkListener(mNetworkReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        networkUtility.unRegisterNetworkListener(mNetworkReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
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
    }


    public boolean onBackPressed() {
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
            if (fragment instanceof AlmostDoneFragment) {
                ((AlmostDoneFragment) (fragment)).clearUserData();
            }

            if (fragment instanceof ForgotPasswordFragment) {
                ((ForgotPasswordFragment) (fragment)).backPressed();
            }
            trackHandler();
            try {
                currentFragment = mFragmentManager.getFragments().get(count - 1);
                mFragmentManager.popBackStack();
            } catch (IllegalStateException e) {
                /**
                 * Ignore - No way to avoid this if some action is performed
                 * and the fragment is put into background before that action is completed
                 * See defect - 92539
                 */

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
            String curPage;
            if (mFragmentManager.getFragments() != null) {
                Fragment preFragment = mFragmentManager.getFragments().get(count - 1);
                curPage = getTackingPageName(preFragment);
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

        } else if (fragment instanceof AlmostDoneFragment) {
            return AppTaggingPages.ALMOST_DONE;

        } else if (fragment instanceof MarketingAccountFragment) {
            return AppTaggingPages.MARKETING_OPT_IN;
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

    private void handleUserLoginStateFragments() {
        User mUser = new User(mActivity.getApplicationContext());
        boolean isUserSignIn = mUser.isUserSignIn();
        boolean isEmailVerified = (mUser.isEmailVerified() || mUser.isMobileVerified());
        boolean isEmailVerificationRequired = RegistrationConfiguration.
                getInstance().isEmailVerificationRequired();

        boolean isEmailVerifiedOrNotRequired = isEmailVerified || !isEmailVerificationRequired;

        if (isUserSignIn && isEmailVerifiedOrNotRequired && mRegistrationLaunchMode!=null) {

            if (RegistrationLaunchMode.MARKETING_OPT.equals(mRegistrationLaunchMode)) {
                launchMarketingAccountFragment();

            } else {
                launchMyAccountFragment();
            }

        }else {
            AppTagging.trackFirstPage(AppTaggingPages.HOME);
            replaceWithHomeFragment();
        }
    }

    private void launchMyAccountFragment() {
        UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_reg_fragment_container, userDetailsFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void userRegistrationComplete() {
        if (RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener() != null) {
            RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                    onUserRegistrationComplete(getParentActivity());
        } else {
            RegUtility.showErrorMessage(getParentActivity());
        }
    }

    private void launchMarketingAccountFragment() {
        AppTagging.trackFirstPage(AppTaggingPages.MARKETING_OPT_IN);
        replacMarketingAccountFragment();
    }

    private void trackPage(String currPage) {
        AppTagging.trackPage(currPage);
    }

    public void replaceWithHomeFragment() {
        try {
            if (null != mFragmentManager) {
                currentFragment = new HomeFragment();
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_reg_fragment_container, currentFragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }

    public boolean isHomeFragment() {
        if (currentFragment instanceof HomeFragment) {
            return true;
        }
        return false;
    }

    Fragment currentFragment;

    public void addFragment(Fragment fragment) {

                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
                fragmentTransaction.addToBackStack(fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
                currentFragment = fragment;
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


    private void replacMarketingAccountFragment() {
        try {
            MarketingAccountFragment marketingAccountFragment = new MarketingAccountFragment();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, marketingAccountFragment);
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

    public void addAlmostDoneFragmentforTermsAcceptance() {
        AlmostDoneFragment almostDoneFragment = new AlmostDoneFragment();
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


    private void setCounterState(boolean isCounting) {
        this.isCounterRunning = isCounting;
    }

    public boolean getCounterState() {
        return isCounterRunning;

    }

    @Override
    public boolean handleBackEvent() {
        return !(onBackPressed());
    }


    public void startCountDownTimer() {
        myCountDownTimer.start();
    }

    public void stopCountDownTimer() {
        myCountDownTimer.onFinish();
        myCountDownTimer.cancel();
    }

    @Override
    public void onCounterEventReceived(String event, long timeLeft) {
        if (event.equals(RegConstants.COUNTER_TICK)) {
            setCounterState(true);
        } else {
            setCounterState(false);
        }
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onTick(long timeLeft) {
            CounterHelper.getInstance().notifyCounterEventOccurred(RegConstants.COUNTER_TICK, timeLeft);
        }

        @Override
        public void onFinish() {
            CounterHelper.getInstance().notifyCounterEventOccurred(RegConstants.COUNTER_FINISH, 0);
            setCounterState(false);
        }
    }

    public View getNotificationContentView(String title, String message) {
        View view = View.inflate(getContext(), R.layout.reg_notification_bg_accent, null);
        ((TextView) view.findViewById(R.id.uid_notification_title)).setText(title);
        ((TextView) view.findViewById(R.id.uid_notification_content)).setText(message);
        view.findViewById(R.id.uid_notification_title).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_content).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_icon).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new NotificationBarHandler());
            }
        });
        return view;
    }

}

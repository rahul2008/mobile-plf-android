/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.view.inputmethod.*;
import android.widget.*;

import com.janrain.android.*;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.listener.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.social.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.dhpclient.BuildConfig;
import com.philips.platform.uappframework.listener.*;

import org.greenrobot.eventbus.*;
import org.json.*;

import javax.inject.*;


public class RegistrationFragment extends Fragment implements NetworkStateListener,
        OnClickListener, BackEventListener, CounterListener {

    @Inject
    NetworkUtility networkUtility;

    private FragmentManager mFragmentManager;

    private Activity mActivity;

    private ActionBarListener mActionBarListener;

    private RegistrationLaunchMode mRegistrationLaunchMode = RegistrationLaunchMode.DEFAULT;

    RegistrationContentConfiguration registrationContentConfiguration;
    Intent msgIntent;
    private static long RESEND_DISABLED_DURATION = 60 * 1000;
    private static final long INTERVAL = 1 * 1000;
    static public MyCountDownTimer myCountDownTimer;

    private int titleResourceID = -99;

    public UserRegistrationUIEventListener getUserRegistrationUIEventListener() {
        return userRegistrationUIEventListener;
    }

    public void setUserRegistrationUIEventListener(UserRegistrationUIEventListener
                                                           userRegistrationUIEventListener) {
        this.userRegistrationUIEventListener = userRegistrationUIEventListener;
    }

    private UserRegistrationUIEventListener userRegistrationUIEventListener;

    private NetworkStateReceiver mNetworkReceiver = new NetworkStateReceiver();

    private boolean isCounterRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onCreate");
        RLog.i(RLog.VERSION, "Jump Version :" + Jump.getJumpVersion());
        RLog.i(RLog.VERSION, "Registration Version :" +
                RegistrationHelper.getRegistrationApiVersion());
        RLog.i(RLog.VERSION, "HSDP Version :" + BuildConfig.VERSION_CODE);
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
        URInterface.getComponent().inject(this);
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.reg_fragment_registration, container, false);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationFragment  Register: NetworkStateListener");
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
        networkUtility.registerNetworkListener(mNetworkReceiver);
    }

    @Override
    public void onPause() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onPause");
        super.onPause();
        networkUtility.unRegisterNetworkListener(mNetworkReceiver);
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
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationFragment Unregister: NetworkStateListener,Context");
        RegistrationBaseFragment.mWidth = 0;
        RegistrationBaseFragment.mHeight = 0;
        setPrevTiltle();
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
            if (fragment instanceof AlmostDoneFragment) {
                ((AlmostDoneFragment) (fragment)).clearUserData();
            }

            if (fragment instanceof ForgotPasswordFragment) {
                ((ForgotPasswordFragment)(fragment)).backPressed();
            }
            trackHandler();
            try {
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

        if (RegistrationLaunchMode.MARKETING_OPT.equals(mRegistrationLaunchMode)) {
            if (isUserSignIn && isEmailVerified) {
                launchMarketingAccountFragment();
                return;
            }

            if (isUserSignIn && !isEmailVerificationRequired) {
                launchMarketingAccountFragment();
                return;
            }
            AppTagging.trackFirstPage(AppTaggingPages.HOME);
            replaceWithHomeFragment();
        } else {
            if (isUserSignIn && isEmailVerified) {
                userRegistrationComplete();
                return;
            }
            if (isUserSignIn && !isEmailVerificationRequired) {
                userRegistrationComplete();
                return;
            }
            AppTagging.trackFirstPage(AppTaggingPages.HOME);
            replaceWithHomeFragment();
        }
    }

    public void userRegistrationComplete() {
        if (getUserRegistrationUIEventListener() != null) {
            getUserRegistrationUIEventListener().
                    onUserRegistrationComplete(getParentActivity());
        }else {
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
            if (null != mFragmentManager) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
                fragmentTransaction.addToBackStack(fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
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

    public void addPlainAlmostDoneFragment() {
        AlmostDoneFragment almostDoneFragment = new AlmostDoneFragment();
        addFragment(almostDoneFragment);
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

    public View getNotificationContentView(String title, String message ) {
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

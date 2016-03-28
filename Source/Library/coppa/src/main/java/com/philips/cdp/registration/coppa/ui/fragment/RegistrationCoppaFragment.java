
package com.philips.cdp.registration.coppa.ui.fragment;

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
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaHelper;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.social.AlmostDoneFragment;
import com.philips.cdp.registration.ui.social.MergeAccountFragment;
import com.philips.cdp.registration.ui.social.MergeSocialToSocialAccountFragment;
import com.philips.cdp.registration.ui.traditional.AccountActivationFragment;
import com.philips.cdp.registration.ui.traditional.CreateAccountFragment;
import com.philips.cdp.registration.ui.traditional.ForgotPasswordFragment;
import com.philips.cdp.registration.ui.traditional.HomeFragment;
import com.philips.cdp.registration.ui.traditional.LogoutFragment;
import com.philips.cdp.registration.ui.traditional.PhilipsNewsFragment;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.traditional.SignInAccountFragment;
import com.philips.cdp.registration.ui.traditional.WelcomeFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.tagging.Tagging;
import com.philips.dhpclient.BuildConfig;

import org.json.JSONObject;

public class RegistrationCoppaFragment extends Fragment implements NetworStateListener, OnClickListener {


    private final String REGISTRATION_VERSION_TAG = "registrationVersion";

    private FragmentManager mFragmentManager;

    private Activity mActivity;

    private RegistrationTitleBarListener mRegistrationUpdateTitleListener;

    private int titleResourceID = -99;

    private boolean isAccountSettings = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onCreate");
        RLog.i(RLog.VERSION, "Jump Version :" + Jump.getJumpVersion());
        RLog.i(RLog.VERSION, "LocaleMatch Version :" + PILLocaleManager.getLacaleMatchVersion());
        RLog.i(RLog.VERSION, "Registration Version :" + RegistrationHelper.getRegistrationApiVersion());
        RLog.i(RLog.VERSION, "HSDP Version :" + BuildConfig.VERSION_CODE);
        Tagging.setComponentVersionKey(REGISTRATION_VERSION_TAG);
        Tagging.setComponentVersionVersionValue(RegistrationHelper.getRegistrationApiVersion());
        RegistrationCoppaBaseFragment.mWidth = 0;
        RegistrationCoppaBaseFragment.mHeight = 0;
        Bundle bunble = getArguments();
        if (bunble != null) {
            isAccountSettings = bunble.getBoolean(RegConstants.ACCOUNT_SETTINGS, true);
        }
        RLog.d("RegistrationCoppaFragment", "isAccountSettings : " + isAccountSettings);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaFragment  Register: NetworStateListener");
        mFragmentManager = getChildFragmentManager();
        loadFirstFragment();
        return view;
    }

    @Override
    public void onStart() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        mActivity = getActivity();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onResume");

        super.onResume();
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
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaFragment Unregister: NetworStateListener,Context");
        RegistrationCoppaBaseFragment.mWidth = 0;
        RegistrationCoppaBaseFragment.mHeight = 0;
        super.onDestroy();
    }

    public boolean onBackPressed() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationCoppaFragment : onBackPressed");
        hideKeyBoard();
        return handleBackStack();
    }

    private boolean handleBackStack() {
        int count = mFragmentManager.getBackStackEntryCount();

        RLog.i("Back count ", "" + count);

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


    public void loadFirstFragment() {
        try {

            replaceWithParentalAccess();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaFragment :FragmentTransaction Exception occured in loadFirstFragment  :"
                            + e.getMessage());
        }
    }

    private void replaceWithParentalAccess() {

        try {
            ParentalAccessFragment parentalAccessFragment = new ParentalAccessFragment();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, parentalAccessFragment, "Parental Access");
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }

    private void handleUserLoginStateFragments() {
        User mUser = new User(mActivity.getApplicationContext());

        //account setting true or no
        //if true follow bellow else cckech for sign in status and repave with wel come screen on sing els ehome
        if (isAccountSettings) {
            if (mUser.isUserSignIn() && mUser.getEmailVerificationStatus()) {
                AppTagging.trackFirstPage(AppTaggingPages.USER_PROFILE);
                replaceWithLogoutFragment();
                return;
            }

            if (mUser.isUserSignIn() && !RegistrationConfiguration.getInstance().getFlow().isEmailVerificationRequired()) {
                AppTagging.trackFirstPage(AppTaggingPages.USER_PROFILE);
                replaceWithLogoutFragment();
                return;
            }
            AppTagging.trackFirstPage(AppTaggingPages.HOME);
            replaceWithHomeFragment();
        } else {
            if (mUser.isUserSignIn() && mUser.getEmailVerificationStatus()) {
                AppTagging.trackFirstPage(AppTaggingPages.WELCOME);
                // replaceWithLogoutFragment();
                //replace with welcome
                replaceWithWelcomeFragment();
                return;
            }
            if (mUser.isUserSignIn() && !RegistrationConfiguration.getInstance().getFlow().isEmailVerificationRequired()) {
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
                    "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
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
                    "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
        hideKeyBoard();
    }

    public void replaceWelcomeFragmentOnLogin(Fragment fragment) {
        navigateToHome();
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
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
        replaceFragment(welcomeFragment);
    }

    public void replaceFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
        hideKeyBoard();
    }

    private void replaceWithWelcomeFragment() {
        try {
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, welcomeFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
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
                    "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
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
        MergeSocialToSocialAccountFragment mergeAccountFragment = new MergeSocialToSocialAccountFragment();
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
        InputMethodManager imm = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mActivity.getWindow() != null && mActivity.getWindow().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(mActivity.getWindow().getCurrentFocus().getWindowToken(), 0);
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
        if (!UserRegistrationInitializer.getInstance().isJanrainIntialized() && !UserRegistrationInitializer.getInstance().isJumpInitializationInProgress()) {
            RLog.d(RLog.NETWORK_STATE, "RegistrationCoppaFragment :onNetWorkStateReceived");
            RegistrationHelper registrationSettings = RegistrationHelper.getInstance();
            registrationSettings
                    .initializeUserRegistration(mActivity
                            .getApplicationContext(), RegistrationHelper.getInstance().getLocale(getContext()));
            RLog.d(RLog.JANRAIN_INITIALIZE,
                    "RegistrationCoppaFragment : Janrain reinitialization with locale : "
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

    public RegistrationTitleBarListener getUpdateTitleListener() {
        return mRegistrationUpdateTitleListener;
    }

    public void setOnUpdateTitleListener(RegistrationTitleBarListener listener) {
        this.mRegistrationUpdateTitleListener = listener;
    }

    public void setResourceID(int titleResourceId) {
        titleResourceID = titleResourceId;
    }

    public int getResourceID() {
        return titleResourceID;
    }

    public boolean isUserSignIn(Context context) {
        CaptureRecord captured = CaptureRecord.loadFromDisk(context);
        if (captured == null) {
            return false;
        }
        return true;
    }


    public void launchRegistrationFragment(boolean isAccountSettings) {
        try {
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(mRegistrationUpdateTitleListener);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_reg_fragment_container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }

    private static UserRegistrationListener mUserRegistrationListener = new UserRegistrationListener() {
        @Override
        public void onUserRegistrationComplete(Activity activity) {
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(activity);
            }
        }

        @Override
        public void onPrivacyPolicyClick(Activity activity) {
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyOnPrivacyPolicyClickEventOccurred(activity);
            }
        }

        @Override
        public void onTermsAndConditionClick(Activity activity) {
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyOnTermsAndConditionClickEventOccurred(activity);
            }
        }

        @Override
        public void onUserLogoutSuccess() {
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyOnUserLogoutSuccess();
            }
        }

        @Override
        public void onUserLogoutFailure() {
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyOnUserLogoutFailure();
            }
        }

        @Override
        public void onUserLogoutSuccessWithInvalidAccessToken() {
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyOnLogoutSuccessWithInvalidAccessToken();
            }
        }
    };

    public static UserRegistrationListener getUserRegistrationListener() {
        return mUserRegistrationListener;
    }

}

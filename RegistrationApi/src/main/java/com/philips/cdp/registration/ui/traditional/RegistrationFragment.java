
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
import com.philips.cdp.registration.AppTagging.AppTaggingPages;
import com.philips.cdp.registration.AppTagging.AppTagging;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.social.AlmostDoneFragment;
import com.philips.cdp.registration.ui.social.MergeAccountFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONObject;

public class RegistrationFragment extends Fragment implements NetworStateListener, OnClickListener {

    private FragmentManager mFragmentManager;

    private final boolean VERIFICATION_SUCCESS = true;

    private Activity mActivity;

    private RegistrationTitleBarListener mRegistrationUpdateTitleListener;

    private int titleResourceID = -99;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onCreate");
        RLog.i(RLog.VERSION, "Jump Version :" + Jump.getJumpVersion());
        RLog.i(RLog.VERSION, "LocaleMatch Version :" + PILLocaleManager.getLacaleMatchVersion());
        RLog.i(RLog.VERSION, "Registration Version :" + RegistrationHelper.getRegistrationApiVersion());
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationFragment  Register: NetworStateListener");
        mFragmentManager = getChildFragmentManager();
        loadFirstFragment();
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
        RegistrationHelper.getInstance().unregisterListener(mActivity.getApplicationContext());
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationFragment Unregister: NetworStateListener,Context");
        super.onDestroy();
    }

    public boolean onBackPressed() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationFragment : onBackPressed");
        hideKeyBoard();
        return handleBackStack();
    }

    private boolean handleBackStack() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count == 0) {
            return true;
        }
        Fragment fragment = mFragmentManager.getFragments().get(count);
        if (fragment instanceof WelcomeFragment) {
            navigateToHome();
            trackPage(AppTaggingPages.HOME);
        } else {
            trackHandler();
            mFragmentManager.popBackStack();
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
            handleUserLoginStateFragments();

        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationFragment :FragmentTransaction Exception occured in loadFirstFragment  :"
                            + e.getMessage());
        }
    }

    private void handleUserLoginStateFragments() {
        User mUser = new User(mActivity.getApplicationContext());
        if (mUser.getEmailVerificationStatus(mActivity.getApplicationContext())) {
            AppTagging.trackFirstPage(AppTaggingPages.USER_PROFILE);
            replaceWithWelcomeFragment();
            return;
        }
        AppTagging.trackFirstPage(AppTaggingPages.HOME);
        replaceWithHomeFragment();
    }

   private void trackPage(String currPage) {
        AppTagging.trackPage(currPage);
    }

    public void replaceWithHomeFragment() {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, new HomeFragment());
            fragmentTransaction.commitAllowingStateLoss();
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
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
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
        for (int i = fragmentCount; i >= 0; i--) {
            fragmentManager.popBackStack();
        }
    }

    public void addWelcomeFragmentOnVerification() {
        navigateToHome();
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        Bundle welcomeFragmentBundle = new Bundle();
        welcomeFragmentBundle.putBoolean(RegConstants.VERIFICATIN_SUCCESS, VERIFICATION_SUCCESS);
        welcomeFragment.setArguments(welcomeFragmentBundle);
        replaceFragment(welcomeFragment);
    }

    public void replaceFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, fragment, fragment.getTag());
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
            Bundle welcomeFragmentBundle = new Bundle();
            welcomeFragmentBundle
                    .putBoolean(RegConstants.VERIFICATIN_SUCCESS, VERIFICATION_SUCCESS);
            welcomeFragmentBundle.putBoolean(RegConstants.IS_FROM_BEGINING, true);
            welcomeFragment.setArguments(welcomeFragmentBundle);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, welcomeFragment);
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
        socialAlmostDoneFragment.setArguments(socialAlmostDoneFragmentBundle);
        addFragment(socialAlmostDoneFragment);
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
        if (!RegistrationHelper.getInstance().isJanrainIntialized()) {
            RLog.d(RLog.NETWORK_STATE, "RegistrationFragment :onNetWorkStateReceived");
            RegistrationHelper registrationSettings = RegistrationHelper.getInstance();
            registrationSettings
                    .intializeRegistrationSettings(mActivity
                            .getApplicationContext(), RegistrationHelper.getInstance().getLocale());
            RLog.d(RLog.JANRAIN_INITIALIZE,
                    "RegistrationFragment : Janrain reinitialization with locale : "
                            + RegistrationHelper.getInstance().getLocale());
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

}

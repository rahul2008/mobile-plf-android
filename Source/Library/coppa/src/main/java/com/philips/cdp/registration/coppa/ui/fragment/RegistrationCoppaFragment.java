
package com.philips.cdp.registration.coppa.ui.fragment;

import android.app.Activity;
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
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaHelper;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.tagging.Tagging;
import com.philips.dhpclient.BuildConfig;

public class RegistrationCoppaFragment extends Fragment implements NetworStateListener, OnClickListener {


    private final String REGISTRATION_VERSION_TAG = "registrationVersion";

    public static FragmentManager mFragmentManager;

    private Activity mActivity;

    private RegistrationTitleBarListener mRegistrationUpdateTitleListener;

    private int titleResourceID = -99;

    private boolean isAccountSettings = true;

    private static int lastKnownResourceId = -99;



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
        lastKnownResourceId = -99;
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
        mFragmentManager = getChildFragmentManager();
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
        return handleBackStack();
    }

    private boolean handleBackStack() {
        int count = mFragmentManager.getBackStackEntryCount();
        RLog.i("Back count ", "" + count);
        if (count == 0) {
            return false;
        }
        Fragment fragment = mFragmentManager.getFragments().get(count);
        if (fragment != null && fragment instanceof RegistrationFragment) {
            boolean isRegFragHandledBack = ((RegistrationFragment) fragment).onBackPressed();
            //true  not consumed
            //false  consumed
            if (isRegFragHandledBack) {
                //Message for killing fragmet app but by pass this condition not
                if (count >= 1) {
                    mFragmentManager.popBackStack();
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            mFragmentManager.popBackStack();
        }
        return true;
    }


    public void loadFirstFragment() {

        User user = new User(mActivity);
        if (user.isUserSignIn()) {
            launchRegistrationFragmentOnLoggedIn(isAccountSettings);
        } else {
            replaceWithParentalAccess();
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


    public void addParentalConfirmFragment() {
        ParentalAccessConfirmFragment parentalAccessConfirmFragment = new ParentalAccessConfirmFragment();
        addFragment(parentalAccessConfirmFragment);
    }


    private void addFragment(Fragment fragment) {
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
                            .getApplicationContext());
            RLog.d(RLog.JANRAIN_INITIALIZE,
                    "RegistrationCoppaFragment : Janrain reinitialization with locale : "
                            + RegistrationHelper.getInstance().getLocale(getContext()));
        }
    }

    public Activity getParentActivity() {
        return mActivity;
    }

    public int getFragmentBackStackCount() {
        return  mFragmentManager.getBackStackEntryCount();
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


    public void launchRegistrationFragment() {
        try {
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
            registrationFragment.setPreviousResourceId(titleResourceID);
            registrationFragment.setOnUpdateTitleListener(new RegistrationTitleBarListener() {
                @Override
                public void updateRegistrationTitle(int titleResourceID) {
                    lastKnownResourceId =titleResourceID;
                    mRegistrationUpdateTitleListener.updateRegistrationTitleWithBack(titleResourceID);
                }

                @Override
                public void updateRegistrationTitleWithBack(int titleResourceID) {
                    lastKnownResourceId =titleResourceID;
                    mRegistrationUpdateTitleListener.updateRegistrationTitleWithBack(titleResourceID);
                }

                @Override
                public void updateRegistrationTitleWithOutBack(int titleResourceID) {
                    lastKnownResourceId =titleResourceID;
                    mRegistrationUpdateTitleListener.updateRegistrationTitleWithBack(titleResourceID);

                }
            });
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(registrationFragment.getTag());
            fragmentTransaction.add(R.id.fl_reg_fragment_container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }


    private void launchRegistrationFragmentOnLoggedIn(boolean isAccountSettings) {
        try {
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(new RegistrationTitleBarListener() {
                @Override
                public void updateRegistrationTitle(int titleResourceID) {
                    lastKnownResourceId =titleResourceID;
                    mRegistrationUpdateTitleListener.updateRegistrationTitle(titleResourceID);
                }

                @Override
                public void updateRegistrationTitleWithBack(int titleResourceID) {
                    lastKnownResourceId =titleResourceID;
                    mRegistrationUpdateTitleListener.updateRegistrationTitleWithBack(titleResourceID);
                }

                @Override
                public void updateRegistrationTitleWithOutBack(int titleResourceID) {
                    lastKnownResourceId =titleResourceID;
                    mRegistrationUpdateTitleListener.updateRegistrationTitleWithBack(titleResourceID);

                }
            });
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }


    private static void addParentalApprovalFragment() {
        //update coppa with prev as registration and adding on top

        if (mFragmentManager != null) {
            try {
                ParentalApprovalFragment parentalAccessFragment = new ParentalApprovalFragment();
                int count = mFragmentManager.getBackStackEntryCount();
                RegistrationFragment registrationFragment = null;
                if (count != 0 && registrationFragment instanceof RegistrationFragment) {
                   registrationFragment =(RegistrationFragment)mFragmentManager.getFragments().get(count);
                }
                if(registrationFragment!=null) {
                    parentalAccessFragment.setPrevTitleResourceId(lastKnownResourceId);
                }
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fl_reg_fragment_container, parentalAccessFragment, "Parental Access");
                fragmentTransaction.addToBackStack(parentalAccessFragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                RLog.e(RLog.EXCEPTION,
                        "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
                                + e.getMessage());
            }
        }
    }

    private static UserRegistrationListener mUserRegistrationListener = new UserRegistrationListener() {
        @Override
        public void onUserRegistrationComplete(Activity activity) {
            //Launch the Approval fragment
            addParentalApprovalFragment();
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

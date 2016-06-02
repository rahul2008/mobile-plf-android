package com.philips.cdp.prodreg.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.prodreg.ui.FragmentLauncher;
import com.philips.cdp.prodreg.util.ProdRegConstants;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class ProdRegBaseFragment extends Fragment {

    private static String TAG = InitialFragment.class.getSimpleName();
    private static int mContainerId = 0;
    private static int mEnterAnimation = 0;
    private static int mExitAnimation = 0;
    private static FragmentActivity mFragmentActivityContext = null;
    private static FragmentActivity mActivityContext = null;
    Button activity, fragment;
    private FragmentManager fragmentManager;
    private ActionbarUpdateListener mActionbarUpdateListener;
    private FragmentLauncher mFragmentLauncher;
    private ImageView mBackToHome = null;
    private ImageView mHomeIcon = null;

    public abstract String getActionbarTitle();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        Log.d(getClass() + "", "onCreate called");
        View view = inflater.inflate(R.layout.initial_fragment, container, false);
        mFragmentActivityContext = getActivity();
        activity = (Button) view.findViewById(R.id.activity);
        fragment = (Button) view.findViewById(R.id.fragment);
        activity.setOnClickListener(onClickActivity());
        fragment.setOnClickListener(onClickFragment());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionbarTitle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideKeyboard();
    }

    public View.OnClickListener onClickActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                RegistrationLaunchHelper.launchDefaultRegistrationActivity(getActivity());
            }
        };
    }

    public View.OnClickListener onClickFragment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                launchRegistrationFragment(R.id.parent_layout, getActivity(), false);
            }
        };
    }

    private void launchRegistrationFragment(int container, FragmentActivity
            fragmentActivity, boolean isAccountSettings) {
        try {
            FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(new RegistrationTitleBarListener() {
                @Override
                public void updateRegistrationTitle(final int i) {

                }

                @Override
                public void updateRegistrationTitleWithBack(final int i) {

                }

                @Override
                public void updateRegistrationTitleWithOutBack(final int i) {

                }
            });
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void showFragment(/*FragmentActivity context, int parentContainer,*/
                             Fragment fragment, FragmentLauncher fragmentLauncher,/*ActionbarUpdateListener actionbarUpdateListener,*/
                             int startAnimation, int endAnimation) {
        Log.i("testing", "DigitalCare Base Fragment -- Fragment Invoke");
        mFragmentLauncher = fragmentLauncher;
        mContainerId = fragmentLauncher.getParentContainerResourceID();
        mActivityContext = fragmentLauncher.getFragmentActivity();
        mActionbarUpdateListener = fragmentLauncher.getActionbarUpdateListener();

        String startAnim = null;
        String endAnim = null;

        if ((startAnimation != 0) && (endAnimation != 0)) {
            startAnim = mActivityContext.getResources().getResourceName(startAnimation);
            endAnim = mActivityContext.getResources().getResourceName(endAnimation);

            String packageName = mActivityContext.getPackageName();
            mEnterAnimation = mActivityContext.getResources().getIdentifier(startAnim,
                    "anim", packageName);
            mExitAnimation = mActivityContext.getResources().getIdentifier(endAnim, "anim",
                    packageName);
        }

        try {
            FragmentTransaction fragmentTransaction = mActivityContext
                    .getSupportFragmentManager().beginTransaction();
            if (mEnterAnimation != 0 && mExitAnimation != 0) {
                fragmentTransaction.setCustomAnimations(mEnterAnimation,
                        mExitAnimation, mEnterAnimation, mExitAnimation);
            }
            fragmentTransaction.replace(mContainerId, fragment, ProdRegConstants.PROD_REG_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    protected void showFragment(Fragment fragment) {
        int containerId = R.id.mainContainer;

        if (mContainerId != 0) {
            containerId = mContainerId;
            mFragmentActivityContext = mActivityContext;
        } else {
            enableActionBarLeftArrow();
            InputMethodManager imm = (InputMethodManager) mFragmentActivityContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mFragmentActivityContext.getWindow() != null
                    && mFragmentActivityContext.getWindow().getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(mFragmentActivityContext
                        .getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        try {
            FragmentTransaction fragmentTransaction = mFragmentActivityContext
                    .getSupportFragmentManager().beginTransaction();
            if (mEnterAnimation != 0 && mExitAnimation != 0) {
                fragmentTransaction.setCustomAnimations(mEnterAnimation,
                        mExitAnimation, mEnterAnimation, mExitAnimation);
            }
            fragmentTransaction.replace(containerId, fragment, ProdRegConstants.PROD_REG_FRAGMENT_TAG);
            fragmentTransaction.hide(this);
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void enableActionBarLeftArrow(ImageView hambergermenu, ImageView backarrow) {
        Log.d(TAG, "BackArrow Enabled");
        if (hambergermenu != null && backarrow != null) {
            backarrow.setVisibility(View.VISIBLE);
            backarrow.bringToFront();
        }
    }

    protected void hideActionBarIcons(ImageView hambergermenu, ImageView backarrow) {
        Log.d(TAG, "Hide menu & arrow icons");
        if (hambergermenu != null && backarrow != null) {
            hambergermenu.setVisibility(View.GONE);
            backarrow.setVisibility(View.GONE);
        }
    }

    protected boolean backStackFragment() {
        if (fragmentManager == null && mActivityContext != null) {
            fragmentManager = mActivityContext.getSupportFragmentManager();
        } else if (fragmentManager == null) {
            fragmentManager = mFragmentActivityContext.getSupportFragmentManager();
        }
        fragmentManager.popBackStack();
        return false;
    }

    protected void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Updating action bar title. The text has to be updated at each fragment
     * seletion/creation.
     */
    private void setActionbarTitle() {
        if (mContainerId == 0) {
            ((TextView) getActivity().findViewById(
                    R.id.action_bar_title)).setText(getActionbarTitle());
        } else {
            updateActionbar();
        }
    }

    private void updateActionbar() {
        if (this.getClass().getSimpleName()
                .equalsIgnoreCase(InitialFragment.class.getSimpleName())) {
            mActionbarUpdateListener.updateActionbar(getActionbarTitle(), true);
        } else {
            mActionbarUpdateListener.updateActionbar(getActionbarTitle(), false);
        }
    }

    private void enableActionBarLeftArrow() {
        mBackToHome = (ImageView) mFragmentActivityContext
                .findViewById(R.id.back_to_home_img);
        mHomeIcon = (ImageView) mFragmentActivityContext
                .findViewById(R.id.home_icon);
        mHomeIcon.setVisibility(View.GONE);
        mBackToHome.setVisibility(View.VISIBLE);
        mBackToHome.bringToFront();
    }
}


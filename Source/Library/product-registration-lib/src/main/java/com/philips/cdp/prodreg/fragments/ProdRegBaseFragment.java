package com.philips.cdp.prodreg.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.alert.ModalAlertDemoFragment;
import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.prodreg.listener.ProdRegBackListener;
import com.philips.cdp.prodreg.util.ProdRegConstants;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class ProdRegBaseFragment extends Fragment implements ProdRegBackListener {

    private static String TAG = ProdRegBaseFragment.class.getSimpleName();
    private static ActionbarUpdateListener mActionbarUpdateListener;
    private int mEnterAnimation = 0;
    private int mExitAnimation = 0;

    public abstract String getActionbarTitle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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

    public void showFragment(Fragment fragment, FragmentLauncher fragmentLauncher,
                             int startAnimation, int endAnimation) {
        Log.i("testing", "Product Registration Base Fragment -- Fragment Invoke");
        final FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
        mActionbarUpdateListener = fragmentLauncher.getActionbarUpdateListener();
        int containerId = fragmentLauncher.getParentContainerResourceID();
        String startAnim;
        String endAnim;
        if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
            if ((startAnimation != 0) && (endAnimation != 0)) {
                startAnim = fragmentActivity.getResources().getResourceName(startAnimation);
                endAnim = fragmentActivity.getResources().getResourceName(endAnimation);

                String packageName = fragmentActivity.getPackageName();
                mEnterAnimation = fragmentActivity.getResources().getIdentifier(startAnim,
                        "anim", packageName);
                mExitAnimation = fragmentActivity.getResources().getIdentifier(endAnim, "anim",
                        packageName);
            }

            try {
                FragmentTransaction fragmentTransaction = fragmentActivity
                        .getSupportFragmentManager().beginTransaction();
                if (mEnterAnimation != 0 && mExitAnimation != 0) {
                    fragmentTransaction.setCustomAnimations(mEnterAnimation,
                            mExitAnimation, mEnterAnimation, mExitAnimation);
                }
                fragmentTransaction.replace(containerId, fragment, fragment.getClass().getSimpleName());
                Fragment currentFrag = fragmentActivity.getSupportFragmentManager()
                        .findFragmentById(getId());

                if (!(currentFrag instanceof ProdRegBaseFragment))
                    fragmentTransaction.addToBackStack(ProdRegConstants.PROD_REG_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    protected void showFragment(Fragment fragment) {
        try {
            final FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
                FragmentTransaction fragmentTransaction = fragmentActivity
                        .getSupportFragmentManager().beginTransaction();
                if (mEnterAnimation != 0 && mExitAnimation != 0) {
                    fragmentTransaction.setCustomAnimations(mEnterAnimation,
                            mExitAnimation, mEnterAnimation, mExitAnimation);
                }
                fragmentTransaction.replace(getId(), fragment, fragment.getClass().getSimpleName());
                Fragment currentFrag = fragmentActivity.getSupportFragmentManager()
                        .findFragmentById(getId());
                if (!(currentFrag instanceof ProdRegBaseFragment))
                    fragmentTransaction.addToBackStack(ProdRegConstants.PROD_REG_TAG);
                else {
                    fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                }
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
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
        updateActionbar();
    }

    private void updateActionbar() {
        mActionbarUpdateListener.updateActionbar(getActionbarTitle());
    }

    protected void showAlert(final String title, final String description) {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            final ModalAlertDemoFragment modalAlertDemoFragment = new ModalAlertDemoFragment();
            modalAlertDemoFragment.show(activity.getSupportFragmentManager(), "dialog");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    modalAlertDemoFragment.setTitle(title);
                    modalAlertDemoFragment.setDescription(description);
                }
            }, 200);
        }
    }

    public boolean clearFragmentStack() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            if (activity instanceof ProdRegBaseActivity) {
                activity.finish();
            } else {
                FragmentManager fragManager = activity.getSupportFragmentManager();
                return fragManager.popBackStackImmediate(ProdRegConstants.PROD_REG_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return false;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}


/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.error.ErrorHandler;
import com.philips.cdp.prodreg.error.ProdRegErrorMap;
import com.philips.cdp.prodreg.launcher.ProdRegUiHelper;
import com.philips.cdp.prodreg.listener.DialogOkButtonListener;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.List;

abstract class ProdRegBaseFragment extends Fragment implements BackEventListener {

    private static String TAG = ProdRegBaseFragment.class.getSimpleName();
    private static ActionBarListener mActionbarUpdateListener;
    private int mEnterAnimation = 0;
    private int mExitAnimation = 0;

    public abstract int getActionbarTitleResId();

    public abstract String getActionbarTitle();

    public abstract boolean getBackButtonState();

    public abstract List<RegisteredProduct> getRegisteredProducts();

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

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(getActionbarTitleResId());
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
    }

    public void showFragment(Fragment fragment, FragmentLauncher fragmentLauncher,
                             int startAnimation, int endAnimation) {
        ProdRegLogger.i(TAG, "Product Registration Base Fragment -- Fragment Invoke");
        final FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
        mActionbarUpdateListener = fragmentLauncher.getActionbarListener();
        int containerId = fragmentLauncher.getParentContainerResourceID();
        if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
            initAnimation(startAnimation, endAnimation, fragmentActivity);
            addFragment(fragment, fragmentActivity, containerId);
        }
    }

    protected void showFragment(Fragment fragment) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
            addFragment(fragment, fragmentActivity, getId());
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
        if (mActionbarUpdateListener != null) {
            mActionbarUpdateListener.updateActionBar(getActionbarTitleResId(), getBackButtonState());
            mActionbarUpdateListener.updateActionBar(getActionbarTitle(), getBackButtonState());
        }
    }

    protected void handleCallBack(final boolean onBack) {
        final ProdRegUiListener prodRegUiListener = ProdRegUiHelper.getInstance().getProdRegUiListener();
        final UserWithProducts signedInUserWithProducts = new ProdRegHelper().getSignedInUserWithProducts();
        if (onBack && prodRegUiListener != null)
            prodRegUiListener.onProdRegBack(getRegisteredProducts(), signedInUserWithProducts);
        else if (prodRegUiListener != null)
            prodRegUiListener.onProdRegContinue(getRegisteredProducts(), signedInUserWithProducts);
    }

    private void addFragment(final Fragment fragment, final FragmentActivity fragmentActivity, final int containerId) {
        try {
            FragmentTransaction fragmentTransaction = fragmentActivity
                    .getSupportFragmentManager().beginTransaction();
            if (mEnterAnimation != 0 && mExitAnimation != 0) {
                fragmentTransaction.setCustomAnimations(mEnterAnimation,
                        mExitAnimation, mEnterAnimation, mExitAnimation);
            }
            final String simpleName = fragment.getClass().getSimpleName();
            fragmentTransaction.replace(containerId, fragment, simpleName);
            Fragment currentFrag = fragmentActivity.getSupportFragmentManager()
                    .findFragmentById(getId());

            if (!(currentFrag instanceof ProdRegBaseFragment))
                fragmentTransaction.addToBackStack(ProdRegConstants.PROD_REG_VERTICAL_TAG);
            else
                fragmentTransaction.addToBackStack(simpleName);

            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
    }

    private void initAnimation(final int startAnimation, final int endAnimation, final FragmentActivity fragmentActivity) {
        final String startAnim;
        final String endAnim;
        if ((startAnimation != 0) && (endAnimation != 0)) {
            startAnim = fragmentActivity.getResources().getResourceName(startAnimation);
            endAnim = fragmentActivity.getResources().getResourceName(endAnimation);

            String packageName = fragmentActivity.getPackageName();
            mEnterAnimation = fragmentActivity.getResources().getIdentifier(startAnim,
                    "anim", packageName);
            mExitAnimation = fragmentActivity.getResources().getIdentifier(endAnim, "anim",
                    packageName);
        }
    }

    protected void showAlertOnError(final int statusCode) {
        try {
            final FragmentActivity activity = getActivity();
            if (activity != null && !activity.isFinishing()) {
                final ProdRegErrorMap prodRegErrorMap = new ErrorHandler().getError(activity, statusCode);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("error_dialog");
                if (prev != null) {
                    ft.remove(prev);
                    ft.commitAllowingStateLoss();
                }
                // Create and show the dialog.
                ProdRegErrorAlertFragment newFragment = ProdRegErrorAlertFragment.newInstance(prodRegErrorMap.getTitle(), prodRegErrorMap.getDescription());
                newFragment.setDialogOkButtonListener(getDialogOkButtonListener());
                newFragment.show(getActivity().getSupportFragmentManager(), "error_dialog");
            }
        } catch (IllegalStateException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
    }

    /**
     *
     */
    protected void resetErrorDialogIfExists() {
        Fragment prev = getFragmentManager().findFragmentByTag("error_dialog");
        if (prev != null && prev instanceof ProdRegErrorAlertFragment) {
            ((ProdRegErrorAlertFragment) prev).setDialogOkButtonListener(getDialogOkButtonListener());
        }
    }

    protected void dismissAlertOnError() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("error_dialog");
            if (prev instanceof ProdRegErrorAlertFragment) {
                ((ProdRegErrorAlertFragment) prev).dismiss();
            }
        }
    }

    public boolean clearFragmentStack() {
        final FragmentActivity activity = getActivity();
        try {
            if (activity != null && !activity.isFinishing()) {
                if (activity instanceof ProdRegBaseActivity) {
                    activity.finish();
                } else {
                    FragmentManager fragManager = activity.getSupportFragmentManager();
                    return fragManager.popBackStackImmediate(ProdRegConstants.PROD_REG_VERTICAL_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } catch (IllegalStateException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public DialogOkButtonListener getDialogOkButtonListener() {
        return null;
    }
}


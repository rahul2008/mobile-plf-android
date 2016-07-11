package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.listener.DialogOkButtonListener;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.register.ProdRegProcessController;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegProcessFragment extends ProdRegBaseFragment implements ProdRegProcessController.ProcessControllerCallBacks {

    public static final String TAG = ProdRegProcessFragment.class.getName();
    private ProdRegProcessController prodRegProcessController;

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        final FragmentActivity activity = getActivity();
        prodRegProcessController = new ProdRegProcessController(this, activity);
        if (savedInstanceState == null) {
            final ProdRegCache prodRegCache = new ProdRegCache(activity);
            ProdRegUtil.storeProdRegTaggingMeasuresCount(prodRegCache, ProdRegConstants.Product_REGISTRATION_SCAN_COUNT, 1);
            ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegProcessScreen", "noOfScannedProducts", String.valueOf(prodRegCache.getIntData(ProdRegConstants.Product_REGISTRATION_SCAN_COUNT)));

            showLoadingDialog();
        } else {
            prodRegProcessController.setLaunchedRegistration(savedInstanceState.getBoolean(ProdRegConstants.IS_SIGN_IN_CALLED, false));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        resetErrorDialogIfExists();
        final Bundle arguments = getArguments();
        prodRegProcessController.process(arguments);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean(ProdRegConstants.PROGRESS_STATE, prodRegProcessController.isApiCallingProgress());
        outState.putBoolean(ProdRegConstants.IS_SIGN_IN_CALLED, prodRegProcessController.isLaunchedRegistration());
        super.onSaveInstanceState(outState);
    }

    @Override
    public DialogOkButtonListener getDialogOkButtonListener() {
        return new DialogOkButtonListener() {
            @Override
            public void onOkButtonPressed() {
                dismissAlertOnError();
                final FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing()) {
                    clearFragmentStack();
                }
            }
        };
    }

    @Override
    public void exitProductRegistration() {
        clearFragmentStack();
    }

    @Override
    public void showAlertOnError(int responseCode) {
        super.showAlertOnError(responseCode);
    }

    @Override
    public void showFragment(Fragment fragment) {
        super.showFragment(fragment);
    }

    @Override
    public void showLoadingDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
            ft.commitAllowingStateLoss();
        }
        DialogFragment newFragment = ProdRegLoadingFragment.newInstance(getString(R.string.PPR_Looking_For_Products_Lbltxt));
        newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    @Override
    public void dismissLoadingDialog() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev instanceof DialogFragment) {
                ((DialogFragment) prev).dismissAllowingStateLoss();
            }
        }
    }
}

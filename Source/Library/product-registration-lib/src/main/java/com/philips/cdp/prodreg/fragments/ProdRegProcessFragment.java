/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.listener.DialogOkButtonListener;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.register.ProdRegProcessController;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.AnalyticsConstants;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.product_registration_lib.R;

import java.util.List;

public class ProdRegProcessFragment extends ProdRegBaseFragment implements ProdRegProcessController.ProcessControllerCallBacks {

    public static final String TAG = ProdRegProcessFragment.class.getName();
    private ProdRegProcessController prodRegProcessController;
    private boolean isFailedOnError = false;
    private int responseCode;
    private boolean isFirstLaunch;
    private int resId;

    @Override
    public int getActionbarTitleResId() {
        return R.string.PPR_NavBar_Title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public boolean getBackButtonState() {
        return isFirstLaunch;
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return prodRegProcessController.getRegisteredProductsList();
    }

    @Override
    public void setImageBackground() {
        if (getView() != null) {
            //TODO getView().setBackgroundResource(resId);
        }
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            isFirstLaunch = arguments.getBoolean(ProdRegConstants.PROD_REG_IS_FIRST_LAUNCH);
            resId = arguments.getInt(ProdRegConstants.PROD_REG_FIRST_IMAGE_ID);
        }
        setRetainInstance(true);
        final FragmentActivity activity = getActivity();
        prodRegProcessController = new ProdRegProcessController(this, activity);
        if (savedInstanceState == null) {
            final ProdRegCache prodRegCache = new ProdRegCache();
            new ProdRegUtil().storeProdRegTaggingMeasuresCount(prodRegCache, AnalyticsConstants.Product_REGISTRATION_SCAN_COUNT, 1);
            ProdRegTagging.getInstance().trackPage("ProdRegProcessScreen", "noOfScannedProducts", String.valueOf(prodRegCache.getIntData(AnalyticsConstants.Product_REGISTRATION_SCAN_COUNT)));
            showLoadingDialog();
        } else {
            prodRegProcessController.setLaunchedRegistration(savedInstanceState.getBoolean(ProdRegConstants.IS_SIGN_IN_CALLED, false));
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_activity, container, false);
        return view;
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
                    PRUiHelper.getInstance().getProdRegUiListener().onProdRegFailed(ProdRegError.fromId(responseCode));
                    if (activity instanceof ProdRegBaseActivity) {
                        getActivity().finish();
                    }
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
        this.responseCode = responseCode;
        isFailedOnError = true;
    }

    @Override
    protected void resetErrorDialogIfExists() {
        Fragment prev = getFragmentManager().findFragmentByTag("error_dialog");
        if (prev != null && prev instanceof ProdRegErrorAlertFragment) {
            ((ProdRegErrorAlertFragment) prev).setDialogOkButtonListener(getDialogOkButtonListener());
        } else if (isFailedOnError) {
            showAlertOnError(responseCode);
        }
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

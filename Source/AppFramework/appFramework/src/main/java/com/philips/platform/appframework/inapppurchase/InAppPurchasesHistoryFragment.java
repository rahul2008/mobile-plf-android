/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.inapppurchase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.actionlayout.IAPActionLayout;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.R;

import java.util.ArrayList;

public class InAppPurchasesHistoryFragment extends AppFrameworkBaseFragment {

    private View mRootView;
    private int mInitFragmentBackStackCount = 0;
    private IAPHandler mIapHandler;
    private IAPActionLayout mLayout;
    private IAPSettings mIapSettings;

    private View mCustomView;

    private String mCtn = "HX6064/33" /*"UK-HX6064/33"*/;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int position = 0;
        fragmentPresenter = new InAppPurchaseHistoryFragmentPresenter();
        hideActionbar();
        mRootView = inflater.inflate(R.layout.af_inapppurchase_history_fragment, container, false);
        Bundle bundle = this.getArguments();
        if (null != bundle) {
            position = bundle.getInt("catagoryNumber");
            mCtn = bundle.getString("ctn");
        }

        mInitFragmentBackStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();

        mLayout = new IAPActionLayout(getContext(), getActivity().getSupportFragmentManager());
        mCustomView = mLayout.getCustomView(getContext());
        ((ViewGroup) mRootView.findViewById(R.id.ll_custom_action_history)).addView(mCustomView);
        ((TextView) mCustomView.findViewById(R.id.iap_header_title)).setText("Shopping List");
        mCustomView.findViewById(R.id.iap_header_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        launchInAppPurchases(/*position, ctn*/);

        return mRootView;
    }

    @Override
    public String getActionbarTitle() {
        return "Shopping";
    }

    public void launchInAppPurchases(/*int position, String ctn*/) {
        ArrayList<String> ctnList;
        if (NetworkUtility.getInstance().isNetworkAvailable(getContext())) {
            String countryCode = getResources().getString(R.string.af_country);
            String languageCode = getResources().getString(R.string.af_language);

            try {
                mIapSettings = new IAPSettings(countryCode, languageCode, R.style.Theme_Philips_DarkBlue_Gradient_WhiteBackground);
                mIapSettings.setUseLocalData(false);
                mIapSettings.setLaunchAsFragment(true);
                mIapSettings.setFragProperties(getFragmentManager(), R.id.vertical_Container_history);
                mIapHandler = IAPHandler.init(getContext(), mIapSettings);
                mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PURCHASE_HISTORY_VIEW, null, null);
            } catch (IllegalArgumentException e) {

            }
        } else {
            showIAPToast(IAPConstant.IAP_ERROR_NO_CONNECTION);
        }
    }

    private void showIAPToast(int errorCode) {
        Context context = getContext();
        String errorText = context.getResources().getString(R.string.iap_unknown_error);
        if (IAPConstant.IAP_ERROR_NO_CONNECTION == errorCode) {
        } else if (IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT == errorCode) {
        } else if (IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE == errorCode) {
        } else if (IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR == errorCode) {
        }

        if (null != context) {
            Toast toast = Toast.makeText(context, errorText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onBackPressed() {
        hideKeyboard();
        boolean result = mLayout.onHWBackPressed();

        if ((getFragmentManager().getBackStackEntryCount() - mInitFragmentBackStackCount) == 2) {
            getFragmentManager().popBackStackImmediate();
        }
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showActionbar();
    }

    private void showActionbar() {
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.show();
    }

    private void hideActionbar() {
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.hide();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
    }
}
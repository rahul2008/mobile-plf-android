package com.philips.cdp.di.iapdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Apple on 27/06/16.
 */
public class InAppPurchaseHostFragment extends Fragment {
    private View mRootView;

    private int mInitFragmentBackStackCount = 0;

    private IAPHandler mIapHandler;

    private IAPActionLayout mLayout;

    private IAPSettings mIapSettings;

    private View mCustomView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_launcher_layout, container, false);

        mInitFragmentBackStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();

        mLayout = new IAPActionLayout(getContext(), getActivity().getSupportFragmentManager());
        mCustomView = mLayout.getCustomView(getContext());
        ((ViewGroup) mRootView.findViewById(R.id.ll_custom_action)).addView(mCustomView);
        ((TextView) mCustomView.findViewById(R.id.iap_header_title)).setText(R.string.iap_product_catalog);
        mCustomView.findViewById(R.id.iap_header_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();
        mIapSettings = new IAPSettings(countryCode, languageCode, R.style.Theme_Philips_DarkBlue_Gradient_WhiteBackground);
        mIapSettings.setUseLocalData(true);
        mIapSettings.setLaunchAsFragment(true);
        mIapSettings.setFragProperties(getFragmentManager(), R.id.vertical_Container);
        mIapHandler = IAPHandler.init(getContext(), mIapSettings);

        Bundle bundle = getArguments();
        String entryPoint = bundle.getString("FRAGMENT_ENTRY");
        launchInAppPurchases(entryPoint);

        return mRootView;
    }

    public void launchInAppPurchases(String pEntryPoint) {
        ArrayList<String> ctnList;
        //  if (DemoAppActivity..isOnline(getContext())) {

        ArrayList<String> mProductList = new ArrayList<>();
        mProductList.add("HX9042/64");
        mProductList.add("HX9042/64");
        mProductList.add("HX9042/64");
        if (pEntryPoint.equalsIgnoreCase("SHOPPING_CART"))
            mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_SHOPPING_CART_VIEW, null, null);
        else if (pEntryPoint.equalsIgnoreCase("SHOP_NOW")) {
            mIapHandler.launchCategorizedCatalog(mProductList);
            mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PRODUCT_CATALOG_VIEW, null, null);
        } else if (pEntryPoint.equalsIgnoreCase("PRODUCT_DETAIL"))
            mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PRODUCT_DETAIL_VIEW, "HX9042/64", null);
        else if (pEntryPoint.equalsIgnoreCase("PURCHASE_HISTORY"))
            mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PURCHASE_HISTORY_VIEW, null, null);
//    else {
//            showIAPToast(IAPConstant.IAP_ERROR_NO_CONNECTION);
//        }
    }

    private void showIAPToast(int errorCode) {
        Context context = getContext();
        String errorText = context.getResources().getString(R.string.iap_unknown_error);
        if (IAPConstant.IAP_ERROR_NO_CONNECTION == errorCode) {
            errorText = context.getResources().getString(R.string.iap_no_connection);
        } else if (IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT == errorCode) {
            errorText = context.getResources().getString(R.string.iap_connection_timeout);
        } else if (IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE == errorCode) {
            errorText = context.getResources().getString(R.string.iap_auth_failure);
        } else if (IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR == errorCode) {
            errorText = context.getResources().getString(R.string.iap_prod_out_of_stock);
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

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
    }

}

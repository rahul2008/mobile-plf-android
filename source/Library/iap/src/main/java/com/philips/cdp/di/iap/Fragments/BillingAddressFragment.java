/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.customviews.PuiSwitch;

public class BillingAddressFragment extends ShippingAddressFragment {
    public static final String TAG = BillingAddressFragment.class.getName();
    private Context mContext;
    private PuiSwitch mSwitchBillingAddress;

    private AddressFields mBillingAddressFields;
    private int mDisabledColor;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        assert rootView != null;
        LinearLayout mSameAsBillingAddress = (LinearLayout) rootView.findViewById(R.id.same_as_shipping_ll);
        mSwitchBillingAddress = (PuiSwitch) rootView.findViewById(R.id.switch_billing_address);

        mTvTitle.setText(getResources().getString(R.string.iap_billing_address));
        mSameAsBillingAddress.setVisibility(View.VISIBLE);

        View divider = rootView.findViewById(R.id.address_divider);
        divider.setVisibility(View.VISIBLE);

        mDisabledColor = ContextCompat.getColor(mContext, R.color.uikit_enricher4);

        if (CartModelContainer.getInstance().getShippingAddressFields() != null) {
            mBillingAddressFields = CartModelContainer.getInstance().getShippingAddressFields();
            CartModelContainer.getInstance().setSwitchToBillingAddress(false);
            disableAllFields();
            prePopulateShippingAddress();
            mBtnContinue.setEnabled(true);
        }

        mSwitchBillingAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CartModelContainer.getInstance().setSwitchToBillingAddress(!isChecked);
                if (isChecked) {
                    disableAllFields();
                    prePopulateShippingAddress();
                    mBtnContinue.setEnabled(true);
                } else {
                    clearAllFields();
                    mBtnContinue.setEnabled(false);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.BILLING_ADDRESS_PAGE_NAME);
        if (mSwitchBillingAddress.isChecked()) {
            disableAllFields();
            mBtnContinue.setEnabled(true);
        }
    }

    private void prePopulateShippingAddress() {
        mIgnoreTextChangeListener = true;

        if (mBillingAddressFields != null) {
            mEtFirstName.setText(mBillingAddressFields.getFirstName());
            mEtLastName.setText(mBillingAddressFields.getLastName());
            mEtSalutation.setText(mBillingAddressFields.getTitleCode());
            mEtAddressLineOne.setText(mBillingAddressFields.getLine1());
            mEtAddressLineTwo.setText(mBillingAddressFields.getLine2());
            mEtTown.setText(mBillingAddressFields.getTown());
            mEtPostalCode.setText(mBillingAddressFields.getPostalCode());
            mEtCountry.setText(HybrisDelegate.getInstance(mContext).getStore().getCountry());
            mEtEmail.setText(HybrisDelegate.getInstance(getContext()).getStore().getJanRainEmail());

            if (HybrisDelegate.getInstance().getStore().getCountry().equalsIgnoreCase("US") &&
                    mBillingAddressFields.getRegionName() != null) {
                mEtState.setText(mBillingAddressFields.getRegionName());
                mlLState.setVisibility(View.VISIBLE);
            } else {
                mlLState.setVisibility(View.GONE);
            }
            mIgnoreTextChangeListener = false;
            mEtPhoneNumber.setText(mBillingAddressFields.getPhoneNumber());
        }
        setTextColor();
    }

    private void setTextColor() {

        mInlineFormsParent.post(new Runnable() {
            @Override
            public void run() {
                mTvSalutation.setTextColor(mDisabledColor);
                mTvFirstName.setTextColor(mDisabledColor);
                mTvLastName.setTextColor(mDisabledColor);
                mTvAddressLineOne.setTextColor(mDisabledColor);
                mTvAddressLineTwo.setTextColor(mDisabledColor);
                mTvTown.setTextColor(mDisabledColor);
                mTvPostalCode.setTextColor(mDisabledColor);
                if (mlLState.getVisibility() == View.VISIBLE) {
                    mTvState.setTextColor(mDisabledColor);
                }
                mTvCountry.setTextColor(mDisabledColor);
                mTvEmail.setTextColor(mDisabledColor);
                mTvPhoneNumber.setTextColor(mDisabledColor);

                mEtSalutation.setTextColor(mDisabledColor);
                mEtFirstName.setTextColor(mDisabledColor);
                mEtLastName.setTextColor(mDisabledColor);
                mEtAddressLineOne.setTextColor(mDisabledColor);
                mEtAddressLineTwo.setTextColor(mDisabledColor);
                mEtTown.setTextColor(mDisabledColor);
                mEtPostalCode.setTextColor(mDisabledColor);
                if (mlLState.getVisibility() == View.VISIBLE) {
                    mEtState.setTextColor(mDisabledColor);
                }
                mEtCountry.setTextColor(mDisabledColor);
                mEtEmail.setTextColor(mDisabledColor);
                mEtPhoneNumber.setTextColor(mDisabledColor);
            }
        });
    }

    private void clearAllFields() {
        mIgnoreTextChangeListener = true;
        mEtFirstName.setText("");
        mEtLastName.setText("");
        mEtSalutation.setText("");
        mEtAddressLineOne.setText("");
        mEtAddressLineTwo.setText("");
        mEtTown.setText("");
        mEtPostalCode.setText("");
        mEtPhoneNumber.setText("");
        mEtState.setText("");
        if (HybrisDelegate.getInstance().getStore().getCountry().equalsIgnoreCase("US")) {
            mlLState.setVisibility(View.VISIBLE);
        } else {
            mlLState.setVisibility(View.GONE);
        }
        mIgnoreTextChangeListener = false;
        enableAllFields();
        enableFocus();
        removeErrorInAllFields();
    }

    private void enableAllFields() {
        setFieldsEnabled(true);
    }

    private void disableAllFields() {
        removeErrorInAllFields();
        setFieldsEnabled(false);
        disableFocus();
    }

    private void enableFocus() {
        setFieldsFocusable(true);
    }

    private void disableFocus() {
        setFieldsFocusable(false);
    }

    private void removeErrorInAllFields() {
        mInlineFormsParent.post(new Runnable() {
            @Override
            public void run() {
                mInlineFormsParent.removeError(mEtFirstName);
                mInlineFormsParent.removeError(mEtLastName);
                mInlineFormsParent.removeError(mEtAddressLineOne);
                mInlineFormsParent.removeError(mEtAddressLineTwo);
                mInlineFormsParent.removeError(mEtTown);
                mInlineFormsParent.removeError(mEtPostalCode);
                mInlineFormsParent.removeError(mEtPhoneNumber);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        Utility.hideKeypad(mContext);
        if(!isNetworkConnected())return;
        if (v == mBtnContinue) {
            mBillingAddressFields = setAddressFields(mBillingAddressFields.clone());
            CartModelContainer.getInstance().setBillingAddress(mBillingAddressFields);
            addFragment(OrderSummaryFragment.createInstance(new Bundle(), AnimationType.NONE),
                    OrderSummaryFragment.TAG);
        } else if (v == mBtnCancel) {
            Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
            if (fragment != null) {
                moveToVerticalAppByClearingStack();
            } else {
                moveToPreviousFragment();
            }
        }
    }

    public static BillingAddressFragment createInstance(Bundle args, AnimationType animType) {
        BillingAddressFragment fragment = new BillingAddressFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    private void setFieldsEnabled(boolean enable) {
        mEtFirstName.setEnabled(enable);
        mEtLastName.setEnabled(enable);
        mEtSalutation.setEnabled(enable);
        mEtAddressLineOne.setEnabled(enable);
        mEtAddressLineTwo.setEnabled(enable);
        mEtTown.setEnabled(enable);
        mEtPostalCode.setEnabled(enable);
        if (mlLState.getVisibility() == View.VISIBLE) {
            mEtState.setEnabled(enable);
        }
        mEtPhoneNumber.setEnabled(enable);
    }

    private void setFieldsFocusable(boolean focusable) {
        mEtFirstName.setFocusable(focusable);
        mEtLastName.setFocusable(focusable);
        mEtSalutation.setFocusable(focusable);
        mEtAddressLineOne.setFocusable(focusable);
        mEtAddressLineTwo.setFocusable(focusable);
        mEtTown.setFocusable(focusable);
        mEtPostalCode.setFocusable(focusable);
        if (mlLState.getVisibility() == View.VISIBLE) {
            mEtState.setFocusable(focusable);
        }
        mEtPhoneNumber.setFocusable(focusable);

        if (focusable) {
            mEtFirstName.setFocusableInTouchMode(true);
            mEtLastName.setFocusableInTouchMode(true);
            mEtSalutation.setFocusableInTouchMode(true);
            mEtAddressLineOne.setFocusableInTouchMode(true);
            mEtAddressLineTwo.setFocusableInTouchMode(true);
            mEtTown.setFocusableInTouchMode(true);
            mEtPostalCode.setFocusableInTouchMode(true);
            if (mlLState.getVisibility() == View.VISIBLE) {
                mEtState.setFocusableInTouchMode(true);
            }
            mEtPhoneNumber.setFocusableInTouchMode(true);
        }

        mEtCountry.setFocusable(false);
        mEtCountry.setFocusableInTouchMode(false);
        mEtEmail.setFocusableInTouchMode(false);
        mEtEmail.setFocusable(false);
        mEtEmail.setEnabled(false);
        mEtCountry.setEnabled(false);
    }

    @Override
    public boolean handleBackEvent() {
        Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
        if (fragment != null) {
            if (getFragmentManager().findFragmentByTag(ShippingAddressFragment.TAG) != null) {
                return false;
            }
            moveToVerticalAppByClearingStack();
        }
        return super.handleBackEvent();
    }
}

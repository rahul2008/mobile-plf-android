/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;

public class PaymentConfirmationFragment extends InAppBaseFragment
        implements TwoButtonDialogFragment.TwoButtonDialogListener {
    public static final String TAG = PaymentConfirmationFragment.class.getName();
    private Context mContext;

    private TextView mConfirmationText;
    private TextView mOrderText;
    private TextView mOrderNumber;
    private TextView mConfirmWithEmail;

    private TwoButtonDialogFragment mDialog;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.iap_payment_confirmation,
                container, false);
        initViews(view);
        updatePaymentUI();
        return view;
    }

    private void initViews(ViewGroup viewGroup) {
        mConfirmationText = (TextView) viewGroup.findViewById(R.id.tv_thank);
        mOrderText = (TextView) viewGroup.findViewById(R.id.tv_your_order_num);
        mOrderNumber = (TextView) viewGroup.findViewById(R.id.tv_order_num);
        mConfirmWithEmail = (TextView) viewGroup.findViewById(R.id.tv_confirm_email);
        final Button mOKButton = (Button) viewGroup.findViewById(R.id.tv_confirm_ok);
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!isNetworkConnected()) return;
                handleExit();
            }
        });
    }

    private void updatePaymentUI() {
        Bundle arguments = getArguments();
        boolean isPaymentSuccessful = arguments.getBoolean(ModelConstants.PAYMENT_SUCCESS_STATUS, false);
        if (isPaymentSuccessful) {
            updatePaymentSuccessUI(arguments);
        } else {
            updatePaymentFailureUI();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.PAYMENT_CONFIRMATION_PAGE_NAME);
        setTitleAndBackButtonVisibility(R.string.iap_confirmation, true);
    }

    @Override
    public boolean handleBackEvent() {
        ShowDialogOnBackPressed();
        return true;
    }

    private void ShowDialogOnBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.TWO_BUTTON_DIALOG_DESCRIPTION,
                mContext.getString(R.string.iap_continue_shopping_description));
        if (mDialog == null) {
            mDialog = new TwoButtonDialogFragment();
            mDialog.setOnDialogClickListener(this);
            mDialog.setArguments(bundle);
            mDialog.setShowsDialog(false);
        }
        try {
            mDialog.show(getFragmentManager(), "TwoButtonDialog");
            mDialog.setShowsDialog(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePaymentFailureUI() {
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.PAYMENT_STATUS, IAPAnalyticsConstant.FAILED);

        setPaymentTitle(R.string.iap_payment_failed);
        mOrderText.setVisibility(View.INVISIBLE);
        mOrderNumber.setVisibility(View.INVISIBLE);
        mConfirmWithEmail.setVisibility(View.INVISIBLE);
    }

    private void updatePaymentSuccessUI(final Bundle arguments) {
        if (arguments != null) {
            if (arguments.containsKey(ModelConstants.ORDER_NUMBER)) {
                mOrderNumber.setText(arguments.getString(ModelConstants.ORDER_NUMBER));

                HashMap<String, String> contextData = new HashMap<>();
                contextData.put(IAPAnalyticsConstant.PURCHASE_ID, mOrderNumber.getText().toString());
                contextData.put(IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.PURCHASE);
                contextData.put(IAPAnalyticsConstant.PAYMENT_STATUS, IAPAnalyticsConstant.SUCCESS);
                IAPAnalytics.trackMultipleActions(IAPAnalyticsConstant.SEND_DATA, contextData);
            }
            String email = HybrisDelegate.getInstance(mContext).getStore().getJanRainEmail();
            if (arguments.containsKey(ModelConstants.EMAIL_ADDRESS)) {
                email = arguments.getString(ModelConstants.EMAIL_ADDRESS);
            }
            String emailConfirmation = String.format(mContext.getString(R.string.iap_confirmation_email_msg),
                    email);
            mConfirmWithEmail.setText(emailConfirmation);
            setPaymentTitle(R.string.iap_thank_for_order);
        }
    }

    private void setPaymentTitle(final int iap_thank_for_order) {
        mConfirmationText.setText(iap_thank_for_order);
    }

    private void handleExit() {
        Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
        if (fragment != null) {
            moveToVerticalAppByClearingStack();
        } else {
            moveToProductCatalog();
        }
    }

    private void moveToProductCatalog() {
        Fragment fragment = getFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment == null) {
            getFragmentManager().popBackStack(ShoppingCartFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            addFragment(ProductCatalogFragment.createInstance(new Bundle(), AnimationType.NONE),
                    ProductCatalogFragment.TAG);
        } else {
            getFragmentManager().popBackStack(ProductCatalogFragment.TAG, 0);
        }
    }

    @Override
    public void onPositiveButtonClicked() {
        handleExit();
    }

    @Override
    public void onNegativeButtonClicked() {
        //NOP
    }

    public static PaymentConfirmationFragment createInstance(final Bundle bundle, final AnimationType animType) {
        PaymentConfirmationFragment fragment = new PaymentConfirmationFragment();
        bundle.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(bundle);
        return fragment;
    }
}
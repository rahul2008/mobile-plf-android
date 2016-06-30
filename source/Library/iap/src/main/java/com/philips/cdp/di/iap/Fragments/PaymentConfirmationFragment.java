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
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;
import java.util.Map;

public class PaymentConfirmationFragment extends BaseAnimationSupportFragment implements TwoButtonDailogFragment.TwoButtonDialogListener {
    private TextView mConfirmationText;
    private TextView mOrderText;
    private TextView mOrderNumber;
    private TextView mConfirmWithEmail;
    private Context mContext;
    private boolean mPaymentSuccessful;
    public static final String TAG = PaymentConfirmationFragment.class.getName();
    private TwoButtonDailogFragment mDailogFragment;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.iap_payment_confirmation,
                container, false);
        bindViews(view);
        assignValues();
        return view;
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
        setTitle(R.string.iap_confirmation);
        setBackButtonVisibility(View.GONE);
    }

    @Override
    public boolean onBackPressed() {
        ShowDialogOnBackPressed();
        return true;
    }

    private void ShowDialogOnBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.MODEL_ALERT_CONFIRM_DESCRIPTION, getString(R.string.iap_continue_shopping_description));
        if (mDailogFragment == null) {
            mDailogFragment = new TwoButtonDailogFragment();
            mDailogFragment.setOnDialogClickListener(this);
            mDailogFragment.setArguments(bundle);
            mDailogFragment.setShowsDialog(false);
        }
        try {
            mDailogFragment.show(getFragmentManager(), "TwoButtonDialog");
            mDailogFragment.setShowsDialog(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignValues() {
        Bundle arguments = getArguments();
        mPaymentSuccessful = arguments.getBoolean(ModelConstants.PAYMENT_SUCCESS_STATUS, false);
        if (mPaymentSuccessful) {
            updatePaymentSuccessUI(arguments);
        } else {
            updatePaymentFailureUI();
        }
    }

    private void updatePaymentFailureUI() {
        //Track Payment failed action
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.PAYMENT_STATUS, IAPAnalyticsConstant.FAILED);
        setConfirmationTitle(R.string.iap_payment_failed);
        mOrderText.setVisibility(View.INVISIBLE);
        mOrderNumber.setVisibility(View.INVISIBLE);
        mConfirmWithEmail.setVisibility(View.INVISIBLE);
    }

    private void updatePaymentSuccessUI(final Bundle arguments) {
        if (arguments != null) {
            if (arguments.containsKey(ModelConstants.ORDER_NUMBER)) {
                mOrderNumber.setText(arguments.getString(ModelConstants.ORDER_NUMBER));

                //Track confirmation on successful order action
                Map contextData = new HashMap<>();
                contextData.put(IAPAnalyticsConstant.PURCHASE_ID, mOrderNumber.getText().toString());
                contextData.put(IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.PURCHASE);
                contextData.put(IAPAnalyticsConstant.PAYMENT_STATUS, IAPAnalyticsConstant.SUCCESS);
                IAPAnalytics.trackMultipleActions(IAPAnalyticsConstant.SEND_DATA, contextData);
            }
            String email = HybrisDelegate.getInstance(mContext).getStore().getJanRainEmail();
            if (arguments.containsKey(ModelConstants.EMAIL_ADDRESS)) {
                email = arguments.getString(ModelConstants.EMAIL_ADDRESS);
            }
            String emailConfirmation = String.format(mContext.getString(R.string.iap_confirmation_email_msg), email);
            mConfirmWithEmail.setText(emailConfirmation);
            setConfirmationTitle(R.string.iap_thank_for_order);
        }
    }

    private void setConfirmationTitle(final int iap_thank_for_order) {
        mConfirmationText.setText(iap_thank_for_order);
    }

    private void bindViews(ViewGroup viewGroup) {
        mConfirmationText = (TextView) viewGroup.findViewById(R.id.tv_thank);
        mOrderText = (TextView) viewGroup.findViewById(R.id.tv_your_order_num);
        mOrderNumber = (TextView) viewGroup.findViewById(R.id.tv_order_num);
        mConfirmWithEmail = (TextView) viewGroup.findViewById(R.id.tv_confirm_email);
        final Button mOKButton = (Button) viewGroup.findViewById(R.id.tv_confirm_ok);
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isNetworkNotConnected()) return;

                handleExit();
            }
        });
    }

    private void handleExit() {
        if (mPaymentSuccessful) {
            moveToProductCatalog();

        } else {
            moveToFragment(OrderSummaryFragment.TAG);
        }

    }

    private void moveToProductCatalog() {
        CartModelContainer.getInstance().setOrderPlaced(false);
        Fragment fragment = getFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment == null) {
            getFragmentManager().popBackStack(ShoppingCartFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            addFragment(ProductCatalogFragment.createInstance(new Bundle(), AnimationType.NONE),
                    ProductCatalogFragment.TAG);
        } else {
            getFragmentManager().popBackStack(ProductCatalogFragment.TAG, 0);
        }
    }

    public static PaymentConfirmationFragment createInstance(final Bundle bundle, final AnimationType animType) {
        PaymentConfirmationFragment fragment = new PaymentConfirmationFragment();
        bundle.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDialogOkClick() {
        moveToProductCatalog();
    }

    @Override
    public void onDialogCancelClick() {
        //NOP
    }
}
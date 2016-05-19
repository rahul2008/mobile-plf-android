package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.PaymentMethodsAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.tagging.Tagging;

import java.util.List;

public class PaymentSelectionFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, EventListener, PaymentController.PaymentListener {
    public static final String TAG = PaymentSelectionFragment.class.getName();
    private Context mContext;
    private RecyclerView mPaymentMethodsRecyclerView;
    private Button mBtnCancel;
    private PaymentMethodsAdapter mPaymentMethodsAdapter;
    private List<PaymentMethod> mPaymentMethodList;
    private PaymentController mPaymentController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_payment_method, container, false);
        mPaymentMethodsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_payment_method);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle.containsKey(IAPConstant.PAYMENT_METHOD_LIST)) {
            mPaymentMethodList = (List<PaymentMethod>) bundle.getSerializable(IAPConstant.PAYMENT_METHOD_LIST);
        }

        mPaymentMethodsAdapter = new PaymentMethodsAdapter(getContext(), mPaymentMethodList);
        mPaymentMethodsRecyclerView.setAdapter(mPaymentMethodsAdapter);

        mPaymentController = new PaymentController(mContext, this);

        EventHelper.getInstance().registerEventNotification(IAPConstant.USE_PAYMENT, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.ADD_NEW_PAYMENT, this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mPaymentMethodsRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.PAYMENT_SELECTION_PAGE_NAME);
        setTitle(R.string.iap_payment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.USE_PAYMENT, this);
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.ADD_NEW_PAYMENT, this);
    }

    public static PaymentSelectionFragment createInstance(final Bundle args, final AnimationType animType) {
        PaymentSelectionFragment fragment = new PaymentSelectionFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnCancel) {
            moveToFragment(ShoppingCartFragment.TAG);
        }
    }

    private PaymentMethod selectedPaymentMethod() {
        int selectedPosition = mPaymentMethodsAdapter.getSelectedPosition();
        return mPaymentMethodList.get(selectedPosition);
    }

    @Override
    public void onEventReceived(String event) {
        if (event.equalsIgnoreCase(IAPConstant.USE_PAYMENT)) {
            setPaymentDetail();
        } else if (event.equalsIgnoreCase(IAPConstant.ADD_NEW_PAYMENT)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(IAPConstant.FROM_PAYMENT_SELECTION, true);
            addFragment(BillingAddressFragment.createInstance(bundle, AnimationType.NONE),
                    BillingAddressFragment.TAG);
        }
    }

    private void setPaymentDetail() {
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
            Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.PAYMENT_METHOD,
                    selectedPaymentMethod().getCardType().getCode());
            mPaymentController.setPaymentDetails(selectedPaymentMethod().getId());
        }
    }

    @Override
    public void onGetPaymentDetails(Message msg) {

    }

    @Override
    public void onSetPaymentDetails(Message msg) {
        Utility.dismissProgressDialog();
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(mErrorDialogListener, msg, getFragmentManager(), getContext());
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.SELECTED_PAYMENT, selectedPaymentMethod());
            addFragment(OrderSummaryFragment.createInstance(bundle, AnimationType.NONE), OrderSummaryFragment.TAG);
        }
    }
}

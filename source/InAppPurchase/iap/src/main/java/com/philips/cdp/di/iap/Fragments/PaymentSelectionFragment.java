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
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.payment.PaymentController;
import com.philips.cdp.di.iap.payment.PaymentMethodsAdapter;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.List;

public class PaymentSelectionFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, EventListener, PaymentController.PaymentListener {

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
            addFragment(ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
        }
    }

    private PaymentMethod selectedPaymentMethod() {
        int selectedPosition = mPaymentMethodsAdapter.getSelectedPosition();
        return mPaymentMethodList.get(selectedPosition);
    }

    @Override
    public void raiseEvent(String event) {

    }

    @Override
    public void onEventReceived(String event) {
        if (event.equalsIgnoreCase(IAPConstant.USE_PAYMENT)) {
            if (!Utility.isProgressDialogShowing()) {
                if (Utility.isInternetConnected(mContext)) {
                    Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
                    mPaymentController.setPaymentDetails(selectedPaymentMethod().getId());
                } else {
                    NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_time_out), getString(R.string.iap_time_out_description));
                }
            }
        } else if (event.equalsIgnoreCase(IAPConstant.ADD_NEW_PAYMENT)) {
            Bundle bundle = new Bundle();
            if (getArguments().containsKey(IAPConstant.ADDRESS_FIELDS)) {
                bundle.putSerializable(IAPConstant.ADDRESS_FIELDS,
                        getArguments().getSerializable(IAPConstant.ADDRESS_FIELDS));
            }
            addFragment(BillingAddressFragment.createInstance(bundle, AnimationType.NONE), null);
        }
    }

    @Override
    public void onGetPaymentDetails(Message msg) {

    }

    @Override
    public void onSetPaymentDetails(Message msg) {
        Utility.dismissProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IAPConstant.SELECTED_PAYMENT, selectedPaymentMethod());
        addFragment(OrderSummaryFragment.createInstance(bundle, AnimationType.NONE), null);
    }
}

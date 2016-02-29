package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.adapters.OrderProductAdapter;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrderSummaryFragment extends BaseAnimationSupportFragment {
    private RecyclerView mOrderListView;
    OrderProductAdapter mAdapter;
    private ArrayList<ShoppingCartData> mShoppingCartDataList;

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_order_summary);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_order_summary_fragment, container, false);
        IAPLog.d(IAPLog.ORDER_SUMMARY_FRAGMENT, "OrderSummaryFragment ");
        mOrderListView = (RecyclerView) rootView.findViewById(R.id.order_summary);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mOrderListView.setLayoutManager(layoutManager);
        mShoppingCartDataList = new ArrayList<ShoppingCartData>();
        mAdapter = new OrderProductAdapter(getContext(), mShoppingCartDataList);
        return rootView;
    }

    public static OrderSummaryFragment createInstance(Bundle args, AnimationType animType) {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }
}

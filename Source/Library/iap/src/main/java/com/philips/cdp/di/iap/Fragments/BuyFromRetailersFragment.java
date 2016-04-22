/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.adapters.BuyFromRetailersAdapter;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.ArrayList;


public class BuyFromRetailersFragment extends BaseAnimationSupportFragment implements ShoppingCartPresenter.LoadListenerForRetailer {

    FrameLayout mCrossContainer;
    RecyclerView mRecyclerView;
    ImageView mCross;
    BuyFromRetailersAdapter mAdapter;
    String mCtn;

    public static BuyFromRetailersFragment createInstance(Bundle args, BaseAnimationSupportFragment.AnimationType animType) {
        BuyFromRetailersFragment fragment = new BuyFromRetailersFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.iap_retailers_view, container, false);
        mCrossContainer = (FrameLayout) rootView.findViewById(R.id.iap_retailer_cross_container);
        mCross = (ImageView) rootView.findViewById(R.id.iap_retailer_cross);
        Drawable crossDrawable = VectorDrawable.create(getContext(), R.drawable.iap_retailer_cross);
        mCross.setImageDrawable(crossDrawable);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.iap_retailer_list);

        mCtn = getArguments().getString(ModelConstants.PRODUCT_CODE);

        mCrossContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_retailer_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        getRetailersInformation();
    }

    private void getRetailersInformation() {
        ShoppingCartPresenter presenter = new ShoppingCartPresenter(getContext(), this, getFragmentManager());
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_get_cart_details));
            presenter.getRetailersInformation(mCtn);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onLoadFinished(final ArrayList<StoreEntity> data) {
        mAdapter = new BuyFromRetailersAdapter(getContext(), data, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}

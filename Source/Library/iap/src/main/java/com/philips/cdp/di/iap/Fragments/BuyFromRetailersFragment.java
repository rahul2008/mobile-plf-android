/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.BuyFromRetailersAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.ArrayList;


public class BuyFromRetailersFragment extends BaseAnimationSupportFragment {

    public static final String TAG = BuyFromRetailersFragment.class.getName();


    FrameLayout mCrossContainer;
    RecyclerView mRecyclerView;
    ImageView mCross;
    BuyFromRetailersAdapter mAdapter;
    String mCtn;
    private ArrayList<StoreEntity> mStoreEntity;// = new ArrayList<>();

    public static BuyFromRetailersFragment createInstance(Bundle args, BaseAnimationSupportFragment.AnimationType animType) {
        BuyFromRetailersFragment fragment = new BuyFromRetailersFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.iap_retailers_view, container, false);
//        mCrossContainer = (FrameLayout) rootView.findViewById(R.id.iap_retailer_cross_container);
//        mCross = (ImageView) rootView.findViewById(R.id.iap_retailer_cross);
//        Drawable crossDrawable = VectorDrawable.create(getContext(), R.drawable.iap_retailer_cross);
//        mCross.setImageDrawable(crossDrawable);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.iap_retailer_list);
        mStoreEntity = (ArrayList<StoreEntity>) getArguments().getSerializable(IAPConstant.IAP_RETAILER_INFO);

//        mCrossContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                getFragmentManager().popBackStackImmediate();
//            }
//        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.RETAILERS_LIST_PAGE_NAME);
        setTitle(R.string.iap_retailer_title);
        // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if (!isNetworkNotConnected() && mStoreEntity!=null) {
            mAdapter = new BuyFromRetailersAdapter(getContext(), mStoreEntity, getFragmentManager(), getId());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter = new BuyFromRetailersAdapter(getContext(), new ArrayList<StoreEntity>(), getFragmentManager(), getId());
            mRecyclerView.setAdapter(mAdapter);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}

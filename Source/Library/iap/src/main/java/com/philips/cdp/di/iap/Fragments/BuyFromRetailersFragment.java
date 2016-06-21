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
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.adapters.BuyFromRetailersAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;


public class BuyFromRetailersFragment extends BaseAnimationSupportFragment implements
        ShoppingCartPresenter.LoadListener<StoreEntity> {

    public static final String TAG = BuyFromRetailersFragment.class.getName();


    FrameLayout mCrossContainer;
    RecyclerView mRecyclerView;
    ImageView mCross;
    BuyFromRetailersAdapter mAdapter;
    String mCtn;
    private boolean isDataAvailable = false;
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
        mCtn = getArguments().getString(ModelConstants.PRODUCT_CODE);

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
        if (!isNetworkNotConnected() && !isDataAvailable) {
            getRetailersInformation();
        } else {
            mAdapter = new BuyFromRetailersAdapter(getContext(), mStoreEntity, getFragmentManager(), getId());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void getRetailersInformation() {
        ShoppingCartAPI presenter = ControllerFactory.getInstance().getShoppingCartPresenter(getContext(), this, getFragmentManager());

        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
            presenter.getRetailersInformation(mCtn);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onLoadFinished(final ArrayList<StoreEntity> data) {
        mStoreEntity = data;
        mAdapter = new BuyFromRetailersAdapter(getContext(), mStoreEntity, getFragmentManager(), getId());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        isDataAvailable = true;
    }

    @Override
    public void onLoadListenerError(IAPNetworkError error) {
        // NOP
        isDataAvailable = false;
        IAPLog.d(IAPLog.LOG, "IAPNetworkError " + error);
    }

    @Override
    public void onRetailerError(IAPNetworkError errorMsg) {
        NetworkUtility.getInstance().showErrorDialog(getContext(), getFragmentManager(), getContext().getString(R.string.iap_ok), getContext().getString(R.string.iap_retailer_title_for_no_retailers), errorMsg.getMessage());
    }
}

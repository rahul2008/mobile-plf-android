package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.ViewPagerAdaptor;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.uikit.customviews.CircleIndicator;

import java.util.HashMap;

public class ProductDetailFragment extends BaseAnimationSupportFragment{

    private Context mContext;
    TextView mProductDescription;
    TextView mCTN;
    TextView mPrice;
    TextView mProductOverview;

    public static ProductDetailFragment createInstance(Bundle args, AnimationType animType) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.iap_product_details_screen, container, false);

        final ViewPager mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPager.setAdapter(new ViewPagerAdaptor(getFragmentManager()));

        final CircleIndicator mIndicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mProductDescription = (TextView) rootView.findViewById(R.id.product_description);
        mCTN = (TextView) rootView.findViewById(R.id.ctn);
        mPrice = (TextView) rootView.findViewById(R.id.individual_price);
        mProductOverview = (TextView) rootView.findViewById(R.id.product_overview);

        populateView();

        return rootView;
    }

    private void populateView() {
        Bundle bundle = getArguments();
        mProductDescription.setText(bundle.getString(IAPConstant.PRODUCT_TITLE));
        mCTN.setText(bundle.getString(IAPConstant.PRODUCT_CTN));
        mPrice.setText(bundle.getString(IAPConstant.PRODUCT_PRICE));
        mProductOverview.setText(bundle.getString(IAPConstant.PRODUCT_OVERVIEW));
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_shopping_cart_item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onBackPressed() {
        finishActivity();
    }

}

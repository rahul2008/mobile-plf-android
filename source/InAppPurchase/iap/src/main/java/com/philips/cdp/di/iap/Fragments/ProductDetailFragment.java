package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.PRXProductAssetBuilder;
import com.philips.cdp.di.iap.adapters.ProductDetailImageAdaptor;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.uikit.customviews.CircleIndicator;

import java.util.ArrayList;

public class ProductDetailFragment extends BaseAnimationSupportFragment implements PRXProductAssetBuilder.AssetListener {

    private Context mContext;
    TextView mProductDescription;
    TextView mCTN;
    TextView mPrice;
    TextView mProductOverview;
    ArrayList<String> mAsset;
    ProductDetailImageAdaptor mAdapter;
    ViewPager mPager;
    Bundle mBundle;

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
        mBundle = getArguments();

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mAdapter = new ProductDetailImageAdaptor(getFragmentManager(), getContext());
        mPager.setAdapter(mAdapter);

        final CircleIndicator mIndicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mProductDescription = (TextView) rootView.findViewById(R.id.product_description);
        mCTN = (TextView) rootView.findViewById(R.id.ctn);
        mPrice = (TextView) rootView.findViewById(R.id.individual_price);
        mProductOverview = (TextView) rootView.findViewById(R.id.product_overview);
        
        populateViewFromBundle();
        makeAssetRequest();
        return rootView;
    }

    private void populateViewFromBundle() {
        mProductDescription.setText(mBundle.getString(IAPConstant.PRODUCT_TITLE));
        mCTN.setText(mBundle.getString(IAPConstant.PRODUCT_CTN));
        mPrice.setText(mBundle.getString(IAPConstant.PRODUCT_PRICE));
        mProductOverview.setText(mBundle.getString(IAPConstant.PRODUCT_OVERVIEW));
    }

    private void makeAssetRequest() {
        String ctn = mBundle.getString(IAPConstant.PRODUCT_CTN);
        PRXProductAssetBuilder builder = new PRXProductAssetBuilder(mContext, ctn,
                this);
        builder.build();
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

    }

    @Override
    public void onFetchAssetSuccess(final Message msg) {
        IAPLog.d(IAPConstant.PRODUCT_DETAIL_FRAGMENT, "Success");
        Toast.makeText(getContext(),"SUCCESS",Toast.LENGTH_SHORT).show();
        mAsset = (ArrayList<String>)msg.obj;
        mAdapter.setAsset(mAsset);
        mPager.setAdapter(mAdapter);
        mPager.invalidate();
    }

    @Override
    public void onFetchAssetFailure(final Message msg) {
        IAPLog.d(IAPConstant.PRODUCT_DETAIL_FRAGMENT,"Failure");
        Toast.makeText(getContext(),"FAILURE",Toast.LENGTH_SHORT).show();
    }
}

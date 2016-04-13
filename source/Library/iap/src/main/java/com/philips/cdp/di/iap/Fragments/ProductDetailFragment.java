package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.PRXProductAssetBuilder;
import com.philips.cdp.di.iap.adapters.ImageAdaptor;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPHandlerListener;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.customviews.CircleIndicator;

import java.util.ArrayList;

public class ProductDetailFragment extends BaseAnimationSupportFragment implements PRXProductAssetBuilder.AssetListener {

    private Context mContext;
    TextView mProductDescription;
    TextView mCTN;
    TextView mPrice;
    TextView mProductOverview;
    Button mAddToCart;
    Button mBuyFromRetailors;
    ArrayList<String> mAsset;
    ImageAdaptor mAdapter;
    ViewPager mPager;
    Bundle mBundle;
    private IAPHandler mIapHandler;
    private boolean mIsProductCatalogLaunched = false;
    private final int DEFAULT_THEME = R.style.Theme_Philips_DarkPurple_WhiteBackground;
    private String mCTNValue;
    private String mProductTitle;

    private IAPHandlerListener mBuyProductListener = new IAPHandlerListener() {
        @Override
        public void onSuccess(final int count) {
            Utility.dismissProgressDialog();
        }

        @Override
        public void onFailure(final int errorCode) {
            Utility.dismissProgressDialog();
            Toast.makeText(getContext(), "errorCode", Toast.LENGTH_SHORT).show();
        }
    };

    private IAPHandlerListener mProductCountListener = new IAPHandlerListener() {
        @Override
        public void onSuccess(final int count) {
            if (count > 0) {
                updateCount(count);
            } else {
                setCartIconVisibility(View.GONE);
            }
            if(Utility.isProgressDialogShowing()) {
                Utility.dismissProgressDialog();
            }
        }

        @Override
        public void onFailure(final int errorCode) {
            if(Utility.isProgressDialogShowing()) {
                Utility.dismissProgressDialog();
            }
            Toast.makeText(getContext(), "errorCode", Toast.LENGTH_SHORT).show();
        }
    };

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
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mIapHandler = new IAPHandler();
        View rootView = inflater.inflate(R.layout.iap_product_details_screen, container, false);
        mBundle = getArguments();

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mAdapter = new ImageAdaptor(getFragmentManager(), getContext());
        mPager.setAdapter(mAdapter);

        final CircleIndicator mIndicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mProductDescription = (TextView) rootView.findViewById(R.id.product_description);
        mCTN = (TextView) rootView.findViewById(R.id.ctn);
        mPrice = (TextView) rootView.findViewById(R.id.individual_price);
        mProductOverview = (TextView) rootView.findViewById(R.id.product_overview);
        mAddToCart = (Button) rootView.findViewById(R.id.add_to_cart);
        mBuyFromRetailors = (Button) rootView.findViewById(R.id.buy_from_retailor);
        mCTNValue = mBundle.getString(IAPConstant.PRODUCT_CTN);
        mIsProductCatalogLaunched = mBundle.getBoolean(IAPConstant.IS_PRODUCT_CATALOG, false);
        mProductTitle = mBundle.getString(IAPConstant.PRODUCT_TITLE);
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
            if(!Utility.isProgressDialogShowing()) {
                Utility.showProgressDialog(getContext(),getString(R.string.iap_get_image_url));
                String ctn = mBundle.getString(IAPConstant.PRODUCT_CTN);
                PRXProductAssetBuilder builder = new PRXProductAssetBuilder(mContext, ctn,
                        this);
                builder.build();
            }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_shopping_cart_item);
        if(mBundle!=null && mIsProductCatalogLaunched){
            mAddToCart.setVisibility(View.VISIBLE);
            mBuyFromRetailors.setVisibility(View.VISIBLE);
            setCartIconVisibility(View.VISIBLE);

            mAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    buyProduct(mCTNValue);
                }
            });

        }
        mIapHandler.getProductCartCount(getContext(), mProductCountListener);
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
    public void onFetchAssetSuccess(final Message msg) {
        IAPLog.d(IAPConstant.PRODUCT_DETAIL_FRAGMENT, "Success");
        mAsset = (ArrayList<String>)msg.obj;
        mAdapter.setAsset(mAsset);
        mAdapter.notifyDataSetChanged();
        if(Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @Override
    public void onFetchAssetFailure(final Message msg) {
        IAPLog.d(IAPConstant.PRODUCT_DETAIL_FRAGMENT, "Failure");
        if(Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
        NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), getContext());
    }

    void buyProduct(final String ctnNumber) {
        Utility.showProgressDialog(getContext(), "PLease wait");
        mIapHandler.buyProduct(getContext(), ctnNumber, mBuyProductListener, DEFAULT_THEME);
    }
}

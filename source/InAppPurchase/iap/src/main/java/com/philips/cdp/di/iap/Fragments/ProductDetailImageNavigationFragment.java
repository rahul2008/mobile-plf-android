package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;

public final class ProductDetailImageNavigationFragment extends BaseAnimationSupportFragment {
    private String imageURL = "???";
   // private Context mContext;
    private ImageLoader mImageLoader;
    private NetworkImageView mImageView;

    public static ProductDetailImageNavigationFragment newInstance() {
        ProductDetailImageNavigationFragment fragment = new ProductDetailImageNavigationFragment();
        //fragment.imageURL = message;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(IAPConstant.PRODUCT_DETAIL_FRAGMENT_IMAGE_URL)) {
            imageURL = savedInstanceState.getString(IAPConstant.PRODUCT_DETAIL_FRAGMENT_IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        this.imageURL = bundle.getString(NetworkConstants.IAP_ASSET_URL);

        bindImageToViewPager();
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(mImageView);
        return layout;
    }

    private void bindImageToViewPager() {
        mImageView = new NetworkImageView(getActivity());
        // Instantiate the RequestQueue.
        mImageLoader = NetworkImageLoader.getInstance(getContext())
                .getImageLoader();

        mImageLoader.get(imageURL, ImageLoader.getImageListener(mImageView,
                R.drawable.toothbrush, android.R.drawable
                        .ic_dialog_alert));
        mImageView.setImageUrl(imageURL, mImageLoader);

        mImageView.setImageResource(R.drawable.toothbrush);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IAPConstant.PRODUCT_DETAIL_FRAGMENT_IMAGE_URL, imageURL);
    }
}

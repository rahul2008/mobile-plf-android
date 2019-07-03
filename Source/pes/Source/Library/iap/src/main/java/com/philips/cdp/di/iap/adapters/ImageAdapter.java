/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.NetworkImageLoader;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    protected ArrayList<String> mAssetsFromPRX = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public ImageAdapter(Context context, ArrayList<String> assets) {
        super();
        mAssetsFromPRX = assets;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mAssetsFromPRX.size();
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.iap_image_pager_adapter, container, false);
        NetworkImageView imageView = (NetworkImageView) itemView.findViewById(R.id.network_image);
        bindImageToViewPager(imageView, mAssetsFromPRX.get(position));
        container.addView(itemView);
        return itemView;
    }

    private void bindImageToViewPager(NetworkImageView imageView, String imageURL) {
        ImageLoader mImageLoader;
        // Instantiate the RequestQueue.
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();

        mImageLoader.get(imageURL, ImageLoader.getImageListener(imageView,
                R.drawable
                        .no_icon, R.drawable
                        .no_icon));
        imageView.setImageUrl(imageURL, mImageLoader);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mAssetsFromPRX.get(position % mAssetsFromPRX.size());
    }
}
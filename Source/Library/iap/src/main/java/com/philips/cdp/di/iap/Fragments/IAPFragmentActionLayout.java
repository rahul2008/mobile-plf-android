/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.actionlayout.IAPActionLayout;
import com.philips.cdp.di.iap.activity.IAPFragmentListener;

public class IAPFragmentActionLayout extends IAPActionLayout implements IAPFragmentListener {
    private TextView mCountView;
    private TextView mHeaderTitle;
    private ViewGroup mCartContainer;

    public IAPFragmentActionLayout(Context context, FragmentManager v4FragManager) {
        super(context, v4FragManager);
        mCountView = (TextView) mMainLayout.findViewById(R.id.item_count);
        mHeaderTitle = (TextView) mMainLayout.findViewById(R.id.text);
        mCartContainer = (ViewGroup) mMainLayout.findViewById(R.id.cart_container);
        setBackButtonListener();
        setCartContainerListener();
    }

    private void setBackButtonListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onHWBackPressed();
            }
        });
    }

    private void setCartContainerListener() {
        mCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            }
        });
    }

    @Override
    public void setHeaderTitle(final int pResourceId) {
        mHeaderTitle.setText(pResourceId);
    }

    @Override
    public void updateCount(final int count) {
        if (count == 0) {
            mCountView.setVisibility(View.GONE);
        } else {
            mCountView.setVisibility(View.VISIBLE);
            mCountView.setText(String.valueOf(count));
        }
    }

    @Override
    public void setCartIconVisibility(final int visibility) {
        mCartContainer.setVisibility(visibility);
    }

    @Override
    public void setBackButtonVisibility(final int isVisible) {
        mBackButton.setVisibility(isVisible);
    }

    @Override
    public void setHeaderTitle(final String title) {
        mHeaderTitle.setText(title);
    }
}
package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartBaseFragment extends BaseAnimationSupportFragment implements EventListener {
    private FrameLayout mFrameContainer;

    public static ShoppingCartBaseFragment createInstance(AnimationType animType) {
        ShoppingCartBaseFragment fragment = new ShoppingCartBaseFragment();
        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_base, container, false);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.EMPTY_CART_FRGMENT_REPLACED), this);
        getMainActivity().addFragmentAndRemoveUnderneath(
                ShoppingCartFragment.createInstance(AnimationType.NONE), false);
        mFrameContainer = new FrameLayout(getMainActivity());
        mFrameContainer.setId(R.id.fl_shoppingcart_fragment_container);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.EMPTY_CART_FRGMENT_REPLACED), this);
    }

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
    }

    @Override
    protected void updateTitle() {

    }

    @Override
    public void raiseEvent(final String event) {

    }

    @Override
    public void onEventReceived(final String event) {
        if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRGMENT_REPLACED)) {
            //getMainActivity().addFragmentAndRemoveUnderneath(EmptyCartFragment.createInstance(AnimationType.NONE), false);
            EmptyCartFragment emptyCartFragment = new EmptyCartFragment();
            addChildFragment(emptyCartFragment, mFrameContainer.getId());
        }
    }
}

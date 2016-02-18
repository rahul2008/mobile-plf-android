/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPLog;

public class ShippingAddressFragment extends BaseAnimationSupportFragment implements View.OnClickListener, AddressController.AddressListener {

    private Button mBtnCotinue;

    @Override
    protected void updateTitle() {
        setTitle(R.string.iap_shipping_address);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shipping_address_layout, container, false);
        mBtnCotinue = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnCotinue.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onClick(final View v) {
        if (v == mBtnCotinue) {
            IAPLog.d(IAPLog.SHIPPING_ADDRESS_FRAGMENT, "onClick ShippingAddressFragment");
            getMainActivity().addFragmentAndRemoveUnderneath(
                    OrderSummaryFragment.createInstance(AnimationType.NONE), false);
        }
    }

    public static ShippingAddressFragment createInstance(AnimationType animType) {
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
    }
}

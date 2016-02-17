package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartAdapter;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.model.container.CartContainer;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class ShoppingCartFragment extends BaseNoAnimationFragment implements View.OnClickListener, RequestListener, EventListener {

    private Button mCheckoutBtn;
    public ShoppingCartAdapter mAdapter;
    public ListView mListView;

    @Override
    protected void updateTitle() {

    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ShoppingCartAdapter(getContext(), new ArrayList<ShoppingCartData>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED), this);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.EMPTY_CART_FRGMENT_REPLACED), this);
        IAPLog.d(IAPLog.LOG, "ShoppingCartFragment onCreateView");
        View rootView = inflater.inflate(R.layout.shopping_cart_view, container, false);
        mListView = (ListView) rootView.findViewById(R.id.withouticon);
        mCheckoutBtn = (Button) rootView.findViewById(R.id.checkout_btn);
        mCheckoutBtn.setOnClickListener(this);
        Utility.showProgressDialog(getContext(), getString(R.string.iap_get_cart_details));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
        ShoppingCartPresenter presenter = new ShoppingCartPresenter(getContext(), mAdapter);
        presenter.getCurrentCartDetails();
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.EMPTY_CART_FRGMENT_REPLACED), this);
    }

    public static ShoppingCartFragment createInstance(BaseAnimationSupportFragment.AnimationType animType) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();

        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onSuccess(final Message msg) {
        IAPLog.d(IAPLog.SHOPPING_CART_FRAGMENT, "Shopping cart fragment onSuccess");
        CartContainer cartContainer = new CartContainer();
        cartContainer.updateProductDetails(getContext(), msg);
    }

    @Override
    public void onError(final Message msg) {
        Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        Utility.dismissProgressDialog();
    }

    @Override
    public void onClick(final View v) {
        if (v == mCheckoutBtn) {

            IAPLog.d(IAPLog.SHOPPING_CART_FRAGMENT, "onClick ShoppingCartFragment");
            launchShippingAddressFragment();
        }
    }

    private void launchShippingAddressFragment() {
        Fragment parent = getParentFragment();
        IAPLog.d(IAPLog.SHOPPING_CART_FRAGMENT, "ShoppingCartFragment parent = " + parent.toString());
        if (parent == null || (!(parent instanceof ShoppingCartHomeFragment))) {
            return;
        }
        ((ShoppingCartHomeFragment) parent).replaceShippingAddressFragment();
    }

    private void launchEmptyCartFragment() {
        Fragment parent = getParentFragment();
        IAPLog.d(IAPLog.SHOPPING_CART_FRAGMENT, "ShoppingCartFragment parent = " + parent.toString());
        if (parent == null || (!(parent instanceof ShoppingCartHomeFragment))) {
            return;
        }
        ((ShoppingCartHomeFragment) parent).replaceEmptyCartFragment();
    }

    @Override
    public void raiseEvent(final String event) {
        // NOP
    }

    @Override
    public void onEventReceived(final String event) {
        if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRGMENT_REPLACED)) {
            launchEmptyCartFragment();
        }
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED))) {
            mCheckoutBtn.setEnabled(!Boolean.getBoolean(event));
        }
    }
}

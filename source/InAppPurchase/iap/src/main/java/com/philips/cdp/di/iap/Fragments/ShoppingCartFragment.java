package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartAdapter;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class ShoppingCartFragment extends BaseAnimationSupportFragment implements View.OnClickListener, EventListener {

    private Button mCheckoutBtn;
    public ShoppingCartAdapter mAdapter;
    public ListView mListView;
    private FrameLayout mFrameContainer;

    @Override
    protected void updateTitle() {
        setTitle(R.string.iap_shopping_cart);
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
        mFrameContainer = new FrameLayout(getMainActivity());
        mFrameContainer.setId(R.id.empty_cart);
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
        ShoppingCartPresenter presenter = new ShoppingCartPresenter(getContext(), mAdapter);
        presenter.getCurrentCartDetails();
        mListView.setAdapter(mAdapter);
        updateTitle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED), this);

    }

    public static ShoppingCartFragment createInstance(BaseAnimationSupportFragment.AnimationType animType) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();

        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onClick(final View v) {
        if (v == mCheckoutBtn) {
            IAPLog.d(IAPLog.SHOPPING_CART_FRAGMENT, "onClick ShoppingCartFragment");
            getMainActivity().addFragmentAndRemoveUnderneath(
                    ShippingAddressFragment.createInstance(AnimationType.NONE), false);
        }
    }

    @Override
    public void raiseEvent(final String event) {
        // NOP
    }

    @Override
    public void onEventReceived(final String event) {
//        if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRGMENT_REPLACED)) {
//            //getMainActivity().addFragmentAndRemoveUnderneath(EmptyCartFragment.createInstance(AnimationType.NONE), false);
//            EmptyCartFragment emptyCartFragment = new EmptyCartFragment();
//            addChildFragment(emptyCartFragment, mFrameContainer.getId());
//        }
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED))) {
            mCheckoutBtn.setEnabled(!Boolean.getBoolean(event));
        }
    }

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
    }
}

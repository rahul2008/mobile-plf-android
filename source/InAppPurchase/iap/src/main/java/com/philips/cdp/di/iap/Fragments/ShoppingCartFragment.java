package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
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
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class ShoppingCartFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, EventListener, AddressController.AddressListener {

    private Button mCheckoutBtn;
    public ShoppingCartAdapter mAdapter;
    public ListView mListView;
    private AddressController mAddressController;
    private Context mContext;

    public static ShoppingCartFragment createInstance(AnimationType animType) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void updateTitle() {
        setTitle(R.string.iap_shopping_cart);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAdapter = new ShoppingCartAdapter(getContext(), new ArrayList<ShoppingCartData>());
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED), this);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.EMPTY_CART_FRGMENT_REPLACED), this);
        IAPLog.d(IAPLog.LOG, "ShoppingCartFragment onCreateView");
        View rootView = inflater.inflate(R.layout.shopping_cart_view, container, false);
        mListView = (ListView) rootView.findViewById(R.id.withouticon);
        mCheckoutBtn = (Button) rootView.findViewById(R.id.checkout_btn);
        mCheckoutBtn.setOnClickListener(this);
        Utility.showProgressDialog(getContext(), getString(R.string.iap_get_cart_details));
        mAddressController = new AddressController(getContext(), this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ShoppingCartPresenter presenter = new ShoppingCartPresenter(getContext(), mAdapter);
        updateCartDetails(presenter);
        updateTitle();
    }

    private void updateCartDetails(ShoppingCartPresenter presenter) {
        presenter.getCurrentCartDetails();
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED), this);
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.EMPTY_CART_FRGMENT_REPLACED), this);
    }

    @Override
    public void onClick(final View v) {
        if (v == mCheckoutBtn) {
            if (!Utility.isProgressDialogShowing()) {
                if (Utility.isInternetConnected(getContext())) {
                    Utility.showProgressDialog(mContext, mContext.getResources().getString(R.string.iap_please_wait));
                    mAddressController.getShippingAddresses();
                } else {
                    NetworkUtility.getInstance().showNetworkError(mContext);
                }
            }
        }
    }

    @Override
    public void raiseEvent(final String event) {
        // NOP
    }

    @Override
    public void onEventReceived(final String event) {
        if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRGMENT_REPLACED)) {
            getMainActivity().addFragmentAndRemoveUnderneath(EmptyCartFragment.createInstance(AnimationType.NONE), false);
        }
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED))) {
            mCheckoutBtn.setEnabled(!Boolean.getBoolean(event));
        }
    }

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
    }

    @Override
    public void onFetchAddressSuccess(Message msg) {
        Utility.dismissProgressDialog();

        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
            getMainActivity().addFragmentAndRemoveUnderneath(
                    ShippingAddressFragment.createInstance(AnimationType.NONE), false);
        } else {
            getMainActivity().addFragmentAndRemoveUnderneath(
                    AddressSelectionFragment.createInstance(AnimationType.NONE), false);
        }
    }

    @Override
    public void onFetchAddressFailure(Message msg) {
        Utility.dismissProgressDialog();
        Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateAddress(boolean isSuccess) {

    }
}

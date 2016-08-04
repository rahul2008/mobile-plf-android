package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.BuyDirectController;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetUser;
import com.philips.cdp.di.iap.response.carts.CreateCartData;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

/**
 * Created by Apple on 02/08/16.
 */
public class BuyDirectFragment extends BaseAnimationSupportFragment implements BuyDirectController.BuyDirectListener {
    public static final String TAG = BuyDirectFragment.class.getName();
    private BuyDirectController mBuyDirectController;
    private Context mContext;
    private String mCTN;
    private String mAddressId;
    private PaymentMethod paymentMethod;

    public static BuyDirectFragment createInstance(Bundle args, AnimationType animType) {
        BuyDirectFragment fragment = new BuyDirectFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Bundle bundle = getArguments();
        mCTN = bundle.getString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBuyDirectController = new BuyDirectController(getContext(), this);
        String msg = getContext().getString(R.string.iap_please_wait);
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(mContext, msg);
            mBuyDirectController.createCart();
        }

    }

    @Override
    public void onCreateCart(Message msg) {
        IAPLog.d(IAPLog.BUY_DIRECT_FRAGMENT, "onCreateCart =" + TAG);
        CreateCartData createCartData = (CreateCartData) msg.obj;
        String buyDirectCode = createCartData.getCode();
        CartModelContainer.getInstance().setBuyDirectCartNumber(buyDirectCode);
        mBuyDirectController.addToCart(mCTN);
    }

    @Override
    public void onAddToCart(Message msg) {
        IAPLog.d(IAPLog.BUY_DIRECT_FRAGMENT, "onAddToCart =" + TAG);
        mBuyDirectController.getUser();

    }

    @Override
    public void onGetUser(Message msg) {
        IAPLog.d(IAPLog.BUY_DIRECT_FRAGMENT, "onGetUser =" + TAG);
        GetUser getUser = (GetUser) msg.obj;
        Addresses addressId = getUser.getDefaultAddress();
        if (addressId.getId() != null) {
            mAddressId = addressId.getId();
            AddressFields addressFields = new AddressFields();
            addressFields.setFirstName(addressId.getFirstName());
            addressFields.setLastName(addressId.getLastName());
            addressFields.setLine1(addressId.getLine1());
            addressFields.setLine2(addressId.getLine2());
            addressFields.setPhoneNumber(addressId.getPhone1());
            addressFields.setPostalCode(addressId.getPostalCode());
            addressFields.setEmail(addressId.getEmail());
            addressFields.setTitleCode(addressId.getTitleCode());
            addressFields.setTown(addressId.getTown());
            addressFields.setCountryIsocode(addressId.getCountry().getIsocode());
            CartModelContainer.getInstance().setShippingAddressFields(addressFields);
        }
        //Set delivery address from Msg
        mBuyDirectController.setDeliveryAddress(mAddressId);
    }

    @Override
    public void onGetDeliveryAddress(Message msg) {
        IAPLog.d(IAPLog.BUY_DIRECT_FRAGMENT, "onGetDeliveryAddress =" + TAG);
        //Set delivery address from Msg
        mBuyDirectController.setDeliveryAddress(mAddressId);
    }

    @Override
    public void onSetDeliveryAddress(Message msg) {
        IAPLog.d(IAPLog.BUY_DIRECT_FRAGMENT, "onSetDeliveryAddress =" + TAG);
        //Get delivery Mode from Msg
        mBuyDirectController.getDeliveryModes();
    }

    @Override
    public void onGetDeliveryMode(Message msg) {
        IAPLog.d(IAPLog.BUY_DIRECT_FRAGMENT, "onGetDeliveryMode =" + TAG);
        GetDeliveryModes deliveryModes = (GetDeliveryModes) msg.obj;
        ArrayList<DeliveryModes> deliveryModesList = (ArrayList<DeliveryModes>) deliveryModes.getDeliveryModes();
        String code = deliveryModesList.get(0).getCode();
        if (code != null) {
            CartModelContainer.getInstance().setDeliveryModes(deliveryModesList);
            //Set delivery Mode from Msg
            mBuyDirectController.setDeliveryMode(code);
        }

    }

    @Override
    public void onSetDeliveryMode(Message msg) {
        IAPLog.d(IAPLog.BUY_DIRECT_FRAGMENT, "onSetDeliveryMode =" + TAG);
        //Get Payment Mode from Msg
        mBuyDirectController.getPaymentMode();
    }

    @Override
    public void onGetPaymentMode(Message msg) {
        IAPLog.d(IAPLog.BUY_DIRECT_FRAGMENT, "onSetDeliveryMode =" + TAG);
        PaymentMethods paymentMethods = (PaymentMethods) msg.obj;
        paymentMethod = paymentMethods.getPayments().get(0);
        String paymentId = null;
        if (paymentMethod != null) {
            paymentId = paymentMethod.getId();
        }
        //Set Payment Mode from Msg
        mBuyDirectController.setPaymentMode(paymentId);
    }

    @Override
    public void onSetPaymentMode(Message msg) {
        Utility.dismissProgressDialog();
        //Inflate OrderSummary Fragment here
        Bundle bundle = new Bundle();
        bundle.putSerializable(IAPConstant.SELECTED_PAYMENT, paymentMethod);
        addFragment(OrderSummaryFragment.createInstance(bundle, AnimationType.NONE), OrderSummaryFragment.TAG);
    }
}

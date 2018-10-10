
/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.iap.screens;


import android.os.Bundle;
import android.os.Message;
import android.widget.EditText;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddressPresenter implements AddressController.AddressListener,PaymentController.PaymentListener {

    private final AddressContractor addressContractor;

    protected AddressController mAddressController;
    protected PaymentController mPaymentController;

    private final PhoneNumberUtil phoneNumberUtil;


    public AddressPresenter(AddressContractor addressContractor) {

        this.addressContractor = addressContractor;
        mAddressController = new AddressController(addressContractor.getActivityContext(), this);
        mPaymentController = new PaymentController(addressContractor.getActivityContext(), this);
        phoneNumberUtil = PhoneNumberUtil.getInstance();
    }

    public AddressContractor getAddressContractor() {
        return addressContractor;
    }

    public void setContinueButtonState(boolean b) {
        addressContractor.setContinueButtonState(b);
    }

    public void setBillingAddressFields(AddressFields addressFields) {
        addressContractor.setBillingAddressFields(addressFields);
    }

    @Override
    public void onGetRegions(Message msg) {

    }

    @Override
    public void onGetUser(Message msg) {

    }

    @Override
    public void onCreateAddress(Message msg) {
        if (msg.obj instanceof Addresses) {
            Addresses mAddresses = (Addresses) msg.obj;
            CartModelContainer.getInstance().setAddressId(mAddresses.getId());
            CartModelContainer.getInstance().setShippingAddressFields(Utility.prepareAddressFields(mAddresses, HybrisDelegate.getInstance(addressContractor.getActivityContext()).getStore().getJanRainEmail()));
            setDeliveryAddress(mAddresses.getId());
        } else if (msg.obj instanceof IAPNetworkError) {
            addressContractor.hideProgressbar();
            addressContractor.showErrorMessage(msg);
        }
    }

    @Override
    public void onGetAddress(Message msg) {

        if (msg.what == RequestCode.UPDATE_ADDRESS) {
            if (msg.obj instanceof IAPNetworkError) {
                addressContractor.showErrorMessage(msg);
            } else {
                if (addressContractor.getContinueButtonText().equalsIgnoreCase(addressContractor.getActivityContext().getString(R.string.iap_save))) {
                    addressContractor.hideProgressbar();
                    addressContractor.getFragmentActivity().getSupportFragmentManager().popBackStackImmediate();
                } else {
                   setDeliveryAddress(CartModelContainer.getInstance().getAddressId());
                }
            }
        }
    }

    @Override
    public void onSetDeliveryAddress(Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            DeliveryModes deliveryMode = addressContractor.getDeliveryModes();
            if (deliveryMode == null)
               getDeliveryModes();
            else
                mPaymentController.getPaymentDetails();
        } else {
            addressContractor.hideProgressbar();
            IAPLog.d(IAPLog.LOG, msg.getData().toString());
            NetworkUtility.getInstance().showErrorMessage(msg, addressContractor.getFragmentActivity().getSupportFragmentManager(), addressContractor.getActivityContext());
        }
    }

    @Override
    public void onGetDeliveryModes(Message msg) {
        handleDeliveryMode(msg, mAddressController);
    }

    @Override
    public void onSetDeliveryMode(Message msg) {

        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            if (CartModelContainer.getInstance().getBillingAddress() == null)
                mPaymentController.getPaymentDetails();
            else
                setBillingAddressAndOpenOrderSummary();
        } else {
            addressContractor.hideProgressbar();
            NetworkUtility.getInstance().showErrorMessage(msg,addressContractor.getFragmentActivity().getSupportFragmentManager(), addressContractor.getActivityContext());
        }
    }

    public void updateAddress(HashMap<String, String> updateAddressPayload) {
        mAddressController.updateAddress(updateAddressPayload);
    }

    public void createAddress(AddressFields shippingAddressFields) {
        mAddressController.createAddress(shippingAddressFields);
    }

    public void setDeliveryAddress(String id) {
        mAddressController.setDeliveryAddress(id);
    }

    public void getDeliveryModes() {
        mAddressController.getDeliveryModes();
    }

    public void handleDeliveryMode(Message msg, AddressController addressController) {
        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
            addressContractor.hideProgressbar();
        } else if ((msg.obj instanceof IAPNetworkError)) {
            NetworkUtility.getInstance().showErrorMessage(msg, addressContractor.getFragmentActivity().getSupportFragmentManager(), addressContractor.getActivityContext());
            addressContractor.hideProgressbar();
        } else if ((msg.obj instanceof GetDeliveryModes)) {
            GetDeliveryModes deliveryModes = (GetDeliveryModes) msg.obj;
            List<DeliveryModes> deliveryModeList = deliveryModes.getDeliveryModes();
            if (deliveryModeList.size() > 0) {
                CartModelContainer.getInstance().setDeliveryModes(deliveryModeList);
                addressController.setDeliveryMode(deliveryModeList.get(0).getCode());
            }
        }
    }

    @Override
    public void onGetPaymentDetails(Message msg) {
        addressContractor.hideProgressbar();


        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {

            CartModelContainer.getInstance().setShippingAddressFields(addressContractor.getShippingAddressFields());
            addressContractor.enableView(addressContractor.getBillingAddressView());
            addressContractor.disableView(addressContractor.getShippingAddressView());

        } else if ((msg.obj instanceof IAPNetworkError)) {
            NetworkUtility.getInstance().showErrorMessage(msg, addressContractor.getFragmentActivity().getSupportFragmentManager(), addressContractor.getActivityContext());
        } else if ((msg.obj instanceof PaymentMethods)) {
            //Track new address creation
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.NEW_SHIPPING_ADDRESS_ADDED);
            PaymentMethods mPaymentMethods = (PaymentMethods) msg.obj;
            List<PaymentMethod> mPaymentMethodsList = mPaymentMethods.getPayments();
            CartModelContainer.getInstance().setShippingAddressFields(addressContractor.getShippingAddressFields());
            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.PAYMENT_METHOD_LIST, (Serializable) mPaymentMethodsList);
            addressContractor.hideProgressbar();
            addressContractor.addPaymentSelectionFragment(bundle);

        }
    }

    @Override
    public void onSetPaymentDetails(Message msg) {

    }

    public void setBillingAddressAndOpenOrderSummary() {
        CartModelContainer.getInstance().setShippingAddressFields(addressContractor.getShippingAddressFields());
        CartModelContainer.getInstance().setBillingAddress(addressContractor.getBillingAddressFields());
        addressContractor.hideProgressbar();
        addressContractor.addOrderSummaryFragment();
    }

    HashMap<String, String> addressPayload(AddressFields pAddressFields) {
        HashMap<String, String> mShippingAddressHashMap = new HashMap<>();
        if (pAddressFields.getFirstName() != null) {
            mShippingAddressHashMap.put(ModelConstants.FIRST_NAME, pAddressFields.getFirstName());
        }
        if (pAddressFields.getLastName() != null) {
            mShippingAddressHashMap.put(ModelConstants.LAST_NAME, pAddressFields.getLastName());
        }
        if (pAddressFields.getLine1() != null) {
            mShippingAddressHashMap.put(ModelConstants.LINE_1, pAddressFields.getLine1());
        }
        if (pAddressFields.getTitleCode() != null) {
            mShippingAddressHashMap.put(ModelConstants.TITLE_CODE, pAddressFields.getTitleCode().toLowerCase(Locale.getDefault()));
        }
        if (pAddressFields.getCountryIsocode() != null) {
            mShippingAddressHashMap.put(ModelConstants.COUNTRY_ISOCODE, pAddressFields.getCountryIsocode());
        }
        if (pAddressFields.getPostalCode() != null) {
            mShippingAddressHashMap.put(ModelConstants.POSTAL_CODE, pAddressFields.getPostalCode().replaceAll(" ", ""));
        }
        if (pAddressFields.getTown() != null) {
            mShippingAddressHashMap.put(ModelConstants.TOWN, pAddressFields.getTown());
        }
        final String addressId = CartModelContainer.getInstance().getAddressId();
        if (addressId != null) {
            mShippingAddressHashMap.put(ModelConstants.ADDRESS_ID, addressId);
        }
        if (pAddressFields.getPhone1() != null) {
            mShippingAddressHashMap.put(ModelConstants.PHONE_1, pAddressFields.getPhone1().replaceAll(" ", ""));
        }
        if (pAddressFields.getPhone1() != null) {
            mShippingAddressHashMap.put(ModelConstants.PHONE_2, pAddressFields.getPhone1().replaceAll(" ", ""));
        }
        if (pAddressFields.getEmail() != null) {
            mShippingAddressHashMap.put(ModelConstants.EMAIL_ADDRESS, pAddressFields.getEmail());
        }
        if (!CartModelContainer.getInstance().isAddessStateVisible()) {
            mShippingAddressHashMap.put(ModelConstants.REGION_ISOCODE, null);
        } else {
            mShippingAddressHashMap.put(ModelConstants.REGION_ISOCODE, pAddressFields.getRegionIsoCode());
        }

        return mShippingAddressHashMap;
    }

    //This method can be further refactored as this is not testable
    boolean validatePhoneNumber(EditText editText, String country, String number) {
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, country);
            boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            String formattedPhoneNumber = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            editText.setText(formattedPhoneNumber);
            editText.setSelection(editText.getText().length());
            return isValid;
        } catch (Exception e) {
            IAPLog.d("ShippingAddressFragment", "NumberParseException");
        }
        return false;
    }
}
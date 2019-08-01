
/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.ecs.demouapp.ui.screens;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.widget.EditText;


import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.Constants.AddressFieldJsonEnum;
import com.ecs.demouapp.ui.address.AddressFields;
import com.ecs.demouapp.ui.analytics.ECSAnalytics;
import com.ecs.demouapp.ui.analytics.ECSAnalyticsConstant;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.controller.AddressController;
import com.ecs.demouapp.ui.controller.PaymentController;
import com.ecs.demouapp.ui.model.AddressFieldEnabler;
import com.ecs.demouapp.ui.model.SalutationEnum;
import com.ecs.demouapp.ui.response.State.RegionsList;
//import com.ecs.demouapp.ui.response.addresses.Addresses;
import com.ecs.demouapp.ui.response.addresses.DeliveryModes;
import com.ecs.demouapp.ui.response.addresses.GetDeliveryModes;

import com.ecs.demouapp.ui.response.payment.PaymentMethod;
import com.ecs.demouapp.ui.response.payment.PaymentMethods;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.session.IAPNetworkError;
import com.ecs.demouapp.ui.session.NetworkConstants;
import com.ecs.demouapp.ui.session.RequestCode;
import com.ecs.demouapp.ui.utils.ECSConstant;
import com.ecs.demouapp.ui.utils.ECSLog;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.ecs.demouapp.ui.utils.NetworkUtility;
import com.ecs.demouapp.ui.utils.Utility;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.util.ECSErrors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddressPresenter implements AddressController.AddressListener, PaymentController.PaymentListener {

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
        if(msg.obj instanceof Exception){
            CartModelContainer.getInstance().setRegionList(null);
        }else if (msg.obj instanceof IAPNetworkError) {
            CartModelContainer.getInstance().setRegionList(null);
        } else if (msg.obj instanceof RegionsList) {
            CartModelContainer.getInstance().setRegionList((RegionsList) msg.obj);
        } else {
            CartModelContainer.getInstance().setRegionList(null);
        }
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
            //Track new address creation
            ECSAnalytics.trackAction(ECSAnalyticsConstant.SEND_DATA,
                    ECSAnalyticsConstant.SPECIAL_EVENTS, ECSAnalyticsConstant.NEW_SHIPPING_ADDRESS_ADDED);
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
            }else if(msg.obj instanceof Exception){
                addressContractor.showErrorMessage(msg);
            }else {
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
        if (msg.obj.equals(ECSConstant.IAP_SUCCESS)) {
            DeliveryModes deliveryMode = addressContractor.getDeliveryModes();
            if (deliveryMode == null)
                getDeliveryModes();
            else
                mPaymentController.getPaymentDetails();
        } else {
            addressContractor.hideProgressbar();
            ECSLog.d(ECSLog.LOG, msg.getData().toString());
            NetworkUtility.getInstance().showErrorMessage(msg, addressContractor.getFragmentActivity().getSupportFragmentManager(), addressContractor.getActivityContext());
        }
    }

    @Override
    public void onGetDeliveryModes(Message msg) {
        handleDeliveryMode(msg, mAddressController);
    }

    @Override
    public void onSetDeliveryMode(Message msg) {

        if (msg.obj.equals(ECSConstant.IAP_SUCCESS)) {
            if (CartModelContainer.getInstance().getBillingAddress() == null)
                mPaymentController.getPaymentDetails();
            else
                setBillingAddressAndOpenOrderSummary();
        } else {
            addressContractor.hideProgressbar();
            NetworkUtility.getInstance().showErrorMessage(msg, addressContractor.getFragmentActivity().getSupportFragmentManager(), addressContractor.getActivityContext());
        }
    }

    public void updateAddress(HashMap<String, String> updateAddressPayload) {
        mAddressController.updateAddress(updateAddressPayload);
    }

    public void createAddress(AddressFields shippingAddressFields) {
        mAddressController.createAddress(shippingAddressFields);

        Addresses addressRequest = new Addresses();
        addressRequest.setFirstName(addressRequest.getFirstName());
        addressRequest.setLastName(addressRequest.getLastName());
        addressRequest.setTitleCode(addressRequest.getTitle());
        addressRequest.setCountry(addressRequest.getCountry()); // iso
        addressRequest.setLine1(addressRequest.getLine1());
        addressRequest.setLine2(addressRequest.getLine2());
        addressRequest.setPostalCode(addressRequest.getPostalCode());
        addressRequest.setTown(addressRequest.getTown());
        addressRequest.setPhone1(addressRequest.getPhone1());
        addressRequest.setPhone2(addressRequest.getPhone2());
        addressRequest.setRegion(addressRequest.getRegion()); // set Region eg State for US and Canada
        addressRequest.setHouseNumber(addressRequest.getHouseNumber());

        ECSUtility.getInstance().getEcsServices().createNewAddress(addressRequest, new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData result) {

            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {

            }
        });



       // ecsAddressRequest

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
        } else if ((msg.obj instanceof Exception)) {
            Exception exception = (Exception)msg.obj;
            ECSErrors.showECSToast(addressContractor.getFragmentActivity(),exception.getMessage());
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
            ECSAnalytics.trackAction(ECSAnalyticsConstant.SEND_DATA,
                    ECSAnalyticsConstant.SPECIAL_EVENTS, ECSAnalyticsConstant.NEW_SHIPPING_ADDRESS_ADDED);
            PaymentMethods mPaymentMethods = (PaymentMethods) msg.obj;
            List<PaymentMethod> mPaymentMethodsList = mPaymentMethods.getPayments();
            CartModelContainer.getInstance().setShippingAddressFields(addressContractor.getShippingAddressFields());
            Bundle bundle = new Bundle();
            bundle.putSerializable(ECSConstant.PAYMENT_METHOD_LIST, (Serializable) mPaymentMethodsList);
            addressContractor.hideProgressbar();
            addressContractor.addPaymentSelectionFragment(bundle);

        }
    }

    @Override
    public void onSetPaymentDetails(Message msg) {

    }

    public void setBillingAddressAndOpenOrderSummary() {
        if (addressContractor.getShippingAddressFields() != null)
            CartModelContainer.getInstance().setShippingAddressFields(addressContractor.getShippingAddressFields());
        if (addressContractor.getBillingAddressFields() != null)
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
        if (pAddressFields.getHouseNumber() != null) {
            mShippingAddressHashMap.put(ModelConstants.HOUSE_NO, pAddressFields.getHouseNumber());
        }
        if (pAddressFields.getTitleCode() != null) {
            mShippingAddressHashMap.put(ModelConstants.TITLE_CODE, pAddressFields.getTitleCode().toLowerCase(Locale.getDefault()));
        }
        if (pAddressFields.getCountryIsocode() != null) {
            mShippingAddressHashMap.put(ModelConstants.COUNTRY_ISOCODE, pAddressFields.getCountryIsocode());
        }
        if (pAddressFields.getPostalCode() != null) {
            mShippingAddressHashMap.put(ModelConstants.POSTAL_CODE, pAddressFields.getPostalCode().trim());
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

    //This method can be further refactored as this is not testableF
    boolean validatePhoneNumber(EditText editText, String country, String number) {
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, country);
            boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            String formattedPhoneNumber = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            editText.setText(formattedPhoneNumber);
            editText.setSelection(editText.getText().length());
            return isValid;
        } catch (Exception e) {
            ECSLog.d("ShippingAddressFragment", "NumberParseException");
        }
        return false;
    }

    String addressWithNewLineIfNull(String code) {
        if (!TextUtils.isEmpty(code)) {
            return code.replaceAll("null", " ").trim();
        }
        return null;
    }

    AddressFieldEnabler getAddressFieldEnabler(String country) {

        String addressFieldEnablerJson = null;
        AddressFieldEnabler addressFieldEnabler = null;
        try {
            addressFieldEnablerJson = getAddressFieldEnablerJson();

            JSONObject jsonObject = null;

            jsonObject = new JSONObject(addressFieldEnablerJson);

            JSONObject addressEnablerJsonObject = jsonObject.getJSONObject("excludedFields");
            addressFieldEnabler = new AddressFieldEnabler();

            JSONArray jsonArray = addressEnablerJsonObject.getJSONArray(country);


            for (int i = 0; i < jsonArray.length(); i++) {
                final String excludedField = jsonArray.getString(i);
                AddressFieldJsonEnum addressFieldJsonEnum = AddressFieldJsonEnum.getAddressFieldJsonEnumFromField(excludedField);
                setAddressFieldEnabler(addressFieldEnabler, addressFieldJsonEnum);
            }

        } catch (JSONException e) {

        } catch (IOException e) {

        }

        return addressFieldEnabler;
    }

    private String getAddressFieldEnablerJson() throws IOException {

        AssetManager manager = addressContractor.getActivityContext().getAssets();
        InputStream file = manager.open("addressFieldConfiguration.json");
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        return new String(formArray);
    }

    private void setAddressFieldEnabler(AddressFieldEnabler addressFieldEnabler, AddressFieldJsonEnum field) {

        switch (field) {

            case ADDRESS_ONE:
                addressFieldEnabler.setAddress1Enabled(false);
                break;

            case ADDRESS_TWO:
                addressFieldEnabler.setAddress2Enabled(false);
                break;
            case EMAIL:
                addressFieldEnabler.setEmailEnabled(false);
                break;
            case PHONE:
                addressFieldEnabler.setPhoneEnabled(false);
                break;
            case FIRST_NAME:
                addressFieldEnabler.setFirstNameEnabled(false);
                break;
            case LAST_NAME:
                addressFieldEnabler.setLastNmeEnabled(false);
                break;
            case STATE:
                addressFieldEnabler.setStateEnabled(false);
                break;
            case SALUTATION:
                addressFieldEnabler.setSalutationEnabled(false);
                break;
            case COUNTRY:
                addressFieldEnabler.setCountryEnabled(false);
                break;
            case POSTAL_CODE:
                addressFieldEnabler.setPostalCodeEnabled(false);
                break;
            case HOUSE_NUMBER:
                addressFieldEnabler.setHouseNumberEnabled(false);
                break;
            case TOWN:
                addressFieldEnabler.setTownEnabled(false);
                break;

        }
    }

    public SalutationEnum getEnglishSalutation(String salutation) {

        if (salutation.equalsIgnoreCase((addressContractor.getActivityContext().getString(R.string.iap_mr)))) {
            return SalutationEnum.MR;
        } else {
            return SalutationEnum.MS;
        }
    }
}
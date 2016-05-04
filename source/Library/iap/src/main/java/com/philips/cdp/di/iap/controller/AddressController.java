/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CreateAddressRequest;
import com.philips.cdp.di.iap.model.DeleteAddressRequest;
import com.philips.cdp.di.iap.model.GetAddressRequest;
import com.philips.cdp.di.iap.model.GetRegionsRequest;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.model.SetDeliveryAddressModeRequest;
import com.philips.cdp.di.iap.model.SetDeliveryAddressRequest;
import com.philips.cdp.di.iap.model.UpdateAddressRequest;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Locale;

public class AddressController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private AddressListener mAddressListener;
    private HybrisDelegate mDelegate;
    private Store mStore;

    public interface AddressListener {
        void onGetAddress(Message msg);
        void onCreateAddress(Message msg);
        void onSetDeliveryAddress(Message msg);
        void onSetDeliveryModes(Message msg);
        void onGetRegions(Message msg);
    }

    public AddressController(Context context, AddressListener listener) {
        mContext = context;
        mAddressListener = listener;
    }

    public void getRegions(){
        GetRegionsRequest model = new GetRegionsRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_REGIONS, model, model);
    }

    public void createAddress(AddressFields addressFields) {
        HashMap<String, String> params = getAddressHashMap(addressFields);
        CreateAddressRequest model = new CreateAddressRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.CREATE_ADDRESS, model, model);
    }

    public void getShippingAddresses() {
        GetAddressRequest model = new GetAddressRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_ADDRESS, model, model);
    }

    public void deleteAddress(String addressId) {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ADDRESS_ID, addressId);

        DeleteAddressRequest model = new DeleteAddressRequest(getStore(), query, this);
        getHybrisDelegate().sendRequest(RequestCode.DELETE_ADDRESS, model, model);
    }

    public void updateAddress(HashMap<String, String> query) {
        UpdateAddressRequest model = new UpdateAddressRequest(getStore(), query, this);
        getHybrisDelegate().sendRequest(RequestCode.UPDATE_ADDRESS, model, model);
    }

    public void setDeliveryAddress(String pAddressId) {
        if (null != pAddressId) {
            HashMap<String, String> params = new HashMap<>();
            params.put(ModelConstants.ADDRESS_ID, pAddressId);
            SetDeliveryAddressRequest model = new SetDeliveryAddressRequest(getStore(), params, this);
            getHybrisDelegate().sendRequest(RequestCode.SET_DELIVERY_ADDRESS, model, model);
        }
    }

    public void setDeliveryMode() {
        SetDeliveryAddressModeRequest model = new SetDeliveryAddressModeRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.SET_DELIVERY_MODE, model, model);
    }

    @Override
    public void onModelDataLoadFinished(Message msg) {
        sendListener(msg);
    }

    @Override
    public void onModelDataError(Message msg) {
        sendListener(msg);
    }

    private void sendListener(Message msg) {
        int requestCode = msg.what;

        if (null == mAddressListener) return;

        switch (requestCode) {
            case RequestCode.DELETE_ADDRESS:
                mAddressListener.onGetAddress(msg);
                break;
            case RequestCode.UPDATE_ADDRESS:
                mAddressListener.onGetAddress(msg);
                break;
            case RequestCode.CREATE_ADDRESS:
                mAddressListener.onCreateAddress(msg);
                break;
            case RequestCode.GET_ADDRESS:
                mAddressListener.onGetAddress(msg);
                break;
            case RequestCode.SET_DELIVERY_ADDRESS:
                mAddressListener.onSetDeliveryAddress(msg);
                break;
            case RequestCode.SET_DELIVERY_MODE:
                mAddressListener.onSetDeliveryModes(msg);
                break;
            case RequestCode.GET_REGIONS:
                mAddressListener.onGetRegions(msg);
        }
    }

    private HashMap<String, String> getAddressHashMap(final AddressFields addressFields) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.FIRST_NAME, addressFields.getFirstName());
        params.put(ModelConstants.LAST_NAME, addressFields.getLastName());
        params.put(ModelConstants.TITLE_CODE, addressFields.getTitleCode().toLowerCase(Locale.getDefault()));
        params.put(ModelConstants.COUNTRY_ISOCODE, addressFields.getCountryIsocode());
        params.put(ModelConstants.LINE_1, addressFields.getLine1());
        params.put(ModelConstants.LINE_2, addressFields.getLine2());
        params.put(ModelConstants.POSTAL_CODE, addressFields.getPostalCode());
        params.put(ModelConstants.TOWN, addressFields.getTown());
        params.put(ModelConstants.PHONE_1, addressFields.getPhoneNumber());
        params.put(ModelConstants.PHONE_2, "");
        params.put(ModelConstants.REGION_ISOCODE, addressFields.getRegionIsoCode());
        return params;
    }

    public void setDefaultAddress(Addresses address) {
        updateAddress(getAddressesMap(address, Boolean.TRUE));
    }

    public HashMap<String, String> getAddressesMap(Addresses addr, Boolean isDefaultAddress) {
        HashMap<String, String> addressHashMap = new HashMap<>();
        addressHashMap.put(ModelConstants.FIRST_NAME, addr.getFirstName());
        addressHashMap.put(ModelConstants.LAST_NAME, addr.getLastName());
        addressHashMap.put(ModelConstants.TITLE_CODE, addr.getTitleCode());
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, addr.getCountry().getIsocode());
        addressHashMap.put(ModelConstants.LINE_1, addr.getLine1());
        addressHashMap.put(ModelConstants.LINE_2, addr.getLine2());
        addressHashMap.put(ModelConstants.POSTAL_CODE, addr.getPostalCode());
        addressHashMap.put(ModelConstants.TOWN, addr.getTown());
        addressHashMap.put(ModelConstants.ADDRESS_ID, addr.getId());
        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, isDefaultAddress.toString());
        addressHashMap.put(ModelConstants.PHONE_1, addr.getPhone1());
        addressHashMap.put(ModelConstants.PHONE_2, "");
        if (addr.getRegion() != null)
            addressHashMap.put(ModelConstants.REGION_ISOCODE, addr.getRegion().getIsocode());
        return addressHashMap;
    }

    public void setHybrisDelegate(HybrisDelegate delegate) {
        mDelegate = delegate;
    }

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    public void setStore(Store store) {
        mStore = store;
    }

    Store getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
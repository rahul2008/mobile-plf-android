/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.controller;

import android.content.Context;
import android.os.Message;

/*import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CreateAddressRequest;
import com.philips.cdp.di.iap.model.DeleteAddressRequest;
import com.philips.cdp.di.iap.model.GetAddressRequest;
import com.philips.cdp.di.iap.model.GetDeliveryModesRequest;
import com.philips.cdp.di.iap.model.GetRegionsRequest;
import com.philips.cdp.di.iap.model.GetUserRequest;
import com.philips.cdp.di.iap.model.SetDeliveryAddressModeRequest;
import com.philips.cdp.di.iap.model.SetDeliveryAddressRequest;
import com.philips.cdp.di.iap.model.UpdateAddressRequest;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.ModelConstants;*/

import com.ecs.demouapp.ui.address.AddressFields;
import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.model.CreateAddressRequest;
import com.ecs.demouapp.ui.model.DeleteAddressRequest;
import com.ecs.demouapp.ui.model.GetAddressRequest;
import com.ecs.demouapp.ui.model.GetDeliveryModesRequest;
import com.ecs.demouapp.ui.model.GetRegionsRequest;
import com.ecs.demouapp.ui.model.GetUserRequest;
import com.ecs.demouapp.ui.model.SetDeliveryAddressModeRequest;
import com.ecs.demouapp.ui.model.SetDeliveryAddressRequest;
import com.ecs.demouapp.ui.model.UpdateAddressRequest;
import com.ecs.demouapp.ui.response.addresses.Addresses;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.session.RequestCode;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;

import java.util.HashMap;
import java.util.Locale;

public class AddressController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private AddressListener mAddressListener;
    private HybrisDelegate mDelegate;
    private StoreListener mStore;

    public interface AddressListener {
        void onGetRegions(Message msg);

        void onGetUser(Message msg);

        void onCreateAddress(Message msg);

        void onGetAddress(Message msg);

        void onSetDeliveryAddress(Message msg);

        void onGetDeliveryModes(Message msg);

        void onSetDeliveryMode(Message msg);
    }

    public AddressController(Context context, AddressListener listener) {
        mContext = context;
        mAddressListener = listener;
    }

    public void getRegions() {
        GetRegionsRequest model = new GetRegionsRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_REGIONS, model, model);
    }

    public void getRegions(String countryISO) {
        String country = getStore().getCountry();
        getStore().setLangAndCountry(getStore().getLocale(),countryISO);
        GetRegionsRequest model = new GetRegionsRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_REGIONS, model, model);

        // Todo : Its a hack ,need to be fixed
        getStore().setLangAndCountry(getStore().getLocale(),country);
    }

    public void getUser() {
        GetUserRequest model = new GetUserRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_USER, model, model);
    }

    public void createAddress(AddressFields addressFields) {
        HashMap<String, String> params = getAddressHashMap(addressFields);
        CreateAddressRequest model = new CreateAddressRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.CREATE_ADDRESS, model, model);
    }

    public void getAddresses() {
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

    public void getDeliveryModes() {
        GetDeliveryModesRequest model = new GetDeliveryModesRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_DELIVERY_MODE, model, model);
    }

    public void setDeliveryMode(String deliveryMode) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.DELIVERY_MODE_ID, deliveryMode);

        SetDeliveryAddressModeRequest model = new SetDeliveryAddressModeRequest(getStore(), params, this);
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
            case RequestCode.CREATE_ADDRESS:
                mAddressListener.onCreateAddress(msg);
                break;
            case RequestCode.GET_ADDRESS:
                mAddressListener.onGetAddress(msg);
                break;
            case RequestCode.GET_USER:
                mAddressListener.onGetUser(msg);
                break;
            case RequestCode.DELETE_ADDRESS:
                mAddressListener.onGetAddress(msg);
                break;
            case RequestCode.UPDATE_ADDRESS:
                mAddressListener.onGetAddress(msg);
                break;
            case RequestCode.SET_DELIVERY_ADDRESS:
                mAddressListener.onSetDeliveryAddress(msg);
                break;
            case RequestCode.SET_DELIVERY_MODE:
                mAddressListener.onSetDeliveryMode(msg);
                break;
            case RequestCode.GET_REGIONS:
                mAddressListener.onGetRegions(msg);
                break;
            case RequestCode.GET_DELIVERY_MODE:
                mAddressListener.onGetDeliveryModes(msg);
                break;
        }
    }

    private HashMap<String, String> getAddressHashMap(final AddressFields addressFields) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.FIRST_NAME, addressFields.getFirstName());
        params.put(ModelConstants.LAST_NAME, addressFields.getLastName());
        params.put(ModelConstants.TITLE_CODE, addressFields.getTitleCode().toLowerCase(Locale.getDefault()));
        params.put(ModelConstants.COUNTRY_ISOCODE, addressFields.getCountryIsocode());
        params.put(ModelConstants.LINE_1, addressFields.getLine1());
        params.put(ModelConstants.HOUSE_NO, addressFields.getHouseNumber());
        params.put(ModelConstants.POSTAL_CODE, addressFields.getPostalCode());
        params.put(ModelConstants.TOWN, addressFields.getTown());
        params.put(ModelConstants.PHONE_1, addressFields.getPhone1());
        params.put(ModelConstants.PHONE_2, addressFields.getPhone1());
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
        addressHashMap.put(ModelConstants.HOUSE_NO, addr.getHouseNumber());
        addressHashMap.put(ModelConstants.LINE_2, addr.getLine2());
        addressHashMap.put(ModelConstants.POSTAL_CODE, addr.getPostalCode());
        addressHashMap.put(ModelConstants.TOWN, addr.getTown());
        addressHashMap.put(ModelConstants.ADDRESS_ID, addr.getId());
        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, isDefaultAddress.toString());
        addressHashMap.put(ModelConstants.PHONE_1, addr.getPhone1());
        addressHashMap.put(ModelConstants.PHONE_2, addr.getPhone2());
        if (addr.getRegion() != null)
            addressHashMap.put(ModelConstants.REGION_ISOCODE, addr.getRegion().getIsocode());
        return addressHashMap;
    }

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    public void setHybrisDelegate(HybrisDelegate delegate) {
        mDelegate = delegate;
    }

    public void setStore(StoreListener store) {
        mStore = store;
    }

    StoreListener getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
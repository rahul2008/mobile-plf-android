/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.address;

import android.content.Context;
import android.os.Message;
import android.widget.Toast;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CreateAddressRequest;
import com.philips.cdp.di.iap.model.DeleteAddressRequest;
import com.philips.cdp.di.iap.model.GetAddressRequest;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.model.UpdateAddressRequest;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.HashMap;

public class AddressController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private AddressListener mAddressListener;
    private HybrisDelegate mDelegate;
    private Store mStore;

    public interface AddressListener {
        void onFetchAddressSuccess(Message msg);

        void onFetchAddressFailure(Message msg);

        void onCreateAddress(boolean isSuccess);
    }

    public AddressController(Context context, AddressListener listener) {
        mContext = context;
        mAddressListener = listener;
    }

    public void createAddress(AddressFields addressFields) {
        HashMap<String, String> params = getAddressHashMap(addressFields);
        CreateAddressRequest model = new CreateAddressRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.CREATE_ADDRESS, model, model);
    }

    private HashMap<String, String> getAddressHashMap(final AddressFields addressFields) {
        HashMap<String, String> params = new HashMap<>();
        //Add all the payloads
        params.put(ModelConstants.FIRST_NAME, addressFields.getFirstName());
        params.put(ModelConstants.LAST_NAME, addressFields.getLastName());
        params.put(ModelConstants.TITLE_CODE, addressFields.getTitleCode());
        params.put(ModelConstants.COUNTRY_ISOCODE, addressFields.getCountryIsocode());
        params.put(ModelConstants.LINE_1, addressFields.getLine1());
        params.put(ModelConstants.LINE_2, addressFields.getLine2());
        params.put(ModelConstants.POSTAL_CODE, addressFields.getPostalCode());
        params.put(ModelConstants.TOWN, addressFields.getTown());
        params.put(ModelConstants.PHONE_NUMBER, addressFields.getPhoneNumber());
        return params;
    }

    void setHybrisDelegate(HybrisDelegate delegate) {
        mDelegate = delegate;
    }

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    void setStore(Store store) {
        mStore = store;
    }

    Store getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }

    public void deleteAddress(String addressId) {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put(ModelConstants.ADDRESS_ID, addressId);

        DeleteAddressRequest model = new DeleteAddressRequest(getStore(), query, this);
        getHybrisDelegate().sendRequest(RequestCode.DELETE_ADDRESS, model, model);
    }

    public void updateAddress(HashMap<String, String> query) {

        UpdateAddressRequest model = new UpdateAddressRequest(getStore(), query, this);
        getHybrisDelegate().sendRequest(RequestCode.UPDATE_ADDRESS, model, model);
    }

    public void getShippingAddresses() {
        GetAddressRequest model = new GetAddressRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_ADDRESS, model, model);
    }

    @Override
    public void onModelDataLoadFinished(Message msg) {

        int requestCode = msg.what;
        switch (requestCode) {
            case RequestCode.DELETE_ADDRESS:
                mAddressListener.onFetchAddressSuccess(msg);
                Utility.dismissProgressDialog();
                break;
            case RequestCode.UPDATE_ADDRESS:
                mAddressListener.onFetchAddressSuccess(msg);
                Utility.dismissProgressDialog();
                break;
            case RequestCode.CREATE_ADDRESS: {
                if (mAddressListener != null) {
                    mAddressListener.onCreateAddress(true);
                }
                break;
            }
            case RequestCode.GET_ADDRESS: {
                if (mAddressListener != null) {
                    mAddressListener.onFetchAddressSuccess(msg);
                }
                break;
            }
        }
    }

    @Override
    public void onModelDataError(Message msg) {
        int requestCode = msg.what;

        switch (requestCode) {
            case RequestCode.DELETE_ADDRESS:
                mAddressListener.onFetchAddressFailure(msg);
                Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                Utility.dismissProgressDialog();
                break;
            case RequestCode.UPDATE_ADDRESS:
                mAddressListener.onFetchAddressFailure(msg);
                Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                Utility.dismissProgressDialog();
                break;
            case RequestCode.CREATE_ADDRESS: {
                if (mAddressListener != null) {
                    mAddressListener.onCreateAddress(false);
                }
                break;
            }
            case RequestCode.GET_ADDRESS:
                if (mAddressListener != null) {
                    mAddressListener.onFetchAddressFailure(msg);
                }
                showMessage("Unable to get address");
                break;
        }
    }

    void showMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
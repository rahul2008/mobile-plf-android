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

        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CreateAddressRequest model = new CreateAddressRequest(delegate.getStore(), params, null);
        delegate.sendRequest(RequestCode.CREATE_ADDRESS, model, model);
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
            mStore = mDelegate.getStore();
        }
        return mStore;
    }
    public void deleteAddress(String addressId) {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put(ModelConstants.ADDRESS_ID, addressId);

        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        DeleteAddressRequest model = new DeleteAddressRequest(getStore(), query, this);

        getHybrisDelegate().sendRequest(RequestCode.DELETE_ADDRESS, model, model);
    }

    public void updateAddress(HashMap<String, String> query) {

        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
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
                Toast.makeText(mContext, "delete Success", Toast.LENGTH_SHORT).show();
                break;
            case RequestCode.UPDATE_ADDRESS:
                Toast.makeText(mContext, "update Success", Toast.LENGTH_SHORT).show();
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
                showMessage("update Error");
                break;
            case RequestCode.UPDATE_ADDRESS:
                showMessage("update Error");
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
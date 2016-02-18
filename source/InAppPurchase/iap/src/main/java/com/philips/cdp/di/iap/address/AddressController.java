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
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
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
        void onFinish(GetShippingAddressData shippingAddresses);
    }

    public AddressController(Context context, AddressListener listener) {
        mContext = context;
        mAddressListener = listener;
        mDelegate = HybrisDelegate.getInstance(context);
        mStore = mDelegate.getStore();
    }

    public void createAddress() {
        HashMap<String, String> params = new HashMap<>();

        //Add all the payloads
        params.put(ModelConstants.FIRST_NAME, "");
        params.put(ModelConstants.LAST_NAME, "");
        params.put(ModelConstants.TITLE_CODE, "");
        params.put(ModelConstants.COUNTRY_ISOCODE, "");
        params.put(ModelConstants.LINE_1, "");
        params.put(ModelConstants.LINE_2, "");
        params.put(ModelConstants.POSTAL_CODE, "");
        params.put(ModelConstants.TOWN, "");
        params.put(ModelConstants.PHONE_NUMBER, "");

        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CreateAddressRequest model = new CreateAddressRequest(delegate.getStore(), params, null);
        delegate.sendRequest(RequestCode.CREATE_ADDRESS, model, model);
    }

    public void deleteAddress(String addressId){
        HashMap<String, String> query = new HashMap<String, String>();
        query.put(ModelConstants.ADDRESS_ID, addressId);

        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        DeleteAddressRequest model = new DeleteAddressRequest(delegate.getStore(), query, this);

        delegate.sendRequest(RequestCode.DELETE_ADDRESS, model, model);
    }

    public void updateAddress(HashMap<String, String> query){

        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        UpdateAddressRequest model = new UpdateAddressRequest(delegate.getStore(), query, this);

        delegate.sendRequest(RequestCode.UPDATE_ADDRESS, model, model);
    }

    @Override
    public void onModelDataLoadFinished(Message msg) {
        int requestCode = msg.what;
        switch (requestCode){
            case RequestCode.DELETE_ADDRESS:
                Toast.makeText(mContext, "delete Success", Toast.LENGTH_SHORT).show();
                break;
            case RequestCode.UPDATE_ADDRESS:
                Toast.makeText(mContext,"update Success",Toast.LENGTH_SHORT).show();
                break;
            case RequestCode.CREATE_ADDRESS:
                break;
        }
    }

    public void getShippingAddresses() {
        GetAddressRequest model = new GetAddressRequest(mStore,null, this);
        mDelegate.sendRequest(RequestCode.GET_ADDRESS, model, model);
    }

    @Override
    public void onModelDataError(Message msg) {
        int requestCode = msg.what;

        switch (requestCode){
            case RequestCode.DELETE_ADDRESS:
                Toast.makeText(mContext,"delete Error",Toast.LENGTH_SHORT).show();
                break;
            case RequestCode.UPDATE_ADDRESS:
                Toast.makeText(mContext,"update Error",Toast.LENGTH_SHORT).show();
                break;
            case RequestCode.CREATE_ADDRESS:
                break;
            case RequestCode.GET_ADDRESS:
                 GetShippingAddressData addresses = (GetShippingAddressData) msg.obj;
                break;
        }
    }
}
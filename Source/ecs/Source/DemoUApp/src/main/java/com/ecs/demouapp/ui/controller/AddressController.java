/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.controller;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.ecs.demouapp.ui.address.AddressFields;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.session.RequestCode;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.Country;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.address.Region;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrors;

import java.util.HashMap;
import java.util.Locale;

public class AddressController {

    private Context mContext;
    private AddressListener mAddressListener;
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

        ECSUtility.getInstance().getEcsServices().getRegions(new ECSCallback<RegionsList, Exception>() {
            @Override
            public void onResponse(RegionsList result) {
                CartModelContainer.getInstance().setRegionList(result);
                Message message = new Message();
                message.obj = result;
                mAddressListener.onGetRegions(message);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                Message message = new Message();
                message.obj = error;
                mAddressListener.onGetRegions(message);
            }
        });
    }


    public static   Addresses getAddressesObject(AddressFields addressFields){

        Addresses addressRequest = new Addresses();

        addressRequest.setFirstName(addressFields.getFirstName());
        addressRequest.setLastName(addressFields.getLastName());
        addressRequest.setTitleCode(addressFields.getTitleCode());
        Country country= new Country();
        country.setIsocode(ECSConfig.INSTANCE.getCountry());
        //country.se
        addressRequest.setCountry(country); // iso
        addressRequest.setLine1(addressFields.getLine1());
        //   addressRequest.setLine2(shippingAddressFields.getLine2());
        addressRequest.setPostalCode(addressFields.getPostalCode());
        addressRequest.setTown(addressFields.getTown());
        addressRequest.setPhone1(addressFields.getPhone1());
        addressRequest.setPhone2(addressFields.getPhone2());
        Region region = new Region();
        region.setIsocodeShort(addressFields.getRegionIsoCode());
        addressRequest.setRegion(region); // set Region eg State for US and Canada
        addressRequest.setHouseNumber(addressFields.getHouseNumber());
        return addressRequest;
    }

    public void createAddress(AddressFields addressFields) {
      /*  HashMap<String, String> params = getAddressHashMap(addressFields);
        CreateAddressRequest model = new CreateAddressRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.CREATE_ADDRESS, model, model);*/
        Addresses addressRequest=getAddressesObject(addressFields);


        ECSUtility.getInstance().getEcsServices().createNewAddress(addressRequest, new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses result) {
                //addressContractor.hideProgressbar();
                if(null!=result){
                    Log.v("ECS ADDRESS",""+result.toString());

                    Message message = new Message();
                    message.obj=result;
                    mAddressListener.onCreateAddress(message);
                }

            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                //addressContractor.hideProgressbar();

                Log.v("ECS ADDRESS",""+detailErrorMessage);
            }
        },true);

    }

    public void getAddresses() {

        ECSUtility.getInstance().getEcsServices().getListSavedAddress(new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData result) {


                Message message = new Message();
                if(result==null || result.getAddresses() ==null || result.getAddresses().size() ==0){
                    message.obj ="";
                }else{
                    message.obj = result;
                }
                mAddressListener.onGetAddress(message);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {

                Message message = new Message();
                message.obj = error;
                mAddressListener.onGetAddress(message);
            }
        });

        //GetAddressRequest model = new GetAddressRequest(getStore(), null, this);
        //getHybrisDelegate().sendRequest(RequestCode.GET_ADDRESS, model, model);
    }

    public void deleteAddress(Addresses address) {

        ECSUtility.getInstance().getEcsServices().deleteAddress(address, new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData result) {

                Message message = new Message();
                message.obj = result;
                mAddressListener.onGetAddress(message);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {

                Message message = new Message();
                message.obj= error;
                mAddressListener.onGetAddress(message);

            }
        });
        /*HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ADDRESS_ID, addressId);

        DeleteAddressRequest model = new DeleteAddressRequest(getStore(), query, this);
        getHybrisDelegate().sendRequest(RequestCode.DELETE_ADDRESS, model, model);*/
    }

    public void updateAddress(AddressFields addressFields, String addressID) {
        Addresses addresses=getAddressesObject(addressFields);
        addresses.setId(addressID);
        updateAddress(addresses);

    }

    public void updateAddress(Addresses addresses){
        ECSUtility.getInstance().getEcsServices().updateAddress(addresses, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {

                Message message = new Message();
                message.obj =result;
                message.what = RequestCode.UPDATE_ADDRESS;
                mAddressListener.onGetAddress(message);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {

                ECSErrors.showECSAlertDialog(mContext,"Error",detailErrorMessage);
                Message message = new Message();
                message.obj = error;
                mAddressListener.onGetAddress(message);
            }
        });

    }

    public void setDeliveryAddress(Addresses addresses) {
        addresses.setDefaultAddress(true);
       /* address.getId()
        if (null != pAddressId) {
            HashMap<String, String> params = new HashMap<>();
            params.put(ModelConstants.ADDRESS_ID, pAddressId);
            SetDeliveryAddressRequest model = new SetDeliveryAddressRequest(getStore(), params, this);
            getHybrisDelegate().sendRequest(RequestCode.SET_DELIVERY_ADDRESS, model, model);
        }*/

        ECSUtility.getInstance().getEcsServices().setDeliveryAddress(addresses, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                Message message = new Message() ;
                message.obj = result;
                mAddressListener.onSetDeliveryAddress(message);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                Message message = new Message() ;
                message.obj = detailErrorMessage;
                mAddressListener.onSetDeliveryAddress(message);
            }
        });
    }

    public void getDeliveryModes() {

        ECSUtility.getInstance().getEcsServices().getDeliveryModes(new ECSCallback<GetDeliveryModes, Exception>() {
            @Override
            public void onResponse(GetDeliveryModes result) {
                Message message = new Message();
                message.obj = result;
                mAddressListener.onGetDeliveryModes(message);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                Message message = new Message();
                message.obj = error;
                mAddressListener.onGetDeliveryModes(message);
            }
        });
        /*GetDeliveryModesRequest model = new GetDeliveryModesRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_DELIVERY_MODE, model, model);*/
    }

    public void setDeliveryMode(String deliveryMode) {

        ECSUtility.getInstance().getEcsServices().setDeliveryMode(deliveryMode, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                Message message = new Message();
                message.obj = result;
                mAddressListener.onSetDeliveryMode(message);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                Message message = new Message();
                message.obj = error;
                mAddressListener.onSetDeliveryMode(message);
            }
        });

       /* HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.DELIVERY_MODE_ID, deliveryMode);

        SetDeliveryAddressModeRequest model = new SetDeliveryAddressModeRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.SET_DELIVERY_MODE, model, model);*/
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
        updateAddress(address);
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


}
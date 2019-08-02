package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.constants.ModelConstants;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrors;

import java.util.HashMap;
import java.util.Map;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UpdateAddressRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String>  {

    private final Addresses addresses;
    private final ECSCallback<Boolean,Exception> ecsCallback;

    public UpdateAddressRequest(Addresses addresses, ECSCallback<Boolean, Exception> ecsCallback) {
        this.addresses = addresses;
        this.ecsCallback = ecsCallback;
    }


    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getEditAddressUrl(addresses.getId());
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> addressHashMap = new HashMap<>();

        if(addresses.getFirstName()!=null)
        addressHashMap.put(ModelConstants.FIRST_NAME, addresses.getFirstName());

        if(addresses.getLastName()!=null)
        addressHashMap.put(ModelConstants.LAST_NAME, addresses.getLastName());

        if(addresses.getTitleCode()!=null)
        addressHashMap.put(ModelConstants.TITLE_CODE,addresses.getTitleCode());

        if(addresses.getCountry()!=null && addresses.getCountry().getIsocode()!=null)
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE,addresses.getCountry().getIsocode());

        if(addresses.getHouseNumber()!=null)
        addressHashMap.put(ModelConstants.HOUSE_NO,addresses.getHouseNumber());

        if(addresses.getLine1()!=null)
        addressHashMap.put(ModelConstants.LINE_1,addresses.getLine1());

        if(addresses.getLine2()!=null)
        addressHashMap.put(ModelConstants.LINE_2, addresses.getLine2());

        if(addresses.getPostalCode()!=null)
        addressHashMap.put(ModelConstants.POSTAL_CODE, addresses.getPostalCode());

        if(addresses.getTown()!=null)
        addressHashMap.put(ModelConstants.TOWN, addresses.getTown());

        if(addresses.getPhone1()!=null)
        addressHashMap.put(ModelConstants.PHONE_1,addresses.getPhone1());

        if(addresses.getPhone2()!=null)
        addressHashMap.put(ModelConstants.PHONE_2, addresses.getPhone2());


        if(addresses.getRegion()!=null && addresses.getRegion().getIsocode()!=null) {
            addressHashMap.put(ModelConstants.REGION_ISOCODE, addresses.getRegion().getIsocode());
        }


        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, String.valueOf(addresses.isDefaultAddress()));

        if(addresses.getId()!=null)
        addressHashMap.put(ModelConstants.ADDRESS_ID, addresses.getId());

        return addressHashMap;
    }


    @Override
    public void onResponse(String response) {
        ecsCallback.onResponse(true);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Print error from updateAddress"+ECSErrors.getDetailErrorMessage(error));
        ecsCallback.onFailure(ECSErrors.getErrorMessage(error),ECSErrors.getDetailErrorMessage(error),9000);
    }

    @Override
    public Response.Listener<String> getStringSuccessResponseListener() {
        return this;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }
}

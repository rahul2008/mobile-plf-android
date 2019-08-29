package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.Country;
import com.philips.cdp.di.ecs.model.address.Region;
import com.philips.cdp.di.ecs.util.ECSConfig;

public class StaticBlock {

public static final String  mockAccessToken = "acceesstoken";

    public static void initialize(){
        ECSConfig.INSTANCE.setBaseURL("acc.us.pil.shop.philips.com/");
        ECSConfig.INSTANCE.setSiteId("US_Tuscany");
        ECSConfig.INSTANCE.setPropositionID("Tuscany2016");
        ECSConfig.INSTANCE.setLocale("en_US");

        ECSConfig.INSTANCE.setAuthToken(mockAccessToken);
    }

    public static  String getBaseURL(){
        return ECSConfig.INSTANCE.getBaseURL();
    }

    public static String getSiteID(){
        return ECSConfig.INSTANCE.getSiteId();
    }

    public static String getPropositionID(){
        return ECSConfig.INSTANCE.getPropositionID();
    }

    public static String getLocale(){
        return ECSConfig.INSTANCE.getLocale();
    }


    public static Addresses getAddressesObject(){

        Addresses addressRequest = new Addresses();
        addressRequest.setId("1234567");
        addressRequest.setFirstName("First name");
        addressRequest.setLastName("Second name");
        addressRequest.setTitleCode("Mr");
        Country country= new Country();
        country.setIsocode("DE");
        //country.se
        addressRequest.setCountry(country); // iso
        addressRequest.setLine1("Line 1");
        //   addressRequest.setLine2(shippingAddressFields.getLine2());
        addressRequest.setPostalCode("10111");
        addressRequest.setTown("Berlin");
        addressRequest.setPhone1("5043323");
        addressRequest.setPhone2("5043323");
        Region region = new Region();
        region.setIsocodeShort("Region");
        addressRequest.setRegion(region); // set Region eg State for US and Canada
        addressRequest.setHouseNumber("12A");
        addressRequest.setDefaultAddress(true);
        return addressRequest;
    }
}

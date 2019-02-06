package com.philips.cdp.di.iap.Constants;

/**
 * Created by philips on 2/5/19.
 */

public enum AddressFieldJsonEnum {

    SALUTATION("salutation"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastNme"),
    EMAIL("email"),
    PHONE("phone"),
    HOUSEN_UMBER("houseNumber"),
    ADDRESS_ONE("address1"),
    ADDRESS_TWO("address2"),
    TOWN("town"),
    POSTAL_CODE("postalCode"),
    STATE ("state"),
    COUNTRY ("country");

   private String field ;


   AddressFieldJsonEnum(String field){
       this.field = field;
   }

   public String getField(){
       return this.field;
    }

    public static AddressFieldJsonEnum getAddressFieldJsonEnumFromField(String field){
        AddressFieldJsonEnum[] values = AddressFieldJsonEnum.values();

        for(AddressFieldJsonEnum addressFieldJsonEnum:values){

            if(addressFieldJsonEnum.getField().equalsIgnoreCase(field.trim())){
                return addressFieldJsonEnum;
            }
        }
        return null;
    }
}

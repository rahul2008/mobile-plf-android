package com.ecs.demouapp.ui.Constants;

/**
 * Created by philips on 2/5/19.
 */

public enum AddressFieldJsonEnum {

    SALUTATION("salutation"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    PHONE("phone"),
    HOUSE_NUMBER("houseNumber"),
    ADDRESS_ONE("address1"),
    ADDRESS_TWO("address2"),
    TOWN("town"),
    POSTAL_CODE("postalCode"),
    STATE ("state"),
    COUNTRY ("country");

   private String addressField;


   AddressFieldJsonEnum(String addressField){
       this.addressField = addressField;
   }

   public String getAddressField(){
       return this.addressField;
    }

    public static AddressFieldJsonEnum getAddressFieldJsonEnumFromField(String field){
        AddressFieldJsonEnum[] values = AddressFieldJsonEnum.values();

        for(AddressFieldJsonEnum addressFieldJsonEnum:values){

            if(addressFieldJsonEnum.getAddressField().equalsIgnoreCase(field.trim())){
                return addressFieldJsonEnum;
            }
        }
        return null;
    }
}

package com.philips.platform.pif.DataInterface.USR;

public interface UserProfileInterface {
    String getFirstName();
    String getLastName();
    String getGender();
    String getEmail();
    String getMobileNumber();
    String getBirthday();
    String getAddress();
    String getReceiveMarketingEmail();

    String getUUID();
    String getAccessToken();
}

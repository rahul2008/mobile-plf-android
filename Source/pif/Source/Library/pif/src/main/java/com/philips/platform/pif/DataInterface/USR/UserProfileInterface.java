package com.philips.platform.pif.DataInterface.USR;

import java.util.Date;

public interface UserProfileInterface {
    String getFirstName();

    String getLastName();

    String getGender();

    String getEmail();

    String getMobileNumber();

    //TODo: Convert String to Date in implemnetation
    Date getBirthDate();

    String getAddress();

    String getReceiveMarketingEmail();

    String getUUID();

    String getAccessToken();
}

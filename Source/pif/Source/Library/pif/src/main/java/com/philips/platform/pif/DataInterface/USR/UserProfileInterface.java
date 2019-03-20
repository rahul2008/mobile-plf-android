package com.philips.platform.pif.DataInterface.USR;

import java.util.Date;
import java.util.Map;

public interface UserProfileInterface {
    String getFirstName();

    String getLastName();

    String getGender();

    String getEmail();

    String getMobileNumber();

    //TODo: Convert String to Date in implemnetation
    Date getBirthDate();

    Map<String, Object> getAddress();



    String getUUID();

    String getAccessToken();
}

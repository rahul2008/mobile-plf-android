package com.philips.platform.pim.models;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.DataInterface.USR.UserProfileInterface;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.utilities.PIMUserDetails;

import net.openid.appauth.AuthState;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMOIDCUserProfile implements UserProfileInterface {

    private final String TAG = PIMOIDCUserProfile.class.getSimpleName();
    private LoggingInterface mLoggingInterface;
    private Map<String, Object> mUserProfileMap;
    private String profileJson;
    private AuthState authState;
    private final String ADDRESS_ADDRESS1 = "address.address1";
    private final String ADDRESS_ADDRESS2 = "address.address2";
    private final String ADDRESS_ADDRESS3 = "address.address3";
    private final String ADDRESS_HOUSE_NUMBER = "address.house_number";
    private final String ADDRESS_LOCALITY = "address.locality";
    private final String ADDRESS_COUNTRY = "address.country";
    private final String ADDRESS_REGION = "address.region";
    private final String ADDRESS_POSTAL_CODE = "address.postal_code";

    //AuthState can be null.
    public PIMOIDCUserProfile(SecureStorageInterface secureStorageInterface, AuthState authState) {
        this.authState = authState;
        mUserProfileMap = new HashMap<>();
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        parseUserProfileDataToMap(secureStorageInterface);
    }

    @Override
    public String getFirstName() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        return mUserProfileMap.get(PIMUserDetails.FIRST_NAME).toString();
    }

    @Override
    public String getLastName() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        return mUserProfileMap.get(PIMUserDetails.LAST_NAME).toString();
    }

    @Override
    public String getGender() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        return mUserProfileMap.get(PIMUserDetails.GENDER).toString();
    }

    @Override
    public String getEmail() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        return mUserProfileMap.get(PIMUserDetails.EMAIL).toString();
    }

    @Override
    public String getMobileNumber() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        return mUserProfileMap.get(PIMUserDetails.MOBILE_NUMBER).toString();
    }

    @Override
    public Date getBirthDate() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        try {
            return sdf.parse(mUserProfileMap.get(PIMUserDetails.BIRTHDAY).toString());
        } catch (ParseException e) {
            mLoggingInterface.log(DEBUG,TAG,"getBirthDate : exception is parsing date");
            return null;
        }
    }

    @Override
    public Map getAddress() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        Map<String,Object> addresMap = new HashMap<>();
        if(mUserProfileMap.get(ADDRESS_ADDRESS1) != null)
            addresMap.put(ADDRESS_ADDRESS1,mUserProfileMap.get(ADDRESS_ADDRESS1));
        if(mUserProfileMap.get(ADDRESS_ADDRESS2) != null)
            addresMap.put(ADDRESS_ADDRESS2,mUserProfileMap.get(ADDRESS_ADDRESS2));
        if(mUserProfileMap.get(ADDRESS_ADDRESS3) != null)
            addresMap.put(ADDRESS_ADDRESS3,mUserProfileMap.get(ADDRESS_ADDRESS3));
        if(mUserProfileMap.get(ADDRESS_HOUSE_NUMBER) != null)
            addresMap.put(ADDRESS_HOUSE_NUMBER,mUserProfileMap.get(ADDRESS_HOUSE_NUMBER));
        if(mUserProfileMap.get(ADDRESS_LOCALITY) != null)
            addresMap.put(ADDRESS_LOCALITY,mUserProfileMap.get(ADDRESS_LOCALITY));
        if(mUserProfileMap.get(ADDRESS_COUNTRY) != null)
            addresMap.put(ADDRESS_COUNTRY,mUserProfileMap.get(ADDRESS_COUNTRY));
        if(mUserProfileMap.get(ADDRESS_REGION) != null)
            addresMap.put(ADDRESS_REGION,mUserProfileMap.get(ADDRESS_REGION));
        if(mUserProfileMap.get(ADDRESS_POSTAL_CODE) != null)
            addresMap.put(ADDRESS_POSTAL_CODE,mUserProfileMap.get(ADDRESS_POSTAL_CODE));

        return addresMap;
    }

    @Override
    public String getUUID() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        return mUserProfileMap.get(PIMUserDetails.UUID).toString();
    }

    @Override
    public String getAccessToken() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        return mUserProfileMap.get(PIMUserDetails.ACCESS_TOKEN).toString();
    }

    String getReceiveMarketingEmail() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        return mUserProfileMap.get(PIMUserDetails.RECEIVE_MARKETING_EMAIL).toString();
    }

    Date getReceiveMarketingEmailGivenTimestamp() {
        if(mUserProfileMap == null) {
            mLoggingInterface.log(DEBUG,TAG,"UserDetails not found");
            return null;
        }
        return null;
    }

    protected Map<String, String> fetchUserDetails(ArrayList<String> stringArrayList) {
        return null;
    }

    private void parseUserProfileDataToMap(SecureStorageInterface secureStorageInterface) {
        String userProfileString = secureStorageInterface.fetchValueForKey("uuid_userprofile",new SecureStorageInterface.SecureStorageError());
        if(userProfileString == null){
            mLoggingInterface.log(DEBUG,TAG,"fetchValueForKey returns null");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(userProfileString);
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                mUserProfileMap.put(key, jsonObject.get(key));
            }
        } catch (JSONException e) {
           mLoggingInterface.log(DEBUG,TAG,"parseUserProfileDataToMap : Exception in fetching User Details ");
        }
    }
}

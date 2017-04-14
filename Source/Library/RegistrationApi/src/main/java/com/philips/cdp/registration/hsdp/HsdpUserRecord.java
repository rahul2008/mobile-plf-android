package com.philips.cdp.registration.hsdp;

import com.philips.dhpclient.util.MapUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 310190722 on 9/21/2015.
 */
public class HsdpUserRecord implements Serializable {

    private Profile mProfile;
    private String loginId;

    private String userUUID;
    private int userIsActive;
    private AccessCredential accessCredential;

    private String refreshSecret;

    private static final long serialVersionUID = 6128016096756071380L;



    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Profile getProfile() {
        return mProfile;
    }

    public void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }

    public String getRefreshSecret() {
        return refreshSecret;
    }

    public class AccessCredential implements Serializable{

        private String refreshToken;
        private String accessToken;
        private int expiresIn;

        private static final long serialVersionUID = 2128016096756071380L;
        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }
    }

    class Profile implements Serializable{
        private String givenName;
        private String middleName;
        private String gender;
        private String birthday;
        private String preferredLanguage;
        private String receiveMarketingEmail;
        private String currentLocation;
        private String displayName;
        private String familyName;
        private String locale;
        private String timeZone;
        private String height;
        private String weight;

        private AccessCredential accessCredential;

        private static final long serialVersionUID = 1128016096756071380L;

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getPreferredLanguage() {
            return preferredLanguage;
        }

        public void setPreferredLanguage(String preferredLanguage) {
            this.preferredLanguage = preferredLanguage;
        }

        public String getReceiveMarketingEmail() {
            return receiveMarketingEmail;
        }

        public void setReceiveMarketingEmail(String receiveMarketingEmail) {
            this.receiveMarketingEmail = receiveMarketingEmail;
        }

        public String getCurrentLocation() {
            return currentLocation;
        }

        public void setCurrentLocation(String currentLocation) {
            this.currentLocation = currentLocation;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public PrimaryAddress getPrimaryAddress() {
            return primaryAddress;
        }

        public void setPrimaryAddress(PrimaryAddress primaryAddress) {
            this.primaryAddress = primaryAddress;
        }

        private PrimaryAddress primaryAddress;

        public void setPhotos(ArrayList<Photo> photos) {
            this.photos = photos;
        }

        class PrimaryAddress implements Serializable {

            private static final long serialVersionUID = 3128016096756071380L;
            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            private String country;
        }

        public ArrayList<Photo> getPhotos() {
            return photos;
        }

        //DhpUserIdentity.Photo
        private ArrayList<Photo> photos;

        public class Photo implements Serializable{
            private static final long serialVersionUID = 4128016096756071380L;
            private String type;
            private String value;

            public Photo(String type, String value) {
                this.type = type;
                this.value = value;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    public HsdpUserRecord parseHsdpUserInfo(Map<String, Object> rawResponse) {

        mProfile = new Profile();
        Profile.PrimaryAddress primaryAddress = mProfile.new PrimaryAddress();
        mProfile.setPrimaryAddress(primaryAddress);
        String loginId = MapUtils.extract(rawResponse, "exchange.user.loginId");
        setLoginId(loginId);

        String givenName = MapUtils.extract(rawResponse, "exchange.user.profile.givenName");
        mProfile.setGivenName(givenName);

        String middleName = MapUtils.extract(rawResponse, "exchange.user.profile.middleName");
        mProfile.setMiddleName(middleName);

        String gender = MapUtils.extract(rawResponse, "exchange.user.profile.gender");
        mProfile.setGender(gender);

        String birthday = MapUtils.extract(rawResponse, "exchange.user.profile.birthday");
        mProfile.setBirthday(birthday);

        String preferredLanguage = MapUtils.extract(rawResponse, "exchange.user.profile.preferredLanguage");
        mProfile.setPreferredLanguage(preferredLanguage);

        String receiveMarketingEmail = MapUtils.extract(rawResponse, "exchange.user.profile.receiveMarketingEmail");
        mProfile.setReceiveMarketingEmail(receiveMarketingEmail);

        String currentLocation = MapUtils.extract(rawResponse, "exchange.user.profile.currentLocation");
        mProfile.setCurrentLocation(currentLocation);

        String displayName = MapUtils.extract(rawResponse, "exchange.user.profile.displayName");
        mProfile.setDisplayName(displayName);

        String familyName = MapUtils.extract(rawResponse, "exchange.user.profile.familyName");
        mProfile.setFamilyName(familyName);

        String locale = MapUtils.extract(rawResponse, "exchange.user.profile.locale");
        mProfile.setLocale(locale);

        String timeZone = MapUtils.extract(rawResponse, "exchange.user.profile.timeZone");
        mProfile.setTimeZone(timeZone);

        userUUID = MapUtils.extract(rawResponse, "exchange.user.userUUID");
        userIsActive = MapUtils.extract(rawResponse, "exchange.user.userIsActive");

        List<Map<String, String>> rawPhotos = MapUtils.extract(rawResponse, "exchange.user.profile.photos");
        ArrayList<Profile.Photo> photos = new ArrayList<>();
        if (null != rawPhotos) {
            for (Map<String, String> photoMap : rawPhotos)
                photos.add(mProfile.new Photo(photoMap.get("type"), photoMap.get("value")));
        }
        mProfile.setPhotos(photos);

        accessCredential =  new AccessCredential();
        String refreshToken = MapUtils.extract(rawResponse, "exchange.accessCredential.refreshToken");
        accessCredential.setRefreshToken(refreshToken);

        String accessToken = MapUtils.extract(rawResponse, "exchange.accessCredential.accessToken");
        accessCredential.setAccessToken(accessToken);

        String expiresInString = MapUtils.extract(rawResponse, "exchange.accessCredential.expiresIn");
        Integer expiresIn = Integer.parseInt(String.valueOf(expiresInString)) ;
        accessCredential.setExpiresIn(expiresIn.intValue());

        return this;
    }
    public String getUserUUID() {
        return userUUID;
    }

    public int getUserIsActive() {
        return userIsActive;
    }

    public AccessCredential getAccessCredential() {
        return accessCredential;
    }
}


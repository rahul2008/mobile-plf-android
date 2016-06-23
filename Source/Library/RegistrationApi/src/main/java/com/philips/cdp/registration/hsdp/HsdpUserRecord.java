/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.hsdp;

import android.content.Context;

import com.philips.dhpclient.util.MapUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 310190722 on 9/21/2015.
 */

/**
 * Class hsdp user record
 */
public class HsdpUserRecord implements Serializable {

    private Profile mProfile;
    private String loginId;
    private transient Context context;
    private String userUUID;
    private int userIsActive;
    private AccessCredential accessCredential;
    private String refreshSecret;

    private static final long serialVersionUID = 6128016096756071380L;


    /**
     * Class constructor
     *
     * @param context
     */
    public HsdpUserRecord(Context context) {
        this.context = context;
    }

    /**
     * get login id
     *
     * @return loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * Set login id
     *
     * @param loginId login id
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Get profile
     * {@link Profile}
     *
     * @return mProfile profile
     */
    public Profile getProfile() {
        return mProfile;
    }

    /**
     * Class Access credential
     */
    public class AccessCredential implements Serializable {

        private String refreshToken;
        private String accessToken;
        private int expiresIn;

        private static final long serialVersionUID = 2128016096756071380L;

        /**
         * get refresh token
         *
         * @return refreshToken refresh token
         */
        public String getRefreshToken() {
            return refreshToken;
        }

        /**
         * Set Refresh token
         *
         * @param refreshToken refresh token
         */
        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        /**
         * Get access token
         *
         * @return access token
         */
        public String getAccessToken() {
            return accessToken;
        }

        /**
         * set access token
         *
         * @param accessToken access token
         */
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        /**
         * get expires in
         *
         * @return expires in
         */
        public int getExpiresIn() {
            return expiresIn;
        }

        /**
         * set expires in
         *
         * @param expiresIn expries in
         */
        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }
    }

    class Profile implements Serializable {
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

        /**
         * Get given name
         *
         * @return givenName givnen name
         */
        public String getGivenName() {
            return givenName;
        }

        /**
         * set given name
         *
         * @param givenName given name
         */
        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        /**
         * get middle name
         *
         * @return middleName middle name
         */
        public String getMiddleName() {
            return middleName;
        }

        /**
         * set middle name
         *
         * @param middleName middle name
         */
        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        /**
         * get gender
         *
         * @return gender
         */
        public String getGender() {
            return gender;
        }

        /**
         * set gender
         *
         * @param gender gender
         */
        public void setGender(String gender) {
            this.gender = gender;
        }

        /**
         * Get birthday
         *
         * @return birthday birthday
         */
        public String getBirthday() {
            return birthday;
        }

        /**
         * set birth day
         *
         * @param birthday
         */
        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        /**
         * get preferred language
         *
         * @return
         */
        public String getPreferredLanguage() {
            return preferredLanguage;
        }

        /**
         * set preferred language
         *
         * @param preferredLanguage
         */
        public void setPreferredLanguage(String preferredLanguage) {
            this.preferredLanguage = preferredLanguage;
        }

        /**
         * get received marketing email
         *
         * @return
         */
        public String getReceiveMarketingEmail() {
            return receiveMarketingEmail;
        }

        /**
         * set received marketing email
         *
         * @param receiveMarketingEmail
         */
        public void setReceiveMarketingEmail(String receiveMarketingEmail) {
            this.receiveMarketingEmail = receiveMarketingEmail;
        }

        /**
         * get current location
         *
         * @return
         */
        public String getCurrentLocation() {
            return currentLocation;
        }

        /**
         * set current location
         *
         * @param currentLocation
         */
        public void setCurrentLocation(String currentLocation) {
            this.currentLocation = currentLocation;
        }

        /**
         * get display name
         *
         * @return display name
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * set display name
         *
         * @param displayName display name
         */
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        /**
         * get family name
         *
         * @return
         */
        public String getFamilyName() {
            return familyName;
        }

        /**
         * set family name
         *
         * @param familyName
         */
        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        /**
         * get locale
         *
         * @return locale
         */
        public String getLocale() {
            return locale;
        }

        /**
         * set locale
         *
         * @param locale locale
         */
        public void setLocale(String locale) {
            this.locale = locale;
        }

        /**
         * Get time zone
         *
         * @return timeZone time zone
         */
        public String getTimeZone() {
            return timeZone;
        }

        /**
         * set time zone
         *
         * @param timeZone time zone
         */
        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        /**
         * get height
         *
         * @return height
         */
        public String getHeight() {
            return height;
        }

        /**
         * set height
         *
         * @param height height
         */
        public void setHeight(String height) {
            this.height = height;
        }

        /**
         * get height
         *
         * @return height
         */
        public String getWeight() {
            return weight;
        }

        /**
         * set weight
         *
         * @param weight weight
         */
        public void setWeight(String weight) {
            this.weight = weight;
        }

        /**
         * Get primary address
         * {@link PrimaryAddress}
         *
         * @return primaryAddress primary address
         */
        public PrimaryAddress getPrimaryAddress() {
            return primaryAddress;
        }

        /**
         * Set primary address
         *
         * @param primaryAddress
         */
        public void setPrimaryAddress(PrimaryAddress primaryAddress) {
            this.primaryAddress = primaryAddress;
        }

        private PrimaryAddress primaryAddress;

        /**
         * Set photos
         *
         * @param photos
         */
        public void setPhotos(ArrayList<Photo> photos) {
            this.photos = photos;
        }

        /**
         * Class primary address
         */
        class PrimaryAddress implements Serializable {

            private static final long serialVersionUID = 3128016096756071380L;

            /**
             * get country
             *
             * @return country
             */
            public String getCountry() {
                return country;
            }

            /**
             * set country
             *
             * @param country
             */
            public void setCountry(String country) {
                this.country = country;
            }

            private String country;
        }

        /**
         * get photos
         *
         * @return
         */
        public ArrayList<Photo> getPhotos() {
            return photos;
        }

        //DhpUserIdentity.Photo
        private ArrayList<Photo> photos;

        /**
         *
         */
        public class Photo implements Serializable {
            private static final long serialVersionUID = 4128016096756071380L;
            private String type;
            private String value;

            /**
             * class constructor
             *
             * @param type
             * @param value
             */
            public Photo(String type, String value) {
                this.type = type;
                this.value = value;
            }

            /**
             * get type
             *
             * @return type
             */
            public String getType() {
                return type;
            }

            /**
             * set type
             *
             * @param type type
             */
            public void setType(String type) {
                this.type = type;
            }

            /**
             * get value
             *
             * @return value
             */
            public String getValue() {
                return value;
            }

            /**
             * set value
             *
             * @param value
             */
            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    /**
     * Parse hsdp user info
     *
     * @param rawResponse raw response map
     * @return hsdp user record
     */
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

        accessCredential = new AccessCredential();
        String refreshToken = MapUtils.extract(rawResponse, "exchange.accessCredential.refreshToken");
        accessCredential.setRefreshToken(refreshToken);

        String accessToken = MapUtils.extract(rawResponse, "exchange.accessCredential.accessToken");
        accessCredential.setAccessToken(accessToken);

        Integer expiresIn = Integer.parseInt(String.valueOf(MapUtils.extract(rawResponse, "exchange.accessCredential.expiresIn")));
        accessCredential.setExpiresIn(expiresIn.intValue());

        return this;
    }

    /**
     * get user uuid
     *
     * @return userUUID user uuid
     */
    public String getUserUUID() {
        return userUUID;
    }

    /**
     * getUserIsActive
     *
     * @return userIsActive user is active
     */
    public int getUserIsActive() {
        return userIsActive;
    }

    /**
     * Get access credential
     * {@link AccessCredential}
     *
     * @return accessCredential access credential
     */
    public AccessCredential getAccessCredential() {
        return accessCredential;
    }

    /**
     * Get refresh secret
     *
     * @return refreshSecret refresh secret
     */
    public String getRefreshSecret() {
        return refreshSecret;
    }

    /**
     * Set refresh secret
     *
     * @param refreshSecret refresh secret
     */
    public void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }
}


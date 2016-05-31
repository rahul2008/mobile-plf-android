/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.dhpclient.request;

import com.philips.dhpclient.util.Objects;

import java.util.List;

public final class DhpUserIdentity {
    public final static class Profile {
        public final String givenName;
        public final String middleName;
        public final String familyName;
        public final String birthday;
        public final String currentLocation;
        public final String displayName;
        public final String locale;
        public final String gender;
        public final String timeZone;
        public final String preferredLanguage;
        public final Double height;
        public final Double weight;
        public final Address primaryAddress;
        public final List<Photo> photos;

        public Profile(String givenName, String middleName, String familyName, String birthday, String currentLocation, String displayName,
                String locale, String gender, String timeZone, String preferredLanguage, Double height, Double weight, Address primaryAddress, List<Photo> photos) {
            this.givenName = givenName;
            this.middleName = middleName;
            this.familyName = familyName;
            this.birthday = birthday;
            this.currentLocation = currentLocation;
            this.displayName = displayName;
            this.locale = locale;
            this.gender = gender;
            this.timeZone = timeZone;
            this.preferredLanguage = preferredLanguage;
            this.height = height;
            this.weight = weight;
            this.primaryAddress = primaryAddress;
            this.photos = photos;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Profile profile = (Profile) o;
            return Objects.equals(height, profile.height) &&
                   Objects.equals(weight, profile.weight) &&
                   Objects.equals(givenName, profile.givenName) &&
                   Objects.equals(middleName, profile.middleName) &&
                   Objects.equals(familyName, profile.familyName) &&
                   Objects.equals(birthday, profile.birthday) &&
                   Objects.equals(currentLocation, profile.currentLocation) &&
                   Objects.equals(displayName, profile.displayName) &&
                   Objects.equals(locale, profile.locale) &&
                   Objects.equals(gender, profile.gender) &&
                   Objects.equals(timeZone, profile.timeZone) &&
                   Objects.equals(preferredLanguage, profile.preferredLanguage) &&
                   Objects.equals(primaryAddress, profile.primaryAddress) &&
                   Objects.equals(photos, profile.photos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(givenName, middleName, familyName, birthday, currentLocation, displayName, locale, gender, timeZone, preferredLanguage, height, weight,
                    primaryAddress, photos);
        }

        @Override
        public String toString() {
            return "Profile{" +
                   "givenName='" + givenName + '\'' +
                   ", middleName='" + middleName + '\'' +
                   ", familyName='" + familyName + '\'' +
                   ", birthday='" + birthday + '\'' +
                   ", currentLocation='" + currentLocation + '\'' +
                   ", displayName='" + displayName + '\'' +
                   ", locale='" + locale + '\'' +
                   ", gender='" + gender + '\'' +
                   ", timeZone='" + timeZone + '\'' +
                   ", preferredLanguage='" + preferredLanguage + '\'' +
                   ", height=" + height +
                   ", weight=" + weight +
                   ", primaryAddress=" + primaryAddress +
                   ", photos=" + photos +
                   '}';
        }
    }

    public final static class Address {
        public final String country;

        public Address(String country) {
            this.country = country;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Address address = (Address) o;
            return Objects.equals(country, address.country);
        }

        @Override
        public int hashCode() {
            return Objects.hash(country);
        }

        @Override
        public String toString() {
            return "Address{" +
                   "country='" + country + '\'' +
                   '}';
        }
    }

    public final static class Photo {
        public final String type; // jpg, png etc
        public final String value; // base64

        public Photo(String type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Photo photo = (Photo) o;
            return Objects.equals(type, photo.type) &&
                   Objects.equals(value, photo.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }

        @Override
        public String toString() {
            return "Photo{" +
                   "type='" + type + '\'' +
                   ", value='" + value + '\'' +
                   '}';
        }
    }

    public final String loginId;
    public final String password;
    public final Profile profile;

    public DhpUserIdentity(String loginId, String password, Profile profile) {
        this.loginId = loginId;
        this.password = password;
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DhpUserIdentity request = (DhpUserIdentity) o;
        return Objects.equals(loginId, request.loginId) &&
               Objects.equals(password, request.password) &&
               Objects.equals(profile, request.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginId, password, profile);
    }

    @Override
    public String toString() {
        return "DhpUserIdentity{" +
               "loginId='" + loginId + '\'' +
               ", password='" + password + '\'' +
               ", profile=" + profile +
               '}';
    }
}

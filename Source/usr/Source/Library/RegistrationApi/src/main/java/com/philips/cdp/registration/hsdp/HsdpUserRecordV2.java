package com.philips.cdp.registration.hsdp;

import android.os.Parcel;
import android.os.Parcelable;

import com.philips.dhpclient.util.MapUtils;

import java.util.Map;

/**
 * Created by 310190722 on 9/21/2015.
 */
public class HsdpUserRecordV2 implements Parcelable {

    public static final String SS_KEY_FOR_SAVING_RECORD = "HsdpUserRecordV2";

    private String userUUID;

    private AccessCredential accessCredential;

    private String refreshSecret;

    public HsdpUserRecordV2() {
    }

    public void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }

    public String getRefreshSecret() {
        return refreshSecret;
    }

    public class AccessCredential {

        private String refreshToken;

        private String accessToken;

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

    }

    public HsdpUserRecordV2 parseHsdpUserInfo(Map<String, Object> rawResponse) {

        userUUID = MapUtils.extract(rawResponse, "exchange.user.userUUID");

        accessCredential = new AccessCredential();
        String refreshToken = MapUtils.extract(rawResponse, "exchange.accessCredential.refreshToken");
        accessCredential.setRefreshToken(refreshToken);

        String accessToken = MapUtils.extract(rawResponse, "exchange.accessCredential.accessToken");
        accessCredential.setAccessToken(accessToken);

        return this;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public AccessCredential getAccessCredential() {
        return accessCredential;
    }

    public void setAccessCredential(AccessCredential accessCredential) {
        this.accessCredential = accessCredential;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUUID);
        dest.writeString(refreshSecret);
        dest.writeString(accessCredential.refreshToken);
        dest.writeString(accessCredential.accessToken);
    }

    public static final Parcelable.Creator<HsdpUserRecordV2> CREATOR
            = new Parcelable.Creator<HsdpUserRecordV2>() {
        public HsdpUserRecordV2 createFromParcel(Parcel in) {
            return new HsdpUserRecordV2(in);
        }

        public HsdpUserRecordV2[] newArray(int size) {
            return new HsdpUserRecordV2[size];
        }
    };

    private HsdpUserRecordV2(Parcel in) {
        userUUID = in.readString();
        refreshSecret = in.readString();
        accessCredential = new AccessCredential();
        accessCredential.setRefreshToken(in.readString());
        accessCredential.setAccessToken(in.readString());
    }
}


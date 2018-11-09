package com.philips.cdp.registration.hsdp;

import java.util.Map;
import java.util.Objects;

public class HsdpAuthenticationResponse extends HsdpResponse {
    public final String accessToken;
    public final String refreshToken;
    private final Integer expiresIn;
    private final String userId;

    HsdpAuthenticationResponse(Map<String, Object> rawResponse) {
        this(null, null, null, null, rawResponse);
    }

    HsdpAuthenticationResponse(String accessToken, String refreshToken, Integer expiresIn, String userId, Map<String, Object> rawResponse) {
        super(rawResponse);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        HsdpAuthenticationResponse that = (HsdpAuthenticationResponse) o;
        return Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(refreshToken, that.refreshToken) &&
                Objects.equals(expiresIn, that.expiresIn) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accessToken, refreshToken, expiresIn, userId);
    }

    @Override
    public String toString() {
        return "HsdpAuthenticationResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", userId='" + userId + '\'' +
                "} " + super.toString();
    }
}
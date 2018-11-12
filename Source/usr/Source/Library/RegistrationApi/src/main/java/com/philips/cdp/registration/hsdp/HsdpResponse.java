package com.philips.cdp.registration.hsdp;


import com.philips.cdp.registration.ui.utils.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class HsdpResponse {
    static {
        Map<String, Object> SUCCESS_MAP = new HashMap<String, Object>();
        SUCCESS_MAP.put("responseCode", "200");
        SUCCESS_MAP.put("responseMessage", "Success");

    }

    final Map<String, Object> rawResponse;
    public final String responseCode;
    public final String message;

    HsdpResponse(Map<String, Object> rawResponse) {
        this.rawResponse = rawResponse;
        this.responseCode = MapUtils.extract(rawResponse, "responseCode");
        this.message = MapUtils.extract(rawResponse, "responseMessage");
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//
//        if (o == null || getClass() != o.getClass())
//            return false;
//        HsdpResponse response = (HsdpResponse) o;
//        return Objects.equals(rawResponse, response.rawResponse) &&
//                Objects.equals(responseCode, response.responseCode) &&
//                Objects.equals(message, response.message);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(rawResponse, responseCode, message);
//    }
//
//    @Override
//    public String toString() {
//        return "HsdpResponse{" +
//                "rawResponse=" + rawResponse +
//                ", responseCode='" + responseCode + '\'' +
//                ", message='" + message + '\'' +
//                '}';
//    }
}

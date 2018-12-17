package com.philips.platform.appinfra.logging.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishek on 5/18/18.
 */

public class LogData {
    @SerializedName("message")
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.error;

/**
 * Class to get the type of errors in My account
 * @since 2017.5.0
 */
public enum MyaError {
    USERNOTLOGGEDIN(1001,"User not logged in");

    private int code;
    private String description;

    /**
     * Constructor for MyaError
     * @since 2018.5.0
     * @param code error code for the error
     * @param description description for the type of error
     */
    MyaError(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Get the code of error
     * @since 2018.1.0
     * @return returns the error code
     */
    public int getCode() {
        return code;
    }

    /**
     * Set the error code
     * @since 2018.1.0
     * @param code pass the error code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Get the description of error
     * @since 2018.1.0
     * @return returns the description of error
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of error
     * @since 2018.1.0
     * @param description pass the description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

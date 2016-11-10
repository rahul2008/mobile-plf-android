/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */
package com.philips.cdp.registration.ui.utils;

/**
 * Gender Enum for updating gender.
 */
public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String gender;

    /**
     * @param gender
     */
    private Gender(final String gender) {
        this.gender = gender;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return gender;
    }
}

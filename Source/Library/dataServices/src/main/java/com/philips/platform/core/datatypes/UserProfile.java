/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

/**
 * DataBase Interface for creating UserProfile Object
 */
public class UserProfile {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String gUid;

    public UserProfile(final String firstName, final String lastName, final String email, final String gUid) {
        this.gUid = gUid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGUid() {
        return gUid;
    }
}

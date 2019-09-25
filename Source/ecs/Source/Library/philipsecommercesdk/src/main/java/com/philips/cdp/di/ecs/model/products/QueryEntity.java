/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.ecs.model.products;

import java.io.Serializable;

public class QueryEntity implements Serializable {
    private String value;

    public String getValue() {
        return value;
    }
}

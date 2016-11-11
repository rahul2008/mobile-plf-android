/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.ConsentDetailType;
import com.philips.platform.core.datatypes.MomentType;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LoadConsentsRequest extends Event {

    private final ConsentDetailType type;
    private ConsentDetailType[] types;

    public LoadConsentsRequest() {
        types = null;
        type = null;
    }

    public LoadConsentsRequest(final @NonNull ConsentDetailType... type) {
        this.type = type[0];
        this.types = type;
    }



    public boolean hasType() {
        return type != null;
    }

    public ConsentDetailType getType() {
        return type;
    }

    public ConsentDetailType[] getTypes() {
        return types;
    }
}

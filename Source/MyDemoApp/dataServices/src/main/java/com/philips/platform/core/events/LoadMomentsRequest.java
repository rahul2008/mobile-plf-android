/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.MomentType;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LoadMomentsRequest extends Event {

    private final MomentType type;
    private MomentType[] types;

    public LoadMomentsRequest() {
        types = null;
        type = null;
    }

    public LoadMomentsRequest(final @NonNull MomentType... type) {
        this.type = type[0];
        this.types = type;
    }



    public boolean hasType() {
        return type != null;
    }

    public MomentType getType() {
        return type;
    }

    public MomentType[] getTypes() {
        return types;
    }
}

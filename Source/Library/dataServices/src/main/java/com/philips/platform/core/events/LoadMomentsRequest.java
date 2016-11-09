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
    private final int momentID;
    private MomentType[] types;

    public LoadMomentsRequest() {
        types = null;
        type = null;
        momentID = -1;
    }

    public LoadMomentsRequest(final @NonNull MomentType... type) {
        this.type = type[0];
        this.types = type;
        momentID = -1;
    }

    public LoadMomentsRequest(int momentID) {
        this.momentID = momentID;
        this.type = null;
    }

    public int getMomentID(){
        return momentID;
    }

    public boolean hasType() {
        return type != null;
    }

    public boolean hasID() {
        return momentID != -1;
    }

    public MomentType getType() {
        return type;
    }

    public MomentType[] getTypes() {
        return types;
    }
}

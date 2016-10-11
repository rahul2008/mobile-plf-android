/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ListSaveResponse extends Event {

    private final boolean savedAllData;

    public ListSaveResponse(final int referenceId, final boolean savedAllData) {
        super(referenceId);
        this.savedAllData = savedAllData;
    }

    public boolean isSavedAllData() {
        return savedAllData;
    }
}

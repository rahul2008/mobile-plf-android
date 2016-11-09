/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;

import java.util.LinkedList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentDetailsUpdateRequest extends Event {

    final List<ConsentDetail> consentDetails;

    public ConsentDetailsUpdateRequest(final List<ConsentDetail> consentDetails) {
        this.consentDetails = consentDetails;
    }

    public List<ConsentDetail> getConsentDetails() {
        return consentDetails;
    }
}

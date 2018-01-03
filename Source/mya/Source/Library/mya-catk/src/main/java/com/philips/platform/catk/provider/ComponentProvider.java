/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.provider;

import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.injection.CatkComponent;

public interface ComponentProvider {

    CatkComponent getComponent(CatkInputs catkInputs);
}

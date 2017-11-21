package com.philips.platform.catk.provider;

import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.injection.CatkComponent;

public interface ComponentProvider {

    CatkComponent getComponent(CatkInputs catkInputs);
}

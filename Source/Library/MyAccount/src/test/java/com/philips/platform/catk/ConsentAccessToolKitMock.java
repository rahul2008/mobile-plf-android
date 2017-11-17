package com.philips.platform.catk;

import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.provider.ComponentProvider;

public class ConsentAccessToolKitMock extends ConsentAccessToolKit {

    CatkComponentMock catkComponent;

    public ConsentAccessToolKitMock() {
        catkComponent = new CatkComponentMock();
    }

    public CatkComponent getCatkComponent() {
        return catkComponent;
    }

    @Override
    public void init(CatkInputs catkInputs, ComponentProvider componentProvider) {

    }
}

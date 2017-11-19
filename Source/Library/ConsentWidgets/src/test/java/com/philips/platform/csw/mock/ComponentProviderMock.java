package com.philips.platform.csw.mock;


import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.provider.ComponentProvider;

public class ComponentProviderMock implements ComponentProvider {

    public CatkInputs catkInputs;


    @Override
    public CatkComponent getComponent(CatkInputs catkInputs) {
        this.catkInputs = catkInputs;
        return new CatkComponentMock();
    }
}

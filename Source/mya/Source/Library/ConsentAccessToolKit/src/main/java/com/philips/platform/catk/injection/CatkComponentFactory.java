package com.philips.platform.catk.injection;


import com.philips.cdp.registration.User;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.provider.ComponentProvider;

public class CatkComponentFactory implements ComponentProvider {

    @Override
    public CatkComponent getComponent(CatkInputs catkInputs) {
        return DaggerCatkComponent.builder()
                .catkModule(new CatkModule(catkInputs.getContext(), catkInputs.getAppInfra()))
                .userModule(new UserModule(new User(catkInputs.getContext())))
                .build();
    }

}

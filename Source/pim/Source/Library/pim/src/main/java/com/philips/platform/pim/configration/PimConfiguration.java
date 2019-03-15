package com.philips.platform.pim.configration;

import com.philips.platform.pim.injection.PIMComponent;

public class PIMConfiguration {
    private PIMComponent component;

    public PIMComponent getComponent() {
        return component;
    }

    public void setComponent(PIMComponent component) {
        this.component = component;
        this.component.inject(this);
    }

    private static volatile PIMConfiguration pimConfiguration;

    public static synchronized PIMConfiguration getInstance() {
        if (pimConfiguration == null) {
            synchronized (PIMConfiguration.class) {
                if (pimConfiguration == null) {
                    pimConfiguration = new PIMConfiguration();
                }
            }
        }
        return pimConfiguration;
    }
}

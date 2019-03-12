package com.philips.platform.pim.configration;

import com.philips.platform.pim.injection.PimComponent;

public class PimConfiguration {
    private PimComponent component;

    public PimComponent getComponent() {
        return component;
    }

    public void setComponent(PimComponent component) {
        this.component = component;
        this.component.inject(this);
    }

    private static volatile PimConfiguration pimConfiguration;

    public static synchronized PimConfiguration getInstance() {
        if (pimConfiguration == null) {
            synchronized (PimConfiguration.class) {
                if (pimConfiguration == null) {
                    pimConfiguration = new PimConfiguration();
                }
            }
        }
        return pimConfiguration;
    }
}

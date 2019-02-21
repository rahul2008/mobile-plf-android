package com.philips.platform.udi.configration;

import com.philips.platform.udi.injection.AppAuthComponent;

public class UdiConfiguration {
    private AppAuthComponent component;

    public AppAuthComponent getComponent() {
        return component;
    }

    public void setComponent(AppAuthComponent component) {
        this.component = component;
        this.component.inject(this);
    }

    private static volatile UdiConfiguration udiConfiguration;

    public static synchronized UdiConfiguration getInstance() {
        if (udiConfiguration == null) {
            synchronized (UdiConfiguration.class) {
                if (udiConfiguration == null) {
                    udiConfiguration = new UdiConfiguration();
                }
            }
        }
        return udiConfiguration;
    }
}

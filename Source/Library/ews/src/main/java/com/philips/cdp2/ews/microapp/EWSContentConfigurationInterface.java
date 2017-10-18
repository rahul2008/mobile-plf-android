package com.philips.cdp2.ews.microapp;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;

public interface EWSContentConfigurationInterface {

    void setContentConfiguration(BaseContentConfiguration baseContentConfiguration,
                                 HappyFlowContentConfiguration happyFlowContentConfiguration);
}

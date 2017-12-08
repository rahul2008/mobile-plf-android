/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.injection;

import com.philips.cdp.registration.configuration.AppConfiguration;
import com.philips.cdp.registration.configuration.HSDPConfiguration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ConfigurationModule {

    @Singleton
    @Provides
    public HSDPConfiguration providesHsdpConfiguration() {
        return new HSDPConfiguration();
    }

    @Singleton
    @Provides
    public AppConfiguration providesAppConfiguration() {
        return new AppConfiguration();
    }
}

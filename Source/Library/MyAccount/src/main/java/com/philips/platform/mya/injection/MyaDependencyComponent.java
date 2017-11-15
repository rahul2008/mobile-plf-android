/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */
package com.philips.platform.mya.injection;

import com.philips.platform.appinfra.AppInfra;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {MyaDependencyModule.class})
@Singleton
public interface MyaDependencyComponent {
    AppInfra getAppInfra();
}

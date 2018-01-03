/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.injection;

import android.content.Context;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.consenthandlerinterface.ConsentConfiguration;

import java.util.List;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {CswModule.class, AppInfraModule.class})
@Singleton
public interface CswComponent {
    Context context();

    LoggingInterface getLoggingInterface();

    AppTaggingInterface getAppTaggingInterface();

    List<ConsentConfiguration> getConsentConfigurations();
}
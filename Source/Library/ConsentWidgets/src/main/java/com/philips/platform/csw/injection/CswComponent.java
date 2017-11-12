package com.philips.platform.csw.injection;

import android.content.Context;

import com.philips.platform.appinfra.logging.LoggingInterface;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {CswModule.class, AppInfraModule.class})
@Singleton
public interface CswComponent {
    Context context();

    LoggingInterface getLoggingInterface();
}
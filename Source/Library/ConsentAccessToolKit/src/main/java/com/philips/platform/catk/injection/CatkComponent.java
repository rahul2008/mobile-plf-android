
package com.philips.platform.catk.injection;

import android.content.Context;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.catk.network.NetworkController;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {CatkModule.class, AppInfraModule.class})
@Singleton
public interface CatkComponent {
    Context context();

    LoggingInterface getLoggingInterface();

    RestInterface getRestInterface();

   // User getUser();

    void inject(NetworkController networkController);
}

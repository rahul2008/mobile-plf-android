package com.philips.platform.core.monitors;

import android.os.Handler;
import dagger.MembersInjector;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class ExceptionMonitor_Factory implements Factory<ExceptionMonitor> {
  private final MembersInjector<ExceptionMonitor> membersInjector;
  private final Provider<Handler> arg0Provider;

  public ExceptionMonitor_Factory(MembersInjector<ExceptionMonitor> membersInjector, Provider<Handler> arg0Provider) {  
    assert membersInjector != null;
    this.membersInjector = membersInjector;
    assert arg0Provider != null;
    this.arg0Provider = arg0Provider;
  }

  @Override
  public ExceptionMonitor get() {  
    ExceptionMonitor instance = new ExceptionMonitor(arg0Provider.get());
    membersInjector.injectMembers(instance);
    return instance;
  }

  public static Factory<ExceptionMonitor> create(MembersInjector<ExceptionMonitor> membersInjector, Provider<Handler> arg0Provider) {  
    return new ExceptionMonitor_Factory(membersInjector, arg0Provider);
  }
}


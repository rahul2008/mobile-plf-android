package com.philips.platform.core.monitors;

import dagger.MembersInjector;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class LoggingMonitor_Factory implements Factory<LoggingMonitor> {
  private final MembersInjector<LoggingMonitor> membersInjector;

  public LoggingMonitor_Factory(MembersInjector<LoggingMonitor> membersInjector) {  
    assert membersInjector != null;
    this.membersInjector = membersInjector;
  }

  @Override
  public LoggingMonitor get() {  
    LoggingMonitor instance = new LoggingMonitor();
    membersInjector.injectMembers(instance);
    return instance;
  }

  public static Factory<LoggingMonitor> create(MembersInjector<LoggingMonitor> membersInjector) {  
    return new LoggingMonitor_Factory(membersInjector);
  }
}


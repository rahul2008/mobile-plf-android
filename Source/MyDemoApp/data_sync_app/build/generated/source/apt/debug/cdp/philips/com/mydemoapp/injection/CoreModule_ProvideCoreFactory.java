package cdp.philips.com.mydemoapp.injection;

import cdp.philips.com.mydemoapp.database.Database;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.monitors.ExceptionMonitor;
import com.philips.platform.core.monitors.LoggingMonitor;
import com.philips.platform.datasync.Backend;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class CoreModule_ProvideCoreFactory implements Factory<BaseAppCore> {
  private final CoreModule module;
  private final Provider<Database> databaseProvider;
  private final Provider<Backend> backendProvider;
  private final Provider<LoggingMonitor> loggingMonitorProvider;
  private final Provider<ExceptionMonitor> exceptionMonitorProvider;

  public CoreModule_ProvideCoreFactory(CoreModule module, Provider<Database> databaseProvider, Provider<Backend> backendProvider, Provider<LoggingMonitor> loggingMonitorProvider, Provider<ExceptionMonitor> exceptionMonitorProvider) {  
    assert module != null;
    this.module = module;
    assert databaseProvider != null;
    this.databaseProvider = databaseProvider;
    assert backendProvider != null;
    this.backendProvider = backendProvider;
    assert loggingMonitorProvider != null;
    this.loggingMonitorProvider = loggingMonitorProvider;
    assert exceptionMonitorProvider != null;
    this.exceptionMonitorProvider = exceptionMonitorProvider;
  }

  @Override
  public BaseAppCore get() {  
    BaseAppCore provided = module.provideCore(databaseProvider.get(), backendProvider.get(), loggingMonitorProvider.get(), exceptionMonitorProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<BaseAppCore> create(CoreModule module, Provider<Database> databaseProvider, Provider<Backend> backendProvider, Provider<LoggingMonitor> loggingMonitorProvider, Provider<ExceptionMonitor> exceptionMonitorProvider) {  
    return new CoreModule_ProvideCoreFactory(module, databaseProvider, backendProvider, loggingMonitorProvider, exceptionMonitorProvider);
  }
}


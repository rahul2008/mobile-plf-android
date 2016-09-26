package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.moments.MomentsMonitor;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BackendModule_ProvidesBackendFactory implements Factory<Backend> {
  private final BackendModule module;
  private final Provider<MomentsMonitor> momentsMonitorProvider;

  public BackendModule_ProvidesBackendFactory(BackendModule module, Provider<MomentsMonitor> momentsMonitorProvider) {  
    assert module != null;
    this.module = module;
    assert momentsMonitorProvider != null;
    this.momentsMonitorProvider = momentsMonitorProvider;
  }

  @Override
  public Backend get() {  
    Backend provided = module.providesBackend(momentsMonitorProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<Backend> create(BackendModule module, Provider<MomentsMonitor> momentsMonitorProvider) {  
    return new BackendModule_ProvidesBackendFactory(module, momentsMonitorProvider);
  }
}


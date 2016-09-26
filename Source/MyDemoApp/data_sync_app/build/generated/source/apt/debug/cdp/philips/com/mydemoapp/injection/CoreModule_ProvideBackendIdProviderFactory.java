package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.datasync.UCoreAccessProvider;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class CoreModule_ProvideBackendIdProviderFactory implements Factory<BackendIdProvider> {
  private final CoreModule module;
  private final Provider<UCoreAccessProvider> uCoreAccessProvider;

  public CoreModule_ProvideBackendIdProviderFactory(CoreModule module, Provider<UCoreAccessProvider> uCoreAccessProvider) {  
    assert module != null;
    this.module = module;
    assert uCoreAccessProvider != null;
    this.uCoreAccessProvider = uCoreAccessProvider;
  }

  @Override
  public BackendIdProvider get() {  
    BackendIdProvider provided = module.provideBackendIdProvider(uCoreAccessProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<BackendIdProvider> create(CoreModule module, Provider<UCoreAccessProvider> uCoreAccessProvider) {  
    return new CoreModule_ProvideBackendIdProviderFactory(module, uCoreAccessProvider);
  }
}


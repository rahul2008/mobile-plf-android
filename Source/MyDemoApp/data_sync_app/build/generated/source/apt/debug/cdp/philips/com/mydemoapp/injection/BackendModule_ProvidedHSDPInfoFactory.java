package cdp.philips.com.mydemoapp.injection;

import com.philips.cdp.registration.configuration.HSDPInfo;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BackendModule_ProvidedHSDPInfoFactory implements Factory<HSDPInfo> {
  private final BackendModule module;

  public BackendModule_ProvidedHSDPInfoFactory(BackendModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public HSDPInfo get() {  
    HSDPInfo provided = module.providedHSDPInfo();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<HSDPInfo> create(BackendModule module) {  
    return new BackendModule_ProvidedHSDPInfoFactory(module);
  }
}


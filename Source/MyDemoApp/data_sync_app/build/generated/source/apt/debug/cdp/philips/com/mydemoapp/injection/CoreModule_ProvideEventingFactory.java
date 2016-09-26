package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.core.Eventing;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class CoreModule_ProvideEventingFactory implements Factory<Eventing> {
  private final CoreModule module;

  public CoreModule_ProvideEventingFactory(CoreModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public Eventing get() {  
    Eventing provided = module.provideEventing();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<Eventing> create(CoreModule module) {  
    return new CoreModule_ProvideEventingFactory(module);
  }
}


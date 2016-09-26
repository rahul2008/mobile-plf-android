package cdp.philips.com.mydemoapp.injection;

import dagger.internal.Factory;
import javax.annotation.Generated;
import retrofit.RestAdapter.Builder;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BackendModule_ProvideRestAdapterBuilderFactory implements Factory<Builder> {
  private final BackendModule module;

  public BackendModule_ProvideRestAdapterBuilderFactory(BackendModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public Builder get() {  
    Builder provided = module.provideRestAdapterBuilder();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<Builder> create(BackendModule module) {  
    return new BackendModule_ProvideRestAdapterBuilderFactory(module);
  }
}


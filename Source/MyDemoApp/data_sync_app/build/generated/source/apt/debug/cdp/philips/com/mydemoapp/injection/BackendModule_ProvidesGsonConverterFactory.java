package cdp.philips.com.mydemoapp.injection;

import dagger.internal.Factory;
import javax.annotation.Generated;
import retrofit.converter.GsonConverter;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BackendModule_ProvidesGsonConverterFactory implements Factory<GsonConverter> {
  private final BackendModule module;

  public BackendModule_ProvidesGsonConverterFactory(BackendModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public GsonConverter get() {  
    GsonConverter provided = module.providesGsonConverter();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<GsonConverter> create(BackendModule module) {  
    return new BackendModule_ProvidesGsonConverterFactory(module);
  }
}


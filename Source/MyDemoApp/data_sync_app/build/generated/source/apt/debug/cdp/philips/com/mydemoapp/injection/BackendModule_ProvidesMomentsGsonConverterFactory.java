package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.datasync.MomentGsonConverter;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BackendModule_ProvidesMomentsGsonConverterFactory implements Factory<MomentGsonConverter> {
  private final BackendModule module;

  public BackendModule_ProvidesMomentsGsonConverterFactory(BackendModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public MomentGsonConverter get() {  
    MomentGsonConverter provided = module.providesMomentsGsonConverter();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<MomentGsonConverter> create(BackendModule module) {  
    return new BackendModule_ProvidesMomentsGsonConverterFactory(module);
  }
}


package cdp.philips.com.mydemoapp.injection;

import com.philips.cdp.registration.settings.RegistrationHelper;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class RegistrationModule_ProvidesRegistrationHelperFactory implements Factory<RegistrationHelper> {
  private final RegistrationModule module;

  public RegistrationModule_ProvidesRegistrationHelperFactory(RegistrationModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public RegistrationHelper get() {  
    RegistrationHelper provided = module.providesRegistrationHelper();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<RegistrationHelper> create(RegistrationModule module) {  
    return new RegistrationModule_ProvidesRegistrationHelperFactory(module);
  }
}


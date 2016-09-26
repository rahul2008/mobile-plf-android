package cdp.philips.com.mydemoapp.injection;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class RegistrationModule_ProvidesRegistrationConfigurationFactory implements Factory<RegistrationConfiguration> {
  private final RegistrationModule module;

  public RegistrationModule_ProvidesRegistrationConfigurationFactory(RegistrationModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public RegistrationConfiguration get() {  
    RegistrationConfiguration provided = module.providesRegistrationConfiguration();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<RegistrationConfiguration> create(RegistrationModule module) {  
    return new RegistrationModule_ProvidesRegistrationConfigurationFactory(module);
  }
}


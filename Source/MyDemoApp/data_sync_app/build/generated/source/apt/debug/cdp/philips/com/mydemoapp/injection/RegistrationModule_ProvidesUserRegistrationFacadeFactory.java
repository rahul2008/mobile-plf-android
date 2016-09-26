package cdp.philips.com.mydemoapp.injection;

import cdp.philips.com.mydemoapp.registration.UserRegistrationFacadeImpl;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class RegistrationModule_ProvidesUserRegistrationFacadeFactory implements Factory<UserRegistrationFacade> {
  private final RegistrationModule module;
  private final Provider<UserRegistrationFacadeImpl> userRegistrationFacadeProvider;

  public RegistrationModule_ProvidesUserRegistrationFacadeFactory(RegistrationModule module, Provider<UserRegistrationFacadeImpl> userRegistrationFacadeProvider) {  
    assert module != null;
    this.module = module;
    assert userRegistrationFacadeProvider != null;
    this.userRegistrationFacadeProvider = userRegistrationFacadeProvider;
  }

  @Override
  public UserRegistrationFacade get() {  
    UserRegistrationFacade provided = module.providesUserRegistrationFacade(userRegistrationFacadeProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<UserRegistrationFacade> create(RegistrationModule module, Provider<UserRegistrationFacadeImpl> userRegistrationFacadeProvider) {  
    return new RegistrationModule_ProvidesUserRegistrationFacadeFactory(module, userRegistrationFacadeProvider);
  }
}


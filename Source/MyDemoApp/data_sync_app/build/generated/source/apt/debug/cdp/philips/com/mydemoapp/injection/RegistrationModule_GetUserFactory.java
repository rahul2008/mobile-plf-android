package cdp.philips.com.mydemoapp.injection;

import android.content.Context;
import com.philips.cdp.registration.User;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class RegistrationModule_GetUserFactory implements Factory<User> {
  private final RegistrationModule module;
  private final Provider<Context> contextProvider;

  public RegistrationModule_GetUserFactory(RegistrationModule module, Provider<Context> contextProvider) {  
    assert module != null;
    this.module = module;
    assert contextProvider != null;
    this.contextProvider = contextProvider;
  }

  @Override
  public User get() {  
    User provided = module.getUser(contextProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<User> create(RegistrationModule module, Provider<Context> contextProvider) {  
    return new RegistrationModule_GetUserFactory(module, contextProvider);
  }
}


package cdp.philips.com.mydemoapp.injection;

import android.content.Context;
import com.philips.cdp.registration.hsdp.HsdpUser;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class RegistrationModule_GetHsdpUserFactory implements Factory<HsdpUser> {
  private final RegistrationModule module;
  private final Provider<Context> contextProvider;

  public RegistrationModule_GetHsdpUserFactory(RegistrationModule module, Provider<Context> contextProvider) {  
    assert module != null;
    this.module = module;
    assert contextProvider != null;
    this.contextProvider = contextProvider;
  }

  @Override
  public HsdpUser get() {  
    HsdpUser provided = module.getHsdpUser(contextProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<HsdpUser> create(RegistrationModule module, Provider<Context> contextProvider) {  
    return new RegistrationModule_GetHsdpUserFactory(module, contextProvider);
  }
}


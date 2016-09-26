package com.philips.platform.datasync;

import com.philips.platform.datasync.userprofile.UserRegistrationFacade;
import dagger.MembersInjector;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class UCoreAccessProvider_Factory implements Factory<UCoreAccessProvider> {
  private final MembersInjector<UCoreAccessProvider> membersInjector;
  private final Provider<UserRegistrationFacade> arg0Provider;

  public UCoreAccessProvider_Factory(MembersInjector<UCoreAccessProvider> membersInjector, Provider<UserRegistrationFacade> arg0Provider) {  
    assert membersInjector != null;
    this.membersInjector = membersInjector;
    assert arg0Provider != null;
    this.arg0Provider = arg0Provider;
  }

  @Override
  public UCoreAccessProvider get() {  
    UCoreAccessProvider instance = new UCoreAccessProvider(arg0Provider.get());
    membersInjector.injectMembers(instance);
    return instance;
  }

  public static Factory<UCoreAccessProvider> create(MembersInjector<UCoreAccessProvider> membersInjector, Provider<UserRegistrationFacade> arg0Provider) {  
    return new UCoreAccessProvider_Factory(membersInjector, arg0Provider);
  }
}


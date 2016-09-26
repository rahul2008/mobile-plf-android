package cdp.philips.com.mydemoapp.registration;

import android.content.Context;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.core.Eventing;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class UserRegistrationFacadeImpl_Factory implements Factory<UserRegistrationFacadeImpl> {
  private final Provider<Context> contextProvider;
  private final Provider<RegistrationLaunchHelperWrapper> registrationLaunchHelperWrapperProvider;
  private final Provider<User> userProvider;
  private final Provider<RegistrationHelper> registrationHelperProvider;
  private final Provider<Eventing> eventingProvider;
  private final Provider<RegistrationConfiguration> registrationConfigurationProvider;
  private final Provider<HSDPInfo> hsdpInfoProvider;

  public UserRegistrationFacadeImpl_Factory(Provider<Context> contextProvider, Provider<RegistrationLaunchHelperWrapper> registrationLaunchHelperWrapperProvider, Provider<User> userProvider, Provider<RegistrationHelper> registrationHelperProvider, Provider<Eventing> eventingProvider, Provider<RegistrationConfiguration> registrationConfigurationProvider, Provider<HSDPInfo> hsdpInfoProvider) {  
    assert contextProvider != null;
    this.contextProvider = contextProvider;
    assert registrationLaunchHelperWrapperProvider != null;
    this.registrationLaunchHelperWrapperProvider = registrationLaunchHelperWrapperProvider;
    assert userProvider != null;
    this.userProvider = userProvider;
    assert registrationHelperProvider != null;
    this.registrationHelperProvider = registrationHelperProvider;
    assert eventingProvider != null;
    this.eventingProvider = eventingProvider;
    assert registrationConfigurationProvider != null;
    this.registrationConfigurationProvider = registrationConfigurationProvider;
    assert hsdpInfoProvider != null;
    this.hsdpInfoProvider = hsdpInfoProvider;
  }

  @Override
  public UserRegistrationFacadeImpl get() {  
    return new UserRegistrationFacadeImpl(contextProvider.get(), registrationLaunchHelperWrapperProvider.get(), userProvider.get(), registrationHelperProvider.get(), eventingProvider.get(), registrationConfigurationProvider.get(), hsdpInfoProvider.get());
  }

  public static Factory<UserRegistrationFacadeImpl> create(Provider<Context> contextProvider, Provider<RegistrationLaunchHelperWrapper> registrationLaunchHelperWrapperProvider, Provider<User> userProvider, Provider<RegistrationHelper> registrationHelperProvider, Provider<Eventing> eventingProvider, Provider<RegistrationConfiguration> registrationConfigurationProvider, Provider<HSDPInfo> hsdpInfoProvider) {  
    return new UserRegistrationFacadeImpl_Factory(contextProvider, registrationLaunchHelperWrapperProvider, userProvider, registrationHelperProvider, eventingProvider, registrationConfigurationProvider, hsdpInfoProvider);
  }
}


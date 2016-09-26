package cdp.philips.com.mydemoapp;

import android.app.Application;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class DataSyncApplicationClass_MembersInjector implements MembersInjector<DataSyncApplicationClass> {
  private final MembersInjector<Application> supertypeInjector;
  private final Provider<Backend> backendProvider;
  private final Provider<BaseAppCore> coreProvider;
  private final Provider<Eventing> eventingProvider;
  private final Provider<UserRegistrationFacade> userRegistrationFacadeProvider;

  public DataSyncApplicationClass_MembersInjector(MembersInjector<Application> supertypeInjector, Provider<Backend> backendProvider, Provider<BaseAppCore> coreProvider, Provider<Eventing> eventingProvider, Provider<UserRegistrationFacade> userRegistrationFacadeProvider) {  
    assert supertypeInjector != null;
    this.supertypeInjector = supertypeInjector;
    assert backendProvider != null;
    this.backendProvider = backendProvider;
    assert coreProvider != null;
    this.coreProvider = coreProvider;
    assert eventingProvider != null;
    this.eventingProvider = eventingProvider;
    assert userRegistrationFacadeProvider != null;
    this.userRegistrationFacadeProvider = userRegistrationFacadeProvider;
  }

  @Override
  public void injectMembers(DataSyncApplicationClass instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    supertypeInjector.injectMembers(instance);
    instance.backend = backendProvider.get();
    instance.core = coreProvider.get();
    instance.eventing = eventingProvider.get();
    instance.userRegistrationFacade = userRegistrationFacadeProvider.get();
  }

  public static MembersInjector<DataSyncApplicationClass> create(MembersInjector<Application> supertypeInjector, Provider<Backend> backendProvider, Provider<BaseAppCore> coreProvider, Provider<Eventing> eventingProvider, Provider<UserRegistrationFacade> userRegistrationFacadeProvider) {  
      return new DataSyncApplicationClass_MembersInjector(supertypeInjector, backendProvider, coreProvider, eventingProvider, userRegistrationFacadeProvider);
  }
}


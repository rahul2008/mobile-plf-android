package cdp.philips.com.mydemoapp.registration;

import android.support.v4.app.Fragment;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.trackers.Tracker;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class UserRegistrationFragment_MembersInjector implements MembersInjector<UserRegistrationFragment> {
  private final MembersInjector<Fragment> supertypeInjector;
  private final Provider<Eventing> eventingProvider;
  private final Provider<Tracker> trackerProvider;

  public UserRegistrationFragment_MembersInjector(MembersInjector<Fragment> supertypeInjector, Provider<Eventing> eventingProvider, Provider<Tracker> trackerProvider) {  
    assert supertypeInjector != null;
    this.supertypeInjector = supertypeInjector;
    assert eventingProvider != null;
    this.eventingProvider = eventingProvider;
    assert trackerProvider != null;
    this.trackerProvider = trackerProvider;
  }

  @Override
  public void injectMembers(UserRegistrationFragment instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    supertypeInjector.injectMembers(instance);
    instance.eventing = eventingProvider.get();
    instance.tracker = trackerProvider.get();
  }

  public static MembersInjector<UserRegistrationFragment> create(MembersInjector<Fragment> supertypeInjector, Provider<Eventing> eventingProvider, Provider<Tracker> trackerProvider) {  
      return new UserRegistrationFragment_MembersInjector(supertypeInjector, eventingProvider, trackerProvider);
  }
}


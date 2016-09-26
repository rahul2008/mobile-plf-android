package cdp.philips.com.mydemoapp.datasync.trackers;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.trackers.Tracker;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BaseTracker_MembersInjector implements MembersInjector<BaseTracker> {
  private final Provider<Tracker> trackerProvider;
  private final Provider<Eventing> eventingProvider;

  public BaseTracker_MembersInjector(Provider<Tracker> trackerProvider, Provider<Eventing> eventingProvider) {  
    assert trackerProvider != null;
    this.trackerProvider = trackerProvider;
    assert eventingProvider != null;
    this.eventingProvider = eventingProvider;
  }

  @Override
  public void injectMembers(BaseTracker instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.tracker = trackerProvider.get();
    instance.eventing = eventingProvider.get();
  }

  public static MembersInjector<BaseTracker> create(Provider<Tracker> trackerProvider, Provider<Eventing> eventingProvider) {  
      return new BaseTracker_MembersInjector(trackerProvider, eventingProvider);
  }
}


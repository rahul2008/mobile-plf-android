package cdp.philips.com.mydemoapp.datasync.temperature;

import android.support.v4.app.Fragment;
import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class TemperatureTimeLineFragment_MembersInjector implements MembersInjector<TemperatureTimeLineFragment> {
  private final MembersInjector<Fragment> supertypeInjector;
  private final Provider<Eventing> eventingProvider;
  private final Provider<DataPullSynchronise> mDataPullSynchroniseProvider;
  private final Provider<DataPushSynchronise> mDataPushSynchroniseProvider;

  public TemperatureTimeLineFragment_MembersInjector(MembersInjector<Fragment> supertypeInjector, Provider<Eventing> eventingProvider, Provider<DataPullSynchronise> mDataPullSynchroniseProvider, Provider<DataPushSynchronise> mDataPushSynchroniseProvider) {  
    assert supertypeInjector != null;
    this.supertypeInjector = supertypeInjector;
    assert eventingProvider != null;
    this.eventingProvider = eventingProvider;
    assert mDataPullSynchroniseProvider != null;
    this.mDataPullSynchroniseProvider = mDataPullSynchroniseProvider;
    assert mDataPushSynchroniseProvider != null;
    this.mDataPushSynchroniseProvider = mDataPushSynchroniseProvider;
  }

  @Override
  public void injectMembers(TemperatureTimeLineFragment instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    supertypeInjector.injectMembers(instance);
    instance.eventing = eventingProvider.get();
    instance.mDataPullSynchronise = mDataPullSynchroniseProvider.get();
    instance.mDataPushSynchronise = mDataPushSynchroniseProvider.get();
  }

  public static MembersInjector<TemperatureTimeLineFragment> create(MembersInjector<Fragment> supertypeInjector, Provider<Eventing> eventingProvider, Provider<DataPullSynchronise> mDataPullSynchroniseProvider, Provider<DataPushSynchronise> mDataPushSynchroniseProvider) {  
      return new TemperatureTimeLineFragment_MembersInjector(supertypeInjector, eventingProvider, mDataPullSynchroniseProvider, mDataPushSynchroniseProvider);
  }
}


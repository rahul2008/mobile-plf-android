package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.moments.MomentsDataSender;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BackendModule_ProvidesDataPushSynchroniseFactory implements Factory<DataPushSynchronise> {
  private final BackendModule module;
  private final Provider<UCoreAccessProvider> uCoreAccessProvider;
  private final Provider<MomentsDataSender> momentsDataSenderProvider;
  private final Provider<Eventing> eventingProvider;

  public BackendModule_ProvidesDataPushSynchroniseFactory(BackendModule module, Provider<UCoreAccessProvider> uCoreAccessProvider, Provider<MomentsDataSender> momentsDataSenderProvider, Provider<Eventing> eventingProvider) {  
    assert module != null;
    this.module = module;
    assert uCoreAccessProvider != null;
    this.uCoreAccessProvider = uCoreAccessProvider;
    assert momentsDataSenderProvider != null;
    this.momentsDataSenderProvider = momentsDataSenderProvider;
    assert eventingProvider != null;
    this.eventingProvider = eventingProvider;
  }

  @Override
  public DataPushSynchronise get() {  
    DataPushSynchronise provided = module.providesDataPushSynchronise(uCoreAccessProvider.get(), momentsDataSenderProvider.get(), eventingProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<DataPushSynchronise> create(BackendModule module, Provider<UCoreAccessProvider> uCoreAccessProvider, Provider<MomentsDataSender> momentsDataSenderProvider, Provider<Eventing> eventingProvider) {  
    return new BackendModule_ProvidesDataPushSynchroniseFactory(module, uCoreAccessProvider, momentsDataSenderProvider, eventingProvider);
  }
}


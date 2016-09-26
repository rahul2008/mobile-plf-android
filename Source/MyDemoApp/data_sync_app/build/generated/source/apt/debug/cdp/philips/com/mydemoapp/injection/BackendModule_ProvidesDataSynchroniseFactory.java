package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import dagger.internal.Factory;
import java.util.concurrent.ExecutorService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BackendModule_ProvidesDataSynchroniseFactory implements Factory<DataPullSynchronise> {
  private final BackendModule module;
  private final Provider<UCoreAccessProvider> uCoreAccessProvider;
  private final Provider<MomentsDataFetcher> momentsDataFetcherProvider;
  private final Provider<Eventing> eventingProvider;
  private final Provider<ExecutorService> executorProvider;

  public BackendModule_ProvidesDataSynchroniseFactory(BackendModule module, Provider<UCoreAccessProvider> uCoreAccessProvider, Provider<MomentsDataFetcher> momentsDataFetcherProvider, Provider<Eventing> eventingProvider, Provider<ExecutorService> executorProvider) {  
    assert module != null;
    this.module = module;
    assert uCoreAccessProvider != null;
    this.uCoreAccessProvider = uCoreAccessProvider;
    assert momentsDataFetcherProvider != null;
    this.momentsDataFetcherProvider = momentsDataFetcherProvider;
    assert eventingProvider != null;
    this.eventingProvider = eventingProvider;
    assert executorProvider != null;
    this.executorProvider = executorProvider;
  }

  @Override
  public DataPullSynchronise get() {  
    DataPullSynchronise provided = module.providesDataSynchronise(uCoreAccessProvider.get(), momentsDataFetcherProvider.get(), eventingProvider.get(), executorProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<DataPullSynchronise> create(BackendModule module, Provider<UCoreAccessProvider> uCoreAccessProvider, Provider<MomentsDataFetcher> momentsDataFetcherProvider, Provider<Eventing> eventingProvider, Provider<ExecutorService> executorProvider) {  
    return new BackendModule_ProvidesDataSynchroniseFactory(module, uCoreAccessProvider, momentsDataFetcherProvider, eventingProvider, executorProvider);
  }
}


package cdp.philips.com.mydemoapp.injection;

import android.content.Context;
import com.philips.platform.datasync.OkClientFactory;
import com.philips.platform.datasync.UCoreAdapter;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;
import retrofit.RestAdapter.Builder;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BackendModule_ProvidesUCoreAdapterFactory implements Factory<UCoreAdapter> {
  private final BackendModule module;
  private final Provider<OkClientFactory> okClientFactoryProvider;
  private final Provider<Builder> restAdapterBuilderProvider;
  private final Provider<Context> contextProvider;

  public BackendModule_ProvidesUCoreAdapterFactory(BackendModule module, Provider<OkClientFactory> okClientFactoryProvider, Provider<Builder> restAdapterBuilderProvider, Provider<Context> contextProvider) {  
    assert module != null;
    this.module = module;
    assert okClientFactoryProvider != null;
    this.okClientFactoryProvider = okClientFactoryProvider;
    assert restAdapterBuilderProvider != null;
    this.restAdapterBuilderProvider = restAdapterBuilderProvider;
    assert contextProvider != null;
    this.contextProvider = contextProvider;
  }

  @Override
  public UCoreAdapter get() {  
    UCoreAdapter provided = module.providesUCoreAdapter(okClientFactoryProvider.get(), restAdapterBuilderProvider.get(), contextProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<UCoreAdapter> create(BackendModule module, Provider<OkClientFactory> okClientFactoryProvider, Provider<Builder> restAdapterBuilderProvider, Provider<Context> contextProvider) {  
    return new BackendModule_ProvidesUCoreAdapterFactory(module, okClientFactoryProvider, restAdapterBuilderProvider, contextProvider);
  }
}


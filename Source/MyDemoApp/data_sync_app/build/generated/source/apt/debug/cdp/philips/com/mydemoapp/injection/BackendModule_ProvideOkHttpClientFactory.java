package cdp.philips.com.mydemoapp.injection;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import dagger.internal.Factory;
import java.util.List;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class BackendModule_ProvideOkHttpClientFactory implements Factory<OkHttpClient> {
  private final BackendModule module;
  private final Provider<List<Interceptor>> interceptorsProvider;

  public BackendModule_ProvideOkHttpClientFactory(BackendModule module, Provider<List<Interceptor>> interceptorsProvider) {  
    assert module != null;
    this.module = module;
    assert interceptorsProvider != null;
    this.interceptorsProvider = interceptorsProvider;
  }

  @Override
  public OkHttpClient get() {  
    OkHttpClient provided = module.provideOkHttpClient(interceptorsProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<OkHttpClient> create(BackendModule module, Provider<List<Interceptor>> interceptorsProvider) {  
    return new BackendModule_ProvideOkHttpClientFactory(module, interceptorsProvider);
  }
}


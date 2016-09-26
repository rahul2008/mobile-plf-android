package cdp.philips.com.mydemoapp.injection;

import dagger.internal.Factory;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class ApplicationModule_ProvidesExecutorFactory implements Factory<Executor> {
  private final ApplicationModule module;
  private final Provider<ExecutorService> executorServiceProvider;

  public ApplicationModule_ProvidesExecutorFactory(ApplicationModule module, Provider<ExecutorService> executorServiceProvider) {  
    assert module != null;
    this.module = module;
    assert executorServiceProvider != null;
    this.executorServiceProvider = executorServiceProvider;
  }

  @Override
  public Executor get() {  
    Executor provided = module.providesExecutor(executorServiceProvider.get());
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<Executor> create(ApplicationModule module, Provider<ExecutorService> executorServiceProvider) {  
    return new ApplicationModule_ProvidesExecutorFactory(module, executorServiceProvider);
  }
}


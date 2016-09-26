package cdp.philips.com.mydemoapp.injection;

import android.os.Handler;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class ApplicationModule_ProvidesHandlerFactory implements Factory<Handler> {
  private final ApplicationModule module;

  public ApplicationModule_ProvidesHandlerFactory(ApplicationModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public Handler get() {  
    Handler provided = module.providesHandler();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<Handler> create(ApplicationModule module) {  
    return new ApplicationModule_ProvidesHandlerFactory(module);
  }
}


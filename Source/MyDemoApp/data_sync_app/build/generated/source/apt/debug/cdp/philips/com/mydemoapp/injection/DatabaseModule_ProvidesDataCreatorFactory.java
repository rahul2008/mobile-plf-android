package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.core.BaseAppDataCreator;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class DatabaseModule_ProvidesDataCreatorFactory implements Factory<BaseAppDataCreator> {
  private final DatabaseModule module;

  public DatabaseModule_ProvidesDataCreatorFactory(DatabaseModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public BaseAppDataCreator get() {  
    BaseAppDataCreator provided = module.providesDataCreator();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<BaseAppDataCreator> create(DatabaseModule module) {  
    return new DatabaseModule_ProvidesDataCreatorFactory(module);
  }
}


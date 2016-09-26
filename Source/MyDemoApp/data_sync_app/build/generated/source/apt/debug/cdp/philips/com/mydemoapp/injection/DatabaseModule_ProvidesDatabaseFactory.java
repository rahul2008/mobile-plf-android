package cdp.philips.com.mydemoapp.injection;

import cdp.philips.com.mydemoapp.database.Database;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class DatabaseModule_ProvidesDatabaseFactory implements Factory<Database> {
  private final DatabaseModule module;

  public DatabaseModule_ProvidesDatabaseFactory(DatabaseModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public Database get() {  
    Database provided = module.providesDatabase();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<Database> create(DatabaseModule module) {  
    return new DatabaseModule_ProvidesDatabaseFactory(module);
  }
}


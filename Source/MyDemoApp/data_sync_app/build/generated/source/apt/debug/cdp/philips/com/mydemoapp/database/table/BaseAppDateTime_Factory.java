package cdp.philips.com.mydemoapp.database.table;

import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public enum BaseAppDateTime_Factory implements Factory<BaseAppDateTime> {
INSTANCE;

  @Override
  public BaseAppDateTime get() {  
    return new BaseAppDateTime();
  }

  public static Factory<BaseAppDateTime> create() {  
    return INSTANCE;
  }
}


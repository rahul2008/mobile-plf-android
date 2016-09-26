package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.core.BaseAppDataCreator;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class CoreModule_MembersInjector implements MembersInjector<CoreModule> {
  private final Provider<BaseAppDataCreator> dataCreatorProvider;

  public CoreModule_MembersInjector(Provider<BaseAppDataCreator> dataCreatorProvider) {  
    assert dataCreatorProvider != null;
    this.dataCreatorProvider = dataCreatorProvider;
  }

  @Override
  public void injectMembers(CoreModule instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.dataCreator = dataCreatorProvider.get();
  }

  public static MembersInjector<CoreModule> create(Provider<BaseAppDataCreator> dataCreatorProvider) {  
      return new CoreModule_MembersInjector(dataCreatorProvider);
  }
}


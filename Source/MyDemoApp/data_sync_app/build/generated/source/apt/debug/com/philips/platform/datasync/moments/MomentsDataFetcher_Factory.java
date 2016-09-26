package com.philips.platform.datasync.moments;

import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import dagger.MembersInjector;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;
import retrofit.converter.GsonConverter;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class MomentsDataFetcher_Factory implements Factory<MomentsDataFetcher> {
  private final MembersInjector<MomentsDataFetcher> membersInjector;
  private final Provider<UCoreAdapter> arg0Provider;
  private final Provider<UCoreAccessProvider> arg1Provider;
  private final Provider<MomentsConverter> arg2Provider;
  private final Provider<Eventing> arg3Provider;
  private final Provider<GsonConverter> arg4Provider;

  public MomentsDataFetcher_Factory(MembersInjector<MomentsDataFetcher> membersInjector, Provider<UCoreAdapter> arg0Provider, Provider<UCoreAccessProvider> arg1Provider, Provider<MomentsConverter> arg2Provider, Provider<Eventing> arg3Provider, Provider<GsonConverter> arg4Provider) {  
    assert membersInjector != null;
    this.membersInjector = membersInjector;
    assert arg0Provider != null;
    this.arg0Provider = arg0Provider;
    assert arg1Provider != null;
    this.arg1Provider = arg1Provider;
    assert arg2Provider != null;
    this.arg2Provider = arg2Provider;
    assert arg3Provider != null;
    this.arg3Provider = arg3Provider;
    assert arg4Provider != null;
    this.arg4Provider = arg4Provider;
  }

  @Override
  public MomentsDataFetcher get() {  
    MomentsDataFetcher instance = new MomentsDataFetcher(arg0Provider.get(), arg1Provider.get(), arg2Provider.get(), arg3Provider.get(), arg4Provider.get());
    membersInjector.injectMembers(instance);
    return instance;
  }

  public static Factory<MomentsDataFetcher> create(MembersInjector<MomentsDataFetcher> membersInjector, Provider<UCoreAdapter> arg0Provider, Provider<UCoreAccessProvider> arg1Provider, Provider<MomentsConverter> arg2Provider, Provider<Eventing> arg3Provider, Provider<GsonConverter> arg4Provider) {  
    return new MomentsDataFetcher_Factory(membersInjector, arg0Provider, arg1Provider, arg2Provider, arg3Provider, arg4Provider);
  }
}


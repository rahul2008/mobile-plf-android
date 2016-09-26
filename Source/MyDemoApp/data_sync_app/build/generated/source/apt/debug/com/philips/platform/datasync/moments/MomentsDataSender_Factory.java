package com.philips.platform.datasync.moments;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class MomentsDataSender_Factory implements Factory<MomentsDataSender> {
  private final Provider<UCoreAccessProvider> arg0Provider;
  private final Provider<UCoreAdapter> arg1Provider;
  private final Provider<MomentsConverter> arg2Provider;
  private final Provider<BaseAppDataCreator> arg3Provider;
  private final Provider<MomentGsonConverter> arg4Provider;
  private final Provider<Eventing> arg5Provider;

  public MomentsDataSender_Factory(Provider<UCoreAccessProvider> arg0Provider, Provider<UCoreAdapter> arg1Provider, Provider<MomentsConverter> arg2Provider, Provider<BaseAppDataCreator> arg3Provider, Provider<MomentGsonConverter> arg4Provider, Provider<Eventing> arg5Provider) {  
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
    assert arg5Provider != null;
    this.arg5Provider = arg5Provider;
  }

  @Override
  public MomentsDataSender get() {  
    return new MomentsDataSender(arg0Provider.get(), arg1Provider.get(), arg2Provider.get(), arg3Provider.get(), arg4Provider.get(), arg5Provider.get());
  }

  public static Factory<MomentsDataSender> create(Provider<UCoreAccessProvider> arg0Provider, Provider<UCoreAdapter> arg1Provider, Provider<MomentsConverter> arg2Provider, Provider<BaseAppDataCreator> arg3Provider, Provider<MomentGsonConverter> arg4Provider, Provider<Eventing> arg5Provider) {  
    return new MomentsDataSender_Factory(arg0Provider, arg1Provider, arg2Provider, arg3Provider, arg4Provider, arg5Provider);
  }
}


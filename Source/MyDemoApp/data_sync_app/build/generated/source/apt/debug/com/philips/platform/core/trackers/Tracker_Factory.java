package com.philips.platform.core.trackers;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class Tracker_Factory implements Factory<Tracker> {
  private final Provider<Eventing> arg0Provider;
  private final Provider<BaseAppDataCreator> arg1Provider;
  private final Provider<BackendIdProvider> arg2Provider;

  public Tracker_Factory(Provider<Eventing> arg0Provider, Provider<BaseAppDataCreator> arg1Provider, Provider<BackendIdProvider> arg2Provider) {  
    assert arg0Provider != null;
    this.arg0Provider = arg0Provider;
    assert arg1Provider != null;
    this.arg1Provider = arg1Provider;
    assert arg2Provider != null;
    this.arg2Provider = arg2Provider;
  }

  @Override
  public Tracker get() {  
    return new Tracker(arg0Provider.get(), arg1Provider.get(), arg2Provider.get());
  }

  public static Factory<Tracker> create(Provider<Eventing> arg0Provider, Provider<BaseAppDataCreator> arg1Provider, Provider<BackendIdProvider> arg2Provider) {  
    return new Tracker_Factory(arg0Provider, arg1Provider, arg2Provider);
  }
}


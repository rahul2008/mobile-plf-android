package com.philips.platform.datasync.moments;

import dagger.MembersInjector;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class MomentsMonitor_Factory implements Factory<MomentsMonitor> {
  private final MembersInjector<MomentsMonitor> membersInjector;
  private final Provider<MomentsDataSender> arg0Provider;

  public MomentsMonitor_Factory(MembersInjector<MomentsMonitor> membersInjector, Provider<MomentsDataSender> arg0Provider) {  
    assert membersInjector != null;
    this.membersInjector = membersInjector;
    assert arg0Provider != null;
    this.arg0Provider = arg0Provider;
  }

  @Override
  public MomentsMonitor get() {  
    MomentsMonitor instance = new MomentsMonitor(arg0Provider.get());
    membersInjector.injectMembers(instance);
    return instance;
  }

  public static Factory<MomentsMonitor> create(MembersInjector<MomentsMonitor> membersInjector, Provider<MomentsDataSender> arg0Provider) {  
    return new MomentsMonitor_Factory(membersInjector, arg0Provider);
  }
}


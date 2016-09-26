package com.philips.platform.datasync.moments;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.datasync.conversion.MeasurementDetailValueMap;
import com.philips.platform.datasync.conversion.MomentTypeMap;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class MomentsConverter_Factory implements Factory<MomentsConverter> {
  private final Provider<MomentTypeMap> arg0Provider;
  private final Provider<MeasurementDetailValueMap> arg1Provider;
  private final Provider<BaseAppDataCreator> arg2Provider;

  public MomentsConverter_Factory(Provider<MomentTypeMap> arg0Provider, Provider<MeasurementDetailValueMap> arg1Provider, Provider<BaseAppDataCreator> arg2Provider) {  
    assert arg0Provider != null;
    this.arg0Provider = arg0Provider;
    assert arg1Provider != null;
    this.arg1Provider = arg1Provider;
    assert arg2Provider != null;
    this.arg2Provider = arg2Provider;
  }

  @Override
  public MomentsConverter get() {  
    return new MomentsConverter(arg0Provider.get(), arg1Provider.get(), arg2Provider.get());
  }

  public static Factory<MomentsConverter> create(Provider<MomentTypeMap> arg0Provider, Provider<MeasurementDetailValueMap> arg1Provider, Provider<BaseAppDataCreator> arg2Provider) {  
    return new MomentsConverter_Factory(arg0Provider, arg1Provider, arg2Provider);
  }
}


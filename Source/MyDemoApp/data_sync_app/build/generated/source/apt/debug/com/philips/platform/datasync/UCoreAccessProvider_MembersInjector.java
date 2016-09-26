package com.philips.platform.datasync;

import android.content.SharedPreferences;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class UCoreAccessProvider_MembersInjector implements MembersInjector<UCoreAccessProvider> {
  private final Provider<SharedPreferences> sharedPreferencesProvider;

  public UCoreAccessProvider_MembersInjector(Provider<SharedPreferences> sharedPreferencesProvider) {  
    assert sharedPreferencesProvider != null;
    this.sharedPreferencesProvider = sharedPreferencesProvider;
  }

  @Override
  public void injectMembers(UCoreAccessProvider instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.sharedPreferences = sharedPreferencesProvider.get();
  }

  public static MembersInjector<UCoreAccessProvider> create(Provider<SharedPreferences> sharedPreferencesProvider) {  
      return new UCoreAccessProvider_MembersInjector(sharedPreferencesProvider);
  }
}


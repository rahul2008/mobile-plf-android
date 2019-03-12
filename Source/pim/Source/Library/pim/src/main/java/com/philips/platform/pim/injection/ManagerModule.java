package com.philips.platform.pim.injection;

import com.philips.platform.pim.utilities.PimStorageUtility;

import dagger.Module;
import dagger.Provides;

@Module
public class ManagerModule {

    @Provides
    public PimStorageUtility providesPimStorageUtility() {
        return new PimStorageUtility();
    }
}

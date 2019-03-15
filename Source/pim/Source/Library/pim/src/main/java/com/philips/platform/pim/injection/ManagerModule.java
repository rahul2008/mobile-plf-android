package com.philips.platform.pim.injection;

import com.philips.platform.pim.utilities.PIMStorageUtility;

import dagger.Module;
import dagger.Provides;

@Module
public class ManagerModule {

    @Provides
    public PIMStorageUtility providesPimStorageUtility() {
        return new PIMStorageUtility();
    }
}

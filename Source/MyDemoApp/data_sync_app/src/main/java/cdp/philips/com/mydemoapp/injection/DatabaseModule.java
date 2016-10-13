/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.utils.UuidGenerator;

import javax.inject.Singleton;

import cdp.philips.com.mydemoapp.database.Database;
import cdp.philips.com.mydemoapp.database.OrmCreator;
import dagger.Module;
import dagger.Provides;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Module
public class DatabaseModule {

    @Provides
    @Singleton
    BaseAppDataCreator providesDataCreator() {
        return providesDatabase();
    }

    @Provides
    @Singleton
    Database providesDatabase() {
            OrmCreator creator = new OrmCreator(new UuidGenerator());
            return new Database(creator);
        }
}

/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync.settings;

import com.philips.platform.datasync.spy.DBFetchingInterfaceSpy;
import com.philips.testing.verticals.table.OrmSettings;
import com.squareup.okhttp.internal.framed.Settings;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class SettingsSegregatorTest {
    SettingsSegregator settingsSegregator;

    DBFetchingInterfaceSpy dbSpy;

    @Before
    public void setUp() throws Exception {
        dbSpy = new DBFetchingInterfaceSpy();
        settingsSegregator = new SettingsSegregator(dbSpy);
    }

    @Test
    public void shouldReturnDataToSyn_WhenPutSettingsForSyncIsCalled() throws Exception {
        OrmSettings ormSettings=new OrmSettings("Metric","en_US", null);
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(Settings.class, Arrays.asList(ormSettings));
        settingsSegregator.putSettingsForSync(dataToSync);
        assertTrue(dbSpy.fetchNonSyncSettingsCalled);
    }
}
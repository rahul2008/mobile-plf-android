/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync.settings;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.datasync.spy.DBFetchingInterfaceSpy;
import com.philips.testing.verticals.table.OrmSettings;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SettingsSegregatorTest {

    @Test
    public void putSettingsForSyncAddsSettingsToDataToSyncMap() throws Exception {
        givenSettingsToSync(ormSettings);
        whenSelectingSettingsToSync();
        thenDataToSyncContains(Settings.class, ormSettings);
    }

    @Test
    public void putSettingsForSyncDoesNotAddSettingsToSyncMapWhenFailingToRetrieveSettingsFromDb() {
        givenFetchSettingsThrowsException();
        whenSelectingSettingsToSync();
        thenDataToSyncDoesNotContain(Settings.class);
    }

    private void givenSettingsToSync(final OrmSettings... settingsToSync) {
        db.settingsToSync = Arrays.asList(settingsToSync);
    }

    private void givenFetchSettingsThrowsException() {
        db.exceptionWhenFetchingSettingsToSync = new SQLException();
    }

    private void whenSelectingSettingsToSync() {
        settingsSegregator.putSettingsForSync(dataToSync);
    }

    private void thenDataToSyncContains(final Class<?> klazz, final OrmSettings... expectedSettings) {
        assertEquals(Arrays.asList(expectedSettings), dataToSync.get(klazz));
    }

    private void thenDataToSyncDoesNotContain(final Class<?> klazz) {
        assertFalse(dataToSync.containsKey(klazz));
    }

    @Before
    public void setUp() throws Exception {
        db = new DBFetchingInterfaceSpy();
        settingsSegregator = new SettingsSegregator(db);
        dataToSync = new HashMap<>();
    }

    private SettingsSegregator settingsSegregator;
    private DBFetchingInterfaceSpy db;

    private Map<Class, List<?>> dataToSync;
    private OrmSettings ormSettings = new OrmSettings("Metric", "en_US", null);
}